package marketplace.service

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.Agency
import marketplace.Category
import marketplace.CheckBoxCustomField
import marketplace.CheckBoxCustomFieldDefinition
import marketplace.Constants
import marketplace.Contact
import marketplace.ContactType
import marketplace.DropDownCustomField
import marketplace.DropDownCustomFieldDefinition
import marketplace.ExtServiceItem
import marketplace.ExtServiceItemService
import marketplace.FieldValue
import marketplace.ImageURLCustomField
import marketplace.ImageURLCustomFieldDefinition
import marketplace.ObjectNotFoundException
import marketplace.Profile
import marketplace.RecommendedLayout
import marketplace.ServiceItemService
import marketplace.State
import marketplace.TextAreaCustomField
import marketplace.TextAreaCustomFieldDefinition
import marketplace.TextCustomField
import marketplace.TextCustomFieldDefinition
import marketplace.Types
import marketplace.controller.MarketplaceIntegrationTestCase;
import org.grails.web.json.JSONObject
import spock.lang.Ignore
import ozone.utils.Utils
import ozone.marketplace.domain.ValidationException

@Ignore
@Rollback
@Integration
class ExtServiceItemServiceTests extends MarketplaceIntegrationTestCase {
	ExtServiceItemService extServiceItemService
	ServiceItemService serviceItemService
	def JSONDecoratorService

	static final String CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL = "fieldType"

	def svcProps
	def microformat

	def SYSTEM_URI=[
		"external:system1",
		"external:system2"
	]

	void setup() {
//		super.setUp();

        switchExternalAdmin('extadmin')

		// Make sure referenced objects exist in database first
		def types = Types.build(title:"Service:JUnit")
        types.save()
		def typesList = new ArrayList<Types>()
		typesList.add(types)
		def state = State.build(title:"JUnit")
        state.save(failOnError: true)
		def cat1 = Category.build(title:"JUnit Cat 1")
        cat1.save()
		def cat2 = Category.build(title:"JUnit Cat 2")
        cat2.save()
		def cdf1 = TextCustomFieldDefinition.build(name:"JUnit Custom 1", types: typesList, isRequired: true)
        cdf1.save()
		def cdf2 = TextCustomFieldDefinition.build(name:"JUnit Custom 2", types: typesList, isRequired: false)
		cdf2.save()

		microformat = Types.findByTitle('Microformats')

		svcProps = [
					title: "JUnit Test Service",
					description:"Just testing, nothing to see here",
					launchUrl:"http://example.org/launch",
					releasedDate: new Date(),
					avgRate:0,
					totalVotes:0,
					//isDeleted:0,
					isHidden:0,
					imageLargeUrl:"http://launch.url.gov/image.gif",
            		imageMediumUrl:"http://launch.url.gov/image.gif",
					imageSmallUrl:"http://launch.url.gov/image.gif",
					versionName:"2.1",
					organization:"External Service Provider Inc",
					techPocs: ["junitTechPoc"],
					types: types,
					state: state,
//					customFields: [ 1 :
//						new TextCustomField(value: "cust 1",
//							customFieldDefinition: cdf1)
//					],
					customFields: [
						new TextCustomField(value: "cust 1",
							customFieldDefinition: cdf1)
					],
					categories: [ cat1, cat2 ],

                    systemUri:"external:system",
                    externalId: Utils.generateUUID()
				]
	}

    def logIt(def strIn)
    {
        extServiceItemService.logIt(strIn)
    }

	void testUriSearch(){
		when:
		def expected = []as Set

		// Add some internal users
		8.times { newServiceItem "service ${it}" }

		8.times {
			def svc = newExtServiceItem(SYSTEM_URI[0], "ext service ${it}")
			expected.add svc.externalId
		}
		16.times {
			newExtServiceItem(SYSTEM_URI[1], "ext service ${it}")
		}

		// Limit search to first system
		def params = [ systemUri : SYSTEM_URI[0]]

		def list =
				extServiceItemService.extServiceItems(params)['extServiceItemList']

		then:
		// should only get back SYSTEM_URI[0]
		8 == list.size()
		expected == list.collect {it.externalId } as Set

		and:
		list.each {
			expect:
			SYSTEM_URI[0] == it.systemUri
		}
	}

	void testIdSearch(){
		when:
		def expected = []
		8.times {
			def svc = newExtServiceItem(SYSTEM_URI[0], "ext service ${it}")
			expected.add svc
		}

		def params = [ id: expected[0].id ]
		def list =
			extServiceItemService.extServiceItems(params)['extServiceItemList']

		then:
		1 == list.size()
		expected[0] == list[0]
	}

	void testNoUriSearch(){
		when:
		def expected = []as Set

		// Add some internal users
		8.times { newServiceItem "service ${it}" }

		8.times {
			def svc = newExtServiceItem(SYSTEM_URI[0], "ext service ${it}")
			expected.add svc.externalId
		}
		16.times {
			def svc = newExtServiceItem(SYSTEM_URI[1], "ext service ${it}")
			expected.add svc.externalId
		}

		// Limit search to first system
		def params = [:]

		def list =
				extServiceItemService.extServiceItems(params)['extServiceItemList']

		then:
		// should only get back SYSTEM_URI[0]
		24 == list.size()
		expected == list.collect {it.externalId } as Set
	}

