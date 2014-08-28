package marketplace

import org.springframework.security.core.context.SecurityContextHolder as SCH

/**
 * This class is needed because the audit-trail plugin expects the spring-security
 * plugin to be in use, and that plugin provides a service like this
 */
class SpringSecurityService {
    def getPrincipal() {
        SCH.context?.authentication?.principal
    }
}
