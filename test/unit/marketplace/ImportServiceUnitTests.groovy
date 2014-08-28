package marketplace

import grails.test.mixin.TestFor
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.utils.User

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(ImportService)
class ImportServiceUnitTests {
    def importService
    User testAdmin1
    Profile testAdmin1Profile

    Agency agency1

    void setUp() {
        FakeAuditTrailHelper.install()

        testAdmin1 = new User()
        testAdmin1.username = "testAdmin1"

        testAdmin1Profile = new Profile()
        testAdmin1Profile.username = testAdmin1.username
		mockDomain(Profile, [testAdmin1Profile])
        mockDomain(TextCustomFieldDefinition)

        Types type1 = new Types()
		type1.with {
			id = 1
			uuid = "typ11111-1111-1111-1111-111111111111"
			title = "Type 1"
			description = "Type 1 description"
			editedDate = new Date(113, 0, 1) // Jan 1
			createdDate = new Date(113, 0, 1) // Jan 1
		}
        mockDomain(Types, [type1])

		Category category1 = new Category()
		category1.with {
			id = 1
			uuid = "cat11111-1111-1111-1111-111111111111"
			title = "Category 1"
			description = "Category 1 Definition"
			editedDate = new Date(113, 0, 1) // Jan 1
			createdDate = new Date(113, 0, 1) // Jan 1
		}
		mockDomain(Category, [category1])

		State state1 = new State()
		state1.with {
			id = 1
			uuid = "sta11111-1111-1111-1111-111111111111"
			title = "State 1"
			description = "State 1 description"
			isPublished = true
			editedDate = new Date(113, 0, 1) // Jan 1
			createdDate = new Date(113, 0, 1) // Jan 1
		}
        mockDomain(State, [state1])

		CustomFieldDefinition cfd1 = new TextCustomFieldDefinition()
		cfd1.with{
			id = 1
			uuid = "cfd11111-1111-1111-1111-111111111111"
			name = "Custom field 1"
			label = "Custom field 1 label"
			description = "Custom field 1 description"
			tooltip = "Customf field 1 tooltip"
			section="typeProperties"
			isRequired = false
			allTypes = false
			styleType = Constants.CustomFieldDefinitionStyleType.TEXT
			types = [type1]
			editedDate = new Date(113, 0, 1) // Jan 1
			createdDate = new Date(113, 0, 1) // Jan 1
		}
		mockDomain(CustomFieldDefinition, [cfd1])

        agency1 = new Agency(
            title: 'Agency 1',
            iconUrl: 'https://agency1.example.com/icon.png'
        )
        mockDomain(Agency, [agency1])

        importService = new ImportService()
    }

	/**
	 * Import file has same types, states, categories, cfds with all UUIDs matching.
	 * Verify all elements dropped from the end of the JSON.
	 * Import file has all different descriptions, but since the time stamps don't show that the import
	 * file is newer, verify none of the descriptions change.
	 */
    void testUpdateJsonRemoveDuplicates() {
        JSONObject importFile = buildSimpleImportJson1()

		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 0)
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat11111-1111-1111-1111-111111111111")
		assert(updatedJson.states.size() == 0)
		assert(updatedJson.serviceItems[0].state.uuid=="sta11111-1111-1111-1111-111111111111")
		assert(updatedJson.types.size() == 0)
		assert(updatedJson.serviceItems[0].types.uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.customFieldDefs.size() == 0)
		assert(updatedJson.serviceItems[0].customFields.size()==1)
		assert(updatedJson.serviceItems[0].customFields[0].customFieldDefinitionUuid=="cfd11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].customFields[0].value=="Custom field 1 value")
		assert(updatedJson.serviceItems[0].agency.title == agency1.title)
    }

