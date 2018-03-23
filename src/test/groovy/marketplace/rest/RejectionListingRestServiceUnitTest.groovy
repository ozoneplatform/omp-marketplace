package marketplace.rest

import grails.testing.services.ServiceUnitTest

import org.springframework.beans.factory.annotation.Autowired

import marketplace.AccountService
import marketplace.Constants
import marketplace.Profile
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.Types


class RejectionListingRestServiceUnitTest
        extends RestServiceSpecification
        implements ServiceUnitTest<RejectionListingRestService>
{

    Closure doWithSpring() {
        { ->
            auditableDataBindingListener(AuditableDataBindingListener)

            xmlns spock: 'http://www.spockframework.org/spring'

            spock.mock(id: 'accountService', class: AccountService.name)
            spock.mock(id: 'serviceItemRestService', class: ServiceItemRestService.name)
        }
    }

    @Autowired
    @Detached
    ServiceItemRestService serviceItemRestService

    Profile admin1

    def setup() {
        mockDomain RejectionListing
        mockDomain ServiceItem

        admin1 = save createAdmin("testAdmin1")
    }

    void testCreateFromParentIdAndDto() {
        given:
        loggedInAs admin1

        def serviceItem = save makeServiceItem()

        serviceItemRestService.getById(_) >> { Long id -> (id == serviceItem.id) ? serviceItem : null }
        serviceItemRestService.canView(_) >> { ServiceItem item -> true }

        and:
        RejectionListing rejectedListing = null
        serviceItemRestService.reject(_, _) >> { item, passedRejectionListing ->
            assert item == serviceItem
            rejectedListing = passedRejectionListing
        }

        when:
        RejectionListing rejectionListing = new RejectionListing(description: 'desc')

        RejectionListing result = service.createFromParentIdAndDto(serviceItem.id, rejectionListing)

        then:
        result.serviceItem == serviceItem
        result.author == admin1

        result == rejectedListing
    }

    private ServiceItem makeServiceItem() {
        new ServiceItem([title         : "test service item",
                         description   : "a test service item",
                         launchUrl     : "https://localhost/asf",
                         versionName   : '1',
                         owners        : [admin1],
                         types         : new Types(title: 'test type'),
                         approvalStatus: Constants.APPROVAL_STATUSES['IN_PROGRESS']])
    }


}
