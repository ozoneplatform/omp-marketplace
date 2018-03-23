package marketplace.rest

import marketplace.AccountService
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

class AccessDeniedExceptionMapperSpec extends Specification {
    AccessDeniedExceptionMapper mapper

    void setup() {
        mapper = new AccessDeniedExceptionMapper()
        mapper.accountService = [
            loggedInUsername: { 'testAdmin1' }
        ] as AccountService

    }

    void testStatusCode() {
        expect:
        mapper.toResponse(new AccessDeniedException('')).status == 403
    }
}
