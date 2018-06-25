package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import marketplace.Profile
import marketplace.ServiceItemActivity
import org.grails.web.json.JSONObject
import grails.plugins.rest.client.RestResponse

import org.springframework.http.HttpStatus

import marketplace.Constants
import marketplace.ServiceItem
import marketplace.Types

import static marketplace.rest.JsonMatchers.*


@Integration
class ServiceItemRestControllerSpec extends RestSpec {

    Profile admin
    Profile user
    Profile extAdmin

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
    def createExtAdmin() {
        def username = 'testExtAdmin1'
        extAdmin = Profile.findByUsername(username)
        if(extAdmin == null)
            extAdmin = new Profile(username: username).save(failOnError: true)

    }

    @Transactional
    def createExtAdminItem() {
        serviceItem4 = new ServiceItem([title    : "Item 4",
                                        types    : types1,
                                        launchUrl: "http://bazz.biz/url",
                                        owners   : admin,
                                        createdBy: extAdmin?.id])
                .save(failOnError: true)

        serviceItems.push(serviceItem4)

        serviceItemsJson = serviceItems.collect { it.asJSON() }
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
        def username = 'testExtAdmin1'
        extAdmin = Profile.findByUsername(username)
        if(extAdmin)
            extAdmin.delete()
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }

        logout()
    }

    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }

    def 'index - GET /api/serviceItem/'() {
        when:
        RestResponse response = get("/api/serviceItem/")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemsJson)
    }

    def 'show - GET /api/serviceItem/{id} - approved ServiceItem may be viewed by USER'() {
        when:
        RestResponse response = get("/api/serviceItem/${serviceItem1.id}")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemsJson[0])
    }


    def 'show - GET /api/serviceItem/{id} - unapproved ServiceItem may be viewed by ADMIN'() {
        given:
        loginAsAdmin()

        when:
        RestResponse response = get("/api/serviceItem/${serviceItem2.id}")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemsJson[1])
    }

    def 'show - GET /api/serviceItem/{id} - unapproved ServiceItem may NOT be viewed by User'() {
        given:
        loginAsUser()

        when:
        RestResponse response = get("/api/serviceItem/${serviceItem2.id}")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

//        strictlyMatches(response.json, serviceItemsJson[1])
    }

    def 'show - GET /api/serviceItem/{id} - unapproved ServiceItem may be viewed by ExtAdmin whom created it'() {
        given:
        createExtAdmin()
        loginAsExtAdmin()
        createExtAdminItem()

        when:
        RestResponse response = get("/api/serviceItem/${serviceItem4.id}")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemsJson[3])
    }


    def 'show - GET /api/serviceItem/{id} - not found'() {
        when:
        RestResponse response = get("/api/serviceItem/-1")

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def 'create - POST /api/serviceItem/'() {
        when:
        def serviceItem = [title    : "My Item",
                           types    : [id: types1.id],
                           launchUrl: "http://xyzzy.biz/url"]

        RestResponse response = post("/api/serviceItem/", serviceItem)

        then:
        response.statusCode == HttpStatus.CREATED

        matches(response.json) { serviceItem + hasId() }

        countServiceItems() == serviceItems.size() + 1
    }

    def 'update - PUT /api/serviceItem/{id} - allow admin to edit'() {
        given:
        loginAsAdmin()

        when:
        def serviceItem = [id   : serviceItem1.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem1.id", serviceItem)

        then:
        response.statusCode == HttpStatus.OK

        matches(response.json) {
            serviceItemsJson[0] + [title: 'Updated Service Item Title',
                                   launchUrl: 'http://barbaz.biz/url', lastActivity: [activityTimestamp: isNotNull()]] + hasAuditStamp(user, admin)
        }
    }

    def 'update - PUT /api/serviceItem/{id} - allow extadmin to edit own listing'() {
        given:
        createExtAdmin()
        loginAsExtAdmin()
        createExtAdminItem()

        when:
        def serviceItem = [id   : serviceItem4.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem4.id", serviceItem)

        then:
        response.statusCode == HttpStatus.OK

        matches(response.json) {
            serviceItemsJson[3] + [title: 'Updated Service Item Title',
                                   launchUrl: 'http://barbaz.biz/url', lastActivity: [activityTimestamp: isNotNull()]] + hasAuditStamp(extAdmin, extAdmin)
        }
    }

    def 'update - PUT /api/serviceItem/{id} - do not allow extadmin to edit other listing'() {
        given:
        createExtAdmin()
        loginAsExtAdmin()
        createExtAdminItem()

        when:
        def serviceItem = [id   : serviceItem1.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem1.id", serviceItem)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'update - PUT /api/serviceItem/{id} - do not allow non-author user to edit'() {
        when:
        def serviceItem = [id   : serviceItem2.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem2.id", serviceItem)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'update - PUT /api/serviceItem/{id} - allow author to edit listing'() {
        when:
        def serviceItem = [id   : serviceItem1.id,
                           title: 'Updated Service Item Title',
                           launchUrl: 'http://barbaz.biz/url']

        def response = put("/api/serviceItem/$serviceItem1.id", serviceItem)

        then:
        response.statusCode == HttpStatus.OK

        matches(response.json) {
            serviceItemsJson[0] + [title: 'Updated Service Item Title',
                                   launchUrl: 'http://barbaz.biz/url', lastActivity: [activityTimestamp: isNotNull()]] + hasAuditStamp(user, user)
        }
    }

    def 'delete - DELETE /api/serviceItem/{id} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def response = delete("/api/serviceItem/$serviceItem1.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countServiceItems() == serviceItems.size() - 1
    }

    def 'delete - DELETE /api/serviceItem/{id} - with User role should fail'() {
        given:
        loginAsUser()

        when:
        def response = delete("/api/serviceItem/$serviceItem2.id")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countServiceItems() == serviceItems.size()
    }

    def 'delete - DELETE /api/serviceItem/{id} - with User role can delete own'() {
        given:
        loginAsUser()

        when:
        def response = delete("/api/serviceItem/$serviceItem1.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countServiceItems() == serviceItems.size() - 1
    }

    def 'delete - DELETE /api/serviceItem/{id} - with ExtAdmin role should fail'() {
        given:
        createExtAdmin()
        loginAsExtAdmin()
        createExtAdminItem()

        when:
        def response = delete("/api/serviceItem/$serviceItem1.id")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countServiceItems() == serviceItems.size()
    }

    def 'delete - DELETE /api/serviceItem/{id} - with ExtAdmin role can delete own'() {
        given:
        createExtAdmin()
        loginAsExtAdmin()
        createExtAdminItem()

        when:
        def response = delete("/api/serviceItem/$serviceItem4.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countServiceItems() == serviceItems.size() - 1
    }
}
