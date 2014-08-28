package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*

@TestMixin(IntegrationTestMixin)
class AffiliatedMarketplaceServiceTests {
	def affiliatedMarketplaceService

	void testDelete() {
		AffiliatedMarketplace affiliatedMarketplace = new AffiliatedMarketplace(
            name: "ANOTHER DB02 Marketplace",
            serverUrl: "https://owfdb02:8443/marketplace",
            timeout: 4000
        )
		affiliatedMarketplace.save(flush:true)

		def currId = affiliatedMarketplace.id
		assert null != AffiliatedMarketplace.get(currId)
		affiliatedMarketplaceService.delete(affiliatedMarketplace)
		AffiliatedMarketplace affiliatedMarketplaceAfterDelete = AffiliatedMarketplace.get(currId)
		assert null == affiliatedMarketplaceAfterDelete
	}
}
