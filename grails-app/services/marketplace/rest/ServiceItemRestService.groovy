package marketplace.rest

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.plugins.executor.PersistenceContextExecutorWrapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

import org.hibernate.SessionFactory

import marketplace.AccountService
import marketplace.Constants
import marketplace.Constants.Action
import marketplace.OwfSyncUtility
import marketplace.Profile
import marketplace.RejectionListing
import marketplace.Relationship
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemSnapshot
import marketplace.State
import marketplace.data.ServiceItemDataService
import marketplace.validator.ServiceItemValidator

import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.marketplace.enums.RelationshipType
import ozone.utils.User
import org.ozoneplatform.appconfig.server.service.api.ApplicationConfigurationService

import static java.util.Collections.emptySet
import static marketplace.ReflectionService.isPropertyChanged


@Service
class ServiceItemRestService extends RestService<ServiceItem> {

    @Autowired ServiceItemDataService serviceItemDataService
    @Autowired ServiceItemActivityInternalService serviceItemActivityInternalService

    @Autowired AccountService accountService
    @Autowired ApplicationConfigurationService marketplaceApplicationConfigurationService
    @Autowired PersistenceContextExecutorWrapper executorService

    @Autowired SessionFactory sessionFactory

    @Autowired
    ServiceItemRestService(GrailsApplication grailsApplication,
                           ServiceItemValidator serviceItemValidator) {
        super(grailsApplication, ServiceItem.class, serviceItemValidator, null)
    }

    //needed for CGLIB
    ServiceItemRestService() {}

    @ReadOnly
    Set<ServiceItem> getAllByAuthor(Profile profile) {
        if (profile == null) return emptySet()

        getAllByAuthorId(profile.id)
    }

    @ReadOnly
    Set<ServiceItem> getAllByAuthorId(Long profileId) {
        if (profileId == null) return emptySet()

        serviceItemDataService.findByOwnerId(profileId)
                              .findAll { canView(it) } as Set
    }

    /**
     * Recursively find all ServiceItems that are required by this one
     */
    @Transactional(readOnly=true)
    Set<ServiceItem> getAllRequiredServiceItemsByParentId(Long id, Boolean blockInsideListings) {
        ServiceItem parent = getById(id)
        Set<ServiceItem> items = getAllRequiredServiceItems(parent, [parent] as Set).findAll { canView(it) }

        return blockInsideListings ? items.findAll { it.isOutside } : items
    }

    /**
     * Find all ServiceItems that require this one. This is not recursive
     */
    @Transactional(readOnly=true)
    Set<ServiceItem> getRequiringServiceItemsByChildId(Long id,
                                                       Boolean blockInsideListings) {
        //ensure that they are allowed to view the child
        ServiceItem child = getById(id)

        Set<ServiceItem> items = ServiceItem.createCriteria().list() {
            relationships {
                eq('relationshipType', RelationshipType.REQUIRE)

                relatedItems {
                    eq('id', id)
                }
            }
        }.grep{ canView(it) }

        return blockInsideListings ? items.grep { it.isOutside } : items
    }

    /**
     * @param ignore ServiceItems not to recurse into. Necessary to prevent infinite recursion
     */
    private Set<ServiceItem> getAllRequiredServiceItems(ServiceItem parent,
            Set<ServiceItem> ignore) {

        Set<ServiceItem> immediateRequired =
            parent.relationships.collect { it.relatedItems }.flatten() - ignore

        Set<ServiceItem> recursiveRequired = immediateRequired.collect {
            getAllRequiredServiceItems(it, (immediateRequired + ignore + parent))
        }.flatten()

        //get all immediate required items, items that those items require, and ignore the
        //current item
        immediateRequired + recursiveRequired - parent
    }

    @Override
    void deleteById(Long id) {
        ServiceItem item = getById(id)
        //Check that the user is authorized to delete before updating relationships
        authorizeUpdate(item)
        updateRelationshipsForDelete(item)

        super.deleteById(id)
    }

    @Override
    protected boolean canView(ServiceItem si) {
        Profile profile = accountService.findLoggedInUserProfile()

        //owners and admins can always view.  For others, there are more rules
        if (accountService.isAdmin() || si.isAuthor(profile)) return true

        //if it is enabled and approved it is visible to everyone
        if (si.isEnabled && isApproved(si)) return true

        //if the user is an extern admin and the original creator it is visible
        //to them, even if they aren't the original owner
        if (accountService.isExtAdmin() && profile.id == si.createdBy) return true

        return false
    }

    private static boolean isApproved(ServiceItem serviceItem) {
        return serviceItem.approvalStatus == Constants.APPROVAL_STATUSES['APPROVED']
    }

