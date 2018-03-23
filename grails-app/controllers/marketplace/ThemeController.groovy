package marketplace

import grails.converters.JSON

class ThemeController extends BaseMarketplaceRestController {

    ThemeService themeService

    def getImageURL = {
        log.debug "getImageURL: params = ${params}"
        params.img_name
        def imageURL = themeService.getImageURL(params)
        redirect(uri: imageURL ?: '/theme')
        return
    }

    def getAvailableThemes = {
        try {
            def model = []
            def result = themeService.getAvailableThemes()
            result.collect { model.add(it) }
            def total = result.size()
            def jsonResult = (result as JSON)
            log.info("getAvailableThemes - returning ${jsonResult}")
            renderJSONResult(jsonResult, 200)
        }
        catch (Exception e) {
            handleException(e, "getAvailableThemes")
        }

    }

    def selectTheme = {
        log.debug("selectTheme: params = ${params}")
        try {
            themeService.setUserThemePref(params.theme)
            render ([success: true] as JSON)
        }
        catch (Exception e) {
            handleException(e, "getAvailableThemes")
        }
    }

    // Enables javascript to access the current theme
    def getCurrentTheme = {
        render ([currentTheme: themeService.getCurrentTheme()] as JSON)
    }
}
