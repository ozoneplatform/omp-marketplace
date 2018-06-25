package marketplace

import grails.converters.JSON

class AffiliatedMarketplaceController extends BaseMarketplaceRestController {

    AffiliatedMarketplaceService affiliatedMarketplaceService

    /***
     * List AS JSON
     */
    def listAsJSON = {
        def json
        try {
            def items = affiliatedMarketplaceService.listAsJSON(params)
            json = [
                success: true,
                totalCount: items.totalCount,
                data: items.ampList.collect { amp ->
                    assert amp instanceof AffiliatedMarketplace
                    amp.asJSON("${request.contextPath}", true)
                }
            ]
        }
        catch (Exception e) {
            def ref = Helper.generateLogReference()
            log.error "Error listing as JSON affiliatedMarketplace. ${ref}"
            def errorMsg = message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getMessage()}", "Reference = ${ref}"])
            log.error errorMsg
            json = [
                success: false,
                msg: errorMsg
            ]
        }
        render (json as JSON)
    }

    /***
     * CREATE an Affiliate Marketplace Instance
     */
    def create = {
        def json

        log.debug "create: params = ${params}"

        try {
            def affiliatedMarketplace = affiliatedMarketplaceService.buildItemFromParams(params, request)
            if (!params.returnInstance) {
                json = [
                    success: true,
                    totalCount: affiliatedMarketplace ? 1 : 0,
                    data: affiliatedMarketplace.collect { amp ->
                        amp.asJSON("${request.contextPath}")
                    }
                ]
            } else {
                return affiliatedMarketplace
            }
        } catch (Exception e) {
            if (!params.returnInstance) {
                def ref = Helper.generateLogReference()
                log.error "Error deleting affiliatedMarketplace. ${ref}"
                def errorMsg = message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getMessage()}", "Reference = ${ref}"])
                log.error errorMsg
                json = [
                    success: false,
                    msg: errorMsg
                ]
            } else {
                throw e
                return;
            }
        }
        render (json as JSON)
    }

    /***
     * SHOW an Affiliate Marketplace Instance
     */
    def show = {
        def json
        log.debug "show: params = ${params}"
        try {
            def resultMap = affiliatedMarketplaceService.getItemFromParams(params, request)
            if (resultMap.isNewVersion) {
                params.version = "${resultMap.affiliatedMarketplace.version}"
            }
            def affiliatedMarketplace = resultMap?.affiliatedMarketplace
            if (!params.returnInstance) {
                json = [
                    success: true,
                    totalCount: affiliatedMarketplace ? 1 : 0,
                    params: params,
                    data: affiliatedMarketplace.collect { amp ->
                        amp.asJSON("${request.contextPath}")
                    }
                ]
            } else {
                return affiliatedMarketplace
            }
        } catch (Exception e) {
            if (!params.returnInstance) {
                def ref = Helper.generateLogReference()
                log.error "Error showing affiliatedMarketplace. ${ref}"
                def errorMsg = message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getMessage()}", "Reference = ${ref}"])
                log.error errorMsg
                json = [
                    success: false,
                    msg: errorMsg
                ]
            } else {
                throw e
                return
            }
        }
        render (json as JSON)
    }

    /***
     * SAVE an Affiliate Marketplace Instance
     */
    def save = {
        log.info "save: params = ${params}"
        def affiliatedMarketplace, json
        try {
            params.returnInstance = true
            params.active = params.active ? 1 : 0
            if (params.id) {//Performs an update
                affiliatedMarketplace = show()
                affiliatedMarketplace.properties = params
                log.info("Updated affiliated marketplace $affiliatedMarketplace")
            } else {//Performs a save/create
                affiliatedMarketplace = create()
                log.info("Created affiliated marketplace $affiliatedMarketplace")
            }
            params.remove("returnInstance")

            affiliatedMarketplaceService.save(affiliatedMarketplace, request)

            //Saved item will now have an ID
            json = [
                success: true,
                totalCount: affiliatedMarketplace ? 1 : 0,
                data: affiliatedMarketplace.collect { amp ->
                    amp.asJSON("${request.contextPath}", true)
                }
            ]
        } catch (Exception e) {
            def ref = Helper.generateLogReference()
            def errorMsg = message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["Error saving affiliatedMarketplace", "Reference = ${ref}"])
            if (e instanceof grails.validation.ValidationException) {
                log.warn errorMsg
                log.warn message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getFullMessage()}", "Reference = ${ref}"])
            } else {
                log.error errorMsg
                log.error message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getMessage()}", "Reference = ${ref}"])
            }
            json = [
                success: false,
                msg: errorMsg
            ]
        }
        render(text: (json as JSON), contentType: "text/plain", encoding: "UTF-8")
    }

    /***
     * DELETE an Affiliate Marketplace Instance
     */
    def delete = {
        def json
        log.debug "delete: params = ${params}"
        try {
            def resultMap = affiliatedMarketplaceService.getItemFromParams(params, request)
            affiliatedMarketplaceService.delete(resultMap.affiliatedMarketplace)
            json = [
                success: true,
                result: 'success'
            ]
        } catch (Exception e) {
            def ref = Helper.generateLogReference()
            log.error "Error deleting affiliatedMarketplace. ${ref}"
            def errorMsg = message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getMessage()}", "Reference = ${ref}"])
            log.error errorMsg
            json = [
                success: false,
                msg: errorMsg
            ]
        }
        render (json as JSON)
    }

    def defaultMarketplaceIconImage = {
        forward(action: "getMarketplaceIconImage", params: [isDefault: true, retrieveImage: true])
    }

    def getMarketplaceIconImage = {
        def json
        log.debug "getMarketplaceIconImage: params = ${params}"
        try {
            if (!params.contextPath) {
                params.contextPath = request.contextPath
            }
            def iconImageMap = affiliatedMarketplaceService.getMarketplaceIconImage(params)
            if (!params.retrieveImage) {
                json = [
                    result: 'success',
                    success: true,
                    totalCount: iconImageMap ? 1 : 0,
                    data: [
                        url: iconImageMap?.url,
                        contentType: iconImageMap?.contentType,
                        imageSize: iconImageMap?.imageSize,
                        imageId: iconImageMap?.imageId
                    ]
                ]
            } else {
                forward(controller: "images", action: "get", params: [id: iconImageMap?.imageId])
                return
            }
        } catch (Exception e) {
            def ref = Helper.generateLogReference()
            log.error "Error getting icon image for affiliatedMarketplace. ${ref}"
            def errorMsg = message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${e.getMessage()}", "Reference = ${ref}"])
            log.error errorMsg
            def invalidURL = "${request.contextPath}/images/get/null"
            if (!params.retrieveImage) {
                json = [
                    success: false,
                    msg: errorMsg,
                    totalCount: 0,
                    data: [
                        url: invalidURL,
                        contentType: null,
                        imageSize: null,
                        imageId: null
                    ]
                ]
            } else {
                render invalidURL
                return
            }
        }
        render (json as JSON)
    }
}
