package marketplace

import spock.lang.Specification

import java.text.DateFormat
import java.text.SimpleDateFormat

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONObject

import marketplace.domain.builders.DomainBuilderMixin

import ozone.decorator.JSONDecoratorService


@Integration
@Rollback
class ImportTests extends Specification implements DomainBuilderMixin {

    JSONDecoratorService JSONDecoratorService
    ImportService importService
    ImportTaskService importTaskService

    Profile owner1

    JSONObject json

    final DateFormat JSON_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    void setup() {
        def jsonText = """
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
        JSONObject jsonResultObj = (JSONObject) JSON.parse(jsonText)
        JSONDecoratorService.postProcessJSON(jsonResultObj)
        json = jsonResultObj
    }

    void setupData() {
        owner1 = $adminProfile { username = 'importTestAdmin' }

        def configs = InterfaceConfiguration.list()
        if (!configs || configs.size() <= 0) {
            InterfaceConfiguration.FILE_IMPORT.save(flush: true)
            InterfaceConfiguration.OMP_INTERFACE.save(flush: true)
        }
    }

    void testReencodeDates() {
        when:
        setupData()

        def data = json.serviceItems[0]
        ["releaseDate",
         "createdDate",
         "editedDate"].each(JSONUtil.reencodeDate.curry("dd MMM yyyy hh:mm:ss a zzz", data, data))

        def releasedDate = JSON_DATE_FORMAT.parse(data.releaseDate)

        then:
        releasedDate == JSON_DATE_FORMAT.parse("2011-12-11T00:00:00GMT-05:00")
    }


    void testImportTaskResultQuery() {
        given:
        setupData()

        // Resolve the OMP InterfaceConfig so we can create a related ImportTask
        def ompConfig = InterfaceConfiguration.findByName(InterfaceConfiguration.OMP_INTERFACE.name)
        assert ompConfig != null

        when:
        // Create an ImportTask
        def task = save new ImportTask([name           : "Full Import",
                                        updateType     : Constants.IMPORT_TYPE_FULL,
                                        execInterval   : 60, // this is not used in testing
                                        url            : "https://shoppex.org/marketplace", // this is not used in testing
                                        interfaceConfig: ompConfig])

        // Create a SUCCESS ImportTaskResult
        save new ImportTaskResult([runDate: now(-4000),
                                   result : true,
                                   message: "Ran fine, captain",
                                   task   : task])

        // Create a SUCCESS ImportTaskResult
        def expectedResult = save new ImportTaskResult([runDate: now(-3000),
                                                        result : true,
                                                        message: "Ran fine again, captain",
                                                        task   : task])

        // Create a FAIL ImportTaskResult
        save new ImportTaskResult([runDate: now(-2000),
                                   result : false,
                                   message: "FAILED!!, captain",
                                   task   : task])

        // Create another FAIL ImportTaskResult
        save new ImportTaskResult([runDate: now(-1000),
                                   result : false,
                                   message: "FAILED AGAIN!!, captain",
                                   task   : task])

        // Find the latest SUCCESS result -- should be the second
        def latestSuccess = importTaskService.getLatestSuccessfulImportTaskResult(task.id)

        then:
        assert latestSuccess.id == expectedResult.id
    }


    void testFindDupesStates() {
        setup:
        setupData()

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

        $state {
            uuid = '1234'
            title = 'Active'
            description = 'Active listings'
            isPublished = true
        }

        when:
        def jsonResult = importService.findDupes((JSONObject) JSON.parse(testJson))

        then:
        jsonResult.states[0].needsResolving == false
        jsonResult.states[1].needsResolving == true
    }

    void testFindDupesCategories() {
        setup:
        setupData()

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

        $category { title = 'title1'; uuid = '1234' }
        $category { title = 'Category B'; uuid = '4543' }

        when:
        def jsonResult = importService.findDupes((JSONObject) JSON.parse(testJson))

        then:
        jsonResult.categories[0].needsResolving == false
        jsonResult.categories[1].needsResolving == false
        jsonResult.categories[2].needsResolving == true
        jsonResult.categories[3].needsResolving == true
    }

    void testFindCFD() {
        setup:
        setupData()

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

        $fieldDefinition {
            uuid = '1234'
            name = 'CFD A'
            label = 'CFD Label A'
        }

        $fieldDefinition {
            uuid = '4543'
            name = 'name1'
            label = 'label1'
        }

        when:
        def jsonResult = importService.findDupes((JSONObject) JSON.parse(testJson))

        then:
        jsonResult.customFieldDefs[0].needsResolving == false
        jsonResult.customFieldDefs[1].needsResolving == true
    }

    void testFindTypes() {
        setup:
        setupData()

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

        $type {
            uuid = '1234'
            title = 'type1'
        }

        when:
        def jsonResult = importService.findDupes((JSONObject) JSON.parse(testJson))

        then:
        jsonResult.types[0].needsResolving == false
        jsonResult.types[1].needsResolving == true
    }

    void testFindDupesServiceItems() {
        setup:
        setupData()

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

        def newType = $type {
            title = 'Widget'
            uuid = '1234'
        }

        $serviceItem {
            uuid = 'eba1178b-17f1-4721-88de-9536167261d0'
            title = 'Listing'
            type = newType
            owner = owner1
            launchUrl = 'http://www.google.com'
        }

        when:
        def jsonResult = importService.findDupes((JSONObject) JSON.parse(testJsonDupe))

        then:
        jsonResult.serviceItems[0].hasDuplicate == true
        jsonResult.serviceItems[1].hasDuplicate == false
    }

}
