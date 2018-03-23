package marketplace.grails.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.AccessControlException
import marketplace.Profile
import marketplace.ProfileService
import marketplace.ServiceItem

import static marketplace.grails.service.DomainBuilders.createProfile


class ProfileServiceSpec
        extends OwfUnitSpecification
        implements ServiceUnitTest<ProfileService>, DataTest
{

    void setupSpec() {
        mockDomain Profile
        mockDomain ServiceItem
    }

    def 'findByUsername'() {
        given:
        def profile = createProfile(username: 'user1')

        expect:
        service.findByUsername(profile.username) == profile
    }

    def 'getCurrentUserProfile'() {
        given:
        def profile = createProfile(username: 'user1')
        service.accountService = mockAccountService(profile.username)

        expect:
        service.getCurrentUserProfile() == profile
    }

    def 'get'() {
        given:
        def profile = createProfile(username: 'user1')

        expect:
        service.get([id: profile.id]) == profile
    }

    def 'total'() {
        given:
        createProfile(username: 'user1')
        createProfile(username: 'user2')
        createProfile(username: 'user3')

        expect:
        service.total() == 3
    }

    def 'getAllowableUser as same user'() {
        given:
        def profile = createProfile(username: 'user1')

        def sessionParams = [username: profile.username, isAdmin: false]

        expect:
        service.getAllowableUser(profile.id, sessionParams) == profile
    }

    def 'getAllowableUser as non-admin user'() {
        given:
        def profile1 = createProfile(username: 'user1')
        def profile2 = createProfile(username: 'user2')

        def sessionParams = [username: profile2.username, isAdmin: false]

        when:
        service.getAllowableUser(profile1.id, sessionParams)

        then:
        thrown(AccessControlException)
    }

    def 'getAllowableUser as admin user'() {
        given:
        def profile1 = createProfile(username: 'user1')
        def profile2 = createProfile(username: 'user2')

        def sessionParams = [username: profile2.username, isAdmin: true]

        expect:
        service.getAllowableUser(profile1.id, sessionParams) == profile1
    }

    def 'getAllowableUser user not found'() {
        expect:
        service.getAllowableUser(1L, [:]) == null
    }

}
