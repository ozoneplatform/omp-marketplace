package marketplace.listing

import grails.converters.JSON
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import marketplace.AccountService
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.Types
import org.grails.web.json.JSONObject
import ozone.decorator.JSONDecoratorService
import ozone.utils.Utils
import spock.lang.Specification

class ListingControllerSpec extends Specification implements ControllerUnitTest<ListingController>, DataTest{
    Profile someUser
    Profile someAdmin
    Types someType

    void setup() {
        controller.JSONDecoratorService = Mock(JSONDecoratorService) {
            postProcessJSON(*_) >> { json -> return json}
        }

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
        when:
        ServiceItem inProgressListing = new ServiceItem(
                title: "Not submitted",
                owners: [someUser],
                uuid: Utils.generateUUID(),
                types: someType
        )
        inProgressListing.save(failOnError: true)

        controller.accountService = Mock(AccountService) {
            isAdmin(*_) >> true
        }

        // Since we're running as admin, the list action should return service items, regardless of their status
        this.controller.list()

        JSONObject json = JSON.parse(response.contentAsString)
        then:
        1 == json.total
        1 == json.data.size()
        inProgressListing.id == json.data[0].id
    }

    void testListIsOnlyAccessibleByAdmin() {
        when:
        ServiceItem inProgressListing = new ServiceItem(
                title: "Not submitted",
                owners: [someUser],
                uuid: Utils.generateUUID(),
                types: someType
        )
        inProgressListing.save(failOnError: true)

        controller.accountService = Mock(AccountService) {
            isAdmin(*_) >> false
        }

        this.controller.list()

        JSONObject json = JSON.parse(response.contentAsString)
        then:
        0 == json.total
        0 == json.data.size()
    }
}
