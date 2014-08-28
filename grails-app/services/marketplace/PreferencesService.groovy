package marketplace

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.transaction.annotation.Transactional

class PreferencesService extends OzoneService implements ApplicationContextAware {

    def applicationContext
    def accountService

    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext
    }

    @Transactional
    def enableAnimations(def enableAnimations) {
        def user = accountService.getLoggedInUser()
        if (user) {
            log.debug("Setting user enable animation preference to ${enableAnimations} for ${user}")
            def userDomain = accountService.getUserDomain(user)
            userDomain.setAnimationsEnabled(enableAnimations)
            userDomain.save(failOnError: true, flush: true)
        }
    }

    @Transactional
    def enableSPA(def enableSPA) {
        def user = accountService.getLoggedInUser()
        if (user) {
            log.debug("Setting user enable animation preference to ${enableSPA} for ${user}")
            def userDomain = accountService.getUserDomain(user)
            userDomain.setSPAEnabled(enableSPA)
            userDomain.save(failOnError: true, flush: true)
        }
    }

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
