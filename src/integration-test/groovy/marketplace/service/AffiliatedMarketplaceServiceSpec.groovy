package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.AffiliatedMarketplace
import marketplace.AffiliatedMarketplaceService
import spock.lang.Specification

@Integration
@Rollback
class AffiliatedMarketplaceServiceSpec extends Specification {
	AffiliatedMarketplaceService affiliatedMarketplaceService

	void testDelete() {
		when:
		AffiliatedMarketplace affiliatedMarketplace = new AffiliatedMarketplace(
            name: "ANOTHER DB02 Marketplace",
            serverUrl: "https://owfdb02:8443/marketplace",
            timeout: 4000
        )
		affiliatedMarketplace.save(flush:true)

		def currId = affiliatedMarketplace.id
		then:
		null != AffiliatedMarketplace.get(currId)

		when:
		affiliatedMarketplaceService.delete(affiliatedMarketplace)
		AffiliatedMarketplace affiliatedMarketplaceAfterDelete = AffiliatedMarketplace.get(currId)
		then:
		null == affiliatedMarketplaceAfterDelete
	}
}
