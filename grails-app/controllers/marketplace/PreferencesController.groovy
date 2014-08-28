package marketplace

import grails.converters.JSON

class PreferencesController extends BaseMarketplaceRestController {

    def preferencesService

    def enableAnimations = {
        log.debug("enableAnimations: params = ${params}")
        try {
            preferencesService.enableAnimations(params.enableAnimations)
            render ([
                success: true
            ] as JSON)
        }
        catch (Exception e) {
            handleException(e,"enableAnimations")
        }
    }

    def enableSPA = {
        log.debug("enableSPA: params = ${params}")
        try{
            preferencesService.enableSPA(params.enableSPA)
            render ([
                success: true
            ] as JSON)
        }
        catch (Exception e) {
            handleException(e,"enableSPA")
        }
    }

    def getAnimationsEnabled = {
        render ([
            animationsEnabled: preferencesService.getAnimationsEnabled()
        ] as JSON)
    }
}