	void testNoIdSearch(){
		when:
		def expected = []
		8.times {
			def svc = newExtServiceItem(SYSTEM_URI[0], "ext service ${it}")
			expected.add svc
		}

        // TODO: clean up this test
		//def params = [ id: expected[0].id ]
        def params = [ id: 80111 ]

		//expected[0].delete()

		def list =
			extServiceItemService.extServiceItems(params)['extServiceItemList']

		then:
		0 == list.size()
	}


	void testExternalIdSearch(){
		when:
		def expected = []
		8.times {
            def extSvc = newExtServiceItem(SYSTEM_URI[0], "ext service ${it}")
            extSvc.externalId = Utils.generateUUID()
            extSvc.save(flush:true)
			expected.add extSvc
		}

		def params = [ externalId: expected[0].externalId ]
		def list =
			extServiceItemService.extServiceItems(params)['extServiceItemList']

		then:
		1 == list.size()
		expected[0] == list[0]
	}

	void testNoExternalIdSearch(){
		when:
		def expected = []
		8.times {
			def author = Profile.build()


			def extSvc = new ExtServiceItem(
                owners:[author],
                systemUri: SYSTEM_URI[0],
                externalId:Utils.generateUUID())

            extSvc.techPocs = [author.username]
            extSvc.save(flush:true)
			expected.add extSvc
		}

		def params = [ externalId: expected[0].externalId ]

		expected[0].delete()

		def list =
			extServiceItemService.extServiceItems(params)['extServiceItemList']

		then:
		0 == list.size()
	}

	void XtestCreate(){
		when:
		def user = Profile.build(username:"junitAuth")

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		def extSvc = new ExtServiceItem(svcProps)
        def json = extSvc.asJSON()

		extServiceItemService.create(json, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)
		then:
		null != gotExtSvc
		gotExtSvc.isAuthor(user)

		and:
		svcProps.keySet().each{
			expect:
			extSvc."${it}" == svcProps."${it}"
		}
	}

	void XtestCreateGenerateUuid(){
		when:
		def user = Profile.build(username:"junitAuth")

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		svcProps.uuid = "1234"

		def extSvc = new ExtServiceItem(svcProps)

		extServiceItemService.create(extSvc, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)

		then:
		null != gotExtSvc

		// Make sure it generates a UUID
		null != gotExtSvc.uuid
		svcProps.uuid != gotExtSvc.uuid
	}

    // Anyone can set the approval status to pending
	void XtestCreateSetApprovalStatusToPending(){
		when:
		def user = Profile.build(username:"junitAuth")

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		svcProps.approvalStatus = Constants.APPROVAL_STATUSES["PENDING"]

		def extSvc = new ExtServiceItem(svcProps)

		extServiceItemService.create(extSvc, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)

		then:
		null != gotExtSvc

		Constants.APPROVAL_STATUSES["PENDING"] == gotExtSvc.approvalStatus
	}

    // TODO: fix this
    // Anyone can set the approval status to pending
	void XtestCreateWithAutoApprove(){
		when:
		def user = Profile.build(username:"junitAuth")
        switchBothAdmin(user.username)

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		svcProps.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]

		def extSvc = new ExtServiceItem(svcProps)

		extServiceItemService.create(extSvc, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)

		then:
		null != gotExtSvc

