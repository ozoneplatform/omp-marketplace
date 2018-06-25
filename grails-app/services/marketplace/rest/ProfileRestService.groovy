package marketplace.rest

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

import marketplace.AccountService
import marketplace.Profile

import static marketplace.rest.DomainReflection.getOriginalValue


@Service
class ProfileRestService extends RestService<Profile> {

    AccountService accountService

    @Autowired
    ProfileRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, Profile.class, null, null)
    }

    //Keep CGLIB happy
    ProfileRestService() {}

    protected void authorizeUpdate(Profile profile) {
        if (!(accountService.isAdmin() || profile.id == accountService.loggedInUserProfileId)) {
            def username = getOriginalValue(profile, 'username', String)
            throw new AccessDeniedException(
                    "Unauthorized attempt to modify profile ${username} by user ${accountService.loggedInUsername}")
        }
    }

    protected void authorizeCreate(Profile profile) {
        if (!accountService.isAdmin()) {
            throw new AccessDeniedException(
                    "Unauthorized attempt to create profile ${profile.username} by user ${accountService.loggedInUsername}")
        }
    }

    @Deprecated
    @Transactional(readOnly=true)
    Profile getCurrentUserProfile() {
        accountService.findLoggedInUserProfile()
    }

}
