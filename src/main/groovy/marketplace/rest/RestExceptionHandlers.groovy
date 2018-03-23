package marketplace.rest

import groovy.transform.CompileStatic

import grails.artefact.controller.support.ResponseRenderer
import grails.util.Environment
import grails.validation.ValidationException
import org.grails.web.json.JSONException

import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException


@CompileStatic
trait RestExceptionHandlers implements ResponseRenderer {

    def handleNotFoundException(DomainObjectNotFoundException ex) {
        render(status: HttpStatus.NOT_FOUND.value(), message: ex.getMessage())
    }

    def handleAccessDeniedException(AccessDeniedException ex) {
        render(status: HttpStatus.UNAUTHORIZED.value(), message: "Access is denied")
    }

    def handleAuthenticationNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        render(status: HttpStatus.UNAUTHORIZED.value(), message: "Access is denied")
    }

    def handleIllegalArgumentException(IllegalArgumentException ex) {
        if (Environment.current != Environment.PRODUCTION) {
            ex.printStackTrace()
        }

        render(status: HttpStatus.BAD_REQUEST.value(), message: ex.getMessage())
    }

    def handleValidationException(ValidationException ex) {
        if (Environment.current != Environment.PRODUCTION) {
            ex.printStackTrace()
        }

        String errors = ex.getErrors().getFieldErrors().collect { it.toString() + "\n" }

        render(status: HttpStatus.BAD_REQUEST.value(), message: errors)
    }

    def handleJsonException(JSONException ex) {
        render(status: HttpStatus.BAD_REQUEST.value(), message: ex.getMessage())
    }

    def handleException(Exception ex) {
        ex.printStackTrace()

        render(status: HttpStatus.INTERNAL_SERVER_ERROR.value(), message: ex.getMessage())
    }

}
