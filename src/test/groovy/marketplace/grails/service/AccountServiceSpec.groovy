package marketplace

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import ozone.security.authentication.OWFUserDetailsImpl
import ozone.security.authorization.model.GrantedAuthorityImpl
import ozone.security.authorization.target.OwfGroup
import ozone.utils.User
import spock.lang.Specification

//import sun.plugin.liveconnect.SecurityContextHelper

//@TestFor(AccountService)
class AccountServiceSpec extends Specification implements ServiceUnitTest<AccountService>, DataTest {

    Class[] getDomainClassesToMock() {
        [
                UserDomainInstance

        ]
    }
    //UserDomainInstance userDomainInstance = Mock()

    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true } }}
	def user
	//def userDomainInstance
	def mockControl

	void setupData() {
		new UserDomainInstance(username: "testAdmin1")
        new UserDomainInstance(username:"testUser1")
        new UserDomainInstance(username: "testFoo")

//TODO Question why metaClass is necessary in FakeAuditTrailHelper

	}

	void testGetUserDomainUnknown() {
        given:
        setupData()
        user = new User(username: "unknown")

        when:
           service.getUserDomain(user)

        then:
           user.domainInstance == null

//		user = new User( username: "unknown")
//		accountService.getUserDomain(user)
//		assertNull user.domainInstance
//
//		user = new User (username: "NotUnknown")
//		mockControl.demand.save(1..1) { true }
//		accountService.getUserDomain(user)
//		assertNotNull user.domainInstance
	}

    void testGetUserDomainNotUnknown() {
        given:
        setupData()
        user = new User(username: "NotUnknown")

        when:
            service.getUserDomain(user)

        then:
            user.domainInstance != null
    }

	void testGetUserDomainNewUser() {
        def uname = "newishadfasdf;lkajs"
        given:
        setupData()
        user = new User(username: uname)

        when:
           // mockControl.save(true)
            def userDomain = service.getUserDomain(user)

        then:
            user.domainInstance != null
            uname == userDomain.username

//		def uname = "newishadfasdf;lkajs"
//		user = new User (username: uname)
//		mockControl.demand.save(1..1) { true }
//		def userDomain = accountService.getUserDomain(user)
//		assertNotNull user.domainInstance
//		assertEquals uname, userDomain.username
	}
//
	void testGetUserDomainExistingUser() {
        def uname = "testFoo"
        given:
        setupData()
        user = new User(username: uname)

        when:
            //mockControl.save(true)
            def userDomain = service.getUserDomain(user)

        then:
            user.domainInstance != null
            uname == userDomain.username
//		def uname = "testFoo"	// see testInstances
//		user = new User (username: uname)
//		mockControl.demand.save(0..0) { true }
//		def userDomain = accountService.getUserDomain(user)
//		assertNotNull user.domainInstance
//		assertEquals uname, userDomain.username
	}
//
    void testIsUserOnly() {
        given:
        setupData()
        def roles = [Constants.USER]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        when:
        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        then:
        service.loggedInUsername != null
        SecurityContextHolder.context.authentication != null
        username == service.getLoggedInUsername()
        service.isUser() == true
    }

    void testIsUserFailsWhenAlsoAdmin() {
        given:
        setupData()
        def roles = [Constants.USER,Constants.ADMIN]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
       when:
       SecurityContextHolder.context.authentication = authToken

       then:
       service.loggedInUsername != null
       SecurityContextHolder.context.authentication != null
       username == service.getLoggedInUsername()
       service.isUser() == false
    }

    void testIsAdmin() {
        given:
        setupData()
        def roles = [Constants.ADMIN]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        when:
        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        then:
        service.loggedInUsername != null
        SecurityContextHolder.context.authentication != null
        username == service.getLoggedInUsername()
        service.isAdmin() == true
    }

    void testIsExtAdmin() {
        given:
        setupData()
        def roles = [Constants.ADMIN,Constants.EXTERNADMIN]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        when:
        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        then:
        service.loggedInUsername != null
        SecurityContextHolder.context.authentication != null
        username == service.getLoggedInUsername()
        service.isExtAdmin() == true

    }

    void testCheckAdminWithAdmin() {
        given:
        setupData()
        def roles = [Constants.ADMIN,Constants.USER]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        when:
        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken
        service.checkAdmin()

        then:
        //should not throw exception
        notThrown AccessDeniedException
    }

    void testCheckAdminWithUser() {
        given:
        setupData()
        def roles = [Constants.USER]
        def username = 'user'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken
        when:
        service.checkAdmin()

        then:
        AccessDeniedException ex = thrown()
        ex.message == "Attempt to access Admin-only functionality"

    }

    void testCheckAdminWithExternAdmin() {
        given:
        setupData()
        def roles = [Constants.EXTERNADMIN]
        def username = 'extern'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        when:
        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken
        service.checkAdmin()

        then:
        AccessDeniedException ex = thrown()
        ex.message == "Attempt to access Admin-only functionality"

    }
}
