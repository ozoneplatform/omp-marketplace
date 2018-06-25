package marketplace.controller

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.AffiliatedMarketplace
import marketplace.AffiliatedMarketplaceController
import marketplace.AffiliatedMarketplaceService


@Integration
@Rollback
class AffiliatedMarketplaceControllerSpec
		extends Specification
		implements ControllerTestMixin<AffiliatedMarketplaceController> {

	AffiliatedMarketplaceService affiliatedMarketplaceService

	AffiliatedMarketplace amp1
    AffiliatedMarketplace amp2
    AffiliatedMarketplace amp3

	String defaultImageUrl

	String INVALID_IMAGE_URL

    void setup() {
		controller.affiliatedMarketplaceService = affiliatedMarketplaceService
		controller.request.contextPath = "/marketplace"
		INVALID_IMAGE_URL = "${controller.request.contextPath}/images/get/null"

		defaultImageUrl = controller.request.contextPath + '/images/partner_store.png'
    }

	void setupData() {
		AffiliatedMarketplace.list().each { it.delete(flush:true) }

		amp1 = new AffiliatedMarketplace(name: "3 DB02 Marketplace",
										 serverUrl: "https://owfdb02:8443/marketplace",
										 timeout: 4000).save(flush: true)

		amp2 = new AffiliatedMarketplace(name: "2 ANOTHER DB02 Marketplace",
										 serverUrl: "https://owfdb02:8443/marketplace",
										 timeout: 4000).save(flush: true)

		amp3 = new AffiliatedMarketplace(name: "1 <h1>ANOTHER DB02 Marketplace<h1>",
										 serverUrl: "https://owfdb02:8443/marketplace<b>",
										 timeout: 4000).save(flush: true)
	}

    void testListAsJSON() {
        given:
        setupData()

		when:
		// Default parameters, should be: ["start":"0", "limit":"25", "sort":"name", "dir":"asc"]
		controller.listAsJSON()

		def result = responseObject

        def amp1_JSON = amp1.asJSON(requestContextPath)
        assert amp1_JSON.icon instanceof Map

        def amp2_JSON = amp2.asJSON(requestContextPath)
        assert amp2_JSON.icon instanceof Map

        def amp3_JSON = amp3.asJSON(requestContextPath, true)
        assert amp3_JSON.icon instanceof Map

		then:
		result.success == true
        result.totalCount == 3

        def data = verifyProperty(result, 'data', List)

        // Default Sort is by name, dir is asc... so we know which one shows first
        // AMP 3
        def data0 = verifyElement(data, 0, Map)
        data0.id == amp3_JSON.id
		data0.name == amp3_JSON.name
        data0.name.contains("<") == false
        data0.name.contains("&lt;") == true
		data0.timeout == amp3_JSON.timeout
		data0.active == amp3_JSON.active
		data0.serverUrl == amp3_JSON.serverUrl
        data0.serverUrl.contains("<") == false
        data0.serverUrl.contains("&lt;") == true

        def data0_icon = verifyProperty(data0, 'icon', Map)
        data0_icon.id == null
        data0_icon.imageSize == null
        data0_icon.url == amp3_JSON.icon.url
		data0_icon.contentType == amp3_JSON.icon.contentType

        // AMP 2
        def data1 = verifyElement(data, 1, Map)
		data1.name == amp2_JSON.name
		data1.timeout == amp2_JSON.timeout
		data1.active == amp2_JSON.active
		data1.serverUrl == amp2_JSON.serverUrl
		data1.id == amp2_JSON.id

        def data1_icon = verifyProperty(data1, 'icon', Map)
        data1_icon.id == null
        data1_icon.imageSize == null
        data1_icon.url == amp2_JSON.icon.url
		data1_icon.contentType == amp2_JSON.icon.contentType

		//AMP1
        def data2 = verifyElement(data, 2, Map)
        data2.id == amp1_JSON.id
        data2.name == amp1_JSON.name
        data2.timeout == amp1_JSON.timeout
        data2.active == amp1_JSON.active
        data2.serverUrl == amp1_JSON.serverUrl

        def data2_icon = verifyProperty(data2, 'icon', Map)
		data2_icon.imageSize == null
		data2_icon.url == amp1_JSON.icon.url
		data2_icon.id == null
		data2_icon.contentType == amp1_JSON.icon.contentType
    }

	void testListAsJSONwException() {
        given:
        def eMsg = "Cannot invoke method listAsJSON() on null object"

        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

        when:
		controller.listAsJSON()
		
        def result = responseObject

		then:
		result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "])) == true
		!result.success == true
	}

	void testCreateAsJSON() {
		when:
		//JSON is returned as the default behavior if params.returnInstance is null or params.returnInstance = false
		controller.params.name = "New JSON Affiliated Marketplace"
		controller.params.serverUrl = "https://newserver.org:8773/marketplace"
		controller.params.timeout = 5000 //5 seconds
		
        controller.create()

		def result = responseObject

		then:
		result.success == true
		result.totalCount == 1

        def data = verifyProperty(result, 'data', List)
		def data0 = verifyElement(data, 0, Map)
		data0.name == controller.params.name
		data0.timeout == controller.params.timeout
		data0.active == 1
		data0.serverUrl == controller.params.serverUrl
        data0.id == null //we haven't saved this yet, newly created, no id

        def data0_icon = verifyProperty(data0, 'icon', Map)
		data0_icon.url == defaultImageUrl
	}

	void testCreateAsInstance() {
		when:
		//New instance is returned when params.returnInstance = true
		controller.params.returnInstance = true
		controller.params.name = "New JSON Affiliated Marketplace"
		controller.params.serverUrl = "https://newserver.org:8773/marketplace"
		controller.params.timeout = 5000 //5 seconds
        
		def model = controller.create()

		then:
		model != null
		model instanceof AffiliatedMarketplace
		model.validate()
		model.id == null //Haven't saved it yet, so null
		model.name == controller.params.name
		model.serverUrl == controller.params.serverUrl
		model.timeout == controller.params.timeout
		model.active == new Integer(1)

		//this instance has no icon
		model.icon == null
	}
    
	void testCreateAsJSONwException() {
        given:
        def eMsg = "Cannot invoke method buildItemFromParams() on null object"
        
        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

        when:
		controller.create()
        
		def result = responseObject

		then:
		result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "])) == true
		!result.success == true
	}

	void testCreateAsInstancewException() {
        given:
        def eMsg = "Cannot invoke method buildItemFromParams() on null object"

        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null
        
		when:
        controller.params.returnInstance = true

        def model = controller.create()
        
		then:
		def ex = thrown(Exception)
		ex.getMessage().contains("${eMsg}")
		model == null
	}

	void testShowAsJSON() {
		given:
        setupData()
        assert amp1.icon == null
        
        when:
		controller.params.id = amp1.id
		controller.params.action = "show"
		
        controller.show()

		def result = responseObject
        
		then:
		result.success == true
		result.totalCount == 1
		result.params.id == controller.params.id

        def data = verifyProperty(result, 'data', List)
        def data0 = verifyElement(data, 0, Map)
        data0.id == amp1.id
		data0.name == amp1.name
		data0.timeout == amp1.timeout
		data0.active == amp1.active
		data0.serverUrl == amp1.serverUrl

        def data0_icon = verifyProperty(data0, 'icon', Map)
		data0_icon.url == defaultImageUrl
	}



	void testShowAsInstance() {
		given:
        setupData()
        assert amp1.icon == null
        
        when:
		controller.params.returnInstance = true
		controller.params.id = amp1.id
		controller.params.action = "show"

		def model = controller.show()

		then:
        model != null
		model instanceof AffiliatedMarketplace
		model.validate()
		model.id == amp1.id
		model.name == amp1.name
		model.serverUrl == amp1.serverUrl
		model.timeout == amp1.timeout
		model.active == amp1.active
        model.icon == null
	}


	void testShowAsJSONwException() {
        given:
        def eMsg = "Cannot invoke method getItemFromParams() on null object"
        
        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null
        
		when:
		controller.show()
        
		def result = responseObject
        
		then:
        result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "])) == true
        !result.success == true
	}

	void testShowAsInstancewException() {
        given:
        def eMsg = "Cannot invoke method getItemFromParams() on null object"

        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

        when:
		controller.params.returnInstance = true
		def model = controller.show()
		
        then:
		def e = thrown(Exception)
		e.getMessage().contains("${eMsg}")
        model == null
	}

	void testSaveNew() {
		when:
		controller.params.name = "NEW DB02 Marketplace"
		controller.params.serverUrl = "https://owfdb02:8443/marketplace"
		controller.params.timeout = 4000
		controller.params.active = true
		controller.params.action = "save"
        assert controller.params.id == null

		controller.save()

		then:
        def result = responseObject

        result.success == true
		result.totalCount == 1

        def data = verifyProperty(result, 'data', List)
        def data0 = verifyElement(data, 0, Map)
        data0.id != null

		def amp3 = AffiliatedMarketplace.get(data0.id)

        data0.id == amp3.id
		data0.name == amp3.name
		data0.name == controller.params.name
		data0.timeout == amp3.timeout
		data0.timeout == controller.params.timeout
		data0.active == amp3.active
		data0.active == controller.params.active ? 1 : 0
		data0.serverUrl == amp3.serverUrl
		data0.serverUrl == controller.params.serverUrl

        amp3.icon == null
        def data0_icon = verifyProperty(data0, 'icon', Map)
		data0_icon.url == defaultImageUrl
	}

	void testSaveExisting() {
		given:
        setupData()
        
        when:
		controller.params.id = amp2.id
		controller.params.name = "UPDATED ANOTHER DB02 Marketplace"
		controller.params.serverUrl = "https://owfdb02:8773/marketplace"
		controller.params.timeout = 6000
		controller.params.active = true
		controller.params.action = "save"

		controller.save()
        
		def result = responseObject
        
		then:
		result.success == true
		result.totalCount == 1
		result.data instanceof List

        def data = verifyProperty(result, 'data', List)
        def data0 = verifyElement(data, 0, Map)

		def _amp2 = AffiliatedMarketplace.get(amp2.id)
		def amp3 = AffiliatedMarketplace.get(data0.id)

        data0.id == amp3.id
        data0.id == _amp2.id
		data0.name == amp3.name
		data0.name == _amp2.name
		data0.name == controller.params.name
		data0.timeout == amp3.timeout
		data0.timeout == _amp2.timeout
		data0.timeout == controller.params.timeout
		data0.active == amp3.active
		data0.active == _amp2.active
		data0.active == controller.params.active ? 1 : 0
		data0.serverUrl == amp3.serverUrl
		data0.serverUrl == _amp2.serverUrl
		data0.serverUrl == controller.params.serverUrl

        _amp2.icon == null
        amp3.icon == null
		def data0_icon = verifyProperty(data0, 'icon', Map)
        data0_icon.url == defaultImageUrl
	}

	void testSavewException() {
		given:
        def eMsg = "Error saving affiliatedMarketplace"

        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

        when:
		controller.save()

		def result = responseObject

		then:
        result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
        !result.success
	}

	void testDelete() {
		given:
        setupData()

        when:
		controller.params.action = "delete"
		controller.params.id = amp1.id

		controller.delete()

		def result = responseObject

		then:
		result.success == true
		result.result == 'success'

		def amp3 = AffiliatedMarketplace.get(amp1.id)

        amp3 == null
	}

	void testDeletewException() {
        given:
        def eMsg = "Cannot invoke method getItemFromParams() on null object"

        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

		when:
		controller.delete()

		def result = responseObject

		then:
        result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
        !result.success
	}

	void testDefaultMarketplaceIconImage() {
		when:
		controller.defaultMarketplaceIconImage()

		def result = controller.response

		then:
		result.status == 200
		result.forwardedUrl == "/affiliatedMarketplace/getMarketplaceIconImage?isDefault=true&retrieveImage=true"
	}

	void testGetMarketplaceIconImage() {
		when:
		//Should get default as there is no icon id
		controller.getMarketplaceIconImage()

		def result = responseObject

		then:
		result.success == true
		result.result == 'success'
		result.totalCount == 1
	}


	void testGetMarketplaceIconImagewException() {
        given:
        def eMsg = "Cannot invoke method getMarketplaceIconImage() on null object"

        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

		when:
		controller.getMarketplaceIconImage()

		def result = responseObject

		then:
        result.msg.contains(controller.message(code: "affiliatedMarketplace.log.error.exceptionOccurred", args: ["${eMsg}", "Reference = "]))
        !result.success
		result.totalCount == 0

        def data = verifyProperty(result, 'data', Map)
		data.url == INVALID_IMAGE_URL
        data.imageSize == null
        data.id == null
        data.contentType == null
	}

	void testGetMarketplaceIconImagewRetrieveImagewException() {
		given:
        //Force a null-pointer exception
        controller.affiliatedMarketplaceService = null

        when:
		controller.params.retrieveImage = true

		controller.getMarketplaceIconImage()

		def result = responseString

		then:
		result == INVALID_IMAGE_URL
	}
}
