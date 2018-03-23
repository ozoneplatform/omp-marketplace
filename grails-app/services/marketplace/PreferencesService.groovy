package marketplace

import grails.gorm.transactions.NotTransactional
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware


@Transactional
class PreferencesService extends OzoneService implements ApplicationContextAware {

    ApplicationContext applicationContext

    AccountService accountService

    @NotTransactional
    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext
    }

    void enableAnimations(String enableAnimations) {
        def user = accountService.getLoggedInUser()
        if (!user) return

        log.debug("Setting user enable animation preference to ${enableAnimations} for ${user}")
        def userDomain = accountService.getUserDomain(user)
        userDomain.setAnimationsEnabled(enableAnimations)
        userDomain.save(failOnError: true)
    }

    def enableSPA(def enableSPA) {
        def user = accountService.getLoggedInUser()
        if (user) {
            log.debug("Setting user enable animation preference to ${enableSPA} for ${user}")
            def userDomain = accountService.getUserDomain(user)
            userDomain.setSPAEnabled(enableSPA)
            userDomain.save(failOnError: true, flush: true)
        }
    }

    @ReadOnly
    def getAnimationsEnabled() {
        def animationsEnabled = null
        try {
            def user = accountService.getLoggedInUser()
            if (user) {
                def userDomain = UserDomainInstance.findByUsername(user.username)
                animationsEnabled = userDomain?.getAnimationsEnabled()
            }
        }
        catch (Exception ex) {
            log.warn("Exception trying to get the animations enabled user preference. Exception: ${ex}")
            ex.printStackTrace()
        }
        log.debug "Getting animations enabled, returning: ${animationsEnabled}"
        return animationsEnabled
    }

    @ReadOnly
    def getSPAEnabled() {
        def spaEnabled = null
        try {
            def user = accountService.getLoggedInUser()
            if (user) {
                def userDomain = UserDomainInstance.findByUsername(user.username)
                spaEnabled = userDomain?.getSPAEnabled()
            }
        }
        catch (Exception ex) {
            log.warn("Exception trying to get the SPA enabled user preference. Exception: ${ex}")
            ex.printStackTrace()
        }
        log.debug "Getting SPA enabled, returning: ${spaEnabled}"
        return spaEnabled
    }

}
