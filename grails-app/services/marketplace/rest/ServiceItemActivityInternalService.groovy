package marketplace.rest

import grails.gorm.transactions.Transactional
import org.grails.datastore.gorm.GormEntity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import marketplace.AccountService
import marketplace.ChangeDetail
import marketplace.Constants
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.MarketplaceMessagingService
import marketplace.ModifyRelationshipActivity
import marketplace.OwfProperties
import marketplace.ReflectionService
import marketplace.RejectionActivity
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemSnapshot
import marketplace.ServiceItemTag

import static marketplace.ReflectionService.getPreviousValue
import static marketplace.ReflectionService.isPropertyDirty

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

    @Autowired AccountService accountService
    @Autowired MarketplaceMessagingService marketplaceMessagingService

    /**
     * Create a changelog entry as needed that consists of a MODIFIED activity and one or more
     * change details by comparing two ServiceItems. If there are changes, this activity is added
     * to the updated ServiceItem's set of ServiceItemActivities. If no ChangeDetails are created,
     * the activity is not added, but it is still returned.
     *
     * @param updated the new ServiceItem which will have the changelog entry if it is needed
     * @return The activity that is created, regardless of whether or not it is added to the
     * ServiceItem.
     */
    ServiceItemActivity createChangeLog(ServiceItem updated) {
        def activity = new ServiceItemActivity(action: Constants.Action.MODIFIED)

        def serviceItemAuditables = ReflectionService.getAuditableProperties(ServiceItem)
        serviceItemAuditables.each { property ->
            logIfDifferent(activity, updated, property)
        }

        if (updated.owfProperties) {
            def owfPropsAuditable = ReflectionService.getAuditableProperties(OwfProperties)
            owfPropsAuditable.each { property ->
                logIfDifferent(activity, updated.owfProperties, property)
            }
        }

        handleCustomFieldsChangeLog(updated, activity)

        activity.changeDetails ? addServiceItemActivity(updated, activity) : activity
    }

    /**
     * Add a new ServiceItemActivity to the service item with the specified action
     */
    ServiceItemActivity addServiceItemActivity(ServiceItem si, Constants.Action action) {
        addServiceItemActivity(si, new ServiceItemActivity(action: action))
    }

    @Transactional
    ServiceItemActivity addServiceItemActivity(ServiceItem si,
                                               ServiceItemActivity activity) {
        si.save()

        activity.author = accountService.findLoggedInUserProfile()
        activity.setServiceItem(si)

        si.addToServiceItemActivities(activity)
        si.lastActivity = activity

        marketplaceMessagingService.sendNotificationOfChange(si, activity);

        activity.save(failOnError: true)

        return activity
    }

    @Transactional
    RejectionActivity addRejectionActivity(ServiceItem si, RejectionListing rejectionListing) {
        rejectionListing.save(failOnError: true)

        def activity = new RejectionActivity(rejectionListing: rejectionListing)

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
    void addRelationshipActivities(ServiceItem parent,
                                   Collection<ServiceItem> added,
                                   Collection<ServiceItem> removed)
    {
        //a list consisting of just the parent, used below
        def parentList = [parent]

        // if items were added created an activity on the parent for it, and activities on each
        // of the children
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

    private void addActivity(ServiceItem root, Collection<ServiceItem> items, Constants.Action action) {
        def activity = new ModifyRelationshipActivity(
                action: action,
                author: accountService.findLoggedInUserProfile(),
                items: items.collect { new ServiceItemSnapshot(serviceItem: it, title: it.title)}
        )

        root.addToServiceItemActivities(activity)
        root.lastActivity = activity
        root.save()
    }

    @Transactional
    ServiceItemActivity addServiceItemTagActivity(Constants.Action action,
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
    private void handleCustomFieldsChangeLog(ServiceItem updated, ServiceItemActivity activity) {
        //get a list of all customFieldDefinitions in either ServiceItem
        List<CustomField> customFields = updated.customFields ?: []
        List<CustomFieldDefinition> customFieldDefinitions = customFields*.customFieldDefinition

        //go through each of the definitions, find the matching field on each listing, and
        //if their values are not the same, create a changelog
        customFields.each { customField ->
            String label = customField?.customFieldDefinition?.label

            logIfDifferent(activity, customField, 'fieldValueText', label)
        }
    }

    /**
     * Compares the property with name as provided by propertyName for the updated and the old
     * object and if different, adds a changeDetail to the passed in activity.
     */
    private static void logIfDifferent(ServiceItemActivity activity,
                                       GormEntity updated,
                                       String propertyName,
                                       String displayName = null)
    {
        if (!isPropertyDirty(updated, propertyName)) return

        def newVal = convertToComparableLogValue(updated[propertyName])
        def oldVal = convertToComparableLogValue(getPreviousValue(updated, propertyName))
        if (newVal != oldVal) {
            activity.addToChangeDetails(new ChangeDetail(fieldName: displayName ?: propertyName,
                                                         newValue: newVal.toString(),
                                                         oldValue: oldVal.toString()))
        }
    }

    static def convertToComparableLogValue(def value) {
        if (value == null) return 'None'
        if (value instanceof Boolean) return value

        // Convert collections to Sets. For some reason PersistentSet won't do object
        //comparison with another Set on the  instances in the intents collection
        //(Intent.equals() never gets called).  Converting PersistentSet to a Set via
        // an intermediate array conversion is a workaround.
        def logValue = (value instanceof Collection) ? value.toArray() as Set : value

        // if logValue satisfies groovy truth, return it, otherwise return the string 'None'
        logValue ? logValue : 'None'
    }
}
