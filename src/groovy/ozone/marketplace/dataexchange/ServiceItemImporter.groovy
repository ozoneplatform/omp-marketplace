package ozone.marketplace.dataexchange

import marketplace.Constants
import marketplace.ExtServiceItem
import marketplace.ItemComment
import marketplace.ServiceItem
import marketplace.Profile
import marketplace.RejectionJustification
import marketplace.RejectionActivity
import marketplace.RejectionListing
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.enums.MarketplaceApplicationSetting

import java.text.ParseException
import ozone.marketplace.domain.ValidationException

class ServiceItemImporter extends AbstractImporter {
    public static final int MAX_IMPORT_ERRORMSG_PER_LISTING_LEN = 1024;

    def JSONDecoratorService
    def accountService
    def serviceItemService
    def extServiceItemService
    def marketplaceApplicationConfigurationService
    def itemCommentService
    def rejectionListingService
    def profileService
    def profileImporter
    boolean importRatings
    def contextPath

    ServiceItemImporter(ProfileImporter profileImporter) {
        super("listing")
        this.profileImporter = profileImporter
    }

    ServiceItemImporter(ProfileImporter profileImporter, boolean importRatings, def contextPath) {
        this(profileImporter)
        this.importRatings = importRatings
        this.contextPath = contextPath
    }

    def createNewObject() {
        // This is not used in this implementation because createFromJSONAndSave determines which class to instantiate based on the fieldType
        return null
    }


    def findExistingObject(def json) {
        log.debug "Looking up existing listing: ${json?.title} [${json?.uuid}]"
        def foundItem

        // Attempt lookup by GUID
        if (json?.uuid) {
            log.debug "Looking up by uuid: ${json?.uuid}"
            foundItem = ServiceItem.findByUuid(json?.uuid)
            log.debug "Found item: ${foundItem}"
        }
        log.debug "Found listing ${foundItem}"
        return foundItem
    }


    def createFromJSONAndSave(def listingJson) {
        def newListing

        importAssociatedProfilesIfNecessary(listingJson)

        // See if listing is from the same Storefront
        def hasSystemUri = listingJson.has(Constants.FIELD_SYSTEMURI)
        if (!hasSystemUri) {
            // Ignore external rules/data; save as regular ServiceItem
            log.debug "Case 1: Creating ServiceItem. ${listingJson.title} ${listingJson.uuid}"
            newListing = new ServiceItem()
            bindAndSaveServiceItem(listingJson, newListing, true)
        }
        else {
            log.debug "Case 2: Creating ExtServiceItem. ${listingJson.title} ${listingJson.uuid}"
            // Apply all ExtServiceItem rules using the ExtServiceItemService
            def importItem = new JSONObject(
                    serviceItem: listingJson
            )
            JSONDecoratorService.postProcessJSON(importItem)
            def tmpName = accountService.getLoggedInUsername()
            newListing = extServiceItemService.create(importItem, tmpName, true, contextPath)
        }
        if (this.importRatings) {
            importItemComments(listingJson, newListing)
        }

        importRejection(listingJson, newListing)

        return newListing
    }


    def updateFromJSONAndSave(def currentListing, def listingJson) {
        def username = accountService.getLoggedInUsername()

        importAssociatedProfilesIfNecessary(listingJson)

        // See if listing is from the same Storefront
        if (listingJson.has(Constants.FIELD_SYSTEMURI)) {
            // Listing received is a different SystemUri -- expect external listing (ExtServiceItem) update
            ExtServiceItem extServiceItem = currentListing // Force an exception if the current listing is not an external listing
            log.debug "Case 5: Updating ExtServiceItem:: $extServiceItem"
            updateExtServiceItem(extServiceItem, listingJson, username)
        }
        else {
            if (currentListing instanceof ExtServiceItem) {
                log.debug "Case 7: Updating ExtServiceItem:: $currentListing"
                log.warn "Import received matching an ExtServiceItem on UUID [${currentListing.uuid}] but with no systemUri; updating based on uuid"
                updateExtServiceItem(currentListing, listingJson, username)
            }
            else {
                log.debug "Case 8: Updating ServiceItem:: $currentListing"
                // Ignore external rules/data; save as regular ServiceItem
                bindAndSaveServiceItem(listingJson, currentListing, false)
            }
        }
        if (this.importRatings) {
            importItemComments(listingJson, currentListing)
        }
        return currentListing
    }

