package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import marketplace.Constants
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.Types
import org.grails.web.json.JSONObject
import org.json.JSONArray
import org.springframework.http.HttpStatus

import static marketplace.rest.JsonMatchers.strictlyMatches

@Integration
class ServiceItemRestControllerActivitySpec extends RestSpec {
    Profile admin
    Profile user
    Profile extAdmin

    Types types1
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    ServiceItem serviceItem3

    ServiceItemActivity serviceItemActivity1
    ServiceItemActivity serviceItemActivity2
    ServiceItemActivity serviceItemActivity3

    List<ServiceItemActivity> serviceItemActivities
    List<JSONObject> serviceItemActivitiesJson
    List<ServiceItem> serviceItems
    List<JSONObject> serviceItemsJson

    void setupSpec() {
        marshaller ServiceItem
        marshaller Types
        marshaller ServiceItemActivity
    }

//    @Transactional
//    def createExtAdmin() {
//        def username = 'testExtAdmin1'
//        extAdmin = Profile.findByUsername(username)
//        if(extAdmin == null)
//            extAdmin = new Profile(username: username).save(failOnError: true)
//        serviceItem4 = new ServiceItem([title    : "Item 4",
//                                        types    : types1,
//                                        launchUrl: "http://bazz.biz/url",
//                                        createdBy: extAdmin?.id])
//                .save(failOnError: true)
//
//        serviceItems.push(serviceItem4)
//
//        serviceItemsJson = serviceItems.collect { it.asJSON() }
//    }

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
                                        owners        : admin,
                                        launchUrl: "http://bar.biz/url"])
                .save(failOnError: true)

        serviceItem3 = new ServiceItem([title    : "Item 3",
                                        types    : types1,
                                        owners        : admin,
                                        launchUrl: "http://buzz.biz/url"])
                .save(failOnError: true)

        serviceItems = [serviceItem1, serviceItem2, serviceItem3]

        serviceItemsJson = serviceItems.collect { it.asJSON() }


        serviceItemActivity1 = new ServiceItemActivity([action: Constants.Action.CREATED,
                                                        serviceItem: serviceItem1,
                                                        activityTimestamp:  new Date(System.currentTimeMillis() - 10000),
                                                        author: user]).save(failOnError: true)
        serviceItemActivity2 = new ServiceItemActivity([action: Constants.Action.SUBMITTED,
                                                        serviceItem: serviceItem2,
                                                        activityTimestamp:  new Date(System.currentTimeMillis() - 20000),
                                                        author: admin]).save(failOnError: true)
        serviceItemActivity3 = new ServiceItemActivity([action: Constants.Action.REJECTED,
                                                        serviceItem: serviceItem1,
                                                        activityTimestamp:  new Date(System.currentTimeMillis() - 30000),
                                                        author: admin]).save(failOnError: true)

        serviceItemActivities = [serviceItemActivity1, serviceItemActivity2, serviceItemActivity3]
        serviceItemActivitiesJson = serviceItemActivities.collect { it.asJSON() }
    }


    @Transactional
    def cleanup() {
        ServiceItemActivity.findAll().each { it.delete() }
        ServiceItem.findAll().each { it.delete() }
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }

        logout()
    }

//    @Transactional
//    def removeExtAdmin() {
//        def username = 'testExtAdmin1'
//        extAdmin = Profile.findByUsername(username)
//        extAdmin.delete()
//    }
    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }
    //SERVICE ITEM ACTIVITY TESTS
    def 'index - GET /api/serviceItem/activity'() {
        when:
        RestResponse response = get("/api/serviceItem/activity")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemActivitiesJson)
    }

    def 'show - GET /api/serviceItem/${id}/activity - Should fail when not an owner'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem2.id/activity")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'show - GET /api/serviceItem/${id}/activity - Success when owner'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem1.id/activity")
        serviceItemActivitiesJson.remove(1)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemActivitiesJson)
    }

    def 'show - GET /api/serviceItem/${id}/activity - Success when admin'() {
        given:
        loginAsAdmin()

        when:
        RestResponse response = get("/api/serviceItem/$serviceItem1.id/activity")
        serviceItemActivitiesJson.remove(1)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemActivitiesJson)
    }
}
