package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ConfigurationHolder

@TestMixin(IntegrationTestMixin)
class ThemeControllerTests extends MarketplaceIntegrationTestCase {

	def TOTAL_AVAILABLE_THEMES = 1
	def DEFAULT_THEME_NAME = "marketplace"

	def themeService
	def config = ConfigurationHolder.config

	void setUp() {
		super.setUp()
		controller = new ThemeController()
		controller.themeService = themeService
    }

    void testGetAvailableThemes() {
		controller.getAvailableThemes()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		println "JSON: " + controllerResponse

		//Check the total
		//TODO: FIX
		//assert TOTAL_AVAILABLE_THEMES == controllerResponse.total

		//Make sure Theme data exists
//		assert true ==   !controllerResponse.data.isEmpty()

		for(int idx=0; idx < controllerResponse.size(); idx++){
			def item = controllerResponse[idx]
			println "controllerResponse[$idx]: " + controllerResponse[idx]
			println "Theme[${idx}]: ${item.name}"
			assert null != item.name
			if(DEFAULT_THEME_NAME.equals(item.name)){
				validateDefaultTheme(item)
			}
			//As new themes get added, add the theme validation for each
			//here
		}


	}

	protected void validateDefaultTheme(def item){
		assert "ozone-developers@googlegroups.com" == item.contact_email
		assert "themes/${DEFAULT_THEME_NAME}.theme/" == item.base_url
		assert true ==   !item.screenshots.isEmpty()
		assert null != item.modified_date
		assert "Marketplace" == item.display_name
		assert "ozone.marketplace.domain.ThemeDefinition" == item.class
		assert null != item.thumb
		assert null != item.description
		assert null != item.created_date
		assert "themes/${DEFAULT_THEME_NAME}.theme/css/${DEFAULT_THEME_NAME}.css" == item.css
		assert DEFAULT_THEME_NAME == item.name
		assert "Marketplace Team" == item.author
	}

	void testGetAvailableThemeswException() {
		def eMsg = "Error occurred during getAvailableThemes"
		//Force a null-pointer exception
		controller.themeService = null
		controller.getAvailableThemes()
		def result = controller.response.contentAsString
		assert true ==  result.contains(eMsg)
		assert 500 == controller.response.status
	}

	void testGetImageURL() {
		def testImageName = "test_image.png"
		controller.params.img_name = testImageName
		controller.getImageURL()
		def result = controller.response
		assert result.status == 302
		assert "/themes/common/images/${testImageName}" == result.redirectedUrl
	}

	void testGetImageURLwNull() {
		def testImageName = "test_image.png"
		controller.params.img_name = testImageName
		controller.params.return_null_for_invalid_url = true
		controller.getImageURL()
		def result = controller.response
		assert result.status == 302
		assert "/theme" == result.redirectedUrl
	}
}
