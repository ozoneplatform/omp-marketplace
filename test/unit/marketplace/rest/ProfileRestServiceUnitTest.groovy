package marketplace.rest

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import org.springframework.security.access.AccessDeniedException

import marketplace.Profile
import marketplace.UserDomainInstance

import marketplace.AccountService

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin(DomainClassUnitTestMixin)
class ProfileRestServiceUnitTest {
    GrailsApplication grailsApplication

    ProfileRestService service

    def currentUser

    Profile admin1, admin2, user1, user2

    private createGrailsApplication() {
        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.refresh()

        //necessary to get reflection-based marshalling to work
        grailsApplication.addArtefact(Profile.class)
    }

    private Profile makeProfile(username, id) {
        def profile = new Profile(username: username)
        profile.id = id

        return profile
    }

    void setUp() {
        admin1 = makeProfile('testAdmin1', 1)
        admin2 = makeProfile('testAdmin2', 2)
        user1 = makeProfile('testUser1', 3)
        user2 = makeProfile('testUser2', 4)

        FakeAuditTrailHelper.install()

        mockDomain(UserDomainInstance.class)
        mockDomain(Profile.class, [admin1, admin2, user1, user2])

        createGrailsApplication()

        service = new ProfileRestService(grailsApplication)

        service.accountService = [
            isAdmin: { currentUser.username.toLowerCase().contains('admin') },
            getLoggedInUsername: { currentUser.username }
        ] as AccountService
    }

    void testAuthorizeUpdate() {
        currentUser = admin1


        //edit self as admin - should succeed
        service.updateById(admin1.id, makeProfile('testAdmin11', admin1.id))

        //edit other as admin - should succeed
        service.updateById(admin2.id, makeProfile('testAdmin22', admin2.id))

        currentUser = user1

        //edit self as user, should succeed
        service.updateById(user1.id, makeProfile('testUser11', user1.id))

        //edit other as user, should fail
        shouldFail(AccessDeniedException) {
            service.updateById(user2.id, makeProfile('testUser22', user2.id))
        }
    }

    void testAuthorizeCreate() {
        currentUser = admin1

        //create not allowed at all
        shouldFail(AccessDeniedException) {
            service.createFromDto(makeProfile('willFail', 10))
        }
    }

    void testGetCurrentUserProfile() {
        currentUser = admin1
        assert service.currentUserProfile == currentUser

        currentUser = user1
        assert service.currentUserProfile == currentUser
    }
}
