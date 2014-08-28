package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*

@TestMixin(IntegrationTestMixin)
class MyUITagLibTests extends GroovyPagesTestCase {

    def itemCommentService
    def profileService
    def tagLib
    def controller

    def owner

    void setUp() {
        profileService = new ProfileService()
        tagLib = new MyUITagLib()
        controller = new ServiceItemController()
        owner = new Profile(username: 'testUser').save()
    }

    void testYourRating() {
        ServiceItem si = new ServiceItem(
            owners: [owner],
            title:"My Widget1",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError:true)
        ).save(failOnError:true)
        for(i in 1..5){
            def profile = new Profile(username: "testRatingUser${i}", displayName: "Marketplace Tester ${i}")
            profile.save(flush:true)
        }
        for (i in 1..5){
            def currAuthor = profileService.findByUsername("testRatingUser${i}")
            def itemComment = new ItemComment(author: currAuthor, serviceItem: si).save(failOnError: true)
            itemCommentService.rate(itemComment,i)
        }

        def template = '<myui:yourRating serviceItemID="${serviceItem.id}">Your rating: ${it}</myui:yourRating>'
        controller.session.profileID = Profile.findByUsername("testRatingUser1").id
        assertOutputEquals('Your rating: 1', template, [serviceItem:si])

        controller.session.profileID = Profile.findByUsername("testRatingUser5").id
        assertOutputEquals('Your rating: 5', template, [serviceItem:si])
    }
}
