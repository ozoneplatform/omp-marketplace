package marketplace.listing

import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.web.ControllerUnitTestMixin

import grails.converters.JSON
import grails.test.mixin.TestFor
import marketplace.AccountService
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.Types
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.decorator.JSONDecoratorService
import ozone.utils.Utils

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin([ControllerUnitTestMixin, DomainClassUnitTestMixin])
class ListingControllerTests {
    Profile someUser
    Profile someAdmin
    Types someType

    void setUp() {
        def jsonDecoratorServiceMock = mockFor(JSONDecoratorService)
        jsonDecoratorServiceMock.demand.postProcessJSON(0..999) { json -> return json }
        controller.JSONDecoratorService = jsonDecoratorServiceMock.createMock()

        FakeAuditTrailHelper.install()

        mockDomain(ServiceItem)
        mockDomain(Profile)
        someUser = new Profile(username: "someUser")
        someUser.save(failOnError: true)
        someAdmin = new Profile(username: "someAdmin")
        someAdmin.save(failOnError: true)

        mockDomain(Types)
        someType = new Types(title: "Some type", hasLaunchUrl: false)
        someType.save(failOnError: true)
    }

    void testListReturnsAllServiceItems() {
        ServiceItem inProgressListing = new ServiceItem(
                title: "Not submitted",
                owners: [someUser],
                uuid: Utils.generateUUID(),
                types: someType
        )
        inProgressListing.save(failOnError: true)

        def accountServiceMock = mockFor(AccountService)
        accountServiceMock.demand.isAdmin(1) { ->
            return true
        }
        controller.accountService = accountServiceMock.createMock()

        // Since we're running as admin, the list action should return service items, regardless of their status
        this.controller.list()

        JSONObject json = JSON.parse(response.contentAsString)
        assertEquals 1, json.total
        assertEquals 1, json.data.size()
        assertEquals inProgressListing.id, json.data[0].id
    }

    void testListIsOnlyAccessibleByAdmin() {
        ServiceItem inProgressListing = new ServiceItem(
                title: "Not submitted",
                owners: [someUser],
                uuid: Utils.generateUUID(),
                types: someType
        )
        inProgressListing.save(failOnError: true)

        def accountServiceMock = mockFor(AccountService)
        accountServiceMock.demand.isAdmin(1) { ->
            return false
        }
        controller.accountService = accountServiceMock.createMock()

        this.controller.list()

        JSONObject json = JSON.parse(response.contentAsString)
        assertEquals 0, json.total
        assertEquals 0, json.data.size()
    }
}
