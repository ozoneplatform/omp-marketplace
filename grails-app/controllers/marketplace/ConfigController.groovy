package marketplace

import grails.util.Environment

import marketplace.configuration.MarketplaceApplicationConfigurationService
import marketplace.rest.ProfileRestService

import ozone.marketplace.enums.MarketplaceApplicationSetting
import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration


class ConfigController {

	MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    ProfileRestService profileRestService

    AccountService accountService

    ThemeService themeService

    StateService stateService

    TypesService typesService

    def config = {

        List<ApplicationConfiguration> appconfigs = marketplaceApplicationConfigurationService.getAllApplicationConfigurations()

        Integer affiliatedSearchSize =
            Integer.valueOf(marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.AMP_SEARCH_RESULT_SIZE))
        //TODO BVEST Profile not present
        Profile currUser = profileRestService.currentUserProfile
//        User currUser = accountService.getLoggedInUser()

        String accessAlertMsg = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.ACCESS_ALERT_CONTENT)

        Boolean allowImageUpload =
            Boolean.valueOf(marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.ALLOW_IMAGE_UPLOAD))

        State defaultState = stateService.findByTitle(Constants.DEFAULT_SERVICE_ITEM_STATE)
        List<ContactType> contactTypes = ContactType.list()
        List<Types> types = typesService.getAllTypes()

        render(
            view: '/config_js',
            model: [
                conf: [
                    appconfigs: appconfigs.collect { it.asMap() },
                    defaultState: defaultState?.asJSONRef(),
                    types: types.collect { it.asJSON() },
                    contactTypes: contactTypes.collect { it.asJSON() },
                    headerLogoTooltip: g.message(code: "tooltip.ompHeaderLogo"),
                    url: "${request.scheme}://${request.serverName}:${request.serverPort}" +
                        request.contextPath,
                    context: request.contextPath,
                    //TODO BVEST High Priority - Theming
                    //blankImage: p.imageLink(src: 's.gif').replace("'",""),
                    bannerBeanNorth: myui.bannerBeanNorth().replaceAll("[\\r\\n]","")
                        .replaceAll("\'", ""),
                    bannerBeanSouth: myui.bannerBeanSouth().replaceAll("[\\r\\n]","")
                        .replaceAll("\'", ""),
                    currUser: [
                        displayName: currUser?.displayName,
                        username: currUser?.username,
                        email: currUser?.email,
                        id: currUser?.id,
                        isAdmin: accountService.isAdmin(),
                        organization: accountService.getLoggedInUser()?.org
                    ],
                    buildNumber: Environment.current == Environment.PRODUCTION ?
                        meta(name: 'app.version') :
                        new Date().time,
                    theme: themeService.getCurrentTheme(),
                    sessionTimeoutConfig: [
                        maxInactiveInterval: session.maxInactiveInterval,
                        settingSessionDataErrorMsg:
                            g.message(code: 'ErrorMessageString.settingSessionDataMsg'),
                        redirectUrl: "${request.contextPath}/logout.jsp"
                    ],
                    affiliatedSearchSize: affiliatedSearchSize,
                    animationsEnabled: session.animationsEnabled,
                    accessAlertConfig: [
                        show: session.showAccessAlert,
                        message: accessAlertMsg
                    ],
                    allowImageUpload: allowImageUpload,
                    importTaskContext: g.createLink(controller:"importTask")
                ],
                contentType: 'text/javascript'
            ]
        )
    }
}
