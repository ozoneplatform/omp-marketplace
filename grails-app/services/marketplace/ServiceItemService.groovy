package marketplace

import org.codehaus.groovy.grails.web.json.JSONArray
import ozone.marketplace.enums.OzoneSize
import grails.orm.PagedResultList
import ozone.utils.User

import java.text.SimpleDateFormat

import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod

import ozone.marketplace.domain.ValidationException
import ozone.utils.Utils
import grails.plugin.cache.Cacheable
import org.springframework.transaction.annotation.Transactional
import org.hibernate.criterion.Order

import ozone.marketplace.enums.MarketplaceApplicationSetting
import grails.converters.JSON

import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration;

/**
 * ServiceItemService
 */
class ServiceItemService extends OzoneService {
    def dataSource
    def sessionFactory

    def profileService
    def serviceItemActivityInternalService

    def imagesService
    def typesService

    def customFieldDefinitionService
    def accountService
    def itemCommentService
    def scoreCardService
    def relationshipService
    def contactService

    def marketplaceApplicationConfigurationService

    def owfWidgetTypesService
    def importStackService

    @Transactional(readOnly = true)
    def list(def params) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = ServiceItem.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = ServiceItem.list(params)
        }
        return results
    }

    /*
      * Get detail-listing for a service item.
      */

    @Transactional(readOnly = true)
    def getServiceItemListing(def params, boolean eager = false) {
        if (params.sorttype) {
            session.sorttype = params.sorttype
        }
        if ((!params.id) || params.id.equalsIgnoreCase("null"))
            return null
        def id = Long.valueOf(params.id)
        return getServiceItem(id, eager)
    }

    @Transactional(readOnly = true)
    def getServiceItem(def id, boolean eager = false) {
        log.debug "Getting Service Item ${id}"

        def si = eager ?
            ServiceItem.findById(id, [fetch: [state: 'eager', types: 'eager', categories: 'eager', recommendedLayouts: 'eager', customFields: 'eager', author: 'eager', owfProperties: 'eager', lastActivity: 'eager', owners: 'eager']]) :
            ServiceItem.get(id)
        if (!si) {
            def message = "Trying to get a service item that does not exist (${id}).  Eager is set to ${eager}."
            log.error message
            throw new ValidationException(message: message.toString())
        }
        if (!accountService.isAdmin()) {
            if (accountService.isExternAdmin()) {
                if (!si.statApproved()) {
                    def user = Profile.findByUsername((String) accountService.getLoggedInUsername())
                    if (user.id != si.createdBy.id && !si.isAuthor(user)) {
                        log.error "serviceItem id ${si.id} not being returned to user.id = ${user.id} (createdBy = ${si.createdBy.id}, owners = ${si.owners})"
                        def message = "User ${accountService.getLoggedInUsername()} does not have permission to read listing (${si.id})"
                        throw new AccessControlException(message)
                    }
                    log.debug "returning not approved serviceItem ${si.id} to user ${accountService.getLoggedInUsername()} with external admin role"
                } else {
                    log.debug "returning approved serviceItem ${si.id} to user ${accountService.getLoggedInUsername()} with external admin role"
                }
            } else {
                if (!si?.hasAccess((String) accountService.getLoggedInUsername())) {
                    def message = "User ${accountService.getLoggedInUsername()} does not have permission to read listing (${si.id})"
                    log.error(message)
                    throw new AccessControlException(message)
                }
            }
        }
        return si
    }

    /* params.serviceItem - serviceItem to return an image for
     * params.contextPath -
     * params.getSmallUrl - if true, then the imageSmallUrl will be returned if one exists
     * for the specified serviceItem
     *
     * Returns an image for the specified serviceItem.
     */
    // The key for serviceItemIconImageCache includes the serviceItem's version so if the url for
    // a serviceItem changes the version will be updated and we will not get an invalid cache hit.
    @Cacheable("serviceItemIconImageCache")
    def getServiceItemIconImage(params) {
        def image = [:]
        if (params.serviceItem) {
            if (params.getSmallUrl && params.serviceItem.imageSmallUrl) {
                log.debug "returning the smallImageUrl"
                image.url = params.serviceItem.imageSmallUrl
            }
            if (!image.url && params.serviceItem.imageLargeUrl) {
                image.url = params.serviceItem.imageLargeUrl
            }
            if (!image.url) {
                Types type = Types.findById(params.serviceItem.types.id)
                image.url = type.getIconUrl(params.contextPath)
            }
        }

        return image
    }

    @Transactional
    def doEnable(ServiceItem serviceItem, def username, def enableFl) {
        //Check for permission...
        checkPermissionToEdit(serviceItem, username)

        def returnValue = false
        log.debug "doEnable: serviceItem = ${serviceItem.id} username = ${username} enableFl = ${enableFl}"

        if (serviceItem.isHidden == (enableFl ? 0 : 1)) {
            log.info "serviceItem is already ${enableFl ? 'enabled' : 'disabled'}"
            return true
        }

        serviceItem.isHidden = enableFl ? 0 : 1
        if (!serviceItem.hasErrors() && serviceItem.save(flush: true)) {
            def action = Constants.Action.DISABLED
            if (enableFl) {
                action = Constants.Action.ENABLED
            }
            serviceItemActivityInternalService.addServiceItemActivity(serviceItem, action)
            returnValue = true
        } else {
            log.warn "doEnable: error for serviceItem = ${serviceItem} username = ${username} enableFl = ${enableFl}"
            serviceItem.errors.each {
                log.warn it
            }
        }

        return returnValue
    }

    /**
     * AML-924 INSIDE/OUTSIDE
     *
     * @param serviceItem
     * @param username
     * @param isOutside
     * @return
     */
    @Transactional
    def setInsideOutside(ServiceItem serviceItem, username, isOutside) {
        //Check for permission to edit
        checkPermissionToEdit(serviceItem, username)

        def returnValue = false
        log.debug "setInsideOutside: serviceItem = ${serviceItem.id} username = ${username} isOutside = ${isOutside}"

        // Check to see if value has changed
        if (serviceItem.isOutside == isOutside) {
            log.info "serviceItem is already ${isOutside ? 'set to Outside' : 'set to Inside'}"
            return true
        }

        // Set new value
        serviceItem.isOutside = isOutside
        if (!serviceItem.hasErrors() && serviceItem.save(flush: true)) {
            def profile = profileService.findByUsername(username)
            // Set the action and create the service item activity
            def action = isOutside ? Constants.Action.OUTSIDE : Constants.Action.INSIDE
            serviceItemActivityInternalService.addServiceItemActivity(serviceItem, action)
            returnValue = true
        } else {
            log.warn "setInsideOutside: error for serviceItem = ${serviceItem} username = ${username} isOutside = ${isOutside}"
            serviceItem.errors.each {
                log.warn it
            }
        }

        return returnValue
    }

    @Transactional(readOnly = true)
    def getHighestScoringItems(def maxCount) {
        def criteria = ServiceItem.createCriteria()
        criteria.list(max: maxCount) {
            scoreCard {
                order("score", "desc")
            }
        }
    }

    @Transactional(readOnly = true)
    def checkPermissionToEdit(def serviceItem, def username = accountService.getLoggedInUsername()) {
        if (!serviceItem) {
            def message = "User ${username} attempted to Edit a serviceItem that is Not Found, Does Not Exist"
            log.error message
            throw new ObjectNotFoundException(message)
        }
        if (!accountService.isAdmin()) {
            if (accountService.isExternAdmin()) {
                def user = Profile.findByUsername(username)
                if (user.id != serviceItem.createdBy.id && !serviceItem.isAuthor(user)) {
                    def message = "User ${username} does not have permission to update serviceItem id ${serviceItem.id}"
                    log.error message
                    throw new PermissionException(message)
                }
            } else {
                /* Admins can edit anything
                 *  User's can edit In Progress, Pending or Rejected listings if ownerCanEditTheirApprovedListings = false
                 *  If ownerCanEditTheirApprovedListings = true then user's can update a listing regardless of status
                 */
				boolean ownerCanApproveListings = this.marketplaceApplicationConfigurationService.is(MarketplaceApplicationSetting.ALLOW_OWNER_TO_EDIT_APPROVED_LISTING)
                if (!(serviceItem.isAuthor(username) && (serviceItem.statInProgress() || serviceItem.statRejected() || serviceItem.statPending() || ownerCanApproveListings))) {
                    def message = "User ${username} does not have permission to update serviceItem id ${serviceItem.id}"
                    log.error(message)
                    throw new PermissionException(message)
                }
            }
        }
    }

    // Make sure that if the type of the serviceItem is owfCompatible (aka ozoneAware), then it has an
    // OwfProperties record; otherwise, make sure that it does not.
    @Transactional
    def checkOwfProperties(def serviceItem) {
        if (serviceItem.types.ozoneAware && !serviceItem.owfProperties) {
            log.debug "adding OwfProperties to serviceItem ${serviceItem.title}"
            OwfProperties owfProperties = new OwfProperties()
            owfProperties.save()
            serviceItem.owfProperties = owfProperties
        } else if (!serviceItem.types.ozoneAware && serviceItem.owfProperties) {
            log.debug "removing OwfProperties from serviceItem ${serviceItem.title}"
            OwfProperties owfProperties = serviceItem.owfProperties
            serviceItem.owfProperties = null
            owfProperties.delete()
        }

        // AML-2608: if owfProperties exists always set owfWidgetType to default
        serviceItem.owfProperties?.owfWidgetType =
            serviceItem.owfProperties?.owfWidgetType ?: owfWidgetTypesService.defaultOwfWidgetType

        serviceItem.owfProperties?.setServiceItem(serviceItem)
    }

    /**
     * Creates a service item from json
     * @param data json representation of service item. Can be stack or widget
     * @param username
     * @return serviceItem
     */
    @Transactional
    def createListing(def data) {

        def json = JSON.parse(data)
        User user = accountService.getLoggedInUser()
        Profile profile = Profile.findByUsername(user.username)

        // Test for stack. For now, json represents a stack if the stackContext property exists
        if (json.stackContext) {

            try {

                // The serviceItem, flag indicating whether it is new or not, and return message
                def results = importStackService.importStack(data, user)

                // Save the serviceItem
                if (results.item.isAuthor(profile)) {
                    save(results.item, user.username)

                    if (!results.isNew) {
                        // Drop current relationships
                        relationshipService.deleteByServiceItem(results.item)
                    }
                    // Get and save the components associated with the stack
                    def addedComponents = importStackService.importComponentsFromStack(results.item, user)

                }

                results.put("success", true)

                // Return the results
                return results


            } catch (IllegalArgumentException e) {


                return [success: false, message: e]


            } catch (Exception e) {

                return [success: false, message: e]
            }

            // Test for widget. For now, json represents a widget if the widgetTypes property exists. Not implemented yet
        } else if (json.widgetTypes) {

//            def results = importStackService.importWidget(json, user)
//            // Save the serviceItem
//            save(results.item, user.username)
//
//            // Return the results
//            return [item: results.item, isNew: results.isNew, message: message]

            return null


        } else {

            return null
        }
    }



    @Transactional(readOnly = true)
    def populateDefaults(ServiceItem si, User owner) {
        Profile profile = Profile.findByUsername(owner.username)
        si.with {
            owners = [profile]
            techPocs = [owner.username]
            organization = owner.org
            state = State.findByTitle("Active")
        }
    }


    @Transactional(noRollbackFor=ValidationException)
    def save(ServiceItem serviceItem, def username = accountService.getLoggedInUsername()) {
        log.debug "About to Save service Item: ${serviceItem.owners}, ${serviceItem.techPocs},${serviceItem.organization},${serviceItem.uuid}"
        log.debug "customFields - ${serviceItem.customFields}"
        log.debug "categories - ${serviceItem.categories}"
        log.debug "modifying user - $username"
        boolean exists = (serviceItem.id != null && ServiceItem.get(serviceItem.id) != null)

        if (!serviceItem.uuid) {
            serviceItem.uuid = Utils.generateUUID();
        }

        if (exists) checkPermissionToEdit(serviceItem, username)

        def result = cleanBeforeSave(serviceItem)
        checkOwfProperties(serviceItem)


        if (!serviceItem.save(flush: true)) {
            serviceItem.errors.each {
                log.error it
            }
            throw new ValidationException(fieldErrors: serviceItem.errors)
        }

        //Audit trail
        Profile profile = profileService.findByUsername(username)
        if (!exists) {
            log.debug "ServiceItem ${serviceItem.id} created"

            setRequiredInsideOutsideIfNeeded(serviceItem, username)

            serviceItemActivityInternalService.addServiceItemActivity(serviceItem, Constants.Action.CREATED)
        }
        else {
            log.debug "ServiceItem ${serviceItem.id} updated"
            serviceItemActivityInternalService.addServiceItemActivity(serviceItem, Constants.Action.MODIFIED)
        }

    }



    @Transactional
    def delete(def id) {
        log.info "Inside serviceItemService delete(${id})"
        def serviceItem = ServiceItem.get(id)
        if (serviceItem) {
            relationshipService.deleteByServiceItem(serviceItem)
            serviceItem.save(flush: true)
            ServiceItemSnapshot.findAllByServiceItem(serviceItem).each { it.serviceItem = null }

            serviceItem.delete(flush: true)
        } else {
            log.error "Unable to get ${id} for delete request"
            throw new RuntimeException("Unable to get ${id} for delete request")
        }
    }

    @Transactional
    def approve(ServiceItem serviceItem, def username) {
        log.debug "${username}:inside approve for ${serviceItem.id}"

        if (!accountService.isAdmin()) {
            def messageText = "User ${username} is not an admin and cannot approve listing ${serviceItem.id}"
            log.error messageText
            throw new PermissionException(messageText)
        }

        if (serviceItem.isOutside == null) {
            def messageText = "Listing ${serviceItem.id} cannot be approved until it is set as Inside or Outside."
            log.error messageText
            throw new ValidationException(message: messageText)
        }

        doApprove(serviceItem, username)
    }

    @Transactional
    def autoApprove(ServiceItem serviceItem, def username) {
        doApprove(serviceItem, username)
    }

    private void doApprove(ServiceItem serviceItem, def username) {
        log.debug "need to approve this service item"
        def profile = profileService.findByUsername(username)
        def siAct = new ServiceItemActivity(serviceItem: serviceItem, author: profile, action: Constants.Action.APPROVED)
        serviceItemActivityInternalService.addServiceItemActivity(serviceItem, siAct)

        log.debug "created service item activity approval for this item"

        serviceItem.approvedDate = siAct.activityDate
        serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]

        // set the OWF approved flag in the stack descriptor if it's a stack
        if (serviceItem?.owfProperties && serviceItem?.owfProperties?.stackContext) {
            def stackDescriptorJSON = JSON.parse(serviceItem.owfProperties.stackDescriptor)
            stackDescriptorJSON.approved = true
            serviceItem.owfProperties.stackDescriptor = stackDescriptorJSON.toString();
        }

        log.debug "set approved, now save it"

        try {
            serviceItem.save(failOnError: true)
        } catch(grails.validation.ValidationException ve) {
            throw new ValidationException(fieldErrors: ve.errors)
        }
    }

    @Transactional(readOnly = true)
    def boolean isDirty(ServiceItem serviceItem) {
        log.debug "isDirty: serviceItem.isDirty() = ${serviceItem.isDirty()}"
        boolean returnValue = false
        def ignore = null
        if (serviceItem.isDirty()) {
            def dirtyProperties = serviceItem.dirtyPropertyNames

            dirtyProperties.each { log.debug it }
            if (serviceItem.auditable instanceof java.util.Map && serviceItem.auditable.containsKey('ignore')) {

                def list = serviceItem.auditable['ignore']
                if (list instanceof java.util.List) {
                    ignore = list
                }
            }
            // If any dirty property is not in the ignore list, then the serviceItem is dirty.
            if (dirtyProperties?.any { !ignore.contains(it) }) {
                log.debug 'one or more properties was dirty'
                returnValue = true
            }
        }

        // TODO: move this into serviceItem class
        if (!returnValue) {
            def collectionsToCheck = ['categories', 'recommendedLayouts', 'customFields']
            collectionsToCheck.each { log.debug "${it} - ${serviceItem.getProperty(it)} - ${serviceItem.getProperty(it)?.isDirty()}" }
            if (collectionsToCheck.any { serviceItem.getProperty(it)?.isDirty() }) {
                log.debug 'one or more collections was dirty'
                returnValue = true
            }
        }

        if (!returnValue) {
            if (serviceItem.owfProperties) {
                if (serviceItem.owfProperties.isDirty()) {
                    log.debug 'serviceItem.owfProperties was dirty'
                    returnValue = true
                }
                if (serviceItem.owfProperties.intents.isDirty()) {
                    log.debug 'serviceItem.owfProperties.intents was dirty'
                    returnValue = true
                }
            }
        }

        if (!returnValue) {
            // We need to do the get because isDirty on the javassist proxy object was not working.
            // If a cf has not been saved, then its id will be null!
            if (serviceItem.customFields.any {
                if (it.id) {
                    def cf2 = CustomField.get(it.id)
                    return cf2.isDirty()
                }
                return false
            }) {
                log.debug 'one or more customFields was dirty'
                returnValue = true
            }
        }
        return returnValue
    }

    // remove any custom fields from the serviceItem that no longer belong to the type of the serviceItem
    @Transactional(readOnly = true)
    def boolean cleanBeforeSave(ServiceItem serviceItem) {
        def isDirty = false
        log.debug 'cleanBeforeSave: this.types = ' + serviceItem.types
        serviceItem.customFields?.each {
            if (!it.customFieldDefinition.belongsToType(serviceItem.types)) {
                log.debug "removing custom field ${it}"
                isDirty = true
            }
        }

        if (isDirty) {
            // actually remove custom fields from the collection
            if (serviceItem.customFields?.retainAll({ it.customFieldDefinition.belongsToType(serviceItem.types) })) {
                log.debug 'something was removed by service item type test!'
                //serviceItem.customFields?.each { log.debug it }
            }
        }

        serviceItem.scrubCR()

        return isDirty
    }

    @Transactional(readOnly = true)
    def touch(ServiceItem serviceItem) {
        // It seems like this will force an update to the serviceItem but will not
        // cause a skip in the version numbers.
        // We need this to make sure the serviceItem gets updated when changes are made to a
        // customField belonging to the serviceItem.
        serviceItem.version++

        // Or we could try:
        // serviceItem.editedDate = new Date()
    }

    @Transactional(readOnly = true)
    ServiceItem findByUniversalName(def universalName) {
        if (universalName == null || universalName.equals(null)) {
            // Can't search by null universalName
            return null
        }

        List<ServiceItem> existingWidgets = ServiceItem.withCriteria {
            createAlias("owfProperties", "o")
            eq("o.universalName", universalName)
        }
        return existingWidgets.size() == 1 ? existingWidgets.first() : null
    }

    @Transactional(readOnly = true)
    ServiceItem findByOwfProperty(def property, def propertyName) {

        if (property == null || propertyName == null) {
            // Can't search by null property
            return null
        }

        List<ServiceItem> existingSi = ServiceItem.withCriteria {
            createAlias("owfProperties", "o")
            eq("o.${propertyName}", property)
        }
        return existingSi.size() == 1 ? existingSi.first() : null
    }

    @Transactional(readOnly = true)
    def getAllowableItem(def id, def sessionParams, def rules) throws AccessControlException {
        def item = ServiceItem.get(id)
        def isUser = false
        def isAvailable = false
        def isApproved = false
        def matchesRule = false

        if (item) {
            isUser = item?.isAuthor((String) sessionParams?.username)
            isAvailable = !item.isHidden
            isApproved = item.statApproved()

            if (rules?.allNoRestrictions && isAvailable) {
                matchesRule = true
            }
            if (sessionParams?.isAdmin) {
                matchesRule = true
            }
            if (rules?.allIfApproved && isAvailable && isApproved) {
                matchesRule = true
            }
            if (rules?.userNoRestrictions && isUser) {
                matchesRule = true
            }
            if (rules?.userIfApproved && isUser && isApproved) {
                matchesRule = true
            }

            if (matchesRule) {
                return item
            } else {
                throw new AccessControlException('User is not authorized to access this item');
            }
        }
        return null;
    }

    @Transactional
    def submit(def item, def profile) {
        if (item) {
            item.approvalStatus = Constants.APPROVAL_STATUSES["PENDING"]
            item.save(failOnError: true)
            serviceItemActivityInternalService.addServiceItemActivity(item, Constants.Action.SUBMITTED)
        } else {
            throw new Exception("Service Item does not exist.")
        }
    }

    /**
     * Makes all existing ServiceItems (listings) inside or outside IF their isOutside is not
     * null.
     * @param isOutside
     *      the value to use for the listings' isOutside.
     */
    @Transactional
    def makeListingsOutsideOrInside(boolean isOutside) {
        def affectedItems = ServiceItem.list()
        affectedItems.each { item -> this.setInsideOutside(item, session.username, isOutside) }
    }

    /**
     * Call setInsideOutside on the given serviceItem if system is configured so all listings are
     * Outside or all listings are Inside.
     * @param serviceItem
     * @param username
     */
    private void setRequiredInsideOutsideIfNeeded(def serviceItem, def username) {
        def insideOutsideBehavior = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR);
        if (insideOutsideBehavior != Constants.INSIDE_OUTSIDE_ADMIN_SELECTED) {
            def isOutside = insideOutsideBehavior == Constants.INSIDE_OUTSIDE_ALL_OUTSIDE;
            this.setInsideOutside(serviceItem, username, isOutside);
        }
    }
}
