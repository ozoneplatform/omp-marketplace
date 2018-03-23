package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.AccountService
import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.security.authentication.OWFUserDetailsImpl
import spock.lang.Specification

@Integration
@Rollback
class AccountServiceIntgTests extends Specification {

	@Autowired
	AccountService accountService
	@Autowired
	MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

	def DEFAULT_AGENCY = "DEFAULT_STORE_NAME"

	void setup() {
		def userDetails = new OWFUserDetailsImpl("test", "password", [], [])
		userDetails.organization = 'DEFAULT_STORE_NAME'

		def token = new TestingAuthenticationToken(userDetails,"passwword")
		token.setAuthenticated(true)

		SecurityContextHolder.getContext().setAuthentication(token)

	}



	void testIsUserIsFromStoreAgency(){
		when:
		def applicationConfiguration = marketplaceApplicationConfigurationService.getApplicationConfiguration(MarketplaceApplicationSetting.STORE_NAME)
		applicationConfiguration.value = DEFAULT_AGENCY
		marketplaceApplicationConfigurationService.saveApplicationConfiguration(applicationConfiguration)


		def store = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_NAME)
		//This is true because in setUp we default the organization to the value the store is defaulted too

		then:
		accountService.isUserFromStoreAgency(store)
	}



	void testIsUserIsNotFromStoreAgency(){
		expect:
		//This failes because we changed the store's name but not the users
		!accountService.isUserFromStoreAgency("FOO_STORE")

	}

}