    def updateExtServiceItem(ExtServiceItem currentListing, def listingJson, def adminName) {
        // ExtServiceItemService expects these as singular objects
        //   add a serviceItem name to this
        def importItem = new JSONObject(
                serviceItem: listingJson
        )
        JSONDecoratorService.postProcessJSON(importItem)
        // Apply all ExtServiceItem rules using the ExtServiceItemService
        extServiceItemService.update(currentListing.id, importItem, adminName, contextPath)
    }

    def bindAndSaveServiceItem(def json, def serviceItemIn, def createFlag) {
        // Ensure that we are not importing ratings or comments
        // But don't overwrite if updating an existing listing
        json.remove('totalComments')
        json.remove('avgRate')
        json.remove('totalVotes')

        def username = accountService.getLoggedInUsername()
        def importFlag = true
        if (serviceItemIn.instanceOf(ExtServiceItem)) {
            extServiceItemService.bindFromJSON2(json, serviceItemIn, username, createFlag, importFlag, contextPath)
        }
        else {
            extServiceItemService.bindServiceItemFromJSON(json, serviceItemIn, username, createFlag, importFlag, contextPath)
        }

        serviceItemService.save(serviceItemIn)

        serviceItemIn = ServiceItem.get(serviceItemIn.id)
        boolean approved = serviceItemIn.approvalStatus == Constants.APPROVAL_STATUSES['APPROVED']

        def insideOutsideBehavior = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR)
        // AML-1546's logic only applies if inside/outside is admin-selected.
        if (insideOutsideBehavior == Constants.INSIDE_OUTSIDE_ADMIN_SELECTED) {
            // Note: AML-1546 has changed the behavior of import. Since serviceitems are created with isOutside =  null
            // we now explicitly set isOutside to false for all listings, except for unapproved listings where isOutside is null
            if (approved) {

                // AML-1546 for approved listings, set isOutside to false
                serviceItemService.setInsideOutside(serviceItemIn, username, false);
            } else {
                // AML-1546 for non-approved listings, leave as is if isOutside is null, otherwise set to false
                if (serviceItemIn.isOutside != null) {
                    serviceItemService.setInsideOutside(serviceItemIn, username, false);
                }
            }
        } else {
            serviceItemService.setRequiredInsideOutsideIfNeeded(serviceItemIn, username)
        }

