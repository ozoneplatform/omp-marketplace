package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*

import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import ozone.security.authentication.OWFUserDetailsImpl
import ozone.marketplace.enums.MarketplaceApplicationSetting

@TestMixin(IntegrationTestMixin)
class AccountServiceIntgTests {


	def accountService

	def marketplaceApplicationConfigurationService

	def DEFAULT_AGENCY = "DEFAULT_STORE_NAME"

	void setUp() {
		def userDetails = new OWFUserDetailsImpl("test", "password", [], [])
		userDetails.organization = 'DEFAULT_STORE_NAME'

		def token = new TestingAuthenticationToken(userDetails,"passwword")
		token.setAuthenticated(true)

		SecurityContextHolder.getContext().setAuthentication(token)

	}



	void testIsUserIsFromStoreAgency(){

		def applicationConfiguration = marketplaceApplicationConfigurationService.getApplicationConfiguration(MarketplaceApplicationSetting.STORE_NAME)
		applicationConfiguration.value = DEFAULT_AGENCY
		marketplaceApplicationConfigurationService.saveApplicationConfiguration(applicationConfiguration)


		def store = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_NAME)
		//This is true because in setUp we default the organization to the value the store is defaulted too
		assert true ==  accountService.isUserFromStoreAgency(store)
	}



	void testIsUserIsNotFromStoreAgency(){

		//This failes because we changed the store's name but not the users
		assert false == accountService.isUserFromStoreAgency("FOO_STORE")

	}

}
