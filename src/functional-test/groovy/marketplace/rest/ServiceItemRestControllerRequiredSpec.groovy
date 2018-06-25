package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import org.grails.web.json.JSONObject
import org.json.JSONArray
import org.springframework.http.HttpStatus

import marketplace.Constants
import marketplace.Profile
import marketplace.Relationship
import marketplace.ServiceItem
import marketplace.Types
import ozone.marketplace.enums.RelationshipType

import static marketplace.rest.JsonMatchers.strictlyMatches

@Integration
class ServiceItemRestControllerRequiredSpec extends RestSpec {
    Profile admin
    Profile user
    Profile extAdmin

    Types types1
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    ServiceItem serviceItem3
    ServiceItem serviceItem4
    ServiceItem serviceItem5
    ServiceItem serviceItem6
    ServiceItem serviceItem7

    Relationship relationship1
    Relationship relationship2
    Relationship relationship3
    Relationship relationship4

    List<JSONObject> serviceItem1Json
    List<JSONObject> serviceItems123Json

    private makeRelationship(related) {
        new Relationship(
            relatedItems: related ?: [],
            // only one RelationshipType
            relationshipType: RelationshipType.REQUIRE
        )
    }

    void setupSpec() {
        marshaller ServiceItem
        marshaller Types
        marshaller Relationship
    }

    @Transactional
    def setup() {
        assert countServiceItems() == 0
        loginAsUser()
        admin = Profile.findByUsername('testAdmin1')
        user = Profile.findByUsername('testUser1')
        types1 = new Types(title: "Types 1").save(failOnError: true)

        // three ServiceItem objects to be required by other ServiceItems.  two owned, one not.
        serviceItem1 = new ServiceItem([title         : "Item 1",
                                        types         : types1,
                                        launchUrl     : "http://foo.biz/url",
                                        owners        : user,
                                        isOutside     : true,
                                        approvalStatus: Constants.APPROVAL_STATUSES['APPROVED']])
                .save(failOnError: true)

        serviceItem2 = new ServiceItem([title    : "Item 2",
                                        types    : types1,
                                        isOutside: true,
                                        owners   : admin,
                                        launchUrl: "http://bar.biz/url"])
                .save(failOnError: true)

        serviceItem3 = new ServiceItem([title    : "Item 3",
                                        types    : types1,
                                        isOutside: true,
                                        owners   : user,
                                        launchUrl: "http://buzz.biz/url"])
                .save(failOnError: true)

        // ServiceItem with no required ServiceItem.
        serviceItem4 = new ServiceItem([title        : "Item 4",
                                        types        : types1,
                                        launchUrl    : "http://diz.biz/url",
                                        owners       : user,
                                        relationships: [] ])
                .save(failOnError: true)

        // ServiceItem with one required ServiceItem.
        relationship1 = makeRelationship([serviceItem1])
        serviceItem5 = new ServiceItem([title        : "Item 5",
                                        types        : types1,
                                        launchUrl    : "http://splish.biz/url",
                                        owners       : user,
                                        relationships: [relationship1] ])
                .save(failOnError: true)

        // ServiceItem with three required ServiceItems in one relationship.
        relationship2 = makeRelationship([serviceItem1, serviceItem2, serviceItem3])
        serviceItem6 = new ServiceItem([title    : "Item 6",
                                        types    : types1,
                                        launchUrl: "http://splash.biz/url",
                                        owners   : user,
                                        relationships: [relationship2] ])
                .save(failOnError: true)

        // ServiceItem with three required ServiceItems in two relationships.
        relationship3 = makeRelationship([serviceItem1, serviceItem2])
        relationship4 = makeRelationship([serviceItem3])
        serviceItem7 = new ServiceItem([title    : "Item 7",
                                        types    : types1,
                                        launchUrl: "http://bath.biz/url",
                                        owners   : admin,
                                        relationships: [relationship3, relationship4] ])
                .save(failOnError: true)

        serviceItem1Json = [serviceItem1].collect { it.asJSON() }
        // ordered by title.
        serviceItems123Json = [serviceItem1, serviceItem2, serviceItem3].collect { it.asJSON() }
    }

    @Transactional
    def cleanup() {
        Relationship.findAll().each { it.delete() }
        ServiceItem.findAll().each { it.delete() }
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }

        logout()
    }

    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }

    //SERVICE ITEM REQUIRED TESTS
    def 'show - GET /api/serviceItem/${id}/requiredServiceItems - Fail when not ServiceItem owner'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem7.id/requiredServiceItems")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'show - GET /api/serviceItem/${id}/requiredServiceItems - Success when owner - no required'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem4.id/requiredServiceItems")

        then:
        response.statusCode == HttpStatus.OK

        response.json.size() == 0
    }
    
    def 'show - GET /api/serviceItem/${id}/requiredServiceItems - Success when owner - one required'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem5.id/requiredServiceItems")

        then:
        response.statusCode == HttpStatus.OK

        response.json.size() == 1
        strictlyMatches(response.json, serviceItem1Json)
    }

    def 'show - GET /api/serviceItem/${id}/requiredServiceItems - Success when admin - one required'() {
        given:
        loginAsAdmin()

        when:
        RestResponse response = get("/api/serviceItem/$serviceItem5.id/requiredServiceItems")

        then:
        response.statusCode == HttpStatus.OK

        response.json.size() == 1
        strictlyMatches(response.json, serviceItem1Json)
    }

    def 'show - GET /api/serviceItem/${id}/requiredServiceItems - Success when owner - three required, two owned, one relationship'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem6.id/requiredServiceItems")

        then:
        response.statusCode == HttpStatus.OK

        response.json.size() == 2
        serviceItems123Json.remove(1)
        strictlyMatches(response.json.sort { it.title}, serviceItems123Json)
    }
    
    def 'show - GET /api/serviceItem/${id}/requiredServiceItems - Success when admin - three required, two relationships'() {
        given:
        loginAsAdmin()

        when:
        RestResponse response = get("/api/serviceItem/$serviceItem7.id/requiredServiceItems")

        then:
        response.statusCode == HttpStatus.OK

        response.json.size() == 3        
        strictlyMatches(response.json.sort { it.title }, serviceItems123Json) 
    }    
}
