package marketplace

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.ExportSettings

class ExportController extends BaseMarketplaceRestController {
    def exportService
    def JSONDecoratorService
    def relationshipService

    def index = {}

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    // used for file export
    def exportAll = {
        try {
            ExportSettings settings = ExportSettings.createFromParams(params, relationshipService)
            def model = exportService.retrieveExportData(settings, session.isAdmin, session.username)
            response.contentType = "text/json"
            response.addHeader("Content-Disposition", "attachment; filename=\"results.json")
            // For audit logging
            request.setAttribute("fileName", "results.json");
            def converter = model as JSON
            JSONObject jsonConverterObj = (JSONObject) JSON.parse(converter.toString())
            JSONDecoratorService.postProcessJSON(jsonConverterObj)
            converter = jsonConverterObj as JSON
            // For audit logging
            request.setAttribute("fileSize", converter.toString().getBytes().length)
            response.outputStream << converter
        }
        catch (Exception e) {
            handleException(e, "exportAll")
        }
    }

    def exportData = {
        log.debug("exportData: params = ${params}")
        try {
            ExportSettings settings = ExportSettings.createFromParams(params, relationshipService)
            def model = exportService.retrieveExportData(settings, session.isAdmin, session.username)
            // TODO: don't want to return total!

            def jsonResult = model as JSON
            JSONObject jsonResultObj = (JSONObject) JSON.parse(jsonResult.toString())
            JSONDecoratorService.postProcessJSON(jsonResultObj)
            jsonResult = jsonResultObj as JSON
            // For audit logging
            request.setAttribute("fileSize", jsonResult.toString().getBytes().length)
            request.setAttribute("fileName", "Rendered")
            render jsonResult
        }
        catch (Exception e) {
            handleException(e, "exportData")
        }
    }

}


