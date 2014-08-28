package marketplace.rest

import org.springframework.security.access.AccessDeniedException

import marketplace.AccountService

import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.TestMixin

@TestMixin(GrailsUnitTestMixin)
class AccessDeniedExceptionMapperUnitTest {
    AccessDeniedExceptionMapper mapper

    void setUp() {
        mapper = new AccessDeniedExceptionMapper()
        mapper.accountService = [
            loggedInUsername: { 'testAdmin1' }
        ] as AccountService

    }

    void testStatusCode() {
        assert mapper.toResponse(new AccessDeniedException('')).status == 403
    }
}
