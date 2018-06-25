package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import marketplace.Constants
import marketplace.ItemComment
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.Types
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus
import static marketplace.rest.JsonMatchers.*

@Integration
class ServiceItemRestControllerItemCommentSpec extends RestSpec{
    Profile admin
    Profile user

    Types types1
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    ServiceItem serviceItem3
    ItemComment itemComment1
    ItemComment itemComment2

    List<ServiceItem> serviceItems
    List<JSONObject> serviceItemsJson
    List<ItemComment> itemComments
    List<JSONObject> itemCommentsJson

    void setupSpec() {
        marshaller ItemComment
        marshaller ServiceItem
        marshaller Types
    }

    @Transactional
    def setup() {
        assert countServiceItems() == 0
        assert countItemComments() == 0
        loginAsUser()
        admin = Profile.findByUsername('testAdmin1')
        user = Profile.findByUsername('testUser1')
        types1 = new Types(title: "Types 1").save(failOnError: true)

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
                                        owners   : admin,
                                        launchUrl: "http://buzz.biz/url"])
                .save(failOnError: true)

        serviceItems = [serviceItem1, serviceItem2, serviceItem3]

        serviceItemsJson = serviceItems.collect { it.asJSON() }

        itemComment1 = new ItemComment([rate: 3.0,
                                        text: "Comment 1",
                                        author: user,
                                        serviceItem: serviceItem1])
        itemComment1.save(failOnError: true)
        sleep(1000)
        itemComment2 = new ItemComment([rate: 1.0,
                                        text: "Comment 2",
                                        author: admin,
                                        serviceItem: serviceItem1])
                .save(failOnError: true)

        itemComments = [itemComment2, itemComment1]

        itemCommentsJson = itemComments.collect { it.asJSON() }
    }


    @Transactional
    def cleanup() {
        ServiceItem.findAll().each { it.delete() }
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }
        ItemComment.findAll().each { it.delete() }
        logout()
    }

    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }

    @ReadOnly
    int countItemComments() {
        ItemComment.count()
    }

    def 'show - GET /api/serviceItem/$id/itemComment'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem1.id/itemComment")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, itemCommentsJson)
    }

    def 'create - POST /api/serviceItem/$id/itemComment'() {
        when:
        def itemComment = [author: [id: user.id], rate: 4.0, text: "Comment 3"]
        RestResponse response = post("/api/serviceItem/$serviceItem1.id/itemComment", itemComment)

        then:
        response.statusCode == HttpStatus.CREATED

        matches(response.json) { itemComment + hasId()}

        countItemComments() == itemComments.size() + 1
    }

    def 'update - PUT /api/serviceItem/{id}/itemComment/{itemCommentId} - allow user to edit own comment'() {
        when:
        def itemComment = [id   : itemComment1.id,
                           text: 'Updated Item Comment',
                           rate: 5.0]

        def response = put("/api/serviceItem/$serviceItem1.id/itemComment/$itemComment1.id", itemComment)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            itemCommentsJson[1] + [text: 'Updated Item Comment',
                                   rate: 5.0] + hasAuditStamp(user, user)
        }
    }

    def 'update - PUT /api/serviceItem/{id}/itemComment/{itemCommentId} - disallow user to edit others comment'() {
        when:
        def itemComment = [id   : itemComment2.id,
                           text: 'Updated Item Comment',
                           rate: 5.0]

        def response = put("/api/serviceItem/$serviceItem1.id/itemComment/$itemComment2.id", itemComment)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'update - PUT /api/serviceItem/{id}/itemComment/{itemCommentId} - allow admin to edit own comment'() {
        given:
        loginAsAdmin()

        when:
        def itemComment = [id   : itemComment2.id,
                           text: 'Updated Item Comment',
                           rate: 5.0]

        def response = put("/api/serviceItem/$serviceItem1.id/itemComment/$itemComment2.id", itemComment)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            itemCommentsJson[0] + [text: 'Updated Item Comment',
                                   rate: 5.0] + hasAuditStamp(user, admin)
        }
    }

    def 'update - PUT /api/serviceItem/{id}/itemComment/{itemCommentId} - allow admin to edit others comment'() {
        given:
        loginAsAdmin()

        when:
        def itemComment = [id   : itemComment1.id,
                           text: 'Updated Item Comment',
                           rate: 5.0]

        def response = put("/api/serviceItem/$serviceItem1.id/itemComment/$itemComment1.id", itemComment)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            itemCommentsJson[1] + [text: 'Updated Item Comment',
                                   rate: 5.0] + hasAuditStamp(user, admin)
        }
    }

    def 'delete - DELETE /api/serviceItem/{id}/itemComment/{itemCommentId} - with User role should fail'() {
        when:
        def response = delete("/api/serviceItem/$serviceItem1.id/itemComment/$itemComment1.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countItemComments() == itemComments.size() - 1
    }

    def 'delete - DELETE /api/serviceItem/{id}/itemComment/{itemCommentId} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def response = delete("/api/serviceItem/$serviceItem1.id/itemComment/$itemComment2.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countItemComments() == itemComments.size() - 1
    }
}
