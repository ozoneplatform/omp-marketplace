package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

@Provider
class RuntimeExceptionMapper extends RestExceptionMapper<RuntimeException> {
    RuntimeExceptionMapper() {
        super(Response.Status.INTERNAL_SERVER_ERROR)
    }
}
