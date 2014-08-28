package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.springframework.web.context.request.RequestContextHolder as RCH

@TestMixin(IntegrationTestMixin)
class ItemCommentServiceTests {

    def serviceItem
    def itemCommentService
    def profileService
    def sessionFactory

    def owner

    void setUp() {
        RCH.currentRequestAttributes().session.isAdmin = true
        RCH.currentRequestAttributes().session.username = "testAdminMock"

        sessionFactory.getCurrentSession().clear()

        for(i in 1..5){
            def profile = new Profile(username: "testRatingUser${i}", displayName: "Marketplace Tester ${i}")
            profile.save(flush:true)
        }

        for(i in 1..10){
            def profile = new Profile(username: "testCommentUser${i}", displayName: "Marketplace Comment Tester ${i}")
            profile.save(flush:true)
        }

        owner = new Profile(username: 'testOwner').save()
    }

    void testRateWithSameSid(){
        def rate
        def serviceItem = new ServiceItem(
            owners: [owner],
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: new Types(title: 'test type').save(failOnError:false),
            title: 'service item',
            launchUrl: 'https:///'
        )
        serviceItem.save(flush:true)
        def itemComment = itemCommentService.saveItemComment([username: "testRatingUser1",
                                            serviceItemId: "${serviceItem.id}",
                                            commentTextInput: "New Comment #1"])

        def range = 1..5
        range.each { newRate ->
            rate = itemCommentService.rate(itemComment, newRate)
        }
        serviceItem = ServiceItem.get(serviceItem.id)
        assert 5 == rate
        assert 5 == serviceItem.avgRate
        assert 5 == calculateAvgRating(serviceItem)
        assert 1 == calculateTotalVote(serviceItem)
        assert 1 == serviceItem.totalVotes
    }

    void testRateWithMultipleUsers(){
        def serviceItem = new ServiceItem(
            owners: [owner],
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: new Types(title: 'test type').save(failOnError:false),
            title: 'service item',
            launchUrl: 'https:///'
        )
        serviceItem.save(flush:true)

        def range = 1..5
        range.each { newRate ->
            itemCommentService.saveItemComment([username: "testRatingUser${newRate}",
                                                serviceItemId: "${serviceItem.id}",
                                                newUserRating: newRate])
        }
        serviceItem = ServiceItem.get(serviceItem.id)
        assert 3 == serviceItem.avgRate
        assert 3 == calculateAvgRating(serviceItem)
        assert 5 == calculateTotalVote(serviceItem)
        assert 5 == serviceItem.totalVotes
    }

    void testInvalidNumberOfStars(){
        def serviceItem = new ServiceItem(
            owners: [owner],
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: new Types(title: 'test type').save(failOnError:false),
            title: 'service item',
            launchUrl: 'https:///'
        )
        serviceItem.save(flush:true)
        def itemComment = itemCommentService.saveItemComment([username: "testRatingUser1",
                                                                serviceItemId: "${serviceItem.id}",
                                                                commentTextInput: "New Comment #1"])

        def rate = itemCommentService.rate(itemComment, 6)
        assert 0 == rate
        assert 0 == serviceItem.totalVotes
        assert 0 == serviceItem.avgRate
    }

    def calculateTotalVote(def _serviceItem){
        return ItemComment.createCriteria().count() {
            serviceItem{
                eq("id",_serviceItem.id)
            }
            gt("rate", Float.valueOf(0))
        }
    }

    def calculateAvgRating(def _serviceItem){
        def crit = ItemComment.createCriteria()
        def itemComment = crit.list() {
            serviceItem{
                eq("id",_serviceItem.id)
            }
            gt("rate", Float.valueOf(0))
        }
        def rateNum = itemComment.size()
        def rateSum = 0
        itemComment.each {rateSum += it.rate}
        Integer avgRate = rateNum==0?0:(rateSum/rateNum)
        return avgRate
    }

    def testYourRating(){
        def serviceItem = new ServiceItem(
            owners: [owner],
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: new Types(title: 'test type').save(failOnError:false),
            title: 'service item',
            launchUrl: 'https:///'
        )
        serviceItem.save(flush:true)

        def range = 1..5
        range.each { newRate ->
            itemCommentService.saveItemComment([username: "testRatingUser${newRate}",
                                                serviceItemId: "${serviceItem.id}",
                                                newUserRating: (newRate%5) + 1])
        }
        assert 2 == itemCommentService.yourRating(profileService.findByUsername("testRatingUser1").id,serviceItem.id)
        assert 3 == itemCommentService.yourRating(profileService.findByUsername("testRatingUser2").id,serviceItem.id)
        assert 4 == itemCommentService.yourRating(profileService.findByUsername("testRatingUser3").id,serviceItem.id)
        assert 5 == itemCommentService.yourRating(profileService.findByUsername("testRatingUser4").id,serviceItem.id)
        assert 1 == itemCommentService.yourRating(profileService.findByUsername("testRatingUser5").id,serviceItem.id)
    }
}
