package marketplace.rest

import grails.testing.services.ServiceUnitTest

import org.springframework.security.access.AccessDeniedException

import marketplace.AccountService
import marketplace.Profile
import marketplace.UserDomainInstance


class ProfileRestServiceSpec
        extends RestServiceSpecification
        implements ServiceUnitTest<ProfileRestService>
{

    Closure doWithSpring() {
        { ->
            auditableDataBindingListener(AuditableDataBindingListener)

            xmlns spock: 'http://www.spockframework.org/spring'

            spock.mock(id: 'accountService', class: AccountService.name)
        }
    }

    Profile admin1
    Profile admin2
    Profile user1
    Profile user2

    def setup() {
        mockDomain Profile
        mockDomain UserDomainInstance

        admin1 = save createAdmin('testAdmin1')
        admin2 = save createAdmin('testAdmin2')
        user1 = save createUser('testUser1')
        user2 = save createUser('testUser2')
    }

    def "updateById(): admin may update own Profile"() {
        given:
        loggedInAs admin1

        when:
        bindData(admin1, [username: "testAdmin1_new"])
        def result = service.updateById(admin1.id, admin1)

        then:
        notThrown(AccessDeniedException)

        result.username == "testAdmin1_new"
    }

    def "updateById(): admin may update other admin Profile"() {
        given:
        loggedInAs admin1

        when:
        bindData(admin2, [username: "testAdmin2_new"])
        def result = service.updateById(admin2.id, admin2)

        then:
        notThrown(AccessDeniedException)

        result.username == "testAdmin2_new"
    }

    def "updateById(): user may update own Profile"() {
        given:
        loggedInAs user1

        when:
        bindData(user1, [username: "testUser1_new"])
        def result = service.updateById(user1.id, user1)

        then:
        notThrown(AccessDeniedException)

        result.username == "testUser1_new"
    }

    def "updateById(): user may not update other user Profile"() {
        given:
        loggedInAs user1

        when:
        bindData(user2, [displayName: "Test User 2"])
        service.updateById(user2.id, user2)

        then:
        def ex = thrown(AccessDeniedException)
        ex.message == "Unauthorized attempt to modify profile testUser2 by user testUser1"
    }

    def "updateById(): user may not update other user Profile (error contains unmodified username)"() {
        given:
        loggedInAs user1

        when:
        bindData(user2, [username: "testUser2_new"])
        service.updateById(user2.id, user2)

        then:
        def ex = thrown(AccessDeniedException)
        ex.message == "Unauthorized attempt to modify profile testUser2 by user testUser1"
    }

    def "createFromDto(): admin may create Profile"() {
        given:
        loggedInAs admin1

        when:
        def result = service.createFromDto(new Profile(username: "testUser3"))

        then:
        notThrown(AccessDeniedException)

        result.username == "testUser3"
    }

    def "createFromDto(): user may not create Profile"() {
        given:
        loggedInAs user1

        when:
        service.createFromDto(new Profile(username: "testUser3"))

        then:
        thrown(AccessDeniedException)
    }

}
