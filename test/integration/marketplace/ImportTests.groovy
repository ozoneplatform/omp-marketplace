package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.codehaus.groovy.grails.web.json.JSONObject
import grails.converters.JSON

import java.text.DateFormat
import java.text.SimpleDateFormat

@TestMixin(IntegrationTestMixin)
class ImportTests extends ImportTestBase {

	def JSONDecoratorService
  def importService

    def owner

    final String FULL_TEST_FILE = "resources/marketplaceImportStub-full.json"
    final String FULL_TEST_FILE_PROBLEM = "resources/marketplaceImportStub-full-problemData.json"
    final String DELTA_TEST_FILE1 = "resources/marketplaceImportStub-delta1.json"
    // These external warehouse test files are for testing external warehouses which have
    //   looser import matching semantics
    final String EXT_FULL_TEST_FILE = "resources/marketplaceImportStub-full-extWarehs.json"
    final String EXT_DELTA_TEST_FILE1 = "resources/marketplaceImportStub-delta1-extWarehs.json"

    final DateFormat JSON_DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)

	void setUp() {
        super.setUp()

        owner = new Profile(username: 'testAdmin1')

		json = """
		{
      "states" : [{
					"title" : "Active",
					"description" : "Active listings",
					"published" : "true"
				}
			],
			"types" : [{
					"title" : "Widget",
					"ozoneAware" : "true",
					"hasLaunchURL" : "true",
					"hasIcons" : "true"
				}, {
					"title" : "Webapp",
					"ozoneAware" : "true",
					"hasLaunchURL" : "true",
					"hasIcons" : "true"
				}, {
					"title" : "Service:REST",
					"ozoneAware" : "false",
					"hasLaunchURL" : "true",
					"hasIcons" : "false"
				}
			],
			"categories" : [{
					"title" : "Category A",
					"description" : "A-level applications"
				}, {
					"title" : "Category B"
				}, {
					"title" : "Category X"
				}, {
					"title" : "Category Y"
				}
			],
			"serviceItems" : [{
					"launchUrl" : "",
					"state" : "",
					"versionName" : "(U) 1.0",
					"title" : "(U) Craig Widget 01",
					"releaseDate" : "11 Dec 2011 12:00:00 AM EST",
					"description" : "(U) Description",
					"approvalStatus" : "Approved",
					"categories" : [{
							"title" : "Category A"
						}, {
							"title" : "Category B"
						}
					],
					"types" : "Service:REST,Webapp",
					"isEnabled" : "True",
					"requirements" : "reqs",
					"dependencies" : "deps",
					"organization" : "Civil Aviation Intelligence Analysis Center (CAIAC)",
					"docUrls" : [],
					"recommendedLayouts" : [""],
					"customFields" :
					[
							{
							"name" : "storefront",
							"value" : ""
							},{
							"name" : "storefrontID",
							"value" : ""
							},{
							"name" : "Custom Field 1",
							"value" : ""
							},{
							"name" : "Custom Field 2",
							"value" : ""
							}
					],
					"installUrl" : "",
					"screenshot1Url" : "",
					"screenshot2Url" : "",
					"techPocs" : [""],
					"author" : "testUser1",
					"imageSmallUrl" : "",
					"imageLargeUrl" : "",
					"systemUri" : "external:system",
					"externalId" : "",
					"externalViewUrl" : "http://www.external_view_url.com",
					"externalEditUrl" : "http://www.external_edit_url.com/edit"

			}]
		 }
			"""
		JSONObject jsonResultObj = (JSONObject) JSON.parse(json)
		JSONDecoratorService.postProcessJSON(jsonResultObj)
		json = jsonResultObj
    }




    void testReencodeDates() {
        def jo = json
        def data = jo.serviceItems[0]
System.err.println "before reencoding, releaseDate = ${data.releaseDate}"
        [
            "releaseDate",
            "createdDate",
            "editedDate"
        ].each(JSONUtil.reencodeDate.curry("dd MMM yyyy hh:mm:ss a zzz", data, data))
System.err.println "after reencoding, releaseDate = ${data.releaseDate}"

        def releaseDate = JSON_DATE_FORMAT.parse(data.releaseDate)

        assert releaseDate ==
            JSON_DATE_FORMAT.parse("2011-12-11T00:00:00-05")
    }


    void testImportTaskResultQuery() {
        // Resolve the OMP InterfaceConfig so we can create a related ImportTask
        def ompConfig = InterfaceConfiguration.findByName(InterfaceConfiguration.OMP_INTERFACE.name)
        assert true ==  ompConfig != null

       // Create an ImportTask
       def task = new ImportTask(
               name:"Full Import",
               updateType:Constants.IMPORT_TYPE_FULL,
               execInterval:60, // this is not used in testing
               url:"https://shoppex.org/marketplace", // this is not used in testing
               interfaceConfig: ompConfig
       ).save(failOnError:true)

        int successResult = 0

        // Create a SUCCESS ImportTaskResult
        ImportTaskResult result = new ImportTaskResult(
            runDate: new Date(),
            result: true,
            message:"Ran fine, captain",
            task: task
        ).save(failOnError:true)

        // Pause 500ms
        Thread.currentThread().sleep(500);

        // Create a SUCCESS ImportTaskResult
        result = new ImportTaskResult(
            runDate: new Date(),
            result: true,
            message:"Ran fine again, captain",
            task: task
        ).save(failOnError:true)

        // THIS IS THE RESULT WE EXPECT
        successResult = result.id

        // Pause 500ms
        Thread.currentThread().sleep(500);

        // Create a FAIL ImportTaskResult
        result = new ImportTaskResult(
            runDate: new Date(),
            result: false,
            message:"FAILED!!, captain",
            task: task
        ).save(failOnError:true)

        // Pause 500ms
        Thread.currentThread().sleep(500);

        // Create another FAIL ImportTaskResult
        result = new ImportTaskResult(
            runDate: new Date(),
            result: false,
            message:"FAILED AGAIN!!, captain",
            task: task
        ).save(failOnError:true)

        // Find the latest SUCCESS result -- should be the second
        def latestSuccess = importTaskService.getLatestSuccessfulImportTaskResult(task.id)

        assert latestSuccess.id == successResult
    }



    void testFindDupesStates() {
      def testJson = """
      {
        "states" : [{
            "title" : "Active",
            "description" : "Active listings",
            "published" : "true",
            "uuid" : "4321"
          }, {
            "title" : "Not Active",
            "description" : "Active listings",
            "published" : "true",
            "uuid" : "9876"
          }
        ]
       }
        """

      new State(title: "Active", description: "Active listings", isPublished: true, uuid: "1234").save(failOnError:true)
      def jsonResult = JSON.parse(testJson)
      jsonResult = importService.findDupes(jsonResult)
      assert false == jsonResult.states[0].needsResolving
      assert true ==  jsonResult.states[1].needsResolving
    }

    void testFindDupesCategories() {
      def testJson = """
      {
        "categories" : [{
            "title" : "Category A",
            "description" : "A-level applications",
            "uuid" : "1234"
          }, {
            "title" : "Category B",
            "uuid" : "4321"
          }, {
            "title" : "Category X",
            "uuid" : "9876"
          }, {
            "title" : "Category Y",
            "uuid" : "6789"
          }
        ]
       }
        """

      new Category(title: "title1", uuid: "1234").save(failOnError:true)
      new Category(title: "Category B", uuid: "4543").save(failOnError:true)
      def jsonResult = JSON.parse(testJson)
      jsonResult = importService.findDupes(jsonResult)
      assert false == jsonResult.categories[0].needsResolving
      assert false == jsonResult.categories[1].needsResolving
      assert true ==  jsonResult.categories[2].needsResolving
      assert true ==  jsonResult.categories[3].needsResolving
    }

    void testFindCFD() {
      def testJson = """
      {
        "customFieldDefs" : [{
            "name" : "CFD A",
            "label" : "CFD Label A",
            "uuid" : "1234"
          }, {
            "name" : "CFD B",
            "label" : "CFD Label B",
            "uuid" : "4321"
          }
        ]
      }
        """

      new CustomFieldDefinition(
            name: "CFD A",
            label: "CFD Label A",
            uuid: "1234",
            styleType: Constants.CustomFieldDefinitionStyleType.TEXT
        ).save(failOnError:true)
      new CustomFieldDefinition(
        name: "name1",
        label: "label1",
        uuid: "4543",
        styleType: Constants.CustomFieldDefinitionStyleType.TEXT
      ).save(failOnError:true)
      def jsonResult = JSON.parse(testJson)
      jsonResult = importService.findDupes(jsonResult)
      assert false == jsonResult.customFieldDefs[0].needsResolving
      assert true ==  jsonResult.customFieldDefs[1].needsResolving
    }

    void testFindTypes() {
      def testJson = """
      {
        "types" : [{
            "title" : "type1",
            "uuid" : "1234"
          }, {
            "title" : "type2",
            "uuid" : "4321"
          }
        ]
      }
        """

      new Types(title: "type1", uuid: "1234").save(failOnError:true)
      def jsonResult = JSON.parse(testJson)
      jsonResult = importService.findDupes(jsonResult)
      assert false == jsonResult.types[0].needsResolving
      assert true ==  jsonResult.types[1].needsResolving
    }

    void testFindDupesServiceItems() {
        def testJsonDupe = """
          {
            "serviceItems" : [{
                "launchUrl": "http://example.org/launch",
                "state": "Active",
                "versionName": "2.1",
                "title": "This is a service item title",
                "releaseDate": "2010-10-27T16:06:07Z",
                "description": "Just testing, nothing to see here",
                "approvalStatus": "Pending",
                "types": "App Component",
                "techPoc": "tsmith",
                "universalName":"listing.list.com",
                "author": "testAdmin1",
                "systemUri": "external:system",
                "systemName": "external:system",
                "externalId": "654323246s",
                "externalViewUrl": "http://www.dumbo15.com",
                "externalEditUrl": "http://www.dumbo15.com/edit",
                "uuid": "eba1178b-17f1-4721-88de-9536167261d0"
            },
            {
                "launchUrl": "http://example.org/launch",
                "state": "Active",
                "versionName": "2.1",
                "title": "This is a service item title",
                "releaseDate": "2010-10-27T16:06:07Z",
                "description": "Just testing, nothing to see here",
                "approvalStatus": "Pending",
                "types": "App Component",
                "techPoc": "tsmith",
                "universalName":"unique.unique.com",
                "author": "testAdmin1",
                "systemUri": "external:system",
                "systemName": "external:system",
                "externalId": "654323246s",
                "externalViewUrl": "http://www.dumbo15.com",
                "externalEditUrl": "http://www.dumbo15.com/edit",
                "uuid": "1057e852-259e-41df-aae6-ea0b4b251858"
            }
            ]
          }
            """
        new Types(title: "Widget", uuid: "1234").save(failOnError:true)
        new ServiceItem(
            owners: [owner],
            title: "Listing",
            types: Types.findByTitle("Widget"),
            launchUrl: "http://www.google.com",
            uuid:"eba1178b-17f1-4721-88de-9536167261d0"
        ).save(failOnError:true)
        def jsonResult = JSON.parse(testJsonDupe)
        jsonResult = importService.findDupes(jsonResult)
        assert true ==  jsonResult.serviceItems[0].hasDuplicate
        assert false == jsonResult.serviceItems[1].hasDuplicate
    }

}
