package marketplace

import grails.converters.JSON
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.transaction.annotation.Transactional
import ozone.marketplace.domain.ThemeDefinition
import ozone.marketplace.enums.MarketplaceApplicationSetting

import grails.util.Holders

class ThemeService extends OzoneService implements ApplicationContextAware {

    def applicationContext
    def accountService
    def marketplaceApplicationConfigurationService
    def config = Holders.config

    def getImageURL(def params) {
        def imageName = params.img_name
        def returnNullForInvalidURL = params.return_null_for_invalid_url

        def theme = getCurrentTheme()

        //images may be found in two places.  They may be in the images folder under the folder for the current theme.
        //If not, fall back to the image in the themes/common directory
        def imgUrl = "/${theme.base_url}/images/${imageName}"
        //log.debug "getImageURL: trying ${imgUrl}"
        if (applicationContext.getResource(imgUrl).exists()) {
            return imgUrl
        }

        def commonImgUrl = "/themes/common/images/${imageName}"
        //log.debug "getImageURL: trying ${commonImgUrl}"
        if (returnNullForInvalidURL == true) {
            if (applicationContext.getResource(commonImgUrl).exists()) {
                return commonImgUrl
            }
            return null
        }

        return commonImgUrl
    }

    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext
    }

    def getAvailableThemes() {
        //find all files in the webapp with a path like 'themes/*.theme/theme.json'
        applicationContext.getResources('themes/*.theme/theme.json').collect {
            try {
                return getThemeDefinitionFromResource(it)
            }
            catch (ObjectNotFoundException e) {
                return null
            }
        } - null //remove any nulls from the list
    }

    def getTheme(def themeName) {
        def resource = applicationContext.getResource("themes/${themeName}.theme/theme.json")

        if (!(themeName && resource.exists()))
            throw new ObjectNotFoundException("Cannot find the requested CSS theme: ${themeName}")

        else
            return getThemeDefinitionFromResource(resource)
    }

    def getCurrentTheme() {
        def session = getSession()

        // Use regular theme if one is available
        if(session && session.marketplaceLayout != 'widget' && session.regularTheme){
            return session.regularTheme
        }

        // If in MP Widget, use widget theme
        if (session && session.marketplaceLayout == 'widget' && session.widgetTheme) {
            return session.widgetTheme
        }

        // Else, use regular theme if one is available
        if (session && session.regularTheme) {
            return session.regularTheme
        }

        //If there is a stored preference for this user's theme, use that
        def themeName = getUserThemePref()
        def theme = null
        def found = false

        if (themeName) {
            try {
                theme = getTheme(themeName)
                found = true
            }
            catch (ObjectNotFoundException e) {
                log.debug("User theme preference set to non-existent theme ${themeName}")
            }
        }

        if (!found) {
            //fall back to the default theme
            themeName = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_DEFAULT_THEME)
            theme = getTheme(themeName)
        }

        return theme
    }

    @Transactional
    def setCurrentTheme() {

        //If there is a stored preference for this user's theme, use that
        def themeName = getUserThemePref()
        def theme = null

        if (!themeName) {
            //fall back to the default theme
            themeName = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_DEFAULT_THEME)
            setUserThemePref(themeName)
        }

        if (themeName) {
            try {
                theme = getTheme(themeName)
            }
            catch (ObjectNotFoundException e) {
                log.error("User theme preference set to non-existent theme ${themeName}")
            }
        }

        def session = getSession()
        if (session && theme) {
            session.regularTheme = theme
            session.staticImageMap = null
        }
    }

    private ThemeDefinition getThemeDefinitionFromResource(def resource) {
        try {
            new ThemeDefinition(JSON.parse(new InputStreamReader(resource.inputStream)))
        }
        catch (e) {
            log.warn("Error while attempting to read Theme definition in ${resource.getURL()}. Exception: ${e}")

            throw new ObjectNotFoundException('Error retrieving the requested CSS theme')
        }
    }

    def getUserThemePref() {
        def theme = null
        try {
            def user = accountService.getLoggedInUser()
            if (user) {
                def userDomain = UserDomainInstance.findByUsername(user.username)
                theme = userDomain?.getTheme()
            }
        }
        catch (Exception ex) {
            log.warn("Exception trying to get the user's theme. Will use default theme. Exception: ${ex}")
            ex.printStackTrace()
            //log.info('Error getting theme preference', ex)
        }
        log.debug "getUserThemePref: returning theme ${theme}"
        return theme
    }

    @Transactional
    def setUserThemePref(def themeIn) {
        def user = accountService.getLoggedInUser()
        if (user) {
            log.debug("Setting user theme preference to ${themeIn} for ${user}")
            def userDomain = accountService.getUserDomain(user)
            userDomain.setTheme(themeIn)
            userDomain.save(failOnError: true, flush: true)
        }
        def session = getSession()
        if (session) {
            // session.regularTheme will get reset during the next call to getCurrentTheme
            session.regularTheme = null
            session.staticImageMap = null
        }
    }

    private static final String DEFAULT_OWF_THEME_NAME = "a_default"

    /**
     * Maps Marketplace theme to the parameters of the OWF theme. The mapped theme
     * will be used by the Marketplace Widget.
     * @param requestParams Map containing theme information using keys
     * 'themeName', 'themeFontSize' and 'themeContrast'.
     * @return
     */
    @Transactional(readOnly = true)
    ThemeDefinition matchMarketplaceWidgetTheme(requestParams) {
        def availableThemes = getAvailableThemes()
        if (availableThemes.size() == 1) {
            return availableThemes[0]
        }

        String themeName = requestParams.themeName
        if (themeName == DEFAULT_OWF_THEME_NAME) {
//			def isFranchise = marketplaceApplicationConfigurationService.isFranchiseStore()
//            themeName = isFranchise ? 'aml' : 'marketplace'
            // This does not really have meaning any more as either cobalt or gold could be the default theme
            // so just set to the configured default theme
            themeName = marketplaceApplicationConfigurationService.getApplicationConfiguration(MarketplaceApplicationSetting.STORE_DEFAULT_THEME)
        }

        // Try to match by name
        def matchingTheme = availableThemes.find { it.name == themeName }
        log.debug "Name match: ${matchingTheme?.name}"

        // Try matching by combination of font size and contrast
        if (!matchingTheme) {
            int themeFontSize = Integer.parseInt(requestParams.themeFontSize)
            String themeContrast = requestParams.themeContrast

            // Sort all themes by increasing deviation of font size from the given size
            availableThemes.sort { Math.abs(it.font_size - themeFontSize) }

            matchingTheme = availableThemes.find {
                themeContrast == it.contrast &&
                    Math.abs(it.font_size - themeFontSize) <= 2
            }
            log.debug "Font and contrast match: ${matchingTheme?.name}"
        }

        // Use theme with the closest font size
        if (!matchingTheme) {
            matchingTheme = availableThemes.getAt(0)
            log.debug "Font match: ${matchingTheme?.name}"
        }

        log.debug "Updated Widget theme definition to ${matchingTheme?.name}"

        (ThemeDefinition) matchingTheme
    }
}
