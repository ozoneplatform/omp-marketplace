package marketplace

import grails.util.Environment
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import ozone.security.authentication.OWFUserDetailsImpl
import ozone.utils.User
import ozone.security.authorization.model.GrantedAuthorityImpl

class AutoLoginAccountService extends AccountService {

    boolean transactional = false

    def autoAccountUsername
    def autoAccountName
    def autoAccountEmail
    def autoRoles
    def autoOrganization

    Authentication getAuthentication() {
        if (!SCH.context.authentication)
            createSecurityContext()
        return SCH.context.authentication
    }

    String getLoggedInUsername() {
        // App components may set SCH explicitly; do not ignore
        if (getAuthentication()) {
            autoAccountUsername = getAuthentication().principal.username
        }
        autoAccountUsername
    }

    def getLoggedInUserRoles() {
        // App components may set SCH explicitly; do not ignore
        def roles = []
        if (getAuthentication()) {
            getAuthentication().principal.authorities.each {
                roles << it.authority
            }
        }
        roles
    }

    def setLoggedInUsername(def username) {
        autoAccountUsername = username
    }

    protected boolean hasRole(def role) {
        def roleVal = Constants[role]
        return getLoggedInUserRoles().contains(roleVal)
    }

    User getLoggedInUser() {
        User usr = new User()
        usr.username = getLoggedInUsername()
        usr.name = autoAccountName
        usr.email = autoAccountEmail
        usr.org = autoOrganization
        return usr
    }

    //This method will create a security context programmatically.  Its not ideal.  Ideally we would use xml/groovy config
    //per environment.
    def createSecurityContext() {

        if (Environment.current != Environment.DEVELOPMENT)
            return

        def auths = []

        autoRoles.each {
            auths << new GrantedAuthorityImpl(it)
        }

        def userDetails = new OWFUserDetailsImpl(autoAccountUsername, "password", auths, [])
        userDetails.organization = autoOrganization

        def token = new PreAuthenticatedAuthenticationToken(userDetails, "passwword", auths)

        SCH.getContext().setAuthentication(token)
    }

}
