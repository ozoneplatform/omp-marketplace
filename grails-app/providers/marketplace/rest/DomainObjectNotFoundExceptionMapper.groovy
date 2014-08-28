package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider
import marketplace.rest.DomainObjectNotFoundException

@Provider
class DomainObjectNotFoundExceptionMapper extends RestExceptionMapper<DomainObjectNotFoundException> {
    DomainObjectNotFoundExceptionMapper() {
        super(Response.Status.NOT_FOUND)
    }
}
