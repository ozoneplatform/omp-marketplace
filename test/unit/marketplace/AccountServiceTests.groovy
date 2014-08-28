package marketplace

import grails.test.mixin.TestFor

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.access.AccessDeniedException

import ozone.security.authentication.OWFUserDetailsImpl
import ozone.security.authorization.model.GrantedAuthorityImpl
import ozone.security.authorization.target.OwfGroup
import ozone.utils.User

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(AccountService)
class AccountServiceTests {

	def accountService
	def user
	def userDomainInstance
	def mockControl

	void setUp() {
		def testInstances = [ new UserDomainInstance(username: "testAdmin1"), new UserDomainInstance(username:"testUser1"),
		                      new UserDomainInstance(username: "testFoo") ]

        FakeAuditTrailHelper.install()

		mockDomain(UserDomainInstance, testInstances)

		mockControl = mockFor(UserDomainInstance)
		accountService = new AccountService()
	}

	void testGetUserDomainUnknown() {

		user = new User( username: "unknown")
		accountService.getUserDomain(user)
		assertNull user.domainInstance

		user = new User (username: "NotUnknown")
		mockControl.demand.save(1..1) { true }
		accountService.getUserDomain(user)
		assertNotNull user.domainInstance
	}

	void testGetUserDomainNewUser() {

		def uname = "newishadfasdf;lkajs"
		user = new User (username: uname)
		mockControl.demand.save(1..1) { true }
		def userDomain = accountService.getUserDomain(user)
		assertNotNull user.domainInstance
		assertEquals uname, userDomain.username
	}

	void testGetUserDomainExistingUser() {
		def uname = "testFoo"	// see testInstances
		user = new User (username: uname)
		mockControl.demand.save(0..0) { true }
		def userDomain = accountService.getUserDomain(user)
		assertNotNull user.domainInstance
		assertEquals uname, userDomain.username
	}

    void testIsUserOnly() {
        def roles = [Constants.USER]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        assertNotNull accountService.loggedInUsername
        assertNotNull SecurityContextHolder.context.authentication
        assertEquals username, accountService.getLoggedInUsername()
        assertTrue accountService.isUser()
    }

    void testIsUserFailsWhenAlsoAdmin() {
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
        SecurityContextHolder.context.authentication = authToken

        assertNotNull accountService.loggedInUsername
        assertNotNull SecurityContextHolder.context.authentication
        assertEquals username, accountService.getLoggedInUsername()
        assertFalse accountService.isUser()
    }

    void testIsAdmin() {
        def roles = [Constants.ADMIN]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        assertNotNull accountService.loggedInUsername
        assertNotNull SecurityContextHolder.context.authentication
        assertEquals username, accountService.getLoggedInUsername()
        assertTrue accountService.isAdmin()
    }

    void testIsExtAdmin() {
        def roles = [Constants.ADMIN,Constants.EXTERNADMIN]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        assertNotNull accountService.loggedInUsername
        assertNotNull SecurityContextHolder.context.authentication
        assertEquals username, accountService.getLoggedInUsername()
        assertTrue accountService.isExtAdmin()
    }

    void testCheckAdminWithAdmin() {
        def roles = [Constants.ADMIN,Constants.USER]
        def username = 'admin'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        //should not throw exception
        accountService.checkAdmin()
    }

    void testCheckAdminWithUser() {
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

        //should not throw exception
        shouldFail(AccessDeniedException) {
            accountService.checkAdmin()
        }
    }

    void testCheckAdminWithExternAdmin() {
        def roles = [Constants.EXTERNADMIN]
        def username = 'extern'

        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)

        // Set logged-in user with something looking like normal authentication
        SecurityContextHolder.context.authentication = authToken

        //should not throw exception
        shouldFail(AccessDeniedException) {
            accountService.checkAdmin()
        }
    }
}
