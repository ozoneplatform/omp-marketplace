package marketplace.grails.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.AccountService
import marketplace.PreferencesService
import marketplace.UserDomainInstance

import ozone.utils.User

import static marketplace.grails.service.DomainBuilders.createUserDomainInstance


class PreferencesServiceSpec
        extends OwfUnitSpecification
        implements ServiceUnitTest<PreferencesService>, DataTest
{

    void setupSpec() {
        mockDomain UserDomainInstance
    }

    User user1
    UserDomainInstance userDomainInstance

    AccountService accountService = Mock()

    void setup() {
        user1 = new User(username: "user1")
        userDomainInstance = createUserDomainInstance(user1.username)
        accountService.getLoggedInUser() >> user1
        accountService.getUserDomain(user1) >> userDomainInstance

        service.accountService = accountService
    }

    def 'enableAnimations'() {
        given:
        assert !userDomainInstance.animationsEnabled

        when:
        service.enableAnimations('true')

        then:
        userDomainInstance.animationsEnabled
    }

    def 'getAnimationsEnabled when false'() {
        expect:
        !service.getAnimationsEnabled()
    }

    def 'getAnimationsEnabled when true'() {
        given:
        userDomainInstance.setAnimationsEnabled('true')
        userDomainInstance.save(flush: true)

        expect:
        service.getAnimationsEnabled()
    }

}
