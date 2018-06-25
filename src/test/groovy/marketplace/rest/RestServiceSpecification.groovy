package marketplace.rest

import spock.lang.Specification

import grails.testing.gorm.DataTest
import grails.testing.spring.AutowiredTest
import grails.web.databinding.DataBinder
import org.grails.datastore.gorm.GormEntity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException

import marketplace.AccountService
import marketplace.Profile

import ozone.utils.User


class RestServiceSpecification
        extends Specification
        implements AutowiredTest, DataTest, DataBinder
{

    @Autowired
    @Detached
    AccountService accountService

    protected void loggedInAs(Profile profile, Map userExtra = [:]) {
        def user = new User([username: profile.username, org: "org1"] + userExtra)

        accountService.getLoggedInUser() >> user
        accountService.getProperty('loggedInUser') >> user

        accountService.getLoggedInUsername() >> user.username
        accountService.getProperty('loggedInUsername') >> user.username

        accountService.findLoggedInUserProfile() >> profile

        accountService.getLoggedInUserProfileId() >> profile.id
        accountService.getProperty('loggedInUserProfileId') >> profile.id

        accountService.checkAdmin(*_) >> { List args ->
            def message = firstArg(args, String, "Attempt to access Admin-only functionality")
            if (profile.userRoles != "admin") throw new AccessDeniedException(message)
        }

        accountService.isAdmin() >> { profile.userRoles == "admin" }
        accountService.isExtAdmin() >> { profile.userRoles == "extAdmin" }
    }

    protected Profile createUser(String username) {
        return new Profile(username: username, userRoles: "user")
    }

    protected Profile createAdmin(String username) {
        return new Profile(username: username, userRoles: "admin")
    }

    protected Profile createExtAdmin(String username) {
        return new Profile(username: username, userRoles: "extadmin")
    }

    protected static <T extends GormEntity> T save(T entity) {
        entity.save(failOnError: true)
    }

    protected static <T extends GormEntity> T saveStub(T entity) {
        entity.save(failOnError: true, validate: false)
    }

    static <T> T firstArg(List args, Class<T> type, T defaultValue = null) {
        if (args == null || !(args instanceof List)) return defaultValue

        if (args.size() < 1) return defaultValue

        def value = args.get(0)
        if (value == null) return defaultValue

        type.cast(value)
    }

}
