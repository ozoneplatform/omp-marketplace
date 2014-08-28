package marketplace

import grails.util.Holders
import grails.web.JSONBuilder
import org.apache.commons.lang.exception.ExceptionUtils
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.enums.MarketplaceApplicationSetting
import grails.converters.JSON

import javax.servlet.http.HttpServletResponse

class BaseMarketplaceRestController {

    def config = Holders.config
    def JSONDecoratorService

    def marketplaceApplicationConfigurationService


    protected handleException(Exception e, method) {
        handleException(e, method, HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    }

    protected handleException(Exception e, method, statusCode) {
        if (!statusCode) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
        def ref = Helper.generateLogReference()
        def jsonResult = "Error occurred during ${method}. Error reference = ${ref}"
        log.error(jsonResult, e)
        renderResult(jsonResult, statusCode)
    }

    protected handleExpectedException(Exception e, method, statusCode) {
        String message = ExceptionUtils.getRootCauseMessage(e)
        def jsonResult = "Error occurred during ${method}. ${message}"
        log.info(jsonResult)
        renderResult(jsonResult, statusCode)
    }

    protected handleMarketplaceException(ozone.marketplace.domain.ValidationException ve, method, statusCode) {
        def message
        if (ve.fieldErrors) {
            message = ve.fieldErrors.allErrors.collect { g.message(error: it) }
        } else {
            message = ExceptionUtils.getRootCauseMessage(ve)
        }
        log.info("handleMarketplaceException(ozone.marketplace.domain.ValidationException): ${message}")
        def jsonResult = "Error occurred during ${method}. ${message}"

        renderResult(jsonResult, statusCode)
    }

    protected handleExpectedException2(grails.validation.ValidationException ve, method, statusCode) {
        log.info("handleExpectedException2: ${ve.message}")

        def message = ve.errors.allErrors.collect { g.message(error: it) }
        log.warn message
        def jsonResult = "Error occurred during ${method}. ${message}"

        renderResult(jsonResult, statusCode)
    }

    protected renderResult(Object result, int statusCode) {
        renderResult(result, 1, statusCode)
    }

    protected renderJSONResult(def jsonResult, int statusCode) {
        response.status = statusCode
        render jsonResult
    }

    protected renderResult(Object result, int total, int statusCode) {
        response.status = statusCode
        def jsonResult

        // validate json
        try {
            if (total == -1) {
                jsonResult = result as JSON
            } else {
                jsonResult = [total: total, data: result] as JSON
            }
            JSONObject jsonResultObj = (JSONObject) JSON.parse(jsonResult.toString())
            JSONDecoratorService.postProcessJSON(jsonResultObj)
            jsonResult = jsonResultObj as JSON
        }
        catch (Exception e) {
            if (result.getClass().name == "org.codehaus.groovy.runtime.GStringImpl" ||
                    result.getClass().name == "java.lang.String") {
                jsonResult = "\"${result.encodeAsJavaScript()}\""  // need to escape stuff so parser doesn't choke
            }
        }

        if (params.windowname) {
            model: [value: jsonResult, status: statusCode]
            render(view: '/show-windowname', model: [value: jsonResult, status: statusCode])
        } else if (params.callback) {
            // Process cross-domain scripting request
            def jsCall = params.callback + '(' + jsonResult + ');'
            render(contentType: "text/javascript", text: jsCall)
        } else {
            render jsonResult
        }
    }

    protected renderSimpleResult(def resultStr, int statusCode) {
        response.status = statusCode
        def jsonResult = "\"${resultStr.encodeAsJavaScript()}\""  // need to escape stuff so parser doesn't choke
        render jsonResult
    }

    protected getMessage(ve) {
        if (ve.message)
            return message(code: ve.message, args: ve.args)
    }

    private def getFieldErrorsAsJSON(errs) {
        if (!errs)
            return '{}'
        def sw = new StringWriter()
        def jb = new JSONBuilder(sw)
        jb.json {
            success(false)

            errors
                {
                    errs.each { error ->
                        def fe = error.getFieldError()
                        def arguments = Arrays.asList(fe.getArguments());
                        println "FE CODE: ${fe.getCode()}"
                        errors(id: fe.getField(), msg: message(code: fe.getCode(), args: arguments))
                    }
                }
        }
        sw.toString()
    }

    protected void prepDefaultParamsForIndex(def params) {
        if (params) {
            if (!params.queryString) params.queryString = ""
            if (!(params.offset instanceof String) || !(params.offset?.isNumber())) params.offset = "0"
            if (!params.max) params.max = config.marketplace.defaultSearchPageSize

            if (!params.sort) params.sort = "score"

            if (!(params.order instanceof String) || !(params.order ==~ /desc|asc|auto/)) {
                params.order = "auto"
            }
            if (!params.author) params.author = true
            if (!params.description) params.description = true
            if (!params.newsearch) params.newsearch = "new"
            if (!params.title) params.title = true
            params.accessType = session.accessType ?: Constants.VIEW_USER
            params.username = session.username
            params.categoryIDs = params.categoryIDs ? new ArrayList<String>(JSON.parse(params.categoryIDs)) : null
            params.typeIDs = params.typeIDs ? new ArrayList<String>(JSON.parse(params.typeIDs)) : null
            params.stateIDs = params.stateIDs ? new ArrayList<String>(JSON.parse(params.stateIDs)) : null
            params.statuses = params.statuses ? Constants.translateApprovalStatusList(new ArrayList<String>(JSON.parse(params.statuses))) : null
        }
    }


}
