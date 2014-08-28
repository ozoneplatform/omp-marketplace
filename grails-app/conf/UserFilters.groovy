import grails.util.Environment
import org.apache.commons.lang.StringEscapeUtils
import static ozone.marketplace.enums.MarketplaceApplicationSetting.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper
import ozone.marketplace.util.event.LogInEvent

import java.util.concurrent.Callable
import org.ozoneplatform.appconfig.server.service.api.ApplicationConfigurationService
import org.springframework.security.access.AccessDeniedException
import marketplace.*

/**
 * UserFilters - setup the session for the current user.
 * Determine if the user is an Admin using the AccountService.
 */
class UserFilters {

    def accountService
    def profileService
    def themeService
    def preferencesService

    def marketplaceApplicationConfigurationService

    def config = ConfigurationHolder.config


    def filters = {
        loginCheck(controller: '*', action: '*') {
            before = {
                UserAccount.withTransaction {
                    log.debug "loginCheck before filter - ${controllerName} ${actionName}"
                    log.debug "Request URL: ${session.request.requestURL}"
                    log.debug "Session ID: ${session.request.requestedSessionId}"

                    WebUtil.showAccessAlert = marketplaceApplicationConfigurationService.is(ACCESS_ALERT_ENABLED)
                    WebUtil.accessAlertMsg =
                        StringEscapeUtils.escapeJavaScript(marketplaceApplicationConfigurationService.valueOf(ACCESS_ALERT_CONTENT))

                    boolean displayAccessAlertForRequest = WebUtil.shouldDisplayAcessAlertForRequest()
                    boolean isAccessAlertEnabled = WebUtil.isAccessAlertEnabled(["checkAndSetParams_accessAlertShown": "true",
                        "params_accessAlertShown": "${params.accessAlertShown}"])
                    log.debug "isAccessAlertEnabled: $isAccessAlertEnabled"

                    //AML-1403.  Set the session to whatever the widget param is, if its null
                    if (session.widget == null)
                        session.widget = params.widget

                    setDefaultLayout(params, session)

                    session.scoreCardEnabled = marketplaceApplicationConfigurationService.is(SCORE_CARD_ENABLED)

                    session.animationsEnabled = preferencesService.getAnimationsEnabled()


                    String cName = "${controllerName}"
                    String aName = "${actionName}"
                    //Warning: without this, this loginCheck filter will always return true...
                    if (displayAccessAlertForRequest && isAccessAlertEnabled &&
                            !(cName.equals("profile") && aName.equals("showAccessAlert"))) {
                        WebUtil.prepRedirectToShowAccessAlert()
                        if (params.widget) {
                            redirect(controller: "profile", action: "showAccessAlert", params: [widget: true])
                        } else {
                            redirect(controller: "profile", action: "showAccessAlert")
                        }
                        return false
                    } else {
                        def user = accountService.getLoggedInUser()
                        if (user) {
                            Date currDate = new Date()

                            if (!session.username) {
                                session.username = user.username
                            }
                            if (!session.loggedIn) {
                                UserAccount userAccount = accountService.loginAccount(user.username)

                                session.userAccountID = userAccount.id

                                // ADDING USER PROFILE....
                                def profile = Profile.findByUsername(user.username, [cache: true])
                                if (!profile) {
                                    profile = profileService.createProfile(user, currDate)
                                }
                                else if (Environment.current == Environment.PRODUCTION) {
                                    profileService.updateProfile(profile, user, currDate)
                                }
                                session.profileID = profile.id

                                log.debug "user.username: ${user.username} user.name: ${user.name}"

                                //Load the current theme into the session
                                themeService.setCurrentTheme()
                                log.debug "Set User [${user.username} user.name: ${user.name}] Current Theme in Session"

                                session.contactUs = config.contactEmailAddress

                                if (user.username) {
                                    session.username = user.username
                                    log.debug "loginCheck - set session.fullname = ${user.name}"
                                    session.fullname = user.name
                                    log.debug "${accountService.getLoggedInUserRoles()} ${accountService.isAdmin()}"

                                    if (!session.isAdmin) {
                                        if (accountService.isAdmin()) {
                                            session.isAdmin = true
                                            session.accessType = Constants.VIEW_ADMIN
                                            log.debug "${user.username} is an Admin and so view is Admin"
                                        } else {
                                            session.isAdmin = false
                                            session.accessType = Constants.VIEW_USER
                                        }
                                    }
                                }
                                session.loggedIn = true
                            }
                        }
                    }
                }
            }
        }


        //It used to be the case we just went into this method after every request.  That was not needed.  Now this will blacklist actions and controllers that do not
         //need this model data to be set.
        setAppConfigsInModel(action:'(performDoList|image|listAsJSON|setResultUiViewSettings|handle|getSearchResults|delete*|get*|update*)', invert: true){

            after = {model ->

                //Sometimes an action gets through but we know the controller would not need this model data set.
                if(controllerName in ['config','preferences','jaxrs', 'theme','itemComment']){
                    return
                }

                if(!model) model = [:]

                model.isFranchiseStore        = marketplaceApplicationConfigurationService.isFranchiseStore()
                model.footerFeaturedText      = marketplaceApplicationConfigurationService.valueOf(STORE_FOOTER_FEATURED_TITLE)
                model.footerFeaturedContent   = marketplaceApplicationConfigurationService.valueOf(STORE_FOOTER_FEATURED_CONTENT)
                model.logoUrl                 = marketplaceApplicationConfigurationService.valueOf(STORE_LOGO)
                model.openSearchTitleMessage = marketplaceApplicationConfigurationService.valueOf(OPEN_SEARCH_TITLE_MESSAGE)
                model.allowImageUpload = marketplaceApplicationConfigurationService.valueOf(ALLOW_IMAGE_UPLOAD)
                model.contactEmailAddress = marketplaceApplicationConfigurationService.valueOf(STORE_CONTACT_EMAIL)
                model.defaultAffiliatedMarketplaceTimeout = marketplaceApplicationConfigurationService.valueOf(AMP_SEARCH_DEFAULT_TIMOUT)
                model.ownerCanEditTheirApprovedListings = marketplaceApplicationConfigurationService.valueOf(ALLOW_OWNER_TO_EDIT_APPROVED_LISTING)
            }
        }


        sessionCheck(controller: 'serviceItem|profile|search', action: '*') {
            before = {
                if (!session.searchResultUiViewMode) {
                    session.searchResultUiViewMode = 'grid'
                }

                if (config.httpsession.timeout && config.httpsession.timeout != session.getMaxInactiveInterval())
                    session.setMaxInactiveInterval((int) 60 * (int) (config.httpsession.timeout))

                log.debug "serviceItem before filter - ${controllerName} ${actionName}"
                boolean isShowAccessAlertRequiredAndNeeded = WebUtil.isShowAccessAlertRequiredAndNeeded()

                // In the case of affiliated marketplaces we may receive a request with an accessType of Developer
                // which we need to change to User.
                if (params.accessType?.equals("Developer")) {
                    log.info "sessionCheck before filter - changing params.accessType from Developer to User"
                    params.accessType = "User";
                }

                if (isShowAccessAlertRequiredAndNeeded) {
                    String cName = "${controllerName}"
                    String aName = "${actionName}"
                    if (!(cName.equals("profile") && aName.equals("showAccessAlert"))) {
                        return false
                    }
                } else {
                    // If a call to SearchController is forwarded from ExternalAccessController, bypass the user identification
                    if (params.controller != 'externalAccess') {
                        def user = accountService.getLoggedInUser()

                        if (params.widget) {
                            if (params.themeName) {
                                // This was used to match the owf theme... we are bypassing for now and keeping the
                                // selections independent
//                                session.widgetTheme = themeService.matchMarketplaceWidgetTheme(params)
//                                session.widgetTheme = themeService.getCurrentTheme()
                                session.staticImageMap = null
                            }
                        }
                        log.debug "MarketplaceLayout: ${session.marketplaceLayout} AccessType: ${session.accessType}"

                        boolean returnCode = false
                        if (!session.username) {
                            if (!user.username) {
                                render(view: 'unknownUser')
                            } else {
                                session.username = user.username
                                returnCode = true
                            }
                        } else {
                            returnCode = true
                        }

                        return returnCode
                    } else {
                        return true
                    }
                }

            }
        }
        adminAllCheck(
            controller: '(administration|category|types|state|text|customFieldDefinition|rejectionJustification|importTask|export|scoreCardItem|userAccount|intentAction|intentDataType|contactType)',
            action: '(index|list|show|delete|softDelete|edit|editme|update|updateme|create|createme|save|saveme|imageDelete|importAll|importFromFile|getTasks|getTaskResults|exportAll|execute)'
        ) {
            before = {
                adminCheck('adminAllCheck', delegate)
            }
        }
        adminServiceItemCheck(controller: 'serviceItem', action: '(adminView|adminList)') {
            before = {
                adminCheck('adminServiceItemCheck', delegate)
            }
        }
        adminApplicationConfigurationCheck(controller: 'applicationConfiguration', action: 'show') {
            before = {
                adminCheck('adminApplicationConfigurationCheck', delegate)
            }
        }
        adminProfileListCheck(controller: 'profile', action: 'list|detail') {
            before = {
                adminCheck('adminProfileListCheck', delegate)
            }
        }
        adminDataExchangeCheck(controller: 'dataExchange', action: '*') {
            before = {
                adminCheck('dataExchange', delegate)
            }
        }
        //TODO: originally tried this with controller applicationConfiguration|profile and invert - however it seems to force
        //the filter to the top of the chain which is not what we want here.
        storeNameCheck(controller: '*') {
            before = {
                if (!(controllerName == "profile" && actionName == "showAccessAlert") &&
                    controllerName != "applicationConfiguration" &&
                     controllerName != "error" &&
                     controllerName != "config" &&
                    !WebUtil.isRESTRequest()) {

                    if (marketplaceApplicationConfigurationService.isStoreNameMissing()) {
                        if (session.accessType == Constants.VIEW_USER) {
                            redirect(controller: "error", action: "maintenanceWarning")
                            return false
                        } else {
                            delegate.flash.message = 'application.configuration.store.name.empty'
                            redirect(controller: "applicationConfiguration", action: "show")
                            return false
                        }
                    }
                }
            }
        }
    }

