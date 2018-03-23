package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import marketplace.Constants
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemTag
import marketplace.Tag
import marketplace.Types
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus
import static marketplace.rest.JsonMatchers.*

@Integration
class ServiceItemRestControllerTagSpec extends RestSpec{
    Profile admin
    Profile user
    Tag tag1
    Tag tag2
    Types types1
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    ServiceItem serviceItem3
    ServiceItemTag serviceItemTag1
    ServiceItemTag serviceItemTag2
    ServiceItemTag serviceItemTag3

    List<ServiceItem> serviceItems
    List<JSONObject> serviceItemsJson
    List<ServiceItemTag> serviceItemTags
    List<JSONObject> serviceItemTagsJson

    void setupSpec() {
        marshaller Tag
        marshaller ServiceItemTag
        marshaller ServiceItem
        marshaller Types
    }

    @Transactional
    def setup() {
        assert countServiceItems() == 0
        assert countServiceItemTags() == 0
        loginAsUser()
        admin = Profile.findByUsername('testAdmin1')
        user = Profile.findByUsername('testUser1')
        types1 = new Types(title: "Types 1").save(failOnError: true)
        tag1 = new Tag(title: "Tag 1").save(failOnError: true)
        tag2 = new Tag(title: "Tag 2").save(failOnError: true)

        serviceItem1 = new ServiceItem([title         : "Item 1",
                                        types         : types1,
                                        launchUrl     : "http://foo.biz/url",
                                        owners        : user])
                .save(failOnError: true)
        serviceItem1.setApprovalDate(new Date(System.currentTimeMillis() - 10000))
        serviceItem1.setApprovalStatus(Constants.APPROVAL_STATUSES['APPROVED'])
        serviceItem1.save(failOnError: true)
        serviceItem2 = new ServiceItem([title    : "Item 2",
                                        types    : types1,
                                        owners   : admin,
                                        launchUrl: "http://bar.biz/url"])
                .save(failOnError: true)

        serviceItem3 = new ServiceItem([title    : "Item 3",
                                        types    : types1,
                                        launchUrl: "http://buzz.biz/url",
                                        owners   : admin,
                                        approvalStatus: Constants.APPROVAL_STATUSES['APPROVED']])
                .save(failOnError: true)

        serviceItems = [serviceItem1, serviceItem2, serviceItem3]

        serviceItemsJson = serviceItems.collect { it.asJSON() }

        serviceItemTag1 = new ServiceItemTag([tag: tag1,
                                            serviceItem: serviceItem1,
                                            createdBy: user])
                .save(failOnError: true)

        serviceItemTag2 = new ServiceItemTag([tag: tag2,
                                              serviceItem: serviceItem1,
                                              createdBy: admin])
                .save(failOnError: true)

        serviceItemTag3 = new ServiceItemTag([tag: tag1,
                                              serviceItem: serviceItem2,
                                              createdBy: user])
                .save(failOnError: true)

        serviceItemTags = [serviceItemTag1, serviceItemTag2, serviceItemTag3]

        serviceItemTagsJson = serviceItemTags.collect { it.asJSON() }
    }


    @Transactional
    def cleanup() {
        ServiceItem.findAll().each { it.delete() }
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }
        Tag.findAll().each { it.delete() }
        ServiceItemTag.findAll().each { it.delete() }

        logout()
    }

    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }

    @ReadOnly
    int countServiceItemTags() {
        ServiceItemTag.count()
    }

    def 'show - GET /api/serviceItem/$id/tag'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem1.id/tag")
        serviceItemTagsJson.remove(2)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItemTagsJson)
    }

    def 'create - POST /api/serviceItem/$id/tag'() {
        when:
        def serviceItemTag = [createdBy: [id: user.id], tag: [id: tag1.id]]
        def testRequest = [[serviceItemId: serviceItem3.id, title: tag1.title]]
        RestResponse response = post("/api/serviceItem/$serviceItem3.id/tag", testRequest)

        then:
        response.statusCode == HttpStatus.CREATED

        matches(response.json[0]) { serviceItemTag + hasId()}

        countServiceItemTags() == serviceItemTags.size() + 1
    }

    def 'delete - DELETE /api/serviceItem/{id}/itemComment/{itemCommentId} - with User role should fail'() {
        when:
        def response = delete("/api/serviceItem/$serviceItem1.id/tag/$tag1.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countServiceItemTags() == serviceItemTags.size() - 1
    }

    def 'delete - DELETE /api/serviceItem/{id}/tag/{itemCommentId} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def response = delete("/api/serviceItem/$serviceItem1.id/tag/$tag1.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countServiceItemTags() == serviceItemTags.size() - 1
    }
}
