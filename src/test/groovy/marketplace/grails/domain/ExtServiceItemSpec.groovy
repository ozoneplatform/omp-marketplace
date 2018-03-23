package marketplace

import marketplace.grails.domain.DomainConstraintsUnitTest
import org.grails.web.json.JSONObject
import spock.lang.Specification

//@TestFor(ExtServiceItem)
class ExtServiceItemSpec extends Specification implements DomainConstraintsUnitTest<ExtServiceItem> {
  ExtServiceItem extServiceItem

    void setup() {
		// Seems like we need to mock the embedded object first to avoid error:
		//
		// 	groovy.lang.MissingPropertyException: No such property:
		// 		static for class: groovy.lang.MetaClassImpl
		//
		// when running the test from within STS
		//
		// Might be related to: http://youtrack.jetbrains.net/issue/IDEA-51702
		//
//		mockForConstraintsTests(ServiceItem)
//        mockForConstraintsTests(ExtServiceItem)
        extServiceItem = new ExtServiceItem();
    }

//    void testDummy() {
//       assertEquals 1, 1
//    }

	void XtestASJSON(){
		when:
		extServiceItem.id=1
		extServiceItem.state = new State()
		extServiceItem.types = new Types()

		def cfd1 = new CustomFieldDefinition(id:8, name:"Color", styleType:"TEXT", uuid:'cfd1')//.save()
		def cfd2 = new CustomFieldDefinition(id:9, name:"ShirtSize", styleType:"TEXT", uuid:'cfd2')//.save()
		mockDomain(CustomFieldDefinition, [cfd1, cfd2])
		extServiceItem.customFields = [
			new CustomField(id:3, value:"Red", customFieldDefinition: cfd1),
			new CustomField(id:4, value:"3", customFieldDefinition: cfd2)
		]
		extServiceItem.isHidden=1
		// TODO BVEST property doesn't exist
		// extServiceItem.isDeleted=1
		JSONObject json=extServiceItem.asJSON()

		then:
		assert json?.id != null
		//TODO BVEST These are not included in the asJSON calls
		//assert json?.serviceItem != null
		//assert json?.extCtx != null

		assert 1 == json.isHidden
		//assert 1 == json.serviceItem.isDeleted

		assert 2 == json.customFields.size()

		when:
		def ids=json?.customFields.collect {it.id }.
				grep{it}.sort()

		def expected = extServiceItem.customFields.collect {it.id }.
				grep{it}.sort()
		then:
		assert expected.equals(ids)
	}

}

