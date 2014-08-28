package marketplace.rest

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

import org.apache.log4j.Logger

import org.springframework.security.access.AccessDeniedException

import marketplace.AccountService

@Provider
class AccessDeniedExceptionMapper extends RestExceptionMapper<AccessDeniedException> {
    private static final Logger log = Logger.getInstance(AccessDeniedExceptionMapper.class)

    AccountService accountService

    AccessDeniedExceptionMapper() {
        super(Response.Status.FORBIDDEN)
    }

    protected void doLog(AccessDeniedException e) {
        //should this be info or warn?
        log.warn "ACCESS DENIED for user ${accountService.loggedInUsername}: ${e.message}"

        super.doLog(e)
    }
}
