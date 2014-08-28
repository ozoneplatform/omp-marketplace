package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

import grails.validation.ValidationException

@Provider
class ValidationExceptionMapper extends RestExceptionMapper<ValidationException> {
    ValidationExceptionMapper() {
        super(Response.Status.BAD_REQUEST)
    }
}