    @Override
    protected void authorizeUpdate(ServiceItem existing) {
        Profile profile = accountService.findLoggedInUserProfile()

        //check enabled, approvalStatus, etc
        authorizeView(existing)

        def unauthorized = {
            throw new AccessDeniedException("Unauthorized attempt to modify ServiceItem with " +
                "id ${existing.id} by user ${profile.username}")
        }

        boolean ownerCanAlwaysEdit =
            marketplaceApplicationConfigurationService.is(MarketplaceApplicationSetting.ALLOW_OWNER_TO_EDIT_APPROVED_LISTING)

        //admins can always edit
        if (accountService.isAdmin()) return

        if (accountService.isExtAdmin()) {
            //external admins can edit if they are the original creator or a current owner.
            //This logic was pulled from the old ServiceItemService
            if (existing.createdBy == profile.id || existing.isAuthor(profile)) return

            unauthorized()
        }

        //non-admin, non-owners can never edit
        if (!existing.isAuthor(profile)) {
            unauthorized()
        }
        //owners can edit if the listing is in progress or rejected, or if the
        //ALLOW_OWNER_TO_EDIT_APPROVED_LISTING flag is true
        if (!ownerCanAlwaysEdit && !(existing.statInProgress() || existing.statRejected())) {
            unauthorized()
        }
    }

    @Override
    protected void authorizeCreate(ServiceItem newItem) {
        //anyone can create service items
    }

