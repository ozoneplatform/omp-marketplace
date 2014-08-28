package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

@Provider
class IllegalArgumentExceptionMapper extends RestExceptionMapper<IllegalArgumentException> {
    IllegalArgumentExceptionMapper() {
        super(Response.Status.BAD_REQUEST)
    }
}
