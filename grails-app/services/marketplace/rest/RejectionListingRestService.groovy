package marketplace.rest

import grails.core.GrailsApplication

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

import marketplace.AccountService
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.rest.DomainObjectNotFoundException


@Service
class RejectionListingRestService extends ChildObjectRestService<ServiceItem, RejectionListing> {

    @Autowired
    AccountService accountService

    @Autowired
    RejectionListingRestService(GrailsApplication grailsApplication,
                                ServiceItemRestService serviceItemRestService)
    {
        super(ServiceItem,
              'serviceItem',
              'rejectionListings',
              grailsApplication,
              RejectionListing,
              serviceItemRestService,
              null, null)
    }

    RejectionListingRestService() {}

    @Override
    protected void postprocess(RejectionListing updated, boolean isNew) {
        updated.author = accountService.findLoggedInUserProfile()
        serviceItemService.reject(updated.serviceItem, updated)
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

    private ServiceItemRestService getServiceItemService() {
        parentClassRestService
    }

}