    @Override
    protected void preprocess(ServiceItem si) {
        super.preprocess(si)
        si.updateInsideOutsideFlag(marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR))
        si.processCustomFields()
        si.checkOwfProperties()
    }

    @Override
    protected void postprocess(ServiceItem updated, boolean isNew) {
        if (isNew) {
            serviceItemActivityInternalService.addServiceItemActivity(updated, Action.CREATED)
        }
        else {
            updateInsideOutsideServiceItemActivity(updated)
            updateHiddenServiceItemActivity(updated)
            updateApprovalStatus(updated)
            serviceItemActivityInternalService.createChangeLog(updated)

            // OP-4599: since item -> owfProperties is not a one-many, we can't
            // depend on orphan-delete here, so explicitly delete the old owfProperties
            if (!updated.types.ozoneAware && updated.owfProperties) {
                updated.owfProperties.delete()
            }
        }

        updateRelationshipsServiceItemActivity(updated)

        //this save prevents occasional TransientObjectExceptions in syncServiceItemWithOwf
        save(updated)

        syncServiceItemWithOwf(updated)
    }

    /**
     * Checks for changes to the approvalStatus flag and reacts accordingly.  This method
     * assumes that the ServiceItemValidator has already bee run against this ServiceItem
     */
    private void updateApprovalStatus(ServiceItem updated) {
        if (!isPropertyChanged(updated, 'approvalStatus')) return

        switch (updated.approvalStatus) {
            case Constants.APPROVAL_STATUSES['PENDING']:
                serviceItemActivityInternalService.addServiceItemActivity(updated, Action.SUBMITTED)
                break
            case Constants.APPROVAL_STATUSES['APPROVED']:
                doApprove(updated)
                break
        //other cases are possible during import. Do nothing
        }
    }


    /**
     * Business Logic that must happen when a listing is approved.  This method does not actually
     * do the approval, since that occurs during the update
     */
    private void doApprove(ServiceItem si) {
        ServiceItemActivity activity =
            serviceItemActivityInternalService.addServiceItemActivity(si, Action.APPROVED)

        accountService.checkAdmin()

        si.approvalDate = activity.activityTimestamp

        // set the OWF approved flag in the stack descriptor if it's a stack
        if (si?.owfProperties?.stackDescriptor) {
            def stackDescriptorJSON = JSON.parse(si.owfProperties.stackDescriptor)
            stackDescriptorJSON.approved = true
            si.owfProperties.stackDescriptor = stackDescriptorJSON.toString();
        }

        if (si.owfProperties?.isStack()) {
            approveStackRequirements(si)
        }
    }

    /**
     * Find child app components to this stack and approve them
     */
    private void approveStackRequirements(ServiceItem si) {
        Set<ServiceItem> toApprove = si.relationships?.relatedItems?.flatten()?.grep { !it.statApproved() }

        toApprove?.each { requiredItem ->
            update(requiredItem, [isOutside     : requiredItem.isOutside ?: false,
                                  approvalStatus: Constants.APPROVAL_STATUSES['APPROVED']], true)
        }
    }

    /**
     * Update the listing to be rejected.  This includes setting the approvalStatus, adding the
     * RejectionListing to the ServiceItem, and creating the RejectionActivity
     */
    void reject(ServiceItem si, RejectionListing rejectionListing) {
        if (si.approvalStatus != Constants.APPROVAL_STATUSES['PENDING']) {
            throw new IllegalArgumentException(
                    "Cannot reject ServiceItem ${si.id} that has approval status of ${si.approvalStatus}")
        }

        accountService.checkAdmin()

        si.approvalStatus = Constants.APPROVAL_STATUSES["REJECTED"]
        serviceItemActivityInternalService.addRejectionActivity(si, rejectionListing)

        if (!si.rejectionListings?.contains(rejectionListing)) {
            si.addToRejectionListings(rejectionListing)
        }

        si.save()
    }

    @Override
    protected void populateDefaults(ServiceItem dto) {
        Profile profile = accountService.findLoggedInUserProfile()?: Profile.getSystemUser()
        User user = accountService.loggedInUser
        dto.with {
            owners = owners?.size() ? owners : [profile]
            techPocs = techPocs?.size() ? techPocs : [profile.username]
            organization = organization ?: user.org
            state = state ?: State.findByTitle("Active")
        }
    }

    /**
     * If necessary, add a service item activity stating that the inside/outside state
     * of the listing was changed.
     *
     * @param oldIsOutside The previous isOutside value for this listing
     * @param curentItem The listing that was updated.  If isOutside on this listing
     * does not match oldIsOutside, a proper ServiceItemActivity will be generated and added
     */
    private void updateInsideOutsideServiceItemActivity(ServiceItem updated) {
        if (!isPropertyChanged(updated, 'isOutside')) return

        def action = Action.valueOf(updated.isOutside ? 'OUTSIDE' : 'INSIDE')
        serviceItemActivityInternalService.addServiceItemActivity(updated, action)
    }

    /**
     * Add a ServiceItemActivity for the changing of the hidden flag (known externally as
     * enabled
     */
    private void updateHiddenServiceItemActivity(ServiceItem updated) {
        if (!isPropertyChanged(updated, 'isEnabled')) return

        def action = Action.valueOf(updated.isEnabled ? 'ENABLED' : 'DISABLED')
        serviceItemActivityInternalService.addServiceItemActivity(updated, action)
    }

    /**
     * Create the appropriate ServiceItemActivities for any relationship changes
     */
    private void updateRelationshipsServiceItemActivity(ServiceItem updated) {
        Set<Relationship> oldRelationships = updated.getOldValue('relationships', Set) ?: new HashSet()
        Set<ServiceItem> oldRelated = oldRelationships.findResults { it.relatedItems }.flatten() as Set
        Set<ServiceItem> newRelated = updated.relationships.collect { it.relatedItems }.flatten() as Set

        Set<ServiceItem> added = newRelated - oldRelated
        Set<ServiceItem> removed = oldRelated - newRelated

        if (added.isEmpty() && removed.isEmpty()) return

        serviceItemActivityInternalService.addRelationshipActivities(updated, added, removed)
    }

    /**
     * Create the necessary changelog entries, and unhook the necessary objects, to allow
     * this service item to be deleted
     */
    private void updateRelationshipsForDelete(ServiceItem item) {
        Set<Relationship> relatedBy = Relationship.findRelationshipsByRelatedItem(item) as Set
        Set<Relationship> relatedTo = item.relationships as Set

        //use a set to ensure no duplicates
        Set<ServiceItem> relatedByServiceItems = relatedBy.collect { it.owningEntity }
        Set<ServiceItem> relatedToServiceItems = relatedTo.collect {
            it.relatedItems
        }.flatten()

        relatedBy.each { it.removeFromRelatedItems(item) }
        relatedTo.each { item.removeFromRelationships(it) }

        //update ServiceItemActivities
        relatedByServiceItems.each {
            serviceItemActivityInternalService.addRelationshipActivities(it, [], [item])
        }
        serviceItemActivityInternalService.addRelationshipActivities(item, [],
            relatedToServiceItems)

        //OP-5334: this save prevents the "collection not processed by flush" exception with Oracle and Postgresql
        item.save(flush: true)

        //unhook all ServiceItemSnapshots
        (ServiceItemSnapshot.findAllByServiceItem(item) as Set).each { it.serviceItem = null }
    }

    private void syncServiceItemWithOwf(ServiceItem item) {
        if (item.isOWFCompatible() && !item.isHidden()) {
            List<String> ozoneUrls = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OWF_SYNC_URLS)?.split(",")
            ozoneUrls.each { ozoneUrl ->
                executorService.execute(OwfSyncUtility.newSyncRequest(ozoneUrl, item.uuid))
            }
        }
    }
}
