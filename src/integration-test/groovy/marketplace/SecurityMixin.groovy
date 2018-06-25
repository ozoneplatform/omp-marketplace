package marketplace

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

//import ozone.owf.grails.domain.ERoleAuthority
//import ozone.owf.grails.domain.Person

/**
 * <h3>Notes:</h3>
 *
 * If the implementing class overrides {@code cleanup ( )}, it should manually call either
 * {@code clearSecurityContext ( )} or {@code SecurityMixin.super.cleanup ( )}
 */
trait SecurityMixin {

    private static final String ADMIN_ROLE_NAME = Constants.ADMIN
    private static final String USER_ROLE_NAME = Constants.USER

    void cleanup() {
        clearSecurityContext()
    }

    static void clearSecurityContext() {
        SecurityContextHolder.clearContext()
    }

    static void loggedInAs(Profile user) {
        assert user != null

        def authorities = convertPersonAuthorities(user)
        assert !authorities.empty


        loginAsUsernameAndRole(user.username, authorities)
    }

    static void loggedInAsUser() {
        loginAsUsernameAndRole("user", [new SimpleGrantedAuthority(USER_ROLE_NAME)])
    }

    static void loggedInAsAdmin() {
        loginAsUsernameAndRole("admin", [new SimpleGrantedAuthority(ADMIN_ROLE_NAME)])
    }

    static <T> T runAsAdmin(Closure<T> closure) {
        Authentication previous = authentication

        loggedInAsAdmin()
        T result = closure.call()
        clearSecurityContext()

        authentication = previous

        result
    }

    private static List<GrantedAuthority> convertPersonAuthorities(Profile user) {
        if (user?.userRoles == null) return []

        if (user.userRoles == USER_ROLE_NAME) {
            return [new SimpleGrantedAuthority(USER_ROLE_NAME)]
        }
        if (user.userRoles == ADMIN_ROLE_NAME) {
            return [new SimpleGrantedAuthority(ADMIN_ROLE_NAME)]
        }

        return []
    }

    private static Authentication getAuthentication() {
        SecurityContextHolder.context.authentication
    }

    private static void setAuthentication(Authentication auth) {
        SecurityContextHolder.context.authentication = auth
    }

    private static void loginAsUsernameAndRole(String username, List<GrantedAuthority> authorities) {
        clearSecurityContext()

        def user = new User(username, "password", true, true, true, true, authorities)

        authentication = new UsernamePasswordAuthenticationToken(user, "password")
    }

}
