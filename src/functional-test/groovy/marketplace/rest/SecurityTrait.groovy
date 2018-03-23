package marketplace.rest

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

import marketplace.Constants
import marketplace.Profile

import ozone.security.authentication.OWFUserDetailsImpl
import ozone.security.authorization.model.GrantedAuthorityImpl


trait SecurityTrait {

    static String TEST_USER_NAME = 'testUser1'

    static String TEST_ADMIN_NAME = 'testAdmin1'

    static String TEST_EXT_ADMIN_NAME = 'testExtAdmin1'

    static void loginAsUser() {
        loginAs(TEST_USER_NAME, [Constants.USER])
    }

    static void loginAsUser(Profile profile) {
        loginAs(profile.username, [Constants.USER])
    }

    static void loginAsAdmin() {
        loginAs(TEST_ADMIN_NAME, [Constants.USER, Constants.ADMIN])
    }

    static void loginAsExtAdmin() {
        loginAs(TEST_EXT_ADMIN_NAME, [Constants.USER, Constants.EXTERNADMIN])
    }

    static void loginAs(String username, List<String> authorities) {
        List<GrantedAuthorityImpl> grantedAuthorities = authorities.collect { new GrantedAuthorityImpl(it) }

        def principal = new OWFUserDetailsImpl(username, 'password', grantedAuthorities, [])
        principal.organization = 'DEFAULT_STORE_NAME'

        def token = new PreAuthenticatedAuthenticationToken(principal, principal.password, principal.authorities)

        SecurityContext context = SecurityContextHolder.createEmptyContext()
        context.setAuthentication(token)

        SecurityContextHolder.setContext(context)
    }

    static void logout() {
        SecurityContextHolder.getContext().setAuthentication(null)
        SecurityContextHolder.clearContext()
    }

}
