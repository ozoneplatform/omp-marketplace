package marketplace

import grails.test.mixin.TestFor

import ozone.utils.*

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(AffiliatedMarketplace)
class AffiliatedMarketplaceTests {

	def affiliatedMarketplace

    def grailsApplication

    void setUp() {
        FakeAuditTrailHelper.install()

		mockForConstraintsTests(AffiliatedMarketplace)
		affiliatedMarketplace = new AffiliatedMarketplace()
    }

	void testActiveNotNullInitialized() {
		assert affiliatedMarketplace.active != null, "active should be initialized set"
		assertEquals "active should be initialized to 1", affiliatedMarketplace.active, new Integer(1)
	}

	/**
	* class AffiliatedMarketplace {
	* 		...
	* 		name maxSize: 50
	* 		serverUrl maxSize: 2083
	* 		...
	* }
	*/
	void testSizeContraints(){
		TestUtil.checkSizeConstraintProperty('name',affiliatedMarketplace, 50)
		TestUtil.checkSizeConstraintProperty('serverUrl',affiliatedMarketplace, 2083, null, true)
	}

	/**
	* class AffiliatedMarketplace {
	* 		...
	* 		name blank: false
	* 		...
	* }
	*/
   void testNameNotBlank(){
	   affiliatedMarketplace = new AffiliatedMarketplace(name: '')
       affiliatedMarketplace.name = ''
	   assertFalse affiliatedMarketplace.validate()
	   assertEquals 'name is blank.', 'blank', affiliatedMarketplace.errors['name']

	   affiliatedMarketplace.name = "Not Blank Name For Test"
	   assertFalse affiliatedMarketplace.validate() //Other items haven't been set yet
	   assertNotSame "name is not allowed to be blank.", 'blank', affiliatedMarketplace.errors['name']
   }

   /**
   * class AffiliatedMarketplace {
   * 		...
   * 		serverUrl blank: false
   * 		...
   * }
   */
  void testServerUrlNotBlank(){
	  affiliatedMarketplace = new AffiliatedMarketplace()
      affiliatedMarketplace.serverUrl = ''
	  assertFalse affiliatedMarketplace.validate()
	  assertEquals 'serverUrl is blank.', 'blank', affiliatedMarketplace.errors['serverUrl']

	  affiliatedMarketplace.serverUrl = "http://not.null.url.com"
	  assertFalse affiliatedMarketplace.validate() //Other items haven't been set yet
	  assertNotSame "serverUrl is not allowed to be blank.", 'blank', affiliatedMarketplace.errors['serverUrl']
  }

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
	  TestUtil.assertPropertyRequired('name',affiliatedMarketplace)
	  TestUtil.assertPropertyRequired('serverUrl',affiliatedMarketplace)

	  affiliatedMarketplace = new AffiliatedMarketplace(active: null)
	  TestUtil.assertPropertyRequired('active',affiliatedMarketplace)
  }

  /**
     * class AffiliatedMarketplace {
     * 		...
     * 		timeout nullable: true
     * 		icon nullable: true
     * 		...
     * }
     */
    void testNullable(){
        affiliatedMarketplace = new AffiliatedMarketplace(timeout: null,
            icon: null)

        assertFalse affiliatedMarketplace.validate()

        assertNotSame "timeout should be allowed to be nullable.", 'nullable', affiliatedMarketplace.errors['timeout']
        assertNotSame "icon should be allowed to be nullable.", 'nullable', affiliatedMarketplace.errors['icon']
    }

	/**
	* class AffiliatedMarketplace {
	* 		...
	* 		cache true
	* 		...
	* }
	*/
   void testMappingValues(){
	   def mappingValues = ClosureInterrogator.extractValuesFromClosure(AffiliatedMarketplace.mapping)

	   assertTrue mappingValues.cache
	}

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
		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "https://www.foo.com")
		affiliatedMarketplace.validate()
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "https://192.168.20.28:8443")
		assertFalse affiliatedMarketplace.validate()
		affiliatedMarketplace.errors.allErrors.each {
				println it
		}
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://localhost/marketplaceA")
		assertFalse affiliatedMarketplace.validate()
		affiliatedMarketplace.errors.allErrors.each {
				println it
		}
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://localhost:8080/marketplaceA")
		assertFalse affiliatedMarketplace.validate()
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://my-machine/marketplaceX")
		assertFalse affiliatedMarketplace.validate()
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://pctina/marketplaceY")
		assertFalse affiliatedMarketplace.validate()
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://pctina:8080/marketplaceZ")
		assertFalse affiliatedMarketplace.validate()
		assertNull affiliatedMarketplace.errors['serverUrl']

		affiliatedMarketplace = new AffiliatedMarketplace(serverUrl: "http://pctina:80805/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
		assertFalse affiliatedMarketplace.validate()
		assertNull affiliatedMarketplace.errors['serverUrl']
	}



	void testPrettyPrint(){
		affiliatedMarketplace = new AffiliatedMarketplace(name: "DB02 Marketplace",
													      serverUrl: "https://owfdb02:8443/marketplace")
		println "Pretty Print:"
		println affiliatedMarketplace.prettyPrint()
		println "Test String:"
		println "${affiliatedMarketplace.name} : ${affiliatedMarketplace.serverUrl}"
		assertEquals "${affiliatedMarketplace.id} ${affiliatedMarketplace.name} : ${affiliatedMarketplace.serverUrl}", affiliatedMarketplace.prettyPrint()
	}

}
