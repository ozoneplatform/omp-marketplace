package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

import ozone.marketplace.domain.ValidationException

@Provider
class OzoneValidationExceptionMapper extends RestExceptionMapper<ValidationException> {
    OzoneValidationExceptionMapper() {
        super(Response.Status.BAD_REQUEST)
    }
}