		Constants.APPROVAL_STATUSES["APPROVED"] == gotExtSvc.approvalStatus
	}

     // Only external admin can set the approval status to
	void XtestCreateSetIgnoreApprovalStatus(){
		when:
		def user = Profile.build(username:"junitAuth")
        switchUser(user.username)


		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		svcProps.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]

		def extSvc = new ExtServiceItem(svcProps)

		extServiceItemService.create(extSvc, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)

		then:
		null != gotExtSvc

		Constants.APPROVAL_STATUSES["PENDING"] == gotExtSvc.approvalStatus
	}

	void XtestCreateIgnoreAuthor(){
		when:
		def user = Profile.build(username:"junitAuth1")
		def author = Profile.build(username:"junitAuth2")

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		svcProps.author = author

		def extSvc = new ExtServiceItem(svcProps)

		extServiceItemService.create(extSvc, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)

		then:
		null != gotExtSvc
		gotExtSvc.isAuthor(user)
	}

	public void XtestCreateWithValidationProblems(){
		when:
		def extSvc = new ExtServiceItem()
		extServiceItemService.create(extSvc, "JUnitAuth")
		then:
		thrown(ValidationException)
	}

	public void testUpdate(){
		when:
		def user = Profile.build(username:"junitAuth").save()
		switchUser(user.username)
        State state = State.build()
		def extSvc = newExtServiceItem('ext123', 'ext service Pho')
		extSvc.owners = [user]

		def newTitle = "JUnit new title"
		def newDescription = "JUnit new description"

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		def jsonStr = """
		{
            "serviceItem": {
				"state": ${state.id},
				"title": ${newTitle}
			}
		}
        """

		def json = (JSONObject) JSON.parse(jsonStr)
		JSONDecoratorService.postProcessJSON(json)

		extServiceItemService.update(extSvc.id, json, user.username)

		def gotExtSvc = ExtServiceItem.get(extSvc.id)
		then:
		null != gotExtSvc
		newTitle == gotExtSvc.title
	}

    void testUpdateNoSuchObject(){
		when:
		def user = Profile.build(username:"junitAuth")
		def extServiceItem = newExtServiceItem('ext pho', 'prueba XYZ')
		extServiceItemService.enable(extServiceItem.id + 5, user.username)
		then:
		thrown(ObjectNotFoundException)
    }

	void testBindFromJSONForState(){
		when:
		State state = State.build()

		def jsonStr = """
		{
			"serviceItem": {
				"state": ${state.id}
			}
		}

		"""
		def extServiceItem = new ExtServiceItem(types: microformat)
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)

		then:
		state == extServiceItem.state
	}

	void testBindFromJSONForState2(){
		when:
		logIt('testBindFromJSONForState2:')
		State state = State.build()

		def jsonStr = """
		{
			"serviceItem": {
				"state": {
					"id": ${state.id},
					"title": "${state.title}"
				}
			}
		}
		"""
		def extServiceItem = new ExtServiceItem(types: microformat)
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		state == extServiceItem.state
	}


	void testBindFromJSONForStateDoNotUpdate(){
		when:
		logIt('testBindFromJSONForStateDoNotUpdate:')
		State state = State.build()

		def extServiceItem = new ExtServiceItem(state:state, types:microformat)
		def jsonStr = """
		{
			"serviceItem": {
				"title": "Some Service"
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		// Make sure we haven't overwritten existing types value
		state == extServiceItem.state
	}

	void testBindFromJSONForStateUpdate(){
		when:
		logIt('testBindFromJSONForStateUpdate:')
		State state = State.build()
		State state2 = State.build()

		def extServiceItem = new ExtServiceItem(state:state, types:microformat)
		def jsonStr = """
		{
			"serviceItem": {
				"state": ${state2.id}
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		state2 == extServiceItem.state
	}
	void testBindFromJSONForTypes(){
		when:
		Types types = Types.build()

		def jsonStr = """
		{
			"serviceItem": {
				"types": ${types.id}
			}
		}

		"""
		def extServiceItem = new ExtServiceItem()
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		types == extServiceItem.types
	}

	void testBindFromJSONForTypes2(){
		when:
		Types types = Types.build()
		def jsonStr = """
		{
			"serviceItem": {
				"types": {
					"id": ${types.id},
					"title": "${types.title}"
				}
			}
		}
		"""
		def extServiceItem = new ExtServiceItem()
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		types == extServiceItem.types
	}


	void testBindFromJSONForTypesDoNotUpdate(){
		when:
		Types types = Types.build()

		def extServiceItem = new ExtServiceItem(types:types)
		def jsonStr = """
		{
			"serviceItem": {
				"title": "Some Service"
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		// Make sure we haven't overwritten existing types value
		types == extServiceItem.types
	}

	void testBindFromJSONForTypesUpdate(){
		when:
		Types types = Types.build()
		Types types2 = Types.build()

		def extServiceItem = new ExtServiceItem(types:types)
		def jsonStr = """
		{
			"serviceItem": {
				"types": ${types2.id}
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		types2 == extServiceItem.types
	}

	void testBindFromJSONForCategories(){
		when:
		Category category = Category.build(title:"JUnit Category")

		def jsonStr = """
		{
			"serviceItem": {
				"categories": [ { "title": "${category.title}" } ]
			}
		}
		"""
		def extServiceItem = new ExtServiceItem(types:microformat)
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		1 == extServiceItem.categories?.size()
		category == extServiceItem.categories[0]
	}

    void testEnableListing(){
        when:
		logIt('testEnableListing:')
        def user = Profile.build(username:"junitAuth")
        def extServiceItem = newExtServiceItem('ext pho', 'prueba XYZ')
        // set this by hand so we won't get an exception
        extServiceItem.createdBy = user.id
        extServiceItemService.enable(extServiceItem.id, user.username)

        def gotExtSvc = ExtServiceItem.get(extServiceItem.id)

		then:
		null != gotExtSvc
		gotExtSvc.isHidden == 0
    }

    void testEnableListingNotOwner(){
		when:
		def user = Profile.build(username:"junitAuth")
		def extServiceItem = newExtServiceItem('ext pho', 'prueba XYZ')
		extServiceItemService.enable(extServiceItem.id, user.username)
        then:
		def ex = thrown(Exception)
		"User ${user.username} does not have permission to update serviceItem id ${extServiceItem.id}" == expected.message
    }

    void testEnableListingNoSuchObject(){
        when:
		def user = Profile.build(username:"junitAuth")
        def extServiceItem = newExtServiceItem('ext pho', 'prueba XYZ')
        extServiceItemService.enable(extServiceItem.id + 5, user.username)
        then:
		thrown(ObjectNotFoundException)
    }

     void testDisableListing(){
        when:
		logIt('testDisableListing:')
        def user = Profile.build(username:"junitAuth")
        user.save(failOnError: true)
        def extServiceItem = newExtServiceItem('ext pho', 'prueba XYZ')
        // set this by hand so we won't get an exception
        extServiceItem.createdBy = user.id
        extServiceItemService.disable(extServiceItem.id, user.username)

        def gotExtSvc = ExtServiceItem.get(extServiceItem.id)

		then:
		null != gotExtSvc
		gotExtSvc.isHidden == 1
    }

	void testBindFromJSONForCategoriesDoNotUpdate(){
		when:
		Category category = Category.build(title:"JUnit Category")

		def extServiceItem =
				new ExtServiceItem(categories:[category], types:microformat)
		def jsonStr = """
		{
			"serviceItem": {
				"title": "Some Service"
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		def svc = extServiceItem

		then:
		// Make sure we haven't overwritten existing types value
		1 == svc?.categories?.size()
		category == svc?.categories[0]
	}

	void testBindFromJSONForCategoriesUpdate(){
		when:
		Category category = Category.build(title:"JUnit Category 1")
		Category category2 = Category.build(title:"JUnit Category 2")

		def extServiceItem = new ExtServiceItem(categories:[category], types:microformat)
		def jsonStr = """
		{
			"serviceItem": {
				"categories": [ { "id": "${category2.id}" } ]
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		def svc = extServiceItem

		then:
		// Make sure we haven't overwritten existing types value
		1 == svc?.categories?.size()
		category2 == svc?.categories[0]
	}

	void testBindFromJSONForRecommendedLayouts(){
		when:
		def desc1 = RecommendedLayout.ACCORDION.description
		def desc2 = RecommendedLayout.DESKTOP.description
		def jsonStr = """
		{
			"serviceItem": {
				"recommendedLayouts": [
				   "${desc1}",
				   "${desc2}"
				 ]
			}
		}
		"""
		def extServiceItem = new ExtServiceItem(types:microformat)
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		def expected = new HashSet([desc1, desc2])

		then:
		null != extServiceItem?.recommendedLayouts
		2 == extServiceItem.recommendedLayouts?.size()
		expected == new HashSet(extServiceItem.recommendedLayouts*.description)
	}

	void testBindFromJSONForRecommendedLayoutsDoNotUpdate(){
		when:
		def r1 = RecommendedLayout.ACCORDION.description
		def r2 = RecommendedLayout.DESKTOP.description

		def extServiceItem = new ExtServiceItem(recommendedLayouts: [r1, r2], types:microformat)
		def jsonStr = """
		{
			"serviceItem": {
				"title": "Some Service"
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		def expected = new HashSet([r1, r2])

		then:
		// Make sure we haven't overwritten existing value
		null != extServiceItem?.recommendedLayouts
		2 == extServiceItem?.recommendedLayouts?.size()
		expected == new HashSet(extServiceItem?.recommendedLayouts)
	}

	void testBindFromJSONForRecommendedLayoutsUpdate(){
		when:
		def r1 = RecommendedLayout.ACCORDION
		def r2 = RecommendedLayout.DESKTOP
		def r3 = RecommendedLayout.PORTAL
		def r4 = RecommendedLayout.TABBED

		def extServiceItem =
				new ExtServiceItem(recommendedLayouts: [r1, r2], types:microformat)
		def jsonStr = """
		{
			"serviceItem": {
				"recommendedLayouts": [
				   "${r3.description}",
				   "${r4.description}"
				 ]
			}
		}
		"""
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		def svc = extServiceItem

		def expected = new HashSet([r3, r4])

		then:
		// Make sure we have overwritten existing value
		null != svc?.recommendedLayouts
		2 == svc?.recommendedLayouts?.size()
		expected == new HashSet(svc?.recommendedLayouts)
	}

	void testBindFromJSONForCustomFields(){
        when:
		logIt('testBindFromJSONForCustomFields:')
		// Make sure we have a couple of CustomFieldDefinition's in the db
        def typ=Types.build(title:"My Type")
        typ.save(flush:true)
		def cdf1=TextCustomFieldDefinition.build(name:"Lunch", label:"Lunch", types:[typ], isRequired:false)
        cdf1.save(flush:true)
		def cdf2=TextCustomFieldDefinition.build(name:"Dinner", label:"Dinner", types:[typ], isRequired:true)
        cdf2.save(flush:true)
		def cdf3=new DropDownCustomFieldDefinition(name:"Morning Snack", label:"Morning Snack", types:[typ], isRequired:false)
        cdf3.save(flush:true)
		def cdf4=new DropDownCustomFieldDefinition(name:"Evening Snack", label:"Evening Snack", types:[typ], isRequired:true)
		cdf2.save(flush:true)
		def cdf5=new CheckBoxCustomFieldDefinition(name:"Good to go", label:"Listo", types:[typ], isRequired:false)
		cdf5.save(flush:true)
		def cdf6=new ImageURLCustomFieldDefinition(name:"Screenshot3", label:"tres", types:[typ], isRequired:false)
		cdf6.save(flush:true)
 		def cdf7=new TextAreaCustomFieldDefinition(name:"Descripcion", label:"mas info", types:[typ], isRequired:false)
		cdf7.save(flush:true)

		FieldValue fieldVal1 = new FieldValue()
		fieldVal1.displayText = "Apples"
		fieldVal1.setCustomFieldDefinition(cdf3);
		cdf3.addToFieldValues(fieldVal1)
        cdf3.save(flush:true)

		FieldValue fieldVal2 = new FieldValue()
		fieldVal2.displayText = "Oranges"
		fieldVal2.setCustomFieldDefinition(cdf4);
		cdf4.addToFieldValues(fieldVal2)
        cdf4.save(flush:true)

        def imageURL = "http://www.freeiconsweb.com/Icons-show/customicondesign-socialmedia2/Delicious128.png"
        def textField = "They were discussing their piratical plans, in which all we could hear distinctly was, that they would unite with the crew of a schooner Hornet"
		def jsonStr = """
		{
			"serviceItem": {
				"state": "JUnit",
                                "types":"My Type",
				"customFields": [
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.TEXT.name()}",
						"name": "${cdf1.name}",
						"customFieldDefinitionId": ${cdf1.id},
						"value": "val1"
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.TEXT.name()}",
						"name": "${cdf2.name}",
						"customFieldDefinitionId": ${cdf2.id},
						"value": "val2"
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.DROP_DOWN.name()}",
						"name": "${cdf3.name}",
						"label": "${cdf3.name}",
						"customFieldDefinitionId": ${cdf3.id},
						"value": "${fieldVal1.displayText}"
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.DROP_DOWN.name()}",
						"name": "${cdf4.name}",
						"label": "${cdf4.name}",
						"customFieldDefinitionId": ${cdf4.id},
						"fieldValueId": ${fieldVal2.id}
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.CHECK_BOX.name()}",
						"name": "${cdf5.name}",
						"customFieldDefinitionId": ${cdf5.id},
						"value": "true"
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.IMAGE_URL.name()}",
						"name": "${cdf6.name}",
						"customFieldDefinitionId": ${cdf6.id},
						"value": "${imageURL}"
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.TEXT_AREA.name()}",
						"name": "${cdf7.name}",
						"customFieldDefinitionId": ${cdf7.id},
						"value": "${textField}"
					}
				 ]
			}
		}
		"""
        // aqui
        def extServiceItem = newExtServiceItem('externXYZ', 'Prueba 11')
        extServiceItem.types = typ
        extServiceItem.customFields = []

		then:
		null != extServiceItem.id
        null != extServiceItem.version

		when:
		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)
		def svc = extServiceItem
        svc.customFields.each {logIt "    ${it}"}

		then:
		null != svc.customFields
		7 == svc.customFields.size()

		when:
		/**
		 * TEXT CUSTOM FIELD
		 */
		def cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf1.id
		}
		then:
		null != cf
		cf instanceof TextCustomField
		cf.value == "val1"

		when:
		/**
		* TEXT CUSTOM FIELD
		*/
		cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf2.id
		}
		then:
		null != cf
		cf instanceof TextCustomField
		cf.value == "val2"

		when:
		/**
		* DROP DOWN CUSTOM FIELD
		*/
		cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf3.id
		}
		then:
		null != cf
		cf instanceof DropDownCustomField
		cf.value instanceof FieldValue
		cf.value == fieldVal1
		cf.value.displayText == fieldVal1.displayText

		when:
		/**
		* DROP DOWN CUSTOM FIELD
		*/
		cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf4.id
		}
		then:
		null != cf
		cf instanceof DropDownCustomField
		cf.value instanceof FieldValue
		cf.value == fieldVal2
		cf.value.displayText == fieldVal2.displayText

		when:
		/**
		* CHECK BOX CUSTOM FIELD
		*/
		cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf5.id
		}
		then:
		null != cf
		cf instanceof CheckBoxCustomField
		cf.value == true

		when:
		/**
		* IMAGE URL CUSTOM FIELD
		*/
		cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf6.id
		}
		then:
		null != cf
		cf instanceof ImageURLCustomField
		cf.value == imageURL

		when:
		/**
		* TEXT AREA CUSTOM FIELD
		*/
		cf = svc.customFields.find{
			it.customFieldDefinition.id == cdf7.id
		}
		then:
		null != cf
		cf instanceof TextAreaCustomField
		cf.value == textField
	}

	void testBindFromJSONForCustomFieldsUpdate(){
        when:
		logIt('testBindFromJSONForCustomFieldsUpdate:')
		// Make sure we have a couple of CustomFieldDefinition's in the db
        def typ=Types.build(title:"My Type")
        typ.save(flush:true)
		def cdf1=TextCustomFieldDefinition.build(name:"Lunch", label:"Lunch", types:[typ])
        cdf1.save(flush:true)
		def cdf2=TextCustomFieldDefinition.build(name:"Dinner", label:"Dinner", types:[typ])
        cdf2.save(flush:true)

		def cf1 = new TextCustomField(value:"salad", customFieldDefinition: cdf1)
		 	cf1.save(flush:true)
		def cf2 = new TextCustomField(value:"steak", customFieldDefinition: cdf2)
			cf2.save(flush:true)

        def extServiceItem = newExtServiceItem('externXYZ', 'Prueba 11')
        extServiceItem.types = typ
        extServiceItem.customFields = [cf1, cf2]

		then:
		null != extServiceItem.id
        null != extServiceItem.version

		when:
		def jsonStr = """
		{
			"serviceItem": {
				"state": "JUnit",
				"customFields": [
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.TEXT.name()}",
						"name": "${cdf1.name}",
						"customFieldDefinitionId": ${cdf1.id},
						"value": "tuna"
					},
					{
						"${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}": "${Constants.CustomFieldDefinitionStyleType.TEXT.name()}",
						"name": "${cdf2.name}",
						"customFieldDefinitionId": ${cdf2.id},
						"value": "chicken"
					}
				 ]
			}
		}
		"""

		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)
		def svc = extServiceItem

		then:
		null != svc.customFields
		2 == svc.customFields.size()

		when:
		// Verify new CustomFields are there, tuna for lunch, chicken for dinner
		def cf = svc.customFields.find{
			(it.value && (it.customFieldDefinition.id == cdf1.id))
		}
		then:
		null != cf
		cf.value == "tuna"

		when:
		cf = svc.customFields.find{
			(it.value && (it.customFieldDefinition.id == cdf2.id))
		}
		then:
		null != cf
		cf.value == "chicken"
	}

	void testBindFromJSONForCustomFieldsDoNotUpdate(){
		when:
		// Make sure we have a couple of CustomFieldDefinition's in the db
		def cdf1=TextCustomFieldDefinition.build(name:"Lunch", label:"Lunch")
		def cdf2=TextCustomFieldDefinition.build(name:"Dinner", label:"Dinner")

		def cf1 = new TextCustomField(value:"salad", customFieldDefinition: cdf1)
			cf1.save(flush:true)
		def cf2 = new TextCustomField(value:"steak", customFieldDefinition: cdf2)
			cf2.save(flush:true)

		def extServiceItem = new ExtServiceItem(customFields:[cf1, cf2]);

		def jsonStr = """
		{
			"serviceItem": {
				"title": "Some Service"
			}
		}
		"""

		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)
		def svc = extServiceItem

		then:
		null != svc.customFields
		2 == svc.customFields.size()

		// Verify existing CustomFields are there, salad for lunch,
		// steak for dinner
		cf1 == svc.customFields.get(0)
		cf2 == svc.customFields.get(1)
	}

	void testBindFromJSONForCustomFieldsAbsentNoEffect(){
		when:
		// Make sure we have a couple of CustomFieldDefinition's in the db
		def cdf1=TextCustomFieldDefinition.build(name:"Lunch", label:"Lunch")
		def cdf2=TextCustomFieldDefinition.build(name:"Dinner", label:"Dinner")

		def cf1 = new TextCustomField(value:"sandwich", customFieldDefinition: cdf1)
		def cf2 = new TextCustomField(value:"pasta", customFieldDefinition: cdf2)

		def extServiceItem = new ExtServiceItem(customFields:[cf1, cf2]);

		def jsonStr = """
		{
			"serviceItem": {
				"customFields": []
			}
		}
		"""

		def json = JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
		null != extServiceItem.customFields
		2 == extServiceItem.customFields.size()

		// Verify previous custom field have value set to null
		cf1 == extServiceItem.customFields.get(0)
		cf2 == extServiceItem.customFields.get(1)
	}

    @Ignore
	void testBindFromJSONSimpleProperties(){
		setup:
		def jsonStr = """
		{
			"serviceItem": {
				"avgRate": 1,
				"categories": [
					{ "title": "Category A" },
					{ "title": "Category B" }
				],
				"customFields": [ ],
				"dependencies": "dependencies",
				"description": "description",
				"docUrls": [{"name": "Doc URL", "url": "docUrl"}],
				"imageSmallUrl": "imageSmallUrl",
				"imageMediumUrl": "imageMediumUrl",
				"imageLargeUrl": "imageLargeUrl",
				"installUrl": "installUrl",
				"isHidden": 1,
				"isPublished": true,
				"launchUrl": "launchUrl",
				"organization": "organization",
				"recommendedLayouts": [],
				"releaseDate": "2010-01-01T01:01:01Z",
				"requirements": "requirements",
				"screenshot1Url": "screenshot1Url",
				"screenshot2Url": "screenshot2Url",
				"techPocs": ["techPoc"],
				"title": "title",
				"totalVotes": 1,
				"types": "Service:REST",
				"versionName": "versionName",
   				"systemUri": "systemUri",
   				"externalViewUrl": "externalViewUrl",
   				"externalEditUrl": "externalEditUrl",
   				"externalId": "externalId"
   			}
   		}
		"""

		def json=JSON.parse(jsonStr)
		def extSvc = new ExtServiceItem()
		extServiceItemService.bindFromJSON(json, extSvc, 'userX', true)

		// String properties first
		[
			"dependencies",
			"description",
			"imageSmallUrl",
			"imageLargeUrl",
			"installUrl",
			"launchUrl",
			"organization",
			"requirements",
			"screenshot1Url",
			"screenshot2Url",
			"title",
			"versionName"
		].each {
			expect:
			it == extSvc."${it}"
		}

        'Doc URL' == (extSvc.docUrls as List)[0].name
		'docUrl' == (extSvc.docUrls as List)[0].url
        'techPoc' == (extSvc.techPocs as List)[0]

		[
			"systemUri",
			"externalViewUrl",
			"externalEditUrl",
			"externalId"
		].each {
			it == extSvc."${it}"
		}

		// Integer properties
        // These properties are ignored by the bind so they should be 0 in extSvc
		[
			"totalVotes",
			"isHidden",
			"avgRate"
		].each {
			0 == extSvc."${it}"
		}

		// Date properties
		["releaseDate"].each {
			when:
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.setTimeZone new SimpleTimeZone(0, "GMT")
			cal.set(2010,0,1,1,1,1)
			then:
			cal.getTime() == extSvc.releaseDate
		}
	}

	public testBindFromJSONDoNotUpdate(){
		when:
		ExtServiceItem extSvc = new ExtServiceItem(title: 'sir walter', avgRate:7)

		def title = extSvc.title

		def jsonStr = """
	{
		"serviceItem": {
			"avgRate": 1,
			"description": "description"
		 }
	   }
	"""
		def json=JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json, extSvc, 'userX', true)

		then:
		title == extSvc.title
        // avgRate will still be 7 since the bind ignores avgRate
		7 == extSvc.avgRate
		"description" == extSvc.description
	}

	void testBindFromJSONCheckBoxInvalidValue(){
		when:
		logIt('testBindFromJSONForCheckBox:')
		// Make sure we have a couple of CustomFieldDefinition's in the db
		def typ=Types.build(title:"My Type")
		typ.save(flush:true)
		def cdf1=CheckBoxCustomFieldDefinition.build(name:"Ready to go", label:"Lunch", types:[typ])
		cdf1.save(flush:true)
		def cdf2=CheckBoxCustomFieldDefinition.build(name:"Certified Organic", label:"Dinner", types:[typ])
		cdf2.save(flush:true)

		def cf1 = new CheckBoxCustomField(value:"true", customFieldDefinition: cdf1)
			 cf1.save(flush:true)
		def cf2 = new CheckBoxCustomField(value:"false", customFieldDefinition: cdf2)
			cf2.save(flush:true)

		def extServiceItem = newExtServiceItem('externXYZ', 'Prueba 11')
		extServiceItem.types = typ
		extServiceItem.customFields = [cf1, cf2]

		then:
		null != extServiceItem.id
		null != extServiceItem.version

		when:
		def jsonStr = """
		{
			"serviceItem": {
				"state": "JUnit",
				"customFields": [
					{
						"name": "${cdf1.name}",
						"value": "false"
					},
					{
						"name": "${cdf2.name}",
						"value": "blah"
					}
				 ]
			}
		}
		"""

		def json = JSON.parse(jsonStr)
		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)
		then:
		thrown(ozone.marketplace.domain.ValidationException)
	}

	public void testBindFromJSONImageURLInvalidValue(){
		when:
		logIt('testBindFromJSONForCheckBox:')
		// Make sure we have a couple of CustomFieldDefinition's in the db
		def typ=Types.build(title:"My Type")
		typ.save(flush:true)
		def cdf1=ImageURLCustomFieldDefinition.build(name:"LeftSideView", label:"Lunch", types:[typ])
		cdf1.save(flush:true)
		def cdf2=ImageURLCustomFieldDefinition.build(name:"RightSideView", label:"Dinner", types:[typ])
		cdf2.save(flush:true)

		def cf1 = new ImageURLCustomField(value:"http://www.freeiconsweb.com/Icons-show/mega_mod_pack/Cherry%20Coke.png", customFieldDefinition: cdf1)
			 cf1.save(flush:true)
		def cf2 = new ImageURLCustomField(value:"http://www.freeiconsweb.com/Icons-show/mega_mod_pack/Vanilla%20CPepsi.png", customFieldDefinition: cdf2)
			cf2.save(flush:true)

		def extServiceItem = newExtServiceItem('externXYZ', 'Prueba 11')
		extServiceItem.types = typ
		extServiceItem.customFields = [cf1, cf2]
		then:
		null != extServiceItem.id
		null != extServiceItem.version

		when:
		def jsonStr = """
		{
			"serviceItem": {
				"state": "JUnit",
				"customFields": [
					{
						"name": "${cdf1.name}",
						"value": "http://www.freeiconsweb.com/Icons-show"
					},
					{
						"name": "${cdf2.name}",
						"value": "blah"
					}
				 ]
			}
		}
		"""

		def json = JSON.parse(jsonStr)
		extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)
		then:
		thrown(ozone.marketplace.domain.ValidationException)
	}

	public testBindFromJSONWithNulls(){
		when:
        def user = Profile.build(username:"junitAuth")
		def types = Types.build(title:"Service:JUnit", hasIcons:false)
		def state = State.build(title:"JUnit")
		def cat1 = Category.build(title:"JUnit Cat 1")
		def cat2 = Category.build(title:"JUnit Cat 2")

		def organization = "JUnit Org"
		def launchUrl="http://example.org/launch"
		def releasedDate = new Date()
		def isHidden

		// ServiceItemService retrieves username from session
		serviceItemService.getSession().username = user.username

		ExtServiceItem extServiceItem = new ExtServiceItem(title:"JUnit Test Service",
                                                           description:"Nothing to see here",
                                                           //types:types,
                                                           techPocs: [user.username],
                                                           versionName:"3.0",
                                                           //state:state,
                                                           //categories:[cat1, cat2],
                                                           systemUri:"external:system",
                                                           organization:organization,
                                                           launchUrl: launchUrl,
                                                           releaseDate: releasedDate)

		// Test overwriting existing property values with nulls, we'll need
		// to support this capability to handle updates

		then:
		organization == extServiceItem.organization
		launchUrl == extServiceItem.launchUrl

		when:
		def title = extServiceItem.title

		def jsonStr = """
		{
			"serviceItem": {
				"organization": null,
				"launchUrl": null,
				"releaseDate": null
		 }
	   }
		"""

		def json=JSON.parse(jsonStr)

		extServiceItemService.bindFromJSON(json, extServiceItem, 'userX', true)

		then:
		null == extServiceItem.organization
		null == extServiceItem.launchUrl
		null == extServiceItem.releaseDate
	}

    void testBindFromJSONForAgency() {
        when:
		Agency agency = Agency.build()

        def jsonStr = """
            {
                "serviceItem": {
                    "agency": ${agency.id}
                }
            }

        """
        def extServiceItem = new ExtServiceItem(types: microformat)
        def json = JSON.parse(jsonStr)

        extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)

        then:
		agency.equals(extServiceItem.agency)
    }

    void testBindFromJSONForAgencyDoNotUpdate(){
        when:
		logIt('testBindFromJSONForAgencyDoNotUpdate:')
        Agency agency = Agency.build()

        def extServiceItem = new ExtServiceItem(agency:agency, types:microformat)
        def jsonStr = """
        {
            "serviceItem": {
                "title": "Some Service"
            }
        }
        """
        def json = JSON.parse(jsonStr)

        extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

		then:
        // Make sure we haven't overwritten existing types value
        agency.equals(extServiceItem.agency)
    }

    public void testBindFromJSONForAgencyUpdate(){
        when:
		logIt('testBindFromJSONForAgencyUpdate:')
        Agency agency = Agency.build(title: 'agency 1')
        Agency agency2 = Agency.build(title: 'agency 2')

        def extServiceItem = new ExtServiceItem(agency:agency, types:microformat)
        def jsonStr = """
        {
            "serviceItem": {
                "agency": ${agency2.id}
            }
        }
        """
        def json = JSON.parse(jsonStr)

        extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

        then:
		agency2.equals(extServiceItem.agency)
    }

    void testBindFromJSONForContact() {
        when:
		ContactType contactType = ContactType.build()
        Contact contact = new Contact(type: contactType, email: 'test@test.test',
            securePhone: '111-1111', unsecurePhone: '111-1111')

        def jsonStr = """
            {
                "serviceItem": {
                    "contacts": [${contact as JSON}]
                }
            }

        """
        def extServiceItem = new ExtServiceItem(types: microformat)
        def json = JSON.parse(jsonStr)

        extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', false)

		then:
        extServiceItem.contacts.each {
            contact.email == it.email
            contact.securePhone == it.securePhone
            contact.unsecurePhone == it.unsecurePhone
            contact.type == it.type
        }
    }

    void testBindFromJSONForContactDoNotUpdate(){
        setup:
		logIt('testBindFromJSONForContactDoNotUpdate:')
        ContactType contactType = ContactType.build()
        Contact contact = new Contact(type: contactType, email: 'test@test.test',
            securePhone: '111-1111', unsecurePhone: '111-1111')

        def extServiceItem = new ExtServiceItem(contact:contact, types:microformat)
        def jsonStr = """
        {
            "serviceItem": {
                "title": "Some Service"
            }
        }
        """
        def json = JSON.parse(jsonStr)

        extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

        // Make sure we haven't overwritten existing types value
        extServiceItem.contacts.each {
            expect:
			contact.email == it.email
            contact.securePhone == it.securePhone
            contact.unsecurePhone == it.unsecurePhone
            contact.type == it.type
        }
    }

    void testBindFromJSONForContactUpdate(){
        setup:
		logIt('testBindFromJSONForContactUpdate:')
        ContactType contactType1 = ContactType.build(title: 'contact')
        ContactType contactType2 = ContactType.build(title: 'contact 2')
        Contact contact = new Contact(type: contactType1, email: 'test1@test.test',
            securePhone: '111-1111', unsecurePhone: '111-1111')
        Contact contact2 = new Contact(type: contactType2, email: 'test2@test.test',
            securePhone: '222-2222', unsecurePhone: '222-2222')

        def extServiceItem = new ExtServiceItem(contact:contact, types:microformat)
        def jsonStr = """
        {
            "serviceItem": {
                "contacts": [${contact2 as JSON}]
            }
        }
        """
        def json = JSON.parse(jsonStr)

        extServiceItemService.bindFromJSON(json,extServiceItem, 'userX', true)

        extServiceItem.contacts.each {
            expect:
			contact2.email == it.email
            contact2.securePhone == it.securePhone
            contact2.unsecurePhone == it.unsecurePhone
            contact2.type == it.type
        }
    }

	private newServiceItem(def serviceName){
		def author = Profile.build()
		def ret=ServiceItem.build(title: serviceName, owners:[author])
        ret.techPocs = [author.username]
		ret.save(flush:true)
	}

	private newExtServiceItem(def systemUri, def serviceName){
        def author = Profile.build()

        def organization = "JUnit Org"
		def launchUrl="http://example.org/launch"
		def releasedDate = new Date()

		def extSvc = new ExtServiceItem(svcProps)
        extSvc.title = serviceName
        extSvc.uuid = Utils.generateUUID()
        extSvc.owners = [author]
		extSvc.systemUri = systemUri
        extSvc.externalId = Utils.generateUUID()
        extSvc.techPocs = [author.username]

		if (!extSvc.save(flush: true))
        {
            extSvc.errors.each{
                println "Error on save: ${it}"
            }
        }

		return extSvc
	}
}
