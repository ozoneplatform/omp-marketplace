package marketplace.rest

import marketplace.rest.DomainObjectNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.security.access.AccessDeniedException

import marketplace.AccountService

import marketplace.RejectionListing
import marketplace.ServiceItem

@Service
class RejectionListingRestService extends ChildObjectRestService<ServiceItem, RejectionListing> {
    @Autowired ProfileRestService profileRestService
    @Autowired AccountService accountService

    @Autowired
    RejectionListingRestService(GrailsApplication grailsApplication,
            ServiceItemRestService serviceItemRestService) {

        super(ServiceItem.class, 'serviceItem', 'rejectionListings', grailsApplication,
            RejectionListing.class, serviceItemRestService, null, null)
    }

    RejectionListingRestService() {}

    @Override
    protected void postprocess(RejectionListing updated, RejectionListing old = null) {
        updated.author = profileRestService.currentUserProfile
        parentClassRestService.reject(updated.serviceItem, updated)
    }

    @Override
    protected void authorizeCreate(RejectionListing dto) {
        super.authorizeView(dto)
        accountService.checkAdmin()
    }

    @Override
    protected void authorizeUpdate(RejectionListing dto) {
        throw new AccessDeniedException("Cannot update RejectionListings")
    }

    protected RejectionListing getMostRecentRejectionListing(long serviceItemId) {
        List<RejectionListing> rls = RejectionListing
                                        .createCriteria()
                                        .list(sort: 'createdDate', order: 'desc', limit: 1) {
                                            serviceItem {
                                                eq('id', serviceItemId)
                                            }
                                        }
        if(rls.size() > 0)
            rls.get(0)
        else
            throw new DomainObjectNotFoundException(RejectionListing, serviceItemId)

    }
}
