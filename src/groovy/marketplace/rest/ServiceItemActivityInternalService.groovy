package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemTag
import marketplace.RejectionActivity
import marketplace.RejectionListing
import marketplace.ServiceItemSnapshot
import marketplace.ModifyRelationshipActivity
import marketplace.CustomField
import marketplace.OwfProperties
import marketplace.MarketplaceMessagingService
import marketplace.ChangeDetail

import marketplace.Constants

/**
 * This class was split off from ServiceItemActivityRestService to resolve a circular dependency.
 * This class handles the implicit creation of ServiceItemActivities when other
 * things happen, and is therefore referenced by numerous other services.
 * ServiceItemActivityRestService
 * handles basic retrieval operations that need to be available to the resource layer
 */
@Service
@Transactional
class ServiceItemActivityInternalService {
    @Autowired ProfileRestService profileRestService
    @Autowired MarketplaceMessagingService marketplaceMessagingService

    /**
     * Create a changelog entry as needed that consists of a MODIFIED activity and one or more
     * change details by comparing two ServiceItems. If there are changes, this activity is added
     * to the updated ServiceItem's set of ServiceItemActivities. If no ChangeDetails are created,
     * the activity is not added, but it is still returned.
     *
     * @param updated the new ServiceItem which will have the changelog entry if it is needed
     * @param original the original ServiceItem to use for comparison
     * @return The activity that is created, regardless of whether or not it is added to the
     * ServiceItem.
     */
    public ServiceItemActivity createChangeLog(ServiceItem updated, Map original) {
        def activity = new ServiceItemActivity(action: Constants.Action.MODIFIED)
        def owfPropsChangeLogger = (ServiceItemActivityInternalService.&logIfDifferent).curry(
                activity, updated.owfProperties, original.owfProperties)
        def propsChangeLogger = (ServiceItemActivityInternalService.&logIfDifferent).curry(
                activity, updated, original)

        (ServiceItem.bindableProperties - ServiceItem.auditable.ignore).each(propsChangeLogger)

        if(updated.owfProperties)
            OwfProperties.changeLogProperties.each(owfPropsChangeLogger)

        handleCustomFieldsChangeLog(updated, original, activity)

        activity.changeDetails ? addServiceItemActivity(updated, activity) : activity
    }

    /**
     * Add a new ServiceItemActivity to the service item with the specified action
     */
    public ServiceItemActivity addServiceItemActivity(ServiceItem si,
            Constants.Action action) {
        addServiceItemActivity(si, new ServiceItemActivity(action: action))
    }

    @Transactional
    public ServiceItemActivity addServiceItemActivity(ServiceItem si,
            ServiceItemActivity activity) {
        activity.author = profileRestService.currentUserProfile

        si.save()

        si.addToServiceItemActivities(activity)
        si.lastActivity = activity

        marketplaceMessagingService.sendNotificationOfChange(si, activity);

        return activity
    }

    @Transactional
    public RejectionActivity addRejectionActivity(ServiceItem si,
            RejectionListing rejectionListing) {

        rejectionListing.save()

        def activity = new RejectionActivity(
            rejectionListing: rejectionListing
        )

        addServiceItemActivity(si, activity)
        return activity
    }

    /**
     * Create all necessary ModifyRelationshipActivities for the ServiceItems that were
     * added and removed from this one
     * @param parent The ServiceItem being added and removed from.
     * @param added ServiceItems that were added to the parent
     * @param removed ServiceItems that were removed from the parent
     */
    @Transactional
    public void addRelationshipActivities(ServiceItem parent, Collection<ServiceItem> added,
            Collection<ServiceItem> removed) {

        def addActivity = { root, items, action ->
            def activity = new ModifyRelationshipActivity(
                action: action,
                author: profileRestService.currentUserProfile,
                items: items.collect { new ServiceItemSnapshot(serviceItem: it, title: it.title)}
            )
            root.addToServiceItemActivities(activity)

            root.lastActivity = activity
        }

        //a list consisting of just the parent, used below
        def parentList = [parent]

        //if items were added created an activity on the parent for it, and activities on each
        //of the children
        if (!added.isEmpty()) {
            addActivity(parent, added, Constants.Action.ADDRELATEDITEMS)
            added.each { addActivity(it, parentList, Constants.Action.ADDRELATEDTOITEM) }
        }

        //same as above, but for remove
        if (!removed.isEmpty()) {
            addActivity(parent, removed, Constants.Action.REMOVERELATEDITEMS)
            removed.each { addActivity(it, parentList, Constants.Action.REMOVERELATEDTOITEM) }
        }
    }

    @Transactional
    public ServiceItemActivity addServiceItemTagActivity(Constants.Action action,
            ServiceItemTag serviceItemTag) {
        String tagTitle = serviceItemTag.tag.title
        ServiceItemActivity activity = new ServiceItemActivity(action: action)
        ChangeDetail changeDetail = new ChangeDetail(
            fieldName: "Tag",
            oldValue: null,
            newValue: tagTitle
        )

        activity.addToChangeDetails(changeDetail)
        return addServiceItemActivity(serviceItemTag.serviceItem, activity)
    }

    /**
     * Create the necessary changelogs for the customfields on this serviceitem
     */
    private void handleCustomFieldsChangeLog(ServiceItem updated, Map old,
            ServiceItemActivity activity) {
        //a function that returns true if the customField has the definition specified
        def findField = { customFieldDef, customField ->
            customField.customFieldDefinition == customFieldDef
        }

        //get a list of all customFieldDefinitions in either ServiceItem
        Set customFieldDefinitions = [updated, old].sum {
            //returns a list of customFieldDefinitions.
            //The sum function above acts a a union of the two lists
            (it.customFields ?: [])*.customFieldDefinition
        } as Set

        //go through each of the definitions, find the matching field on each listing, and
        //if their values are not the same, create a changelog
        customFieldDefinitions.each { customFieldDefinition ->
            def finder = findField.curry(customFieldDefinition)

            CustomField updatedField = updated.customFields.find(finder)
            CustomField oldField = old.customFields.find(finder)
            String label = (updatedField ?: oldField).customFieldDefinition.label

            logIfDifferent(activity, updatedField, oldField, 'fieldValueText', label)
        }
    }

    /**
     * Compares the property with name as provided by propertyName for the updated and the old
     * object and if different, adds a changeDetail to the passed in activity.
     */
    private static void logIfDifferent(ServiceItemActivity activity, updated,
            old, String propertyName, String displayName=null) {

//        // Score card change details will be handled by separate activity
//        if(propertyName == "satisfiedScoreCardItems")
//            return


        def convertToComparableLogValue = { item ->
            def itemProperty = item?."$propertyName"

            // Convert collections to Sets. For some reason PersistentSet won't do object
            //comparison with another Set on the  instances in the intents collection
            //(Intent.equals() never gets called).  Converting PersistentSet to a Set via
            // an intermediate array conversion is a workaround.
            def logValue = ([Set, Map, List].grep { itemProperty in it }) ?
                    itemProperty.toArray() as Set : itemProperty

            // if logValue satisfies groovy truth or is actually false, return it,
            //otherwise return the string 'None'
            logValue || (logValue == false) ? logValue : 'None'
        }

        def (newVal, oldVal) = [updated, old].collect { convertToComparableLogValue it }
        if(newVal != oldVal)
            activity.addToChangeDetails(new ChangeDetail(fieldName: displayName ?:
                propertyName, newValue: newVal.toString(), oldValue: oldVal.toString()))
    }
}
