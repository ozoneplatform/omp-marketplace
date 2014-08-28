package marketplace

import grails.converters.JSON
import grails.util.Environment
import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration
import ozone.marketplace.enums.MarketplaceApplicationSetting

import marketplace.rest.ProfileRestService

class ConfigController {

	MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService
    ProfileRestService profileRestService
    AccountService accountService
    ThemeService themeService
    StateService stateService
    TypesService typesService

    def index = {

        List<ApplicationConfiguration> appconfigs = marketplaceApplicationConfigurationService.getAllApplicationConfigurations()

        Integer affiliatedSearchSize =
            Integer.valueOf(marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.AMP_SEARCH_RESULT_SIZE))

        Profile currUser = profileRestService.currentUserProfile

        String accessAlertMsg = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.ACCESS_ALERT_CONTENT)

        Boolean allowImageUpload =
            Boolean.valueOf(marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.ALLOW_IMAGE_UPLOAD))

        State defaultState = stateService.findByTitle(Constants.DEFAULT_SERVICE_ITEM_STATE)
        List<ContactType> contactTypes = ContactType.list()
        List<Types> types = typesService.getAllTypes()

        render(
            view: '/config_js',
            model: [
                config: [
                    appconfigs: appconfigs.collect { it.asMap() },
                    defaultState: defaultState?.asJSONRef(),
                    types: types.collect { it.asJSON() },
                    contactTypes: contactTypes.collect { it.asJSON() },
                    headerLogoTooltip: g.message(code: "tooltip.ompHeaderLogo"),
                    url: "${request.scheme}://${request.serverName}:${request.serverPort}" +
                        request.contextPath,
                    context: request.contextPath,
                    blankImage: p.imageLink(src: 's.gif').replace("'",""),
                    bannerBeanNorth: myui.bannerBeanNorth().replaceAll("[\\r\\n]","")
                        .replaceAll("\'", ""),
                    bannerBeanSouth: myui.bannerBeanSouth().replaceAll("[\\r\\n]","")
                        .replaceAll("\'", ""),
                    currUser: [
                        displayName: currUser.displayName,
                        username: currUser.username,
                        email: currUser.email,
                        id: currUser.id,
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
                ]
            ]
        )
    }
}
