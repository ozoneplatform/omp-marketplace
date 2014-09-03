package marketplace

import grails.util.Environment

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import ozone.utils.User

class AccountService extends OzoneService {

    def sessionFactory


    public Authentication getAuthentication() {
        // Some services will run outside of the request context
        def authentication = SCH.getContext().getAuthentication()
        if (session && !session.authentication) {
            if (authentication != null) {
                session.authentication = authentication;
            }
        }
        return authentication
    }


    def getLoggedInUsername() {
        return this.getAuthentication()?.principal?.username
    }

    def getLoggedInUserRoles() {
        return this.getAuthentication()?.principal?.authorities
    }

    def getloggedInUserAuthorities(){
        return getLoggedInUserRoles().collect{it instanceof String ? it : it.authority}.toString()
    }

    def getLoggedInUser() {
        User usr = null
        def p = this.getAuthentication()?.principal
        if (p && p.username) {
            usr = new User()
            usr.username = p.username
            if (p?.metaClass.hasProperty(p, "displayName")) {
                usr.name = p?.displayName ?: usr.username
            } else {
                usr.name = usr.username
            }
            if (p?.metaClass.hasProperty(p, "organization")) {
                usr.org = p?.organization ?: "unknown"
            }
            if (p?.metaClass.hasProperty(p, "email")) {
                usr.email = p?.email ?: "unknown"
            }
        } else {
            log.debug "getLoggedInUser: principal = ${p}"
        }
        return usr
    }

    /** Is current user a USER with no admin privs? */
    def isUser() {
        // Tempting to just return !isAdmin(), but we must also ensure that ROLE_USER does exist
        boolean hasUserPerm = false;
        boolean hasAdminPerm = false;
        def roles = getRolesAsArray()
        for (role in roles) {
            if (roleMatches(Constants.ADMIN, role)) {
                hasAdminPerm = true;
            } else if (roleMatches(Constants.USER, role)) {
                hasUserPerm = true;
            }
        }
        return hasUserPerm && !hasAdminPerm;
    }

    /** Is current user an ADMIN ? */
    def isAdmin() {
        def roles = getRolesAsArray()
        for (role in roles) {
            if (roleMatches(Constants.ADMIN, role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Covert list to array to avoid ConcurrentModificationException
     * @return
     */
    private Object getRolesAsArray() {
        def roles = getLoggedInUserRoles()
        roles instanceof List ? roles as Object[] : roles
    }

    /** Is current user an EXTADMIN ? */
    def isExtAdmin() {
        def roles = getRolesAsArray()
        for (role in roles) {
            if (roleMatches(Constants.EXTERNADMIN, role)) {
                return true;
            }
        }
        return false;
    }

    def roleMatches(String roleName, GrantedAuthority auth) {
        return roleName == auth?.authority
    }

    def roleMatches(String roleName, String auth) {
        return roleName == auth
    }


    @Transactional
    def getUserDomain(User usr) {
        if (usr && (usr?.username) && (usr?.username != "unknown")) {
            Lock lock = new ReentrantLock();
            lock.lock();
            try {
                def usrDomainInstance = UserDomainInstance.findByUsername(usr.username)
                if (null != usrDomainInstance) {
                    log.debug "Using existing instance for ${usr.username}"
                    usr.domainInstance = usrDomainInstance
                } else {
                    def di = new UserDomainInstance(username: usr.username)
                    di.save()
                    usr.domainInstance = di
                    log.debug "Created new instance for ${usr.name}"
                }
            } finally {
                lock.unlock();
            }
        }
        return usr?.domainInstance
    }

    protected boolean hasRole(def role) {
        return getLoggedInUserRoles().any { it.authority.equals(Constants[role]) }
    }

    def methodMissing(String name, args) {
        // Defines dynamic isRole methods. Role name must be one of the Constants ROLE variable names, case does not matter.
        if (name.startsWith('is')) {
            hasRole(name.substring(2).toUpperCase())
        } else {
            throw new MissingMethodException(name, this.getClass(), args)
        }
    }

    public void setDefaultView(String userName, String accessType) {

        try {
            log.info "setDefaultView: userName = ${userName} accessType = ${accessType}"
            // fix for MARKETPLACE-2967
            UserDomainInstance.withTransaction {
                if (accessType) {
                    User user = new User()
                    user.username = userName
                    def usrdom = this.getUserDomain(user)
                    if (usrdom) {
                        usrdom.setDefaultView(accessType)
                        usrdom.save(flush: true)
                    } else {
                        // Log that User Domain could not be found.
                        log.warn "Could not find user domain instance.  Cannot save default view."
                    }
                } else {
                    // Log that Access Type was not supplied
                    log.warn "Default view not supplied.  Cannot save default view."
                }
            }
        } catch (Exception e) {
            log.warn "Cannot save default view(2)."
            log.warn('error in setDefaultView', e)
        }
    }

    //This will compare the organization from the authentication object to the agency that is defined in the store
    public boolean isUserFromStoreAgency(def store) {
        def principal = this.getAuthentication()?.principal

        if (!principal || !store) return false

        log.debug "Authentication data " + principal
        log.debug "Agency " + store

        return store.equalsIgnoreCase(principal.organization)
    }

    @Transactional
    public UserAccount createUserAccount(String username, Date creationDate) {
        log.debug "Adding UserAccount and saving last login for ${username}"
        def userAccount = new UserAccount()
        userAccount.username = username
        userAccount.lastLogin = creationDate
        userAccount.save()
        def userDomainInstance = new UserDomainInstance(username: username)
        userDomainInstance.save()
        userAccount
    }

    @Transactional
    public UserAccount updateUserAccount(UserAccount userAccount) {
        if (userAccount) {
            log.debug "Saving last login for UserAccount: ${userAccount.username}"
            userAccount.save()
        }
        userAccount
    }

    /**
     * Retrieves , or if necessary, creates, a UserAccount and
     * updates the lastLogin time on that account
     */
    @Transactional
    public UserAccount loginAccount(String username) {
            Date currDate = new Date()

            // ADDING USER ACCOUNT....
            //DO NOT CACHE THIS! - A UserAccount can be manually deleted
            //from the DB, this needs to be a
            //fresh pull of the data EVERY time.
            UserAccount userAccount = UserAccount.findByUsername(username,
                [cache: false])
            userAccount?.refresh()
            if (!userAccount) {
                userAccount = createUserAccount(username, currDate)
            }
            else if(Environment.current == Environment.PRODUCTION) {
                userAccount.lastLogin = currDate
                userAccount = updateUserAccount(userAccount)
            }

        return userAccount
    }

    /**
     * @throws AccessDeniedException if the current user in not an Admin
     * @param msg the Message for the exception
     */
    public void checkAdmin(String msg = "Attempt to access Admin-only functionality") {
        if (!isAdmin()) {
            throw new AccessDeniedException(msg)
        }
    }

    public void loginSystemUser() {
        if (!SCH.context) {
            SCH.context = new SecurityContextImpl()
        }

        if (SCH.context.authentication) {
            throw new IllegalStateException(
                "Cannot log in System user, authentication already exists: " +
                SCH.context.authentication.toString())
        }

        //spring security requires a password for the User object, so fake one
        String password = 'password'
        Collection<GrantedAuthority> authorities = [
            new SimpleGrantedAuthority('ROLE_ADMIN'),
            new SimpleGrantedAuthority('ROLE_USER')
        ]

        UserDetails user = new org.springframework.security.core.userdetails.User(
            Profile.SYSTEM_USER_NAME, password, authorities)

        SCH.context.authentication =
            new PreAuthenticatedAuthenticationToken(user, password, authorities)
    }
}
