package marketplace.rest

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import marketplace.Constants
import marketplace.Profile
import marketplace.RejectionJustification
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.Types
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus

import static marketplace.rest.JsonMatchers.*

@Integration
class ServiceItemRestControllerRejectionListingSpec extends RestSpec {
    Profile admin
    Profile user
    Profile extAdmin

    Types types1
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    ServiceItem serviceItem3
    RejectionListing rejectionListing1
    RejectionListing rejectionListing2
    RejectionJustification rejectionJustification

    List<ServiceItem> serviceItems
    List<JSONObject> serviceItemsJson
    List<RejectionListing> rejectionListings
    List<JSONObject> rejectionListingsJson

    @ReadOnly
    int countServiceItems() {
        ServiceItem.count()
    }

    @ReadOnly
    int countRejectionListings() {
        RejectionListing.count()
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
                                        launchUrl: "http://bar.biz/url",
                                        owners   : admin,
                                        approvalStatus: Constants.APPROVAL_STATUSES['PENDING']])
                .save(failOnError: true)

        serviceItem3 = new ServiceItem([title    : "Item 3",
                                        types    : types1,
                                        launchUrl: "http://buzz.biz/url",
                                        owners   : admin,
                                        approvalStatus: Constants.APPROVAL_STATUSES['PENDING']])
                .save(failOnError: true)

        serviceItems = [serviceItem1, serviceItem2, serviceItem3]

        serviceItemsJson = serviceItems.collect { it.asJSON() }

        rejectionJustification = new RejectionJustification(title: 'Justification 1').save(failOnError: true)

        rejectionListing1 = new RejectionListing([serviceItem: serviceItem3,
                                                 author: user,
                                                 description: "Unapproved Item 3",
                                                 justification: rejectionJustification])
                .save(failOnError: true)

        rejectionListings = [rejectionListing1]
        rejectionListingsJson = rejectionListings.collect { it.asJSON() }
    }

    void setupSpec() {
        marshaller ServiceItem
        marshaller Types
        marshaller RejectionJustification
        marshaller RejectionListing
        marshaller Profile
    }

    @Transactional
    def cleanup() {
        ServiceItem.findAll().each { it.delete() }
        Types.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }
        RejectionJustification.findByCreatedBy(Profile.findByUsername('testUser1').id).each { it.delete() }
        RejectionListing.findAll().each { it.delete() }

        logout()
    }

    def 'index - GET /api/serviceItem/$id/rejectionListing'() {
        when:
        RestResponse response = get("/api/serviceItem/$serviceItem3.id/rejectionListing")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, rejectionListingsJson[0])
    }

    def 'create - POST /api/serviceItem/$id/rejectionListing - Cannot reject approved item'() {
        given:
        loginAsAdmin()

        when:
        def rejectionListing = [author: [id: user.id], description: "Rejected", justification: [id: rejectionJustification.id]]

        RestResponse response = post("/api/serviceItem/$serviceItem1.id/rejectionListing", rejectionListing)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST

        countRejectionListings() == rejectionListings.size()
    }

    def 'create - POST /api/serviceItem/$id/rejectionListing - Can Reject listing that is pending approval'() {
        given:
        loginAsAdmin()

        when:
        def rejectionListing = [author: [id: admin.id], description: "Rejected", justification: [id: rejectionJustification.id]]

        RestResponse response = post("/api/serviceItem/$serviceItem2.id/rejectionListing", rejectionListing)

        then:
        response.statusCode == HttpStatus.CREATED

        matches(response.json) { rejectionListing + hasId() }

        countRejectionListings() == rejectionListings.size() + 1
    }
}