	/**
	 * Import file has same types, states, categories, cfds but all the UUIDs differ.
	 * Verify all elements dropped from the end of the JSON.
	 * Import file has all different descriptions, but since the time stamps don't show that the import
	 * file is newer, verify none of the descriptions change.
	 */
    void testUpdateJsonRemoveDuplicatesLooseMatching() {
		// Change all the uuids and run import
        JSONObject importFile = buildSimpleImportJson1()
		importFile.categories[0].uuid="cat22222-2222-2222-2222-222222222222"
        importFile.serviceItems[0].categories[0].uuid="cat22222-2222-2222-2222-222222222222"
		importFile.types[0].uuid="typ22222-2222-2222-2222-222222222222"
        importFile.serviceItems[0].types.uuid="typ22222-2222-2222-2222-222222222222"
		importFile.states[0].uuid="sta22222-2222-2222-2222-222222222222"
		importFile.serviceItems[0].state.uuid="sta22222-2222-2222-2222-222222222222"
		importFile.customFieldDefs[0].uuid="cfd22222-2222-2222-2222-222222222222"
		importFile.customFieldDefs[0].types[0].uuid="typ22222-2222-2222-2222-222222222222"
		importFile.serviceItems[0].customFields[0].customFieldDefinitionUuid="cfd22222-2222-2222-2222-222222222222"

		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 0)
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat11111-1111-1111-1111-111111111111")
		assert(updatedJson.states.size() == 0)
		assert(updatedJson.serviceItems[0].state.uuid=="sta11111-1111-1111-1111-111111111111")
		assert(updatedJson.types.size() == 0)
		assert(updatedJson.serviceItems[0].types.uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.customFieldDefs.size() == 0)
		assert(updatedJson.serviceItems[0].customFields.size()==1)
		assert(updatedJson.serviceItems[0].customFields[0].customFieldDefinitionUuid=="cfd11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].customFields[0].value=="Custom field 1 value")
		assert(updatedJson.serviceItems[0].agency.title == agency1.title)
    }

	/**
	 * Import file has same types, states, categories, cfds with all UUIDs matching.
	 * Import file has all newer versions.
	 */
    void testUpdateJsonUpdateDuplicates() {
		// Update all the edited dates to make them newer
        JSONObject importFile = buildSimpleImportJson1()
		importFile.categories[0].editedDate ="2023-02-02T00:00:00Z"
		importFile.types[0].editedDate ="2023-02-02T00:00:00Z"
		importFile.states[0].editedDate ="2023-02-02T00:00:00Z"
        importFile.customFieldDefs[0].editedDate = "2023-02-02T00:00:00Z"

		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 1)
		assert(updatedJson.categories[0].title == "Category 1")
		assert(updatedJson.categories[0].description=="New category 1 description")
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat11111-1111-1111-1111-111111111111")
		assert(updatedJson.states.size() == 1)
		assert(updatedJson.states[0].title == "State 1")
		assert(updatedJson.states[0].description=="New state 1 description")
		assert(updatedJson.serviceItems[0].state.uuid=="sta11111-1111-1111-1111-111111111111")
		assert(updatedJson.types.size() == 1)
		assert(updatedJson.types[0].title == "Type 1")
		assert(updatedJson.types[0].description=="New type 1 description")
		assert(updatedJson.serviceItems[0].types.uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.customFieldDefs.size() == 1)
		assert(updatedJson.customFieldDefs[0].name == "Custom field 1")
		assert(updatedJson.customFieldDefs[0].label=="New custom field 1 label")
		assert(updatedJson.serviceItems[0].customFields.size()==1)
		assert(updatedJson.serviceItems[0].customFields[0].customFieldDefinitionUuid=="cfd11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].customFields[0].value=="Custom field 1 value")
		assert(updatedJson.serviceItems[0].agency.title == agency1.title)
    }

