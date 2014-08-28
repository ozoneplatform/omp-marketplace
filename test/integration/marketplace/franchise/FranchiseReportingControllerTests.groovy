package marketplace.franchise

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.codehaus.groovy.grails.web.json.JSONObject;

import grails.converters.JSON
import grails.test.ControllerUnitTestCase

@TestMixin(IntegrationTestMixin)
class FranchiseReportingControllerTests {
    def controller
    def genericQueryService


    void setUp() {
        controller = new FranchiseReportingController()
        controller.genericQueryService = genericQueryService
    }

	void testGetStoreAttributes(){
		controller.getStoreAttributes()

		def responseString = controller.response.getContentAsString()

		assert null != responseString

		JSONObject asJson = new JSONObject(responseString)

		//For now, just prove the elements are available which since many are nullable, is all we want anyway
		assert true ==  asJson.getJSONObject("data").containsKey(FranchiseConstants.IS_FRANCHISE_STORE)
		assert true ==  asJson.getJSONObject("data").containsKey(FranchiseConstants.STORE_AGENCY)

	}
}
