package marketplace.grails.domain

import grails.test.mixin.TestFor
import marketplace.AffiliatedMarketplace
import marketplace.Constants
import marketplace.grails.domain.DomainConstraintsUnitTest
import org.springframework.beans.factory.annotation.Autowired
import ozone.utils.*

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(AffiliatedMarketplace)
class AffiliatedMarketplaceSpec extends Specification implements  DomainConstraintsUnitTest<AffiliatedMarketplace>{

	@Autowired
	AffiliatedMarketplace affiliatedMarketplace
	//def affiliatedMarketplace

    //def grailsApplication
	Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true }}}
    void setup() {
        //FakeAuditTrailHelper.install()

		//mockForConstraintsTests(AffiliatedMarketplace)
		//affiliatedMarketplace = new AffiliatedMarketplace()
    }

	void testActiveNotNullInitialized() {
		expect:
		initializedPropertyIsRequired("active", 1)
//		assert affiliatedMarketplace.active != null, "active should be initialized set"
//		assertEquals "active should be initialized to 1", affiliatedMarketplace.active, new Integer(1)
	}

	/**
	* class AffiliatedMarketplace {
	* 		...
	* 		name maxSize: 50
	* 		serverUrl maxSize: 2083
	* 		...
	* }
	*/
	void testSizeConstraints(){
		expect:
		urlPropertyHasMaxSize("serverUrl", Constants.MAX_URL_SIZE)
		propertyHasMaxSize("name", 50)
//		TestUtil.checkSizeConstraintProperty('name',affiliatedMarketplace, 50)
//		TestUtil.checkSizeConstraintProperty('serverUrl',affiliatedMarketplace, 2083, null, true)
	}
//
//	/**
//	* class AffiliatedMarketplace {
//	* 		...
//	* 		name blank: false
//	* 		...
//	* }
//	*/
   void testNameNotBlank(){
	   expect:
	   propertyIsRequired("name")
//	   affiliatedMarketplace = new AffiliatedMarketplace(name: '')
//       affiliatedMarketplace.name = ''
//	   assertFalse affiliatedMarketplace.validate()
//	   assertEquals 'name is blank.', 'blank', affiliatedMarketplace.errors['name']

//	   affiliatedMarketplace.name = "Not Blank Name For Test"
//	   assertFalse affiliatedMarketplace.validate() //Other items haven't been set yet
//	   assertNotSame "name is not allowed to be blank.", 'blank', affiliatedMarketplace.errors['name']
   }
//
//   /**
//   * class AffiliatedMarketplace {
//   * 		...
//   * 		serverUrl blank: false
//   * 		...
//   * }
//   */
  void testServerUrlNotBlank(){
	  expect:
	  propertyIsRequired("serverUrl")
//	  affiliatedMarketplace = new AffiliatedMarketplace()
//      affiliatedMarketplace.serverUrl = ''
//	  assertFalse affiliatedMarketplace.validate()
//	  assertEquals 'serverUrl is blank.', 'blank', affiliatedMarketplace.errors['serverUrl']

	  propertyValueIsValid("serverUrl", "http://not.null.url.com")
//	  affiliatedMarketplace.serverUrl = "http://not.null.url.com"
//	  assertFalse affiliatedMarketplace.validate() //Other items haven't been set yet
//	  assertNotSame "serverUrl is not allowed to be blank.", 'blank', affiliatedMarketplace.errors['serverUrl']
  }
//
   /**
   * class AffiliatedMarketplace {
   * 		...
   * 		name nullable: false
   * 		serverUrl nullable: false
   * 		active nullable: false
   * 		...
   * }
   */
  void testNullConstraints(){
	  expect:
	  propertyIsRequired("name")
	  propertyIsRequired("serverUrl")
	  initializedPropertyIsRequired("active", 1)

//	  TestUtil.assertPropertyRequired('name',affiliatedMarketplace)
//	  TestUtil.assertPropertyRequired('serverUrl',affiliatedMarketplace)
//
//	  affiliatedMarketplace = new AffiliatedMarketplace(active: null)
//	  TestUtil.assertPropertyRequired('active',affiliatedMarketplace)
  }
