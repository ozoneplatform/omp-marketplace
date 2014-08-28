package marketplace

import grails.converters.JSON

import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import ozone.decorator.JSONDecoratorException

class ExtProfileController extends BaseMarketplaceRestController {

    def extProfileService

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    /*
     */
    def create = {
        try {
            def json = request.JSON
            log.debug "create: request=${json}"

            def extSvc = extProfileService.create(json, session.username)
            def jsonResult = ([id: extSvc.id] as JSON)
            renderJSONResult jsonResult, 201
        }
        catch (ConverterException ex) {
            handleExpectedException(ex, "create", 500)
        }
        catch (grails.validation.ValidationException ex) {
            handleExpectedException2(ex, "create", 400)
        }
        catch (PermissionException ex) {
            handleExpectedException(ex, "create", 403)
        }
        catch (JSONDecoratorException ex) {
            handleExpectedException(ex, "create", 403)
        }
        catch (Exception ex) {
            handleException(ex, "create", 500)
        }
    }

    /*
     */
    def update = {

        try {
            def json = request.JSON
            log.debug "update: request=${json}"
            log.debug "update: params=${params}"
            log.debug "update: session=${session}"

            def extSvc = extProfileService.update(params.id, json, session.username)

            renderSimpleResult('Profile successfully updated', 200)
        }
        catch (ConverterException ex) {
            //TODO: which error code should we return?
            handleExpectedException(ex, "update", 500)
        }
        catch (ObjectNotFoundException noe) {
            handleExpectedException(noe, "update", 404)
        }
        catch (grails.validation.ValidationException ex) {
            handleExpectedException2(ex, "update", 400)
        }
        catch (PermissionException ve) {
            handleExpectedException(ve, "update", 403)
        }
        catch (JSONDecoratorException ex) {
            handleExpectedException(ex, "update", 403)
        }
        catch (Exception e) {
            handleException(e, "update", 500)
        }
    }

    def getListAsJSON = {
        if (!params.max) params.max = 100
        def model
        def total
        try {
            def returnValue = extProfileService.list(params)
            model = returnValue.result
            total = returnValue.total
        }
        catch (Exception e) {
            handleException(e, "getListAsJSON")
            return
        }
        renderResult(model, total, 200)
    }

    def getItemAsJSON = {
        def model
        try {
            model = extProfileService.get(params)
            if (!model) {
                handleExpectedException(new Exception("Profile with id " + params.id + " does not exist"), "getItemAsJSON", 404)
                return
            }
        } catch (Exception e) {
            handleException(e, "getItemAsJSON")
            return
        }
        renderResult(model, 1, 200)
    }
}
