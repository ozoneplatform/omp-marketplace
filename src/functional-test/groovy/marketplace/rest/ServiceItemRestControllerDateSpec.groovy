package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import marketplace.Profile
import marketplace.ServiceItemActivity
import org.grails.web.json.JSONObject

import org.springframework.http.HttpStatus

import marketplace.Constants
import marketplace.ServiceItem
import marketplace.Types

import static marketplace.rest.JsonMatchers.*


@Integration
class ServiceItemRestControllerDateSpec extends RestSpec {

    Profile admin
    Profile user

    Types types1
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    ServiceItem serviceItem3
    ServiceItem serviceItem4

    List<ServiceItem> serviceItems
    List<JSONObject> serviceItemsJson

    void setupSpec() {
        marshaller ServiceItem
        marshaller Types
    }

    @Transactional
    def setup() {
        assert countServiceItems() == 0
        loginAsUser()
        admin = Profile.findByUsername('testAdmin1')
        user = Profile.findByUsername('testUser1')
        types1 = new Types(title: "Types 1").save(failOnError: true)

        serviceItem1 = new ServiceItem([title         : "Item 1",
                                        types         : types1,
                                        launchUrl     : "http://foo.biz/url",
                                        owners        : user,
                                        releasedDate  : '2018-02-16T23:00:00Z',
                                        approvalStatus: Constants.APPROVAL_STATUSES['APPROVED']])
                .save(failOnError: true)

        serviceItem2 = new ServiceItem([title    : "Item 2",
                                        types    : types1,
                                        owners   : admin,
                                        launchUrl: "http://bar.biz/url"])
                .save(failOnError: true)

        serviceItem3 = new ServiceItem([title    : "Item 3",
                                        types    : types1,
                                        owners   : admin,
                                        launchUrl: "http://buzz.biz/url"])
                .save(failOnError: true)

        serviceItems = [serviceItem1, serviceItem2, serviceItem3]

        serviceItemsJson = serviceItems.collect { it.asJSON() }
    }

    @Transactional
    def cleanup() {
        ServiceItemActivity.findAll().each { it.delete() }
        ServiceItem.findAll().each { it.delete() }
        ServiceItemActivity.findAll().each { it.delete() }
        println(ServiceItemActivity.list() + ' - ' + ServiceItem.list())
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }

        logout()
    }

    @Transactional
    def getServiceItem(Long id) {
        ServiceItem.get(id)
    }

    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }
    
    def 'update - PUT /api/serviceItem/{id} - admin edit - confirm no time shift'() {
        given:
        loginAsAdmin()

        when:
        def originalRelease = serviceItem1.releaseDate
        def serviceItem = [id   : serviceItem1.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem1.id", serviceItem)

        serviceItem1 = getServiceItem(serviceItem1.id)
        then:
        response.statusCode == HttpStatus.OK

        originalRelease == serviceItem1.releaseDate
        matches(response.json) {
            serviceItemsJson[0] + [title: 'Updated Service Item Title',
                                   launchUrl: 'http://barbaz.biz/url',
                                   releasedDate: isNotNull(),
                                   lastActivity: [activityTimestamp: isNotNull()]] + hasAuditStamp(user, admin) 
        }
    }    

    def 'update - PUT /api/serviceItem/{id} - author edit - confirm no time shift'() {
        when:
        def originalRelease = serviceItem1.releaseDate
        def serviceItem = [id   : serviceItem1.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem1.id", serviceItem)
        serviceItem1 = getServiceItem(serviceItem1.id)

        then:
        response.statusCode == HttpStatus.OK
        originalRelease == serviceItem1.releaseDate
        matches(response.json) {
            serviceItemsJson[0] + [title: 'Updated Service Item Title',
                                   launchUrl: 'http://barbaz.biz/url',
                                   releasedDate: isNotNull(),
                                   lastActivity: [activityTimestamp: isNotNull()]] + hasAuditStamp(user, user)
        }
    }
}
