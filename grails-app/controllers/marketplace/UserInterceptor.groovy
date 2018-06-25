package marketplace

import grails.config.Config
import grails.util.Environment
import grails.util.Holders

import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.apache.commons.lang.StringEscapeUtils

import ozone.utils.User

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

/**
 * UserInterceptor - setup the session for the current user.
 * Determine if the user is an Admin using the AccountService.
 */
class UserInterceptor {

    int order = 0

    AccountService accountService

    ProfileService profileService

    ThemeService themeService

    PreferencesService preferencesService

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    Config config = Holders.config

    UserInterceptor() {
        match(controller: '*', action: '*').excludes(controller:'config')
    }

    boolean before() {
        log.debug "loginCheck before filter - ${controllerName} ${actionName}"
        log.debug "Request URL: ${session.request.requestURL}"
        log.debug "Session ID: ${session.request.requestedSessionId}"

        WebUtil.showAccessAlert = marketplaceApplicationConfigurationService.is(ACCESS_ALERT_ENABLED)
        WebUtil.accessAlertMsg = StringEscapeUtils.escapeJavaScript(marketplaceApplicationConfigurationService.valueOf(ACCESS_ALERT_CONTENT))

        //AML-1403.  Set the session to whatever the widget param is, if its null
        if (session.widget == null) session.widget = params.widget

        setDefaultLayout(params, session)

        session.scoreCardEnabled = marketplaceApplicationConfigurationService.is(SCORE_CARD_ENABLED)

        def animationsEnabled = preferencesService.getAnimationsEnabled()
        if(animationsEnabled != null)
            session.animationsEnabled = animationsEnabled
//        session.animationsEnabled = preferencesService.getAnimationsEnabled()

        //Warning: without this, this loginCheck filter will always return true...
        if (shouldDisplayAccessAlert()) {
            redirectToAccessAlert()
            return true
        }

        User user = accountService.getLoggedInUser()
        if (!user) { return true }

        log.debug "user.username: ${user.username} user.name: ${user.name}"

        if (!session.username) {
            session.username = user.username
        }

        if (session.loggedIn) return true

        UserAccount userAccount = accountService.loginAccount(user.username)

        Profile profile = findOrCreateUserProfile(user)

        session.userAccountID = userAccount.id
        session.profileID = profile.id
        session.contactUs = config.contactEmailAddress

        //Load the current theme into the session
        themeService.setCurrentTheme()
        log.debug "Set User [${user.username} user.name: ${user.name}] Current Theme in Session"

        if (user.username) {
            session.username = user.username
            session.fullname = user.name

            log.debug "loginCheck - set session.fullname = ${user.name}"
            log.debug "${accountService.getLoggedInUserRoles()} ${accountService.isAdmin()}"

            if (!session.isAdmin) {
                if (accountService.isAdmin()) {
                    session.isAdmin = true
                    session.accessType = Constants.VIEW_ADMIN
                   log.debug "${user.username} is an Admin and so view is Admin"
                }
                else {
                    session.isAdmin = false
                    session.accessType = Constants.VIEW_USER
                }
            }
        }

        session.loggedIn = true

        return true
    }

    boolean after() {
        true
    }

    private boolean isProfileAccessAlertAction() {
        controllerName == "profile" && actionName == "showAccessAlert"
    }

    private boolean shouldDisplayAccessAlert() {
        if (getControllerNamespace() == 'api') return false

        if (isProfileAccessAlertAction()) return false

        boolean displayAccessAlertForRequest = WebUtil.shouldDisplayAcessAlertForRequest()
        if (!displayAccessAlertForRequest) return false

        return WebUtil.isAccessAlertEnabled(
                ["checkAndSetParams_accessAlertShown": "true",
                 "params_accessAlertShown"           : "${params.accessAlertShown}"])
    }

    private Profile findOrCreateUserProfile(User user) {
        Date currDate = new Date()
        def profile = Profile.findByUsername(user.username, [cache: true])

        if (!profile) {
            return profileService.createProfile(user, currDate)
        }

        if (Environment.current == Environment.PRODUCTION) {
            profileService.updateProfile(profile, user, currDate)
        }

        return profile
    }

    private void redirectToAccessAlert() {
        WebUtil.prepRedirectToShowAccessAlert()

        if (params.widget) {
            redirect(controller: "profile", action: "showAccessAlert", params: [widget: true])
            return
        }

        redirect(controller: "profile", action: "showAccessAlert")
    }


    private void setDefaultLayout(def params, def session) {
        if (params.widget) {
            session.marketplaceLayout = config.marketplace?.is?.franchise?.store ? config.marketplace.layout : 'widget'
        } else {
            session.marketplaceLayout = config.marketplace.layout
        }
    }
}
