package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import ozone.marketplace.enums.ImageType;

@TestMixin(IntegrationTestMixin)
class AffiliatedMarketplaceControllerTests {

	def affiliatedMarketplaceService
	def imagesService

	def amp1
	def amp2
	def amp3

	def defaultImage
	def defaultImageUrl
	def defaultImageSize
	def defaultImageContentType

	def INVALID_IMAGE_URL

	def controller

    void setUp() {
		controller = new AffiliatedMarketplaceController()
		controller.affiliatedMarketplaceService = affiliatedMarketplaceService
		controller.request.contextPath = "/marketplace"
		INVALID_IMAGE_URL = "${controller.request.contextPath}/images/get/null"

		defaultImageUrl = controller.request.contextPath +
            '/images/partner_store.png'

        AffiliatedMarketplace.list().each { it.delete(flush:true) }

		amp1 = new AffiliatedMarketplace(name: "DB02 Marketplace",
			serverUrl: "https://owfdb02:8443/marketplace",
				 timeout: 4000).save()

		amp2 = new AffiliatedMarketplace(name: "ANOTHER DB02 Marketplace",
				 serverUrl: "https://owfdb02:8443/marketplace",
				 timeout: 4000).save()

		amp3 = new AffiliatedMarketplace(name: "<h1>ANOTHER DB02 Marketplace<h1>",
				serverUrl: "https://owfdb02:8443/marketplace<b>",
				timeout: 4000).save()

    }

	/**********************************************************
	 *
	 * LIST AS JSON TESTS
	 *
	 */

    void testListAsJSON() {
		//USE DEFAULT PARAMS, no specify they should be: ["start":"0", "limit":"25", "sort":"name", "dir":"asc"]
		controller.listAsJSON()
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==result.success
		assert 3 ==result.totalCount
		assert true ==  result.data instanceof List

		//Default Sort is by name, dir is asc... so we know which one shows first

		def amp3_JSON = amp3.asJSON("${controller.request.contextPath}", true)
		assert true ==  result.data[0] instanceof Map
		assert result.data[0].name == amp3_JSON.name
		assert false ==(result.data[0].name.contains("<"))
		assert true == (result.data[0].name.contains("&lt;"))
		assert result.data[0].timeout == amp3_JSON.timeout
		assert result.data[0].active == amp3_JSON.active
		assert result.data[0].serverUrl == amp3_JSON.serverUrl
		assert false ==(result.data[0].serverUrl.contains("<"))
		assert true == (result.data[0].serverUrl.contains("&lt;"))
		assert result.data[0].id == amp3_JSON.id
		assert true ==  result.data[0].icon instanceof Map
		assert true ==  amp3_JSON.icon instanceof Map
		assert result.data[0].icon.imageSize == JSONObject.NULL
		assert result.data[0].icon.url == amp3_JSON.icon.url
		assert result.data[0].icon.id == JSONObject.NULL
		assert result.data[0].icon.contentType == amp3_JSON.icon.contentType

		//AMP2
		def amp2_JSON = amp2.asJSON("${controller.request.contextPath}")
		assert true ==  result.data[1] instanceof Map
		assert result.data[1].name == amp2_JSON.name
		assert result.data[1].timeout == amp2_JSON.timeout
		assert result.data[1].active == amp2_JSON.active
		assert result.data[1].serverUrl == amp2_JSON.serverUrl
		assert result.data[1].id == amp2_JSON.id
		assert true ==  result.data[1].icon instanceof Map
		assert true ==  amp2_JSON.icon instanceof Map
		assert result.data[1].icon.imageSize == JSONObject.NULL
		assert result.data[1].icon.url == amp2_JSON.icon.url
		assert result.data[1].icon.id == JSONObject.NULL
		assert result.data[1].icon.contentType == amp2_JSON.icon.contentType
		//AMP1
		def amp1_JSON = amp1.asJSON("${controller.request.contextPath}")
		assert true ==  result.data[2] instanceof Map
		assert result.data[2].name == amp1_JSON.name
		assert result.data[2].timeout == amp1_JSON.timeout
		assert result.data[2].active == amp1_JSON.active
		assert result.data[2].serverUrl == amp1_JSON.serverUrl
		assert result.data[2].id == amp1_JSON.id
		assert true ==  result.data[2].icon instanceof Map
		assert true ==  amp1_JSON.icon instanceof Map
		assert result.data[2].icon.imageSize == JSONObject.NULL
		assert result.data[2].icon.url == amp1_JSON.icon.url
		assert result.data[2].icon.id == JSONObject.NULL
		assert result.data[2].icon.contentType == amp1_JSON.icon.contentType
    }

	void testListAsJSONwException() {
		def eMsg = "Cannot invoke method listAsJSON() on null object"
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.listAsJSON()
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==  result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
		assert true ==  !result.success
	}

	/**********************************************************
	*
	* CREATE TESTS
	*
	*/