//
  /**
     * class AffiliatedMarketplace {
     * 		...
     * 		timeout nullable: true
     * 		icon nullable: true
     * 		...
     * }
     */
    void testNullable(){
		expect:
		propertyValueIsValid("timeout", null)
		propertyValueIsValid("icon", null)
//        affiliatedMarketplace = new AffiliatedMarketplace(timeout: null,
//            icon: null)
//
//        assertFalse affiliatedMarketplace.validate()
//
//        assertNotSame "timeout should be allowed to be nullable.", 'nullable', affiliatedMarketplace.errors['timeout']
//        assertNotSame "icon should be allowed to be nullable.", 'nullable', affiliatedMarketplace.errors['icon']
    }
//
	/**
	* class AffiliatedMarketplace {
	* 		...
	* 		cache true
	* 		...
	* }
	*/
   void testMappingValues(){
	   given:
	   def mappingValues = ClosureInterrogator.extractValuesFromClosure(AffiliatedMarketplace.mapping)

	   expect:
	   mappingValues.cache == true
	}
//
	/**
	* class AffiliatedMarketplace {
	* 		...
	* 		serverUrl validator: {
	* 			...<custom>...
	*       }
	* 		...
	* }
	*/
	void testServerURLValid() {
		expect:
		propertyValueIsValid("serverUrl", "https://www.foo.com")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "https://www.foo.com")
//		affiliatedMarketplace.validate()
//		assertNull affiliatedMarketplace.errors['serverUrl']

		propertyValueIsValid("serverUrl", "https://192.168.20.28:8443")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "https://192.168.20.28:8443")
//		/*assertFalse*/ affiliatedMarketplace.validate()
//		affiliatedMarketplace.errors.allErrors.each {
//			println it
//		}
//		assertNull affiliatedMarketplace.errors['serverUrl']
//
		propertyValueIsValid("serverUrl", "http://localhost/marketplaceA")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://localhost/marketplaceA")
//		assertFalse affiliatedMarketplace.validate()
//		affiliatedMarketplace.errors.allErrors.each {
//				println it
//		}
//		assertNull affiliatedMarketplace.errors['serverUrl']
//
		propertyValueIsValid("serverUrl", "http://localhost:8080/marketplaceA")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://localhost:8080/marketplaceA")
//		assertFalse affiliatedMarketplace.validate()
//		assertNull affiliatedMarketplace.errors['serverUrl']
//
		propertyValueIsValid("serverUrl",  "http://my-machine/marketplaceX")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://my-machine/marketplaceX")
//		assertFalse affiliatedMarketplace.validate()
//		assertNull affiliatedMarketplace.errors['serverUrl']
//
		propertyValueIsValid("serverUrl", "http://pctina/marketplaceY")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://pctina/marketplaceY")
//		assertFalse affiliatedMarketplace.validate()
//		assertNull affiliatedMarketplace.errors['serverUrl']
//
		propertyValueIsValid("serverUrl", "http://pctina:8080/marketplaceZ")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://pctina:8080/marketplaceZ")
//		assertFalse affiliatedMarketplace.validate()
//		assertNull affiliatedMarketplace.errors['serverUrl']
//
		propertyValueIsValid("serverUrl", "http://pctina:80805/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
//		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://pctina:80805/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
//		assertFalse affiliatedMarketplace.validate()
//		assertNull affiliatedMarketplace.errors['serverUrl']
	}
//
//
//
	void testPrettyPrint(){
		given:
		affiliatedMarketplace = new AffiliatedMarketplace(name: "DB02 Marketplace",
													      serverUrl: "https://owfdb02:8443/marketplace")
		def prnt = affiliatedMarketplace.prettyPrint()

		expect:
		"${affiliatedMarketplace.id} ${affiliatedMarketplace.name} : ${affiliatedMarketplace.serverUrl}" == prnt
	}

}
