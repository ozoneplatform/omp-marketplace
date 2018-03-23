package marketplace.franchise

import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import marketplace.controller.ControllerTestMixin
import org.grails.web.json.JSONObject
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class FranchiseReportingControllerTests extends Specification implements ControllerTestMixin<FranchiseReportingController> {
//    def controller
    def genericQueryService
	def marketplaceApplicationConfigurationService

    void setup() {
//        controller = new FranchiseReportingController()
        controller.genericQueryService = genericQueryService
		controller.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService
    }

	void testGetStoreAttributes(){
		when:
		controller.getStoreAttributes()

		def responseString = controller.response.getContentAsString()

		then:
		null != responseString

		when:
		JSONObject asJson = new JSONObject(responseString)

		then:
		//For now, just prove the elements are available which since many are nullable, is all we want anyway
		asJson.getJSONObject("data").containsKey(FranchiseConstants.IS_FRANCHISE_STORE)
		asJson.getJSONObject("data").containsKey(FranchiseConstants.STORE_AGENCY)

	}
}