	void testCreateAsJSON() {
		//JSON is returned as the default behavior if params.returnInstance is null or params.returnInstance = false
		controller.params.name = "New JSON Affiliated Marketplace"
		controller.params.serverUrl = "https://newserver.org:8773/marketplace"
		controller.params.timeout = 5000 //5 seconds
		controller.create()
		def result = JSON.parse(controller.response.contentAsString)
		assert result.success == true
		assert result.totalCount == 1
		assert true ==  result.data instanceof List
		println result
		assert true ==  result.data[0] instanceof Map
		assert result.data[0].name == controller.params.name
		assert result.data[0].timeout == controller.params.timeout
		assert result.data[0].active == 1
		assert result.data[0].serverUrl == controller.params.serverUrl
		assert true ==  result.data[0].id.equals(null) //we haven't saved this yet, newly created, no id
		assert true ==  result.data[0].icon instanceof Map

		//Using default image
		assert result.data[0].icon.url == defaultImageUrl
	}

	void testCreateAsInstance() {
		//New instance is returned when params.returnInstance = true
		controller.params.returnInstance = true
		controller.params.name = "New JSON Affiliated Marketplace"
		controller.params.serverUrl = "https://newserver.org:8773/marketplace"
		controller.params.timeout = 5000 //5 seconds
		def model = controller.create()
		assert null != model
		assert true ==  model instanceof AffiliatedMarketplace
		assert true ==  model.validate()
		assert null == model.id //Haven't saved it yet, so null
		assert model.name == controller.params.name
		assert model.serverUrl == controller.params.serverUrl
		assert model.timeout == controller.params.timeout
		assert model.active == new Integer(1)
		//this instance has not icon
		assert null == model.icon
	}


	void testCreateAsJSONwException() {
		//JSON is returned as the default behavior if params.returnInstance is null or params.returnInstance = false
		def eMsg = "Cannot invoke method buildItemFromParams() on null object"
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.create()
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==  result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
		assert true ==  !result.success
	}

	void testCreateAsInstancewException() {
		def eMsg = "Cannot invoke method buildItemFromParams() on null object"
		//New instance is returned when params.returnInstance = true
		controller.params.returnInstance = true
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		def model
		try{
			model = controller.create()
		}catch(Exception e){
			assert true ==  e.getMessage().contains("${eMsg}")
		}
		assert null == model
	}


	/**********************************************************
	*
	* SHOW TESTS
	*
	*/

	void testShowAsJSON() {
		//JSON is returned as the default behavior if params.returnInstance is null or params.returnInstance = false
		controller.params.id = amp1.id
		controller.params.action = "show"
		controller.show()
		def result = JSON.parse(controller.response.contentAsString)
		assert result.success == true
		assert result.totalCount == 1
		assert result.params.id == controller.params.id
		assert true ==  result.data instanceof List
		println result
		assert true ==  result.data[0] instanceof Map
		assert result.data[0].name == amp1.name
		assert result.data[0].timeout == amp1.timeout
		assert result.data[0].active == amp1.active
		assert result.data[0].serverUrl == amp1.serverUrl
		assert result.data[0].id == amp1.id
		assert true ==  result.data[0].icon instanceof Map
		//Amp1 has no icon
		assert null == amp1.icon

		//Using default image
		assert result.data[0].icon.url == defaultImageUrl
	}


	void testShowAsInstance() {
		//New instance is returned when params.returnInstance = true
		controller.params.returnInstance = true
		controller.params.id = amp1.id
		controller.params.action = "show"
		def model = controller.show()
		assert null != model
		assert true ==  model instanceof AffiliatedMarketplace
		assert true ==  model.validate()
		assert model.id == amp1.id
		assert model.name == amp1.name
		assert model.serverUrl == amp1.serverUrl
		assert model.timeout == amp1.timeout
		assert model.active == amp1.active
		//Amp1 has no icon
		assert null == amp1.icon
		//this instance has not icon
		assert null == model.icon
	}


	void testShowAsJSONwException() {
		//JSON is returned as the default behavior if params.returnInstance is null or params.returnInstance = false
		def eMsg = "Cannot invoke method getItemFromParams() on null object"
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.show()
		def content = controller.response.contentAsString
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==  result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
		assert true ==  !result.success
	}

	void testShowAsInstancewException() {
		def eMsg = "Cannot invoke method getItemFromParams() on null object"
		//New instance is returned when params.returnInstance = true
		controller.params.returnInstance = true
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		def model
		try{
			model = controller.show()
		}catch(Exception e){
			assert true ==  e.getMessage().contains("${eMsg}")
		}
		assert null == model
	}

