package marketplace

import grails.util.Environment

class ErrorController {
    def serverError = {
        try {
            log.info "serverError: params = ${params}"
            def env = Environment.current;

            def emsg = request?.exception?.message
            def ref = Helper.generateLogReference()
            log.warn("Error reference - ${ref}", request.exception)

            if (env == Environment.PRODUCTION) {
                render(view: '/serverError', model: [errorRef: ref], errorRef: ref)
            } else {
                render(view: '/serverError', model: [errorRef: ref], errorRef: ref)
            }
        }
        catch (Exception ex) {
            log.error('Exception in ErrorController.serverError')
            log.error('ErrorController', ex)
        }
    }

    def maintenanceWarning = {
        render(view: '/maintenanceWarning')
    }
}