        if (approved) {
            serviceItemService.approve(serviceItemIn, username);
        }
    }

    def importAssociatedProfilesIfNecessary(def listingJson) {
        if (!profileImporter.importAll) {
            def associatedProfiles = getAssociatedProfiles(listingJson, profileImporter.profiles)
            profileImporter.importFromJSONArray(associatedProfiles, profileImporter.stats)
            profileImporter.profiles?.removeAll(associatedProfiles)
        }
    }

    def getAssociatedProfiles(JSONObject listingJson, def profilesJson) {
        def profilesToKeep = []
        def serviceItemOwners = []

        if (listingJson.has("owners")) {
            serviceItemOwners = listingJson.owners
        } else if (listingJson.has("owner")) { // For files exported before marketplace 7.4
            serviceItemOwners = [listingJson.owner]
        }

        def ownerIds = serviceItemOwners.collect { it.id }
        profilesToKeep.addAll(profilesJson.findAll { it.id in ownerIds})

        if (this.importRatings) {
            def authorIds = listingJson.itemComments.collect { it.author.id }
            profilesToKeep.addAll(profilesJson.findAll { it.id in authorIds })
        }

        profilesToKeep.unique()

        return profilesToKeep
    }

    protected def handleException(Exception e, def actionVerb, def json) {
        log.debug "#### Error message: ${e.message}"
        log.debug "#### Error toString(): ${e.toString()}"
        def err = e.toString()
        def errMsg = []
        errMsg << "Error $actionVerb listing [${json?.title ?: json?.launchUrl}]: "

        // Try to identify ValidationExceptions and get info on actual field errors
        //   Pattern: rejected value[\[\]\w\s'\.:]+;
        if (err?.indexOf("ValidationException") >= 0) {
            // Use Regex to find names of invalid fields
            //TODO Future -- can the extServiceItemService actually return this stuff??
            def matcher = err =~ /(Field error.*?rejected value \[.*?\]);/

            if (matcher?.size() > 0) {
                errMsg = matcher.inject(["Validation error: "]) { acc, it ->
                    acc + "${it[1]}; "  // Add invalid field name
                }
            }
            else {
                // No field-level validation message - take direct message if available;
                //    last resort, take general Exception print, which is not great
                if (e.message?.size() > 0) {
                    errMsg << "Validation error: ${e.message}"
                }
                else {
                    errMsg << err
                }
            }
        }
        else {
            // Give a basic idea of the problem on the detailed error message
            //   Truncate to some reasonable size for UI display however
            def msg = e.message?.size() > 0 ? e.message : err
            errMsg << (msg.size() > MAX_IMPORT_ERRORMSG_PER_LISTING_LEN ?
                msg[0..MAX_IMPORT_ERRORMSG_PER_LISTING_LEN - 4] + "..." : msg)
        }

        log.debug "#### Aggregated error message:\n${errMsg.join()}:: $err"

        // Clear the Hibernate session to avoid changes which should not be committed;
        //   these will also cause unsaved transient objects errors
        ServiceItem.withSession { session ->
            try {
                session.clear()
            }
            catch (Exception exc) {
                log.error "Error clearing HBM session: $exc"
            }
        }
        return errMsg.join(' ')
    }

    @Override
    protected boolean shouldUpdate(JSONObject json, existingMatch) {
        ServiceItem existingServiceItem = existingMatch as ServiceItem
        // Try using last activity date
        def lastActivityString = json.lastActivity?.activityDate
        if (lastActivityString && lastActivityString != JSONObject.NULL) {
            try{
                Date lastActivityDate = dateFormat.parse(lastActivityString)
                return (lastActivityDate > existingServiceItem.lastActivityDate)
            }
            catch (ParseException e) {
                log.warn("Invalid last activity date ${json.lastActivity.activityDate}", e)
            }
        }
        return super.shouldUpdate(json, existingMatch)
    }

    protected void importRejection(JSONObject json, ServiceItem svc) {
        //if the listing is currently rejected, create all necessary domain objects
        if (svc.approvalStatus == Constants.APPROVAL_STATUSES['REJECTED']) {
            RejectionJustification justification
            Profile author
            String description

            if (!json?.has('rejectionListing')) {
                justification = description = null
                author = profileService.findByUsername(accountService.loggedInUsername)
            }
            else {
                def rejectionJson = json.rejectionListing
                description = rejectionJson.description
                justification =
                    RejectionJustification.findByTitle((String)rejectionJson.justification?.title)

                //attempt to use the author from the rejectionListing.  If they are not in the
                //system, fall back to using the current user
                author = profileService.findByUsername((String)rejectionJson.author?.username) ?:
                    profileService.findByUsername(accountService.loggedInUsername)
            }


            RejectionListing rejectionListing = new RejectionListing(
                author: author,
                description: description,
                justification: justification
            )
            svc.addToRejectionListings(rejectionListing)

            RejectionActivity activity = new RejectionActivity(
                author: author,
                rejectionListing: rejectionListing,
                activityDate: new Date(),
                serviceItemVersion: svc.version,
                serviceItem: svc
            )

            svc.addToServiceItemActivities(activity)
            svc.lastActivity = activity
        }
    }

    protected void importItemComments(def listingJson, ServiceItem serviceItem) {
        if (listingJson.has("itemComments")) {
            listingJson.itemComments.each { itemCommentJson ->
                ItemComment existingComment = serviceItem?.itemComments.find {
                    it?.author?.username == itemCommentJson?.author?.username
                }

                boolean addComment = (existingComment == null)
                if (existingComment) {
                    Date editedDate = null
                    if (!itemCommentJson.isNull('editedDate')) {
                        editedDate = dateFormat.parse(itemCommentJson.editedDate)
                    }
                    // Compare the comment dates
                    if (editedDate && existingComment.editedDate < editedDate) {
                        itemCommentService.deleteItemComment(existingComment, serviceItem)
                        addComment = true
                    }
                }
                if (addComment) {
                    // The service creates a new comment and updates derived service item fields such as total votes and rate counts
                    itemCommentService.saveItemComment(
                        [
                            serviceItemId: serviceItem.id,
                            username: itemCommentJson?.author?.username,
                            commentTextInput: itemCommentJson.text,
                            newUserRating: itemCommentJson.rate == JSONObject.NULL ? '' : itemCommentJson.rate
                        ]
                    )
                }
            }
        }
    }
}
