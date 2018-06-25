package marketplace.controller

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.ThemeController
import marketplace.ThemeService


@Rollback
@Integration
class ThemeControllerSpec extends MarketplaceIntegrationTestCase implements ControllerTestMixin<ThemeController> {

	private static final int TOTAL_AVAILABLE_THEMES = 3

	private static final String DEFAULT_THEME_NAME = "marketplace"

	ThemeService themeService

	void setup() {
		controller.themeService = themeService
	}

	void testGetAvailableThemes() {
		setup:
		controller.getAvailableThemes()
		def controllerResponse = JSON.parse(controller.response.contentAsString)

		//Check the total
		//TODO: FIX
		//assert TOTAL_AVAILABLE_THEMES == controllerResponse.total

		//Make sure Theme data exists
//		assert true ==   !controllerResponse.data.isEmpty()

		for (int idx = 0; idx < controllerResponse.size(); idx++) {
			when:
			def item = controllerResponse[idx]

			then:
			null != item.name

			and:
			if (DEFAULT_THEME_NAME.equals(item.name)) {
				validateDefaultTheme(item)
			}
			//As new themes get added, add the theme validation for each
			//here
		}


	}

	protected void validateDefaultTheme(def item) {
		assert item.contact_email == "ozone-developers@googlegroups.com"
		assert item.base_url == "themes/${DEFAULT_THEME_NAME}.theme/"
		assert !item.screenshots.isEmpty()
		assert item.modified_date != null
		assert item.display_name == "Marketplace"
		assert item.class == "ozone.marketplace.domain.ThemeDefinition"
		assert item.thumb != null
		assert item.description != null
		assert item.created_date != null
		assert item.css == "themes/${DEFAULT_THEME_NAME}.theme/css/${DEFAULT_THEME_NAME}.css"
		assert item.name == DEFAULT_THEME_NAME
		assert item.author == "Marketplace Team"
	}

	void testGetAvailableThemeswException() {
		when:
		def eMsg = "Error occurred during getAvailableThemes"

		//Force a null-pointer exception
		controller.themeService = null
		controller.getAvailableThemes()

		def result = controller.response.contentAsString

		then:
		result.contains(eMsg)
		controller.response.status == 500
	}

	void testGetImageURL() {
		when:
		def testImageName = "test_image.png"

		controller.params.img_name = testImageName
		controller.getImageURL()

		def result = controller.response

		then:
		result.status == 302
		result.redirectedUrl.endsWith("/themes/common/images/${testImageName}")
	}

	void testGetImageURLwNull() {
		when:
		def testImageName = "test_image.png"

		controller.params.img_name = testImageName
		controller.params.return_null_for_invalid_url = true
		controller.getImageURL()

		def result = controller.response

		then:
		result.status == 302
		result.redirectedUrl.endsWith("/theme")
	}
}
