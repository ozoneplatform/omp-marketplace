package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.ItemComment
import marketplace.ItemCommentService
import marketplace.Profile
import marketplace.ProfileService
import marketplace.ServiceItem
import marketplace.domain.builders.DomainBuilderMixin


@Integration
@Rollback
class ItemCommentServiceSpec extends Specification implements DomainBuilderMixin {

    ItemCommentService itemCommentService
    ProfileService profileService

    ServiceItem serviceItem

    List<Profile> ratingUsers = new ArrayList()

    void setupData() {
        for (i in 1..5) {
            ratingUsers << $userProfile { username = "testRatingUser${i}" }
        }

        for (i in 1..10) {
            $userProfile { username = "testCommentUser${i}" }
        }

        def itemOwner = $userProfile { username = 'testOwner' }
        def itemType = $type { title = "Test Type" }

        serviceItem = $serviceItem {
            title = "Service Item"
            type = itemType
            owner = itemOwner
            approvalStatus = APPROVED
        }
    }

    void testRateWithSameSid() {
        setup:
        setupData()

        when:
        def params = [serviceItemId   : "${serviceItem.id}",
                      commentTextInput: "New Comment #1"]

        def itemComment = itemCommentService.saveItemComment(params, "testRatingUser1", false)

        for (newRate in 1..5) {
            itemCommentService.rate(itemComment, newRate)
        }

        flushSession()
        def result = ServiceItem.get(serviceItem.id)

        then:
        result.avgRate == 5
        calculateAvgRating(result) == 5

        result.totalVotes == 1
        calculateTotalVote(result) == 1
    }

    void testRateWithMultipleUsers() {
        given:
        setupData()

        when:
        for (i in 1..5) {
            def params = [serviceItemId: "${serviceItem.id}",
                          newUserRating: i]

            itemCommentService.saveItemComment(params, "testRatingUser${i}", false)
        }

        flushSession()
        def result = ServiceItem.get(serviceItem.id)

        then:
        result.avgRate == 3
        calculateAvgRating(result) == 3

        result.totalVotes == 5
        calculateTotalVote(result) == 5
    }

    void testInvalidNumberOfStars() {
        given:
        setupData()

        when:
        def params = [serviceItemId   : "${serviceItem.id}",
                      commentTextInput: "New Comment #1"]

        def itemComment = itemCommentService.saveItemComment(params, "testRatingUser1", false)

        def rate = itemCommentService.rate(itemComment, 6)

        flushSession()
        def item = ServiceItem.get(serviceItem.id)

        then:
        rate == 0
        item.totalVotes == 0
        item.avgRate == 0
    }

    def testYourRating() {
        given:
        setupData()

        when:
        def range = 1..5
        range.each { newRate ->
            def params = [serviceItemId: "${serviceItem.id}",
                          newUserRating: (newRate % 5) + 1]

            itemCommentService.saveItemComment(params, "testRatingUser${newRate}", false)
        }

        flushSession()

        then:
        itemCommentService.yourRating(ratingUsers[0].id, serviceItem.id) == 2
        itemCommentService.yourRating(ratingUsers[1].id, serviceItem.id) == 3
        itemCommentService.yourRating(ratingUsers[2].id, serviceItem.id) == 4
        itemCommentService.yourRating(ratingUsers[3].id, serviceItem.id) == 5
        itemCommentService.yourRating(ratingUsers[4].id, serviceItem.id) == 1
    }

    private static Integer calculateTotalVote(ServiceItem serviceItem) {
        ItemComment.countByServiceItemAndRateGreaterThan(serviceItem, 0.0)
    }

    private static Integer calculateAvgRating(ServiceItem serviceItem) {
        def comments = ItemComment.findAllByServiceItemAndRateGreaterThan(serviceItem, 0.0)

        double rateSum = 0.0
        for (def comment : comments) {
            rateSum += comment.rate
        }
        int rateNum = comments.size()

        double avgRate = (rateNum == 0) ? 0.0 : rateSum / rateNum

        Math.floor(avgRate).intValue()
    }
}
