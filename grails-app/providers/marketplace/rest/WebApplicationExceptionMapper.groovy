package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider
import javax.ws.rs.WebApplicationException

@Provider
class WebApplicationExceptionMapper extends RestExceptionMapper<WebApplicationException> {
    WebApplicationExceptionMapper () {
        //NOTE This argument is ignored in favor of the
        //response code in the exception
        super(Response.Status.INTERNAL_SERVER_ERROR)
    }

    @Override
    public Response toResponse(WebApplicationException e) {
        Response exceptionResponse = e.response

        //log if a 5xx error
        if (isInternalError(exceptionResponse.status)) {
            log.error("5xx-level WebApplicationException", e)
        }

        return Response.status(exceptionResponse.status).entity(e).build()
    }
}
