package marketplace.controller

import grails.testing.mixin.integration.Integration
import marketplace.Profile
import marketplace.ProfileController
import marketplace.controller.ControllerTestMixin
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class ProfileControllerTests extends Specification implements ControllerTestMixin<ProfileController>{

    def profileService

    void setupData() {
        controller.profileService = profileService
    }

    void testAccessControlUpdateAsUser() {
        setupData()
        when:
        def result
        def user = new Profile(username: 'testAdmin2', displayName: 'Test Admin', createdDate: new Date())
        user.save(flush:true, failOnError:true)
        controller.params.id = user.id
        controller.session.username = "testUser1"
        controller.session.isAdmin = false
        controller.request.method = 'POST'
        controller.update()
        def resp = controller.response
        result = controller.response.contentAsString

        then:
        true ==  result.contains("User is not authorized to access")
    }

    // When the displayName of a Profile for the logged in user is updated, then the
    // fullname in the session should get updated.
    void testUpdateDisplayName() {
        setupData()
        when:
        def result
        def user = new Profile(username: 'testAdmin2', displayName: 'Batman', createdDate: new Date())
        user.save(flush:true)
        controller.params.id = user.id
        controller.params.displayName = 'Robin'
        controller.session.username = "testAdmin2"
        controller.session.isAdmin = true
        controller.session.fullname = 'Batman'
        controller.request.method = 'POST'
        controller.update()
        then:
        302 == controller.response.status
        'Robin' == controller.session.fullname
        controller.flash.message == "update.success"
    }

}