	/**
	 * Import file has same types, states, categories, cfds but all the UUIDs differ.
	 * Import file has all newer data.
	 */
    void testUpdateJsonUpdateDuplicatesLooseMatching() {
		// Both change uuids and update edited date
        JSONObject importFile = buildSimpleImportJson1()
		importFile.categories[0].editedDate ="2023-02-02T00:00:00Z"
		importFile.categories[0].uuid="cat22222-2222-2222-2222-222222222222"
        importFile.serviceItems[0].categories[0].uuid="cat22222-2222-2222-2222-222222222222"
		importFile.types[0].editedDate ="2023-02-02T00:00:00Z"
		importFile.types[0].uuid="typ22222-2222-2222-2222-222222222222"
        importFile.serviceItems[0].types.uuid="typ22222-2222-2222-2222-222222222222"
		importFile.states[0].editedDate ="2023-02-02T00:00:00Z"
		importFile.states[0].uuid="sta22222-2222-2222-2222-222222222222"
		importFile.serviceItems[0].state.uuid="sta22222-2222-2222-2222-222222222222"
		importFile.customFieldDefs[0].editedDate = "2023-02-02T00:00:00Z"
		importFile.customFieldDefs[0].uuid="cfd22222-2222-2222-2222-222222222222"
		importFile.customFieldDefs[0].types[0].uuid="typ22222-2222-2222-2222-222222222222"
		importFile.serviceItems[0].customFields[0].customFieldDefinitionUuid="cfd22222-2222-2222-2222-222222222222"

		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 1)
		assert(updatedJson.categories[0].title == "Category 1")
		assert(updatedJson.categories[0].description=="New category 1 description")
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat11111-1111-1111-1111-111111111111")
		assert(updatedJson.states.size() == 1)
		assert(updatedJson.states[0].title == "State 1")
		assert(updatedJson.states[0].description=="New state 1 description")
		assert(updatedJson.serviceItems[0].state.uuid=="sta11111-1111-1111-1111-111111111111")
		assert(updatedJson.types.size() == 1)
		assert(updatedJson.types[0].title == "Type 1")
		assert(updatedJson.types[0].description=="New type 1 description")
		assert(updatedJson.serviceItems[0].types.uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.customFieldDefs.size() == 1)
		assert(updatedJson.customFieldDefs[0].name == "Custom field 1")
		assert(updatedJson.customFieldDefs[0].label=="New custom field 1 label")
		assert(updatedJson.customFieldDefs[0].types[0].uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].customFields.size()==1)
		assert(updatedJson.serviceItems[0].customFields[0].customFieldDefinitionUuid=="cfd11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].customFields[0].value=="Custom field 1 value")
		assert(updatedJson.serviceItems[0].agency.title == agency1.title)
   }

	/**
	 * Import file brand new types, states and categories that need to be created.
	 */
	void testUpdateJsonCreate() {
		JSONObject importFile = buildSimpleImportJson2()

		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 1)
		assert(updatedJson.categories[0].title == "Category 2")
		assert(updatedJson.categories[0].description=="New category 2 description")
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat22222-2222-2222-2222-222222222222")
		assert(updatedJson.states.size() == 1)
		assert(updatedJson.states[0].title == "State 2")
		assert(updatedJson.states[0].description=="New state 2 description")
		assert(updatedJson.serviceItems[0].state.uuid=="sta22222-2222-2222-2222-222222222222")
		assert(updatedJson.types.size() == 1)
		assert(updatedJson.types[0].title == "Type 2")
		assert(updatedJson.types[0].description=="New type 2 description")
		assert(updatedJson.serviceItems[0].types.uuid=="typ22222-2222-2222-2222-222222222222")
		assert(updatedJson.customFieldDefs.size() == 1)
		assert(updatedJson.customFieldDefs[0].name == "Custom field 2")
		assert(updatedJson.customFieldDefs[0].types[0].uuid=="typ22222-2222-2222-2222-222222222222")
		assert(updatedJson.serviceItems[0].customFields.size()==1)
		assert(updatedJson.serviceItems[0].customFields[0].customFieldDefinitionUuid=="cfd22222-2222-2222-2222-222222222222")
		assert(updatedJson.serviceItems[0].customFields[0].value=="Custom field 2 value")
	}

	/**
	 * Import file types and categories that need to be mapped to types and categories on this system.
	 * Does not test mapping states.
	 */
	void testUpdateJsonMap() {
		// Change the resolution on the category and type to map to existing category and type.
		// We don't map custom fields, but we do verify a brand new custom field handles the type being mapped.
		JSONObject importFile = buildSimpleImportJson2()
		importFile.categories[0].mapsTo=1
		importFile.types[0].mapsTo=1
		importFile.agencies[0].mapsTo=1
		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 0)
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat11111-1111-1111-1111-111111111111")
		assert(updatedJson.types.size() == 0)
		assert(updatedJson.serviceItems[0].types.uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.customFieldDefs.size() == 1)
		assert(updatedJson.customFieldDefs[0].name == "Custom field 2")
		assert(updatedJson.customFieldDefs[0].types[0].uuid=="typ11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].customFields.size()==1)
		assert(updatedJson.serviceItems[0].customFields[0].customFieldDefinitionUuid=="cfd22222-2222-2222-2222-222222222222")
		assert(updatedJson.serviceItems[0].customFields[0].value=="Custom field 2 value")
		assert(updatedJson.serviceItems[0].agency.id == agency1.id)
	}

	/**
	 * Import file has types and categories should be removed before bringing data into the system.
	 * Does not test removing states.
	 */
	void testUpdateJsonRemove() {
		// First case is drop the new category, new custom field, and new agency
		JSONObject importFile = buildSimpleImportJson2()
		importFile.categories[0].mapsTo="remove"
		importFile.customFieldDefs[0].mapsTo="remove"
		importFile.agencies[0].mapsTo="remove"
		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size()==0)
		assert(updatedJson.serviceItems[0].categories.size()==0)
		assert(updatedJson.customFieldDefs.size()==0)
		assert(updatedJson.serviceItems[0].customFields.size()==0)
		assert(updatedJson.serviceItems[0].agency==null)
	}

	void testMappingMultipleCategories() {
		JSONObject importFile = buildImportWithManyCategories()
		importFile.categories[0].mapsTo=1
		importFile.categories[1].mapsTo=1
		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 1)
		assert(updatedJson.categories[0].title == "Category 4")
		assert(updatedJson.categories[0].description=="Category 4 description")
		assert(updatedJson.serviceItems[0].categories.size()==2)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat11111-1111-1111-1111-111111111111")
		assert(updatedJson.serviceItems[0].categories[1].uuid=="cat44444-4444-4444-4444-444444444444")

	}

	void testDeletingMultipleCategories() {
		JSONObject importFile = buildImportWithManyCategories()
		importFile.categories[0].mapsTo="remove"
		importFile.categories[1].mapsTo="remove"
		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.categories.size() == 1)
		assert(updatedJson.categories[0].title == "Category 4")
		assert(updatedJson.categories[0].description=="Category 4 description")
		assert(updatedJson.serviceItems[0].categories.size()==1)
		assert(updatedJson.serviceItems[0].categories[0].uuid=="cat44444-4444-4444-4444-444444444444")

	}

	void testMappingNoAgency() {
		// First case, map listings with no agency to an existing agency
		JSONObject importFile = buildSimpleImportJson2()
		importFile.serviceItems[0].remove("agency")
		importFile.agencies[1].mapsTo=1
		JSONObject updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.serviceItems[0].agency.id == agency1.id)

		// Second and third case, attempting to create or remove the "no agency" has the same effect.  Agency stays blank.
		importFile = buildSimpleImportJson2()
		importFile.serviceItems[0].remove("agency")
		importFile.agencies[1].mapsTo="remove"
		updatedJson =  importService.updateJSON(importFile)
 		assert(updatedJson.serviceItems[0].agency==null)

		importFile = buildSimpleImportJson2()
		importFile.serviceItems[0].remove("agency")
		importFile.agencies[1].mapsTo="create"
		updatedJson =  importService.updateJSON(importFile)
		assert(updatedJson.serviceItems[0].agency==null)
	}

	void testGetRequiredListings() {
		JSONObject importJSON = buildJSONWithRelationships()

		importJSON = importService.getRequiredListings(importJSON)

		def si0Requires = importJSON.serviceItems[0].getJSONArray("requires")
		def si1Requires = importJSON.serviceItems[1].getJSONArray("requires")
		def si2Requires = importJSON.serviceItems[2].getJSONArray("requires")

		assertEquals 2, si0Requires.size()
		assertEquals 1, si0Requires[0]
		assertEquals 2, si0Requires[1]
		assertEquals 1, si1Requires.size()
		assertEquals 2, si1Requires[0]
		assertEquals 0, si2Requires.size()
	}

	void testGetRequiredListingsWithInvalidRelationships() {
		JSONObject importJSON = buildJSONWithInvalidRelationships()

		importJSON = importService.getRequiredListings(importJSON)

		def siRequires = importJSON.serviceItems[0].getJSONArray("requires")

		assertEquals 0, siRequires.size()
	}

	private JSONObject buildSimpleImportJson1() {
        JSONObject root = new JSONObject();
		root.put("serviceItems", new JSONArray())
		JSONObject serviceItem = (JSONObject) JSON.parse("""\
			{
	            "id":2,
	            "title":"Listing",
	            "description":"Listing description",
	            "approvalStatus":"Approved",
	            "state":{
	                "id":2,
	                "title":"State 1",
	                "uuid":"sta11111-1111-1111-1111-111111111111"
	            },
	            "editedDate":"2013-01-01T00:00:00Z",
	            "types":{
					"id":2,
			    	"title":"Type 1",
			    	"description":"New type 1 description",
			    	"uuid":"typ11111-1111-1111-1111-111111111111",
	            },
	            "createdDate":"2013-01-01T00:00:00Z",
	            "customFields":[
	                {
	                    "customFieldDefinitionUuid":"cfd11111-1111-1111-1111-111111111111",
	                    "id":2,
	                    "fieldType":"TEXT",
	                    "name":"Custom field 1",
	                    "customFieldDefinitionId":2,
	                    "value":"Custom field 1 value",
	                    "label":"New custom field 1 label"
	                }
            	],
	            "isPublished":true,
				"agency":"Agency 1",
	            "categories":[
	                {
	                    "id":2,
	                    "title":"Category 1",
			    		"uuid":"cat11111-1111-1111-1111-111111111111",
	                }
	            ],
	            "uuid":"lis11111-1111-1111-1111-111111111111"
        	}
		""")
		root.serviceItems.add(serviceItem)
		root.put("states", new JSONArray())
		JSONObject state = (JSONObject) JSON.parse("""\
			{
			    "id":2,
			    "title":"State 1",
			    "description":"New state 1 description",
			    "isPublished":true,
			    "editedDate":"2013-01-01T00:00:00Z",
			    "uuid":"sta11111-1111-1111-1111-111111111111",
			    "createdDate":"2013-01-01T00:00:00Z",
				"needsResolving":false
			}
		""")
		root.states.add(state)
		root.put("categories", new JSONArray())
		JSONObject category = (JSONObject) JSON.parse("""\
			{
			    "id":2,
			    "title":"Category 1",
			    "description":"New category 1 description",
			    "editedDate":"2013-01-01T00:00:00Z",
			    "uuid":"cat11111-1111-1111-1111-111111111111",
			    "createdDate":"2013-01-01T00:00:00Z",
				"needsResolving":false
			}
			"""
		);
		root.categories.add(category)
		root.put("profiles", new JSONArray())
		root.put("types", new JSONArray())
		JSONObject type = (JSONObject) JSON.parse("""\
			{
			    "id":2,
			    "title":"Type 1",
			    "description":"New type 1 description",
			    "editedDate":"2013-01-01T00:00:00Z",
			    "uuid":"typ11111-1111-1111-1111-111111111111",
			    "createdDate":"2013-01-01T00:00:00Z",
				"needsResolving":false
			}
		""")
		root.types.add(type)
		root.put("relationships", new JSONArray())
		root.put("customFieldDefs", new JSONArray())
		JSONObject cfd = (JSONObject) JSON.parse("""\
			{
	            "id":2,
	            "name":"Custom field 1",
	            "label":"New custom field 1 label",
	            "description":"New custom field 1 description",
	            "tooltip":"New custom field 1 tooltip",
	            "uuid":"cfd11111-1111-1111-1111-111111111111",
	            "editedDate":"2013-01-01T00:00:00Z",
	            "createdDate":"2013-01-01T00:00:00Z",
	         	"fieldType":"TEXT",
	            "class":"marketplace.TextCustomFieldDefinition",
	            "section":"typeProperties",
	            "isRequired":false,
	            "allTypes":false,
	            "types":[
	                {
	                    "id":2,
	                    "title":"Type 1",
	                    "uuid":"typ11111-1111-1111-1111-111111111111"
	                }
	            ],
				"needsResolving":false
	   		}
		""")
		root.customFieldDefs.add(cfd)
		root.put("agencies", new JSONArray())
		JSONObject agency = (JSONObject) JSON.parse("""\
			{
			    "id":"Agency 1",
			    "name":"Agency 1",
				"needsResolving":false
			}
			"""
		);
		root.agencies.add(agency)
		JSONObject noAgency = (JSONObject) JSON.parse("""\
			{
			    "id":"",
			    "name":"",
				"needsResolving":true,
				"mapsTo":"create"
			}
			"""
		);
		root.agencies.add(noAgency)


        return root
    }

	private JSONObject buildSimpleImportJson2() {
		JSONObject root = new JSONObject();
		root.put("serviceItems", new JSONArray())
		JSONObject serviceItem = (JSONObject) JSON.parse("""\
			{
	            "id":2,
	            "title":"Listing",
	            "description":"Listing description",
	            "approvalStatus":"Approved",
	            "state":{
	                "id":2,
	                "title":"State 2",
	                "uuid":"sta22222-2222-2222-2222-222222222222"
	            },
	            "editedDate":"2013-02-02T00:00:00Z",
	            "types":{
					"id":2,
			    	"title":"Type 2",
			    	"description":"New type 2 description",
			    	"uuid":"typ22222-2222-2222-2222-222222222222",
	            },
	            "createdDate":"2013-02-02T00:00:00Z",
	            "customFields":[
	                {
	                    "customFieldDefinitionUuid":"cfd22222-2222-2222-2222-222222222222",
	                    "id":2,
	                    "fieldType":"TEXT",
	                    "name":"Custom field 2",
	                    "customFieldDefinitionId":2,
	                    "value":"Custom field 2 value",
	                    "label":"Custom field 2 label"
	                }
            	],
	            "isPublished":true,
				"agency":"Agency 2",
                "agencyIcon": "agency2.png",
	            "categories":[
	                {
	                    "id":2,
	                    "title":"Category 2",
			    		"uuid":"cat22222-2222-2222-2222-222222222222",
	                }
	            ],
	            "uuid":"lis22222-2222-2222-2222-222222222222",
        	}
		""")
		root.serviceItems.add(serviceItem)
		root.put("states", new JSONArray())
		JSONObject state = (JSONObject) JSON.parse("""\
			{
			    "id":2,
			    "title":"State 2",
			    "description":"New state 2 description",
			    "isPublished":true,
			    "editedDate":"2013-02-02T00:00:00Z",
			    "uuid":"sta22222-2222-2222-2222-222222222222",
			    "createdDate":"2013-02-02T00:00:00Z",
				"needsResolving":true,
				"mapsTo":"create"
			}
		""")
		root.states.add(state)
		root.put("categories", new JSONArray())
		JSONObject category = (JSONObject) JSON.parse("""\
			{
			    "id":2,
			    "title":"Category 2",
			    "description":"New category 2 description",
			    "editedDate":"2013-02-02T00:00:00Z",
			    "uuid":"cat22222-2222-2222-2222-222222222222",
			    "createdDate":"2013-02-02T00:00:00Z",
				"needsResolving":true,
				"mapsTo":"create"
			}
			"""
		);
		root.categories.add(category)
		root.put("profiles", new JSONArray())
		root.put("types", new JSONArray())
		JSONObject type = (JSONObject) JSON.parse("""\
			{
			    "id":2,
			    "title":"Type 2",
			    "description":"New type 2 description",
			    "editedDate":"2013-02-02T00:00:00Z",
			    "uuid":"typ22222-2222-2222-2222-222222222222",
			    "createdDate":"2013-02-02T00:00:00Z",
				"needsResolving":true,
				"mapsTo":"create"
			}
		""")
		root.types.add(type)
		root.put("relationships", new JSONArray())
		root.put("customFieldDefs", new JSONArray())
		JSONObject cfd = (JSONObject) JSON.parse("""\
			{
	            "id":2,
	            "name":"Custom field 2",
	            "label":"Custom field 2 label",
	            "description":"New custom field 2 description",
	            "tooltip":"New custom field 2 tooltip",
	            "uuid":"cfd22222-2222-2222-2222-222222222222",
	            "editedDate":"2013-02-02T00:00:00Z",
	            "createdDate":"2013-02-02T00:00:00Z",
	         	"fieldType":"TEXT",
	            "class":"marketplace.TextCustomFieldDefinition",
	            "section":"typeProperties",
	            "isRequired":false,
	            "allTypes":false,
	            "types":[
	                {
	                    "id":2,
	                    "title":"Type 2",
	                    "uuid":"typ22222-2222-2222-2222-222222222222"
	                }
	            ],
				"needsResolving":true,
				"mapsTo":"create"
	   		}
		""")
		root.customFieldDefs.add(cfd)
		root.put("agencies", new JSONArray())
		JSONObject agency = (JSONObject) JSON.parse("""\
			{
			    "id":"Agency 2",
			    "name":"Agency 2",
				"needsResolving":true,
				"mapsTo":"create"
			}
			"""
		);
		root.agencies.add(agency)
		JSONObject noAgency = (JSONObject) JSON.parse("""\
			{
			    "id":"",
			    "name":"",
				"needsResolving":true,
				"mapsTo":"create"
			}
			"""
		);
		root.agencies.add(noAgency)
		return root
	}

	/**
	 * Create a listing with 3 new categories
	 */
	private JSONObject buildImportWithManyCategories() {
		JSONObject root = buildSimpleImportJson2();
		// Create the categories
		JSONObject category = (JSONObject) JSON.parse("""\
			{
			    "id":3,
			    "title":"Category 3",
			    "description":"Category 3 description",
			    "editedDate":"2013-03-03T00:00:00Z",
			    "uuid":"cat33333-3333-3333-3333-333333333333",
			    "createdDate":"2013-03-03T00:00:00Z",
				"needsResolving":true,
				"mapsTo":"create"
			}
			"""
		);
		root.categories.add(category)
		category = (JSONObject) JSON.parse("""\
			{
			    "id":4,
			    "title":"Category 4",
			    "description":"Category 4 description",
			    "editedDate":"2013-04-04T00:00:00Z",
			    "uuid":"cat44444-4444-4444-4444-444444444444",
			    "createdDate":"2013-04-04T00:00:00Z",
				"needsResolving":true,
				"mapsTo":"create"
			}
			"""
		);
		root.categories.add(category)

		// Add the categories to the service item
		category = (JSONObject) JSON.parse("""\
			{
				"id":3,
				"title":"Category 3",
				"uuid":"cat33333-3333-3333-3333-333333333333",
			}
			"""
		);
		root.serviceItems[0].categories.add(category)
		category = (JSONObject) JSON.parse("""\
			{
				"id":4,
				"title":"Category 4",
				"uuid":"cat44444-4444-4444-4444-444444444444",
			}
			"""
	);
	root.serviceItems[0].categories.add(category)

		return root
	}

	private JSONObject buildJSONWithRelationships() {
		JSONObject importJSON = new JSONObject()
		importJSON.serviceItems = new JSONArray([
                new JSONObject(
                        id: 1,
                        title:"Listing 1",
	            		uuid:"lis11111-1111-1111-1111-111111111111"
                ), new JSONObject(
                		id:2,
			            title:"Listing 2",
			            uuid:"lis22222-2222-2222-2222-222222222222"
                ), new JSONObject(
                		id:3,
			            title:"Listing 3",
			            uuid:"lis33333-3333-3333-3333-333333333333"
                )
        ])

		importJSON.relationships = new JSONArray([
			new JSONObject(
				serviceItem: new JSONObject(
					title:"Listing 1",
	            	uuid:"lis11111-1111-1111-1111-111111111111"
	            ),
	            requires: new JSONArray([
	            	new JSONObject(
	            		title: "Listing 2",
	            		uuid:"lis22222-2222-2222-2222-222222222222"
	            	), new JSONObject(
	            		title:"Listing 3",
			            uuid:"lis33333-3333-3333-3333-333333333333"
			        )
	            ])
	        ), new JSONObject(
				serviceItem: new JSONObject(
					title: "Listing 2",
	            		uuid:"lis22222-2222-2222-2222-222222222222"
	            ),
	            requires: new JSONArray([
	            	new JSONObject(
	            		title:"Listing 3",
			            uuid:"lis33333-3333-3333-3333-333333333333"
			        )
	            ])
	        )
	    ])

		return importJSON
	}

	private JSONObject buildJSONWithInvalidRelationships() {
		JSONObject importJSON = new JSONObject()

		importJSON.serviceItems = new JSONArray([
                new JSONObject(
                        id: 1,
                        title:"Listing 1",
	            		uuid:"lis11111-1111-1111-1111-111111111111"
                )
        ])

		importJSON.relationships = new JSONArray([
			new JSONObject(
				serviceItem: new JSONObject(
					title:"Listing 1",
	            	uuid:"lis11111-1111-1111-1111-111111111111"
	            ),
	            requires: new JSONArray([
	            	new JSONObject(
	            		title: "Listing 2",
	            		uuid:"lis22222-2222-2222-2222-222222222222"
	            	)
	            ])
	        ), new JSONObject(
				serviceItem: new JSONObject(
					title: "Listing 2",
	            		uuid:"lis22222-2222-2222-2222-222222222222"
	            ),
	            requires: new JSONArray()
	        )
	    ])

		return importJSON
	}

}