    private def adminCheck(filterName, filterDelegate) {
        log.debug "${filterName} before filter - ${filterDelegate.controllerName} ${filterDelegate.actionName}"

        if (WebUtil.isShowAccessAlertRequiredAndNeeded()) {
            log.debug "${filterName} filter - returning due to show Access Alert"
            return false;
        }
        boolean returnCode = false
        def username = accountService.getLoggedInUsername()
        filterDelegate.session.username = username

        if (!filterDelegate.session.isAdmin) {
            if (accountService.isAdmin()) {
                filterDelegate.session.isAdmin = true
                filterDelegate.session.accessType = Constants.VIEW_ADMIN // "Administrator"
                log.info "AdminCheck Filter${username} is an Admin"
                returnCode = true
            } else {
                log.info "Filter: ${username} is not an admin"
                returnCode = false
                if(filterDelegate.session.spaEnabled == true) {
                    filterDelegate.redirect(uri: '/spa')
                }
                else {
                    filterDelegate.flash.message = 'admin.access.denied'
                    filterDelegate.redirect(controller: "serviceItem", action: "shoppe")
                }
            }
        } else {
            returnCode = true
        }
        return returnCode
    }

    private void setDefaultLayout(def params, def session) {
        if (params.widget) {
            session.marketplaceLayout = config.marketplace?.is?.franchise?.store ? config.marketplace.layout : 'widget'
        } else {
            session.marketplaceLayout = config.marketplace.layout
        }
    }
}