	/**********************************************************
	*
	* SAVE TESTS
	*
	*/
	//CREATE NEW & SAVE
	void testSaveNew() {
		controller.params.name = "NEW DB02 Marketplace"
		controller.params.serverUrl = "https://owfdb02:8443/marketplace"
		controller.params.timeout = 4000
		controller.params.active = true
		controller.params.action = "save"
		assert null == controller.params.id
		controller.save()
		def result = JSON.parse(controller.response.contentAsString)
		assert result.success == true
		assert result.totalCount == 1
		assert true ==  result.data instanceof List
		println result
		assert null != result.data[0].id
		def amp3 = AffiliatedMarketplace.get(result.data[0].id)
		assert true ==  result.data[0] instanceof Map
		assert result.data[0].name == amp3.name
		assert result.data[0].name == controller.params.name
		assert result.data[0].timeout == amp3.timeout
		assert result.data[0].timeout == controller.params.timeout
		assert result.data[0].active == amp3.active
		assert result.data[0].active == controller.params.active ? 1 : 0
		assert result.data[0].serverUrl == amp3.serverUrl
		assert result.data[0].serverUrl == controller.params.serverUrl
		assert result.data[0].id == amp3.id
		assert true ==  result.data[0].icon instanceof Map
		//Amp3 has no icon
		assert null == amp3.icon

		//Using default image
		assert result.data[0].icon.url == defaultImageUrl
	}

	//UPDATE EXISTING
	void testSaveExisting() {
		controller.params.id = amp2.id
		controller.params.name = "UPDATED ANOTHER DB02 Marketplace"
		controller.params.serverUrl = "https://owfdb02:8773/marketplace"
		controller.params.timeout = 6000
		controller.params.active = true
		controller.params.action = "save"
		controller.save()
		def result = JSON.parse(controller.response.contentAsString)
		assert result.success == true
		assert result.totalCount == 1
		assert true ==  result.data instanceof List
		println result
		def amp3 = AffiliatedMarketplace.get(result.data[0].id)
		assert true ==  result.data[0] instanceof Map
		assert result.data[0].name == amp3.name
		assert result.data[0].name == amp2.name
		assert result.data[0].name == controller.params.name
		assert result.data[0].timeout == amp3.timeout
		assert result.data[0].timeout == amp2.timeout
		assert result.data[0].timeout == controller.params.timeout
		assert result.data[0].active == amp3.active
		assert result.data[0].active == amp2.active
		assert result.data[0].active == controller.params.active ? 1 : 0
		assert result.data[0].serverUrl == amp3.serverUrl
		assert result.data[0].serverUrl == amp2.serverUrl
		assert result.data[0].serverUrl == controller.params.serverUrl
		assert result.data[0].id == amp3.id
		assert result.data[0].id == amp2.id
		assert true ==  result.data[0].icon instanceof Map
		//Amp2 has no icon
		assert null == amp2.icon
		//Amp3 has no icon
		assert null == amp3.icon

		//Using default image
		assert result.data[0].icon.url == defaultImageUrl
	}

	void testSavewException() {
		def eMsg = "Error saving affiliatedMarketplace"
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.save()
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==  result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
		assert true ==  !result.success
	}

	/**********************************************************
	*
	* DELETE TESTS
	*
	*/
	void testDelete() {
		controller.params.action = "delete"
		controller.params.id = amp1.id
		controller.delete()
		def result = JSON.parse(controller.response.contentAsString)
		assert result.success == true
		assert result.result == 'success'
		//Successfully deleted
		def amp3 = AffiliatedMarketplace.get(controller.params.id)
		assert null == amp3
	}

	void testDeletewException() {
		def eMsg = "Cannot invoke method getItemFromParams() on null object"
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.delete()
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==  result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
		assert true ==  !result.success
	}

	/**********************************************************
	*
	* ICON TESTS
	*
	*/
	void testDefaultMarketplaceIconImage() {
		controller.defaultMarketplaceIconImage()
		def result = controller.response
		assert result.status == 200
		assert result.forwardedUrl == "/grails/affiliatedMarketplace/getMarketplaceIconImage.dispatch?isDefault=true&retrieveImage=true"
	}

	void testGetMarketplaceIconImage() {
		//Should get default as there is no icon id
		controller.getMarketplaceIconImage()
		def result = JSON.parse(controller.response.contentAsString)
		assert result.success == true
		assert result.result == 'success'
		assert result.totalCount == 1
	}


	void testGetMarketplaceIconImagewException() {
		def eMsg = "Cannot invoke method getMarketplaceIconImage() on null object"
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.getMarketplaceIconImage()
		def result = JSON.parse(controller.response.contentAsString)
		assert true ==  result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
		assert true ==  !result.success
		assert result.totalCount == 0
		assert result.data.url == INVALID_IMAGE_URL
		assert true ==  result.data.imageSize.equals(null)
		assert true ==  result.data.id.equals(null)
		assert true ==  result.data.contentType.equals(null)
	}

	void testGetMarketplaceIconImagewRetrieveImagewException() {
		//Force a null-pointer exception
		controller.affiliatedMarketplaceService = null
		controller.params.retrieveImage = true
		controller.getMarketplaceIconImage()
		def result = controller.response.contentAsString
		assert result == INVALID_IMAGE_URL
	}
}
