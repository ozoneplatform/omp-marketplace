package marketplace.rest

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import org.springframework.security.access.AccessDeniedException

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import marketplace.ServiceItem
import marketplace.RejectionListing
import marketplace.Types
import marketplace.Profile
import marketplace.Constants

import marketplace.AccountService

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin(DomainClassUnitTestMixin)
class RejectionListingRestServiceUnitTest {
    GrailsApplication grailsApplication

    def currentUser
    def isAdmin = true

    private ServiceItem makeServiceItem() {
        ServiceItem serviceItem = new ServiceItem(
            title: "test service item",
            description: "a test service item",
            launchUrl: "https://localhost/asf",
            versionName: '1',
            owners: [currentUser],
            types: new Types(title: 'test type'),
            approvalStatus: Constants.APPROVAL_STATUSES['IN_PROGRESS'],
            version: 1
        )
        serviceItem.id = 1

        return serviceItem
    }

    private RejectionListing makeRejectionListing() {
        new RejectionListing(description: 'desc')
    }

    private createGrailsApplication() {
        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.refresh()

        //necessary to get reflection-based marshalling to work
        grailsApplication.addArtefact(RejectionListing.class)
        grailsApplication.addArtefact(ServiceItem.class)
    }

    private RejectionListingRestService createService(
            ServiceItemRestService serviceItemRestService) {
        createGrailsApplication()
        def service = new RejectionListingRestService(grailsApplication, serviceItemRestService)

        service.profileRestService = [
            getCurrentUserProfile: { currentUser }
        ] as ProfileRestService

        service.accountService = [
            checkAdmin: { if (!isAdmin) throw new AccessDeniedException() }
        ] as AccountService

        return service
    }

    void setUp() {

        currentUser = new Profile(username: 'admin')

        FakeAuditTrailHelper.install()

        mockDomain(RejectionListing.class)
        mockDomain(ServiceItem.class)

        def serviceItem = makeServiceItem()
        serviceItem.save(failOnError: true)

    }

    void testCreateFromParentIdAndDto() {
        Long serviceItemId = 1
        RejectionListing rl = makeRejectionListing(), rejectedListing

        def serviceItemRestServiceMock = mockFor(ServiceItemRestService)
        serviceItemRestServiceMock.demand.canView(1..1000) { si -> true }
        serviceItemRestServiceMock.demand.reject(1..1) { si, passedRejectionListing ->
            assert si == ServiceItem.get(1)
            rejectedListing = passedRejectionListing
        }

        def service = createService(serviceItemRestServiceMock.createMock())

        RejectionListing created = service.createFromParentIdAndDto(serviceItemId, rl)

        assert created.serviceItem == ServiceItem.get(serviceItemId)
        assert created.author == currentUser
        assert rejectedListing == created
    }
}
