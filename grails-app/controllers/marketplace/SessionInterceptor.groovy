package marketplace

import grails.config.Config

import marketplace.configuration.MarketplaceApplicationConfigurationService

import grails.util.Holders


class SessionInterceptor {

    int order = 20

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    AccountService accountService

    Config config = Holders.config

    SessionInterceptor() {
        match controller: 'serviceItem|profile|search', action: '*'
    }

    boolean before() {
        if (!session.searchResultUiViewMode) {
            session.searchResultUiViewMode = 'grid'
        }

        if (config.httpsession.timeout && config.httpsession.timeout != session.getMaxInactiveInterval())
            session.setMaxInactiveInterval((int) 60 * (int) (config.httpsession.timeout))

        SessionInterceptor.log.debug "serviceItem before filter - ${controllerName} ${actionName}"
        boolean isShowAccessAlertRequiredAndNeeded = WebUtil.isShowAccessAlertRequiredAndNeeded()

        // In the case of affiliated marketplaces we may receive a request with an accessType of Developer
        // which we need to change to User.
        if (params.accessType?.equals("Developer")) {
            SessionInterceptor.log.info "sessionCheck before filter - changing params.accessType from Developer to User"
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
                SessionInterceptor.log.debug "MarketplaceLayout: ${session.marketplaceLayout} AccessType: ${session.accessType}"

                boolean returnCode = false
                if (!session.username) {
                    if (!user?.username) {
                        render(view: '/serviceItem/unknownUser')
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
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
