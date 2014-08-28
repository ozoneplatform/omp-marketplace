package marketplace

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject

@TestFor(ExtServiceItem)
class ExtServiceItemTests {
  def extServiceItem

    void setUp() {
		// Seems like we need to mock the embedded object first to avoid error:
		//
		// 	groovy.lang.MissingPropertyException: No such property:
		// 		static for class: groovy.lang.MetaClassImpl
		//
		// when running the test from within STS
		//
		// Might be related to: http://youtrack.jetbrains.net/issue/IDEA-51702
		//
		mockForConstraintsTests(ServiceItem)
        mockForConstraintsTests(ExtServiceItem)
        extServiceItem = new ExtServiceItem();
    }

    void testDummy() {
       assertEquals 1, 1
    }

	void XtestASJSON(){
		extServiceItem.id=1
		extServiceItem.state = new State()
		extServiceItem.types = new Types()

		extServiceItem.customFields = [
			1:new CustomField(id:3, value:"Red", customFieldDefinition:
				new CustomFieldDefinition(id:8, name:"Color")
			),
			2:new CustomField(id:4, value:"3", customFieldDefinition:
				new CustomFieldDefinition(id:9, name:"ShirtSize")
			)
		]
		extServiceItem.isHidden=1
		extServiceItem.isDeleted=1

		JSONObject json=extServiceItem.asJSON()

		assertNotNull json?.id
		assertNotNull json?.serviceItem
		assertNotNull json?.extCtx

		assertEquals 1, json.serviceItem.isHidden
		assertEquals 1, json.serviceItem.isDeleted

		assertEquals 2, json.serviceItem.customFields.size()

		def ids=json?.serviceItem.customFields.collect {it.customFieldID }.
			grep{it}.sort()

		def expected = extServiceItem.customFields.collect {it.value.id }.
			grep{it}.sort()

		assertEquals expected, ids
	}
}
