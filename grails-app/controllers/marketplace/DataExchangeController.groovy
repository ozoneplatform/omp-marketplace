package marketplace

import org.apache.commons.lang.StringEscapeUtils

import java.text.DateFormat
import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.web.json.JSONObject

import grails.converters.JSON

class DataExchangeController extends BaseMarketplaceRestController {

    static final String NO_AGENCY = "ID_NO_AGENCY"

    def dataExchangeService
    def importService
    def importTaskService
    def exportController

    def index = {
        int listCount = ImportTask.list([id: params.id, sort: params.sort, order: params.order]).size()
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def history = ImportTask.list().collect { it.runs }.flatten().sort { a, b ->
            b.runDate <=> a.runDate
        }

        return [
            importTaskInstanceList: ImportTask.list(params),
            importTaskInstanceTotal: listCount,
            importTaskHistory: history
        ]
    }

    def exportListings = {
        forward(controller: "export", action: "exportAll")
    }

    def importFromFile = {
        try {
            log.debug("importFromFile: request = ${request} ${request.getClass()}")
            def f = request.getFile("file")
            if (f && !f.empty) {
                log.debug("f.getSize() = ${f.getSize()}")
                def tmpText = f.inputStream.text

                def results = importService.findDupes(JSON.parse(tmpText))
                results = importService.getRequiredListings(results)
                def agencies = importService.generateAgencyList(results)

                // To avoid issues in the client, we replace the empty agency name with a special string indicating no agency,
                // then switch it back before we submit to the server.
                JSONObject noAgencyEntry = agencies?.find { it.id == "" }
                noAgencyEntry?.id = NO_AGENCY
                noAgencyEntry?.name = "no agency"

                results.put("agencies", agencies)

                if (results) {
                    render(text: results, contentType: "text/plain", encoding: "UTF-8")
                } else {
                    render('{"success":false,"message":"No listings found or imported JSON was incorrectly formatted"}')
                }


            }
            else {
                render('{"success":false,"message":"Error importing: file null or empty"}')
            }
        }
        catch (Exception ex) {
            log.error("Error importing from file", ex)
            render("{\"success\":false,\"message\":\"Error importing: ${StringEscapeUtils.escapeJavaScript(ex.message ?: ex)}\"}")
        }
    }

    def importSave = {
        def results = JSON.parse(params['json'])
        def task = importTaskService.getFileImportTask()
        task.json = results

        def fullContextPath = request.getRequestURL()
        def contextPath = request.contextPath
        fullContextPath = fullContextPath[0..<fullContextPath.indexOf(contextPath)]
        fullContextPath += "${contextPath}"

        // The controller created a faux agency for listings with no agency.  Here we
        // map that back to just a blank agency name
        JSONObject noAgencyEntry = task.json.agencies?.find {it.id == NO_AGENCY}
        noAgencyEntry?.id = ""
        noAgencyEntry?.name = ""

        // Set opensInNewBrowserTab to false if not specified for all listings when importing
        task.json.serviceItems.each {
            if(it.opensInNewBrowserTab == null) {
                it.opensInNewBrowserTab = false
            }
        }

        def stats = importService.execute(task, fullContextPath)

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)

        def returnCode = stats?.success ? response.SC_OK : response.SC_BAD_REQUEST

        def jsonResult = ([
            success: stats.success,
            message: stats,
            date: dateFormat.format(new Date()),
            summary: stats.getSummaryMessage()]
         as JSON)

        renderJSONResult(jsonResult, returnCode)
    }

    def deleteAllResults = {
        def issues
        try {
            importTaskService.deleteAllImportTaskResults()
        } catch (e) {
            log.error("Error deleting all results", e)
            render ([success: false, message: "Error deleting all results: $e"] as JSON)
        }

        if (issues?.size() > 0) {
            render ([success: false, message: "Errors: ${issues.join('; ')}"] as JSON)
        } else {
            redirect(action: "index")
        }
    }


}
