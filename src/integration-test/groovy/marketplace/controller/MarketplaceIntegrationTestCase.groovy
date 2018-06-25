package marketplace.controller

import marketplace.AutoLoginAccountService
import marketplace.Constants
import marketplace.Profile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import ozone.security.authentication.OWFUserDetailsImpl
import ozone.security.authorization.model.GrantedAuthorityImpl
import ozone.security.authorization.target.OwfGroup
import spock.lang.Specification

import java.text.MessageFormat

//@Integration
//@Rollback
class MarketplaceIntegrationTestCase extends Specification {
	def controller
	AutoLoginAccountService autoLoginAccountService
	def testUser1
	def props

	void setupTestUser() {
		testUser1 = Profile.findByUsername(System.properties.user ?: "testUser1")
		switchUser(testUser1.username)
//
//		//i18N properties inclusion...
//		props = new Properties()
//		def stream = new FileInputStream("grails-app/i18n/messages.properties")
//		props.load stream
//		stream.close()
//		if(controller){
//			mockI18N(controller)
//		}
	}

	def mockI18N = { controller ->
			controller.message = { Map map ->
			if (!map.code)
				return ""
			if (map.args) {
				def formatter = new MessageFormat("")
				formatter.applyPattern props.getProperty(map.code)
				return formatter.format(map.args.toArray())
			} else {
				return props.getProperty(map.code)
			}
		}
	}

	protected void switchUser(def username, def roles = [Constants.USER]){
		autoLoginAccountService.autoRoles = roles
		Long profileID = Profile.findByUsername(username)?.id
        if (profileID instanceof Number) {
            RequestContextHolder.getRequestAttributes()?.setAttribute("profileID",profileID,RequestAttributes.SCOPE_SESSION)
        }
        autoLoginAccountService.loggedInUsername = username
        // Create Authentication token
        def auths = []
        roles.each {
            auths << new GrantedAuthorityImpl(it)
        }
        Collection<OwfGroup> groups = new ArrayList<OwfGroup>(0)
        def authUser = new OWFUserDetailsImpl(username, "", auths, groups)
        def authToken =  new UsernamePasswordAuthenticationToken(authUser, '', auths)
        SecurityContextHolder.context.authentication = authToken

		if(controller){
			controller.session.username=username
			controller.session.isAdmin = false
		}
	}

	protected void switchAdmin(def username){
		switchUser(username, [Constants.ADMIN])
		if(controller){
			controller.session.isAdmin=true
		}
	}

    protected void switchBothAdmin(def username){
        switchUser(username, [Constants.ADMIN, Constants.EXTERNADMIN])
        if(controller){
            controller.session.isAdmin=true
        }
    }

	protected void switchExternalAdmin(def username){
		switchUser(username, [Constants.EXTERNADMIN])
	}

}
