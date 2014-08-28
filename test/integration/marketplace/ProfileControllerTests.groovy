package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.converters.JSON

@TestMixin(IntegrationTestMixin)
class ProfileControllerTests {

    def controller
    def profileService

    void setUp() {
        controller = new ProfileController()
        controller.profileService = profileService
    }

    void testAccessControlUpdateAsUser() {
        def result
        def user = new Profile(username: 'testAdmin1', displayName: 'Test Admin', createdDate: new Date())
        user.save(flush:true)
        controller.params.id = user.id
        controller.session.username = "testUser1"
        controller.session.isAdmin = false
        controller.update()
        result = controller.response.contentAsString
        assert true ==  result.contains("User is not authorized to access")
    }

    // When the displayName of a Profile for the logged in user is updated, then the
    // fullname in the session should get updated.
    void testUpdateDisplayName() {
        def result
        def user = new Profile(username: 'testAdmin1', displayName: 'Batman', createdDate: new Date())
        user.save(flush:true)
        controller.params.id = user.id
        controller.params.displayName = 'Robin'
        controller.session.username = "testAdmin1"
        controller.session.isAdmin = true
        controller.session.fullname = 'Batman'
        controller.update()
        assert 302 == controller.response.status
        assert 'Robin' == controller.session.fullname
        assert controller.flash.message == "update.success"
    }

}
