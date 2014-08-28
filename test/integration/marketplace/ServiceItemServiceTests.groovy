
package marketplace

import org.junit.Ignore
import org.apache.commons.lang.time.FastDateFormat
import org.springframework.web.context.request.RequestContextHolder as RCH

import ozone.marketplace.enums.DefinedDefaultTypes;
import ozone.utils.Utils
import ozone.marketplace.domain.ValidationException

@Ignore
class ServiceItemServiceTests extends MarketplaceIntegrationTestCase {

	def serviceItemService
    ServiceItem serviceItem
    def changeLogService
    def profileService
	def itemCommentService
	def relationshipService
	static FastDateFormat dateFormatter = FastDateFormat.getInstance("MMM dd, yyyy hh:mm:ss aa zzz")
    def svcProps
	def mpTest1
    def widgetType

    void setUp() {
        super.setUp()

        serviceItemService.profileService = profileService
	    mpTest1 = new Profile(username: 'mpTest1', displayName: 'Marketplace Tester', createdDate: new Date())
        mpTest1.save()

		def genericType = new Types(title: "title", description : "description", ozoneAware: true)
		genericType.save(flush:true)

        def type = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)
        widgetType = genericType
        serviceItem = ServiceItem.build(title: "Test000", owners: [mpTest1], types: type)
    	serviceItemService.getSession().username = 'mpTest1'

		def types = Types.build(title:"Service:JUnit")
        types.save()
        def typesList = new ArrayList<Types>()
		typesList.add(types)
		def state = State.build(title:"JUnit")
        state.save(failOnError: true)
        12.times {
           Category.build(title:"Test Category ${it}")
        }
		def cat1 = Category.build(title:"JUnit Cat 1")
        cat1.save()
		def cat2 = Category.build(title:"JUnit Cat 2")
        cat2.save()
        def cat3 = Category.build(title:"JUnit Cat 3")
        cat3.save()
        def cat4 = Category.build(title:"DoubleDigit")
        cat4.save()
		def cdf1 = TextCustomFieldDefinition.build(name:"JUnit Custom 1", types: typesList, isRequired: true)
        cdf1.save()
		def cdf2 = TextCustomFieldDefinition.build(name:"JUnit Custom 2", types: typesList, isRequired: false)
		cdf2.save()
        svcProps = [
            title: 'JUnit Test Service',
            description:"Just testing, nothing to see here",
            launchUrl:"http://example.org/launch",
            releaseDate: new Date(),
            avgRate:0,
            totalVotes:0,
            isHidden:0,
            imageLargeUrl:"http://launch.url.gov/image.gif",
            imageMediumUrl:"http://launch.url.gov/image.gif",
            imageSmallUrl:"http://launch.url.gov/image.gif",
            versionName:"2.1",
            organization:"External Service Provider Inc",
            techPocs: ["junitTechPoc"],
            types: types,
            state: state,
            customFields: [
                new TextCustomField(value: "cust 1",
                    customFieldDefinition: cdf1)
            ],
            categories: [ cat1, cat2 ],

            systemUri:"external:system",
            externalId: Utils.generateUUID()
        ]
    }

    private def logIt(def strIn)
    {
        profileService.logIt(strIn)
    }

    @Ignore
    void testGetSession() {
    	def retVal = serviceItemService.getSession()
    	assertEquals RCH.currentRequestAttributes().getSession(), retVal
    }

    @Ignore
    void testSave(){
		//Test the save method
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		//Test to make sure save was successful
        assertEquals serviceItem.title, ServiceItem.findByTitle("Test000").title
		//Test the audit trail
		assertEquals 1,serviceItem.serviceItemActivities.size()
		assertEquals serviceItem.lastActivity.action, Constants.Action.MODIFIED
    }

    @Ignore
    void testSaveReturnsIdentity(){
        //Test the save method
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
        //Test to make sure save was successful
        assertEquals serviceItem.title, ServiceItem.findByTitle("Test000").title
        //Test the audit trail
        assertEquals 1,serviceItem.serviceItemActivities.size()
        assertEquals serviceItem.lastActivity.action, Constants.Action.MODIFIED
        assertNotNull serviceItem.id
    }

    @Ignore
	void testSavingAListingThatIDoNotOwn() {
		def userLewis = Profile.build(username: 'rLewis', displayName: 'Ray Lewis').save()
		switchUser(userLewis.username)
		ServiceItem si = ServiceItem.build(title:"Ray's Listing",owners: [userLewis]).save()

		def userBigBen = Profile.build(username: 'bigben', displayName: 'Big Ben').save()
		si.title = "Steel me a Listing"
		switchUser(userBigBen.username)

		shouldFail(PermissionException){
			serviceItemService.save(si)
		}
	}

    @Ignore
	void testSaveWithInvalidCustomFields(){
        println Types.build()
		CustomFieldDefinition cfd = CustomFieldDefinition.build(allTypes: false, types: [Types.build()])
		CustomField cf = TextCustomField.build(customFieldDefinition: cfd)
        // cf has value of null and cfd has a bunch of null fields...
		serviceItem.customFields = [cf]
		serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		ServiceItem si = ServiceItem.get(serviceItem.id)
		assertEquals 0, si.customFields.size()
	}

    // tests the logic that makes sure only custom fields with a service type that matches
    // the service item's service type are saved with the service item.
    @Ignore
    void testCustomFields() {
        def desktopAppType = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)
        assertNotNull desktopAppType
        def webAppType = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title)
        assertNotNull webAppType

        def widgetType = new Types(title: "Widget")
        assertNotNull widgetType

        def plugInType = new Types(title: "plugin")
        assertNotNull plugInType

        CustomFieldDefinition cfd1 = CustomFieldDefinition.build(
            name: 'height', label: 'The Height', description: 'How tall this is',
            allTypes: false, types: [desktopAppType, webAppType, widgetType]);
        println cfd1
        CustomField customField1 = TextCustomField.build(value: '5 8', customFieldDefinition: cfd1)
        CustomFieldDefinition cfd2 = CustomFieldDefinition.build(
            name: 'height2', label: 'The Height2', description: 'How tall this is2',
            allTypes: false, types: [desktopAppType, plugInType]);
        println cfd2
        CustomField customField2 = TextCustomField.build(value: '6 4', customFieldDefinition: cfd2)

        serviceItem.customFields = [customField1, customField2]

        // serviceItem has type rest so all custom fields should be saved
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		ServiceItem si = ServiceItem.get(serviceItem.id)
		assertEquals 2, si.customFields.size()

        serviceItem.types = plugInType
        // customField2 should not be saved since it does not have type plugIn
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		si = ServiceItem.get(serviceItem.id)
		assertEquals 1, si.customFields.size()

        // change types on the customFieldDefinition for customField2
        // so customField2 will not get saved with the serviceItem
        cfd2.types = [desktopAppType]
        cfd2.save()
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
 		si = ServiceItem.get(serviceItem.id)
		assertEquals 0, si.customFields.size()
    }

    @Ignore
    void testDelete(){
        assertTrue serviceItem.validate() //Should be true...
        //Save should pass...
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Now should have a valid id
        assertTrue serviceItem.id != null

        Long currentServiceItemId = new Long(serviceItem.id)

				//Make sure the delete worked
				def checkItem = ServiceItem.get(currentServiceItemId)

        //Test delete method
        serviceItemService.delete(currentServiceItemId)

        //Make sure the delete worked
				assertNull  ServiceItem.get(currentServiceItemId)

    }

    @Ignore
	void testDeleteWithComments(){
		switchUser("mpTest1")

		serviceItemService.save(serviceItem)
		itemCommentService.saveItemComment([username: mpTest1.username, commentTextInput: "Comment 1", serviceItemId: "${serviceItem.id}"])

		def params = [ id: "${serviceItem.id}" ]
		def comments = itemCommentService.getServiceItemComments(params)

		assertEquals 1, comments.size()
		serviceItemService.delete(serviceItem.id)
		assertNull ServiceItem.get(serviceItem.id)
	}

    @Ignore
	void testDeleteWithRelationship(){
		switchUser(testUser1.username)
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])

		def rel  = Relationship.findByOwningEntity(parent)
		assertEquals 3, rel.relatedItems.size()
		serviceItemService.delete(parent.id)
		assertNull ServiceItem.get(parent.id)
		assertNull Relationship.findByOwningEntity(parent)

		def r1RemoveActivity = getActivity(requires1,Constants.Action.REMOVERELATEDTOITEM)
		def r2RemoveActivity = getActivity(requires2,Constants.Action.REMOVERELATEDTOITEM)
		def r3RemoveActivity = getActivity(requires3,Constants.Action.REMOVERELATEDTOITEM)

		assertNotNull r1RemoveActivity
		assertNotNull r2RemoveActivity
		assertNotNull r3RemoveActivity
		assertEquals 1, r1RemoveActivity.items.size()
		assertEquals 1, r2RemoveActivity.items.size()
		assertEquals 1, r3RemoveActivity.items.size()
		assertEquals "Parent", r1RemoveActivity.items[0].title
		assertEquals "Parent", r2RemoveActivity.items[0].title
		assertEquals "Parent", r3RemoveActivity.items[0].title
	}


    /**
     * Some default service items are saved. This ensures they are all deleted in case a test
     * needs to start with an empty database
     */
    private void ensureNoServiceItems() {
        serviceItem.delete()
        assert ServiceItem.count() == 0
    }



	def getActivity = { si,action ->
		si.serviceItemActivities.findAll { it.action == action}.max { it.activityDate }
	}

    @Ignore
	void testDeleteInRelationships(){
		switchUser(testUser1.username)
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem parent2 = ServiceItem.build(title:"Parent 2",owners: [testUser1]).save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])
		relationshipService.addOrRemoveRequires(parent2.id,[requires1.id,requires2.id,requires3.id])

		def rel  = Relationship.findByOwningEntity(parent)
		assertEquals 3, rel.relatedItems.size()
		rel  = Relationship.findByOwningEntity(parent2)
		assertEquals 3, rel.relatedItems.size()

		serviceItemService.delete(requires2.id)
		assertNull ServiceItem.get(requires2.id)

		rel = Relationship.findByOwningEntity(parent)
		assertEquals 2 , rel.relatedItems.size()
		assertEquals "Requires 1", rel.relatedItems[0].title
		assertEquals "Requires 3", rel.relatedItems[1].title

		def parentRemoveActivity = getActivity(parent,Constants.Action.REMOVERELATEDITEMS)
		assertNotNull parentRemoveActivity
		assertEquals 1, parentRemoveActivity.items.size()
		assertEquals "Requires 2", parentRemoveActivity.items[0].title

		rel = Relationship.findByOwningEntity(parent2)
		assertEquals 2 , rel.relatedItems.size()
		assertEquals "Requires 1", rel.relatedItems[0].title
		assertEquals "Requires 3", rel.relatedItems[1].title

		def parent2RemoveActivity = getActivity(parent2,Constants.Action.REMOVERELATEDITEMS)
		assertNotNull parent2RemoveActivity
		assertEquals 1, parent2RemoveActivity.items.size()
		assertEquals "Requires 2", parent2RemoveActivity.items[0].title
	}

    @Ignore
    void testUpdate(){
        assertTrue serviceItem.validate() //Should be true...

		//Save should pass...
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Now should have a valid id
        assertTrue serviceItem.id != null

        //Test Update method...
        serviceItem.title = "NEW_TITLE_FOR_TEST"
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Make sure the update was a success..
        String currentServiceItemTitle = "NEW_TITLE_FOR_TEST"
        assertEquals serviceItem.title, ServiceItem.findByTitle(currentServiceItemTitle).title

        //Check to make sure approved is 0
        assertEquals 'serviceItem.approvalStatus is not Pending', serviceItem.approvalStatus, Constants.APPROVAL_STATUSES["IN_PROGRESS"]
		assertEquals serviceItem.lastActivity.action, Constants.Action.MODIFIED

		//Test Audit trail (double modified)
		assertEquals 2,serviceItem.serviceItemActivities.size()
		assertEquals serviceItem.lastActivity.action, Constants.Action.MODIFIED

	}

    @Ignore
    void testOwfWidgetTypeAddedWhenListingEditedToOwfCompatibleType(){
        assertTrue serviceItem.validate() //Should be true...

		//Save should pass...
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Now should have a valid id
        assertTrue serviceItem.id != null

        // update the type
        serviceItem.types = widgetType
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Make sure the update was a success..
        def updatedItem = ServiceItem.findByTitle('Test000')
        assert updatedItem.types.title == widgetType.title

        // Make sure the OWF widget type is standard, the default.
        assert updatedItem.owfProperties.owfWidgetType == 'standard'
	}

	private void doApprove(){
        def adminUser = Profile.build(username: 'admin', displayName: 'admin', createdDate: new Date())
        switchAdmin(adminUser.username)

        String sessionUsername = serviceItemService.getSession().username

		//Set approved equal to 0
		serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["IN_PROGRESS"]
		assertTrue serviceItem.validate() //Should be true...
		//Save should pass...
		serviceItemService.save(serviceItem, sessionUsername)

		//Now should have a valid id
		assertTrue serviceItem.id != null

        serviceItemService.setInsideOutside(serviceItem, sessionUsername, true)

		//Perform the approve method...
		serviceItemService.approve(serviceItem, sessionUsername)

		//Check to make sure approved is 1
		assertEquals 'serviceItem.approvalStatus is not Approved', serviceItem.approvalStatus, Constants.APPROVAL_STATUSES["APPROVED"]
	}

    @Ignore
    void testApprove(){
        //Approve Service Item
		doApprove()

        //Test Audit trail
		assertEquals serviceItem.lastActivity.action, Constants.Action.APPROVED
    }

    @Ignore
	void testApprovedDate(){
		//Approve Service Item
		doApprove()

		println "SI.ApprovedDate: ${serviceItem?.approvedDate}"
		println "SI.LastActivityDate: ${serviceItem?.lastActivityDate}"

		//Test Approved Date on Index Exists
		assertTrue "ServiceItem ${serviceItem}, approvedDate is NULL.", serviceItem?.approvedDate != null

		//Test Approved Date on Index is equal to Last Activity Date
		if((serviceItem?.approvedDate != null) && (serviceItem?.lastActivityDate != null)){
			println "FORMATTED<SI.ApprovedDate>: ${dateFormatter.format(serviceItem?.approvedDate)}"
			println "FORMATTED<SI.LastActivityDate>: ${dateFormatter.format(serviceItem?.lastActivityDate)}"

			assertEquals dateFormatter.format(serviceItem.approvedDate), dateFormatter.format(serviceItem.lastActivityDate)
		}

		//Test Approved Date query is valid
		ServiceItem.withNewSession { session ->
			def approvedServiceItemActivity = ServiceItemActivity.findByServiceItemAndAction(serviceItem, Constants.Action.APPROVED)
			for(asia in approvedServiceItemActivity){
				if((serviceItem?.approvedDate != null) && (asia?.activityDate != null)){
					assertEquals dateFormatter.format(serviceItem?.approvedDate), dateFormatter.format(asia?.activityDate)
					println "Approved Date: ${serviceItem?.approvedDate}"
				}
				break
			}
		}
	}

    @Ignore
    void testCreate(){
        // create a serviceItem that has not been persisted. Note the build method from the
        // build-test-data does persist the object.
        def newServiceItem = createServiceItem('bruja 99')
        assertNull newServiceItem.id

        serviceItemService.save(newServiceItem,serviceItemService.getSession().username)

        assertEquals 1,newServiceItem.serviceItemActivities.size()
		assertEquals newServiceItem.lastActivity.action, Constants.Action.CREATED
	}

    // Test changing the type with both text and dropdown custom fields
    @Ignore
    void testChangeType() {
        logIt "running testChangeType:"
        def desktopAppsType = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)
        assertNotNull desktopAppsType
        def webApps = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title)
        assertNotNull webApps

        CustomFieldDefinition cfd1 = TextCustomFieldDefinition.build(
            name: 'height', label: 'The Height', description: 'How tall this is',
            allTypes: false, types: [desktopAppsType]);
        CustomField textField1 = TextCustomField.build(value: '5 8', customFieldDefinition: cfd1)

        CustomFieldDefinition cfd2 = TextCustomFieldDefinition.build(
            name: 'weight', label: 'The Wait', description: 'How heavy this is',
            allTypes: false, types: [webApps]);
        CustomField textField2 = TextCustomField.build(value: '155', customFieldDefinition: cfd2)

        CustomFieldDefinition cfd3 = DropDownCustomFieldDefinition.build(
            allTypes: false, name: 'snack', types: [desktopAppsType]);
        FieldValue fieldVal1 = new FieldValue(displayText: 'Apples', customFieldDefinition: cfd3)
		cfd3.addToFieldValues(fieldVal1)
        fieldVal1 = new FieldValue(displayText: 'Bananas', customFieldDefinition: cfd3)
		cfd3.addToFieldValues(fieldVal1)
        cfd3.save(flush:true)
        CustomField dropDownField1 = DropDownCustomField.build(value: fieldVal1, customFieldDefinition: cfd3)
        assertFalse dropDownField1.isEmpty()

        CustomFieldDefinition cfd4 = DropDownCustomFieldDefinition.build(
            name: 'color', allTypes: false, types: [webApps]);
        FieldValue fieldVal2 = new FieldValue(displayText: 'Azul', customFieldDefinition: cfd4)
		cfd4.addToFieldValues(fieldVal2)
        fieldVal2 = new FieldValue(displayText: 'Verde', customFieldDefinition: cfd4)
		cfd4.addToFieldValues(fieldVal2)
        cfd4.save(flush:true)
        CustomField dropDownField2 = DropDownCustomField.build(value: fieldVal2, customFieldDefinition: cfd4)

        CustomFieldDefinition cfd5 = CheckBoxCustomFieldDefinition.build(
            name: 'ready', label: 'Good to go', allTypes: false, types: [desktopAppsType]);
        CustomField checkboxField1 = CheckBoxCustomField.build(customFieldDefinition: cfd5)

        CustomFieldDefinition cfd6 = ImageURLCustomFieldDefinition.build(
            name: 'screenshot3', label: 'screenshot3', allTypes: false, types: [webApps]);
        CustomField imageURLField1 = ImageURLCustomField.build(customFieldDefinition: cfd6,
            value: 'http://www.freeiconsweb.com/Icons-show/choose_your_sport__win/football.png')

         CustomFieldDefinition cfd7 = TextAreaCustomFieldDefinition.build(
            name: 'area33', allTypes: false, types: [webApps]);
        CustomField textAreaField1 = TextAreaCustomField.build(customFieldDefinition: cfd7,
            value: 'Luckily, just before night, all four of us had lashed ourselves firmly to the fragments of the windlass, lying in this manner as flat upon the deck as possible.')

        serviceItem.customFields = [textField1, dropDownField1, checkboxField1]

        // serviceItem has type rest so all custom fields should be saved
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		ServiceItem si = ServiceItem.get(serviceItem.id)
		assertEquals 3, si.customFields.size()

        si.types = webApps
        si.addToCustomFields(textField2)
        si.addToCustomFields(dropDownField2)
        si.addToCustomFields(imageURLField1)
        si.addToCustomFields(textAreaField1)
        // textField1 and dropDownField1 should not be saved since it does not have type widgetType
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		si = ServiceItem.get(serviceItem.id)
		assertEquals 4, si.customFields.size()
        si.customFields.each{ logIt(it) }
        assertTrue si.customFields.contains(textField2)
        assertTrue si.customFields.contains(dropDownField2)
        assertTrue si.customFields.contains(imageURLField1)
        assertTrue si.customFields.contains(textAreaField1)
        assertFalse si.customFields.contains(textField1)
        assertFalse si.customFields.contains(dropDownField1)
        assertFalse si.customFields.contains(checkboxField1)
    }

    // create a serviceItem that has not been persisted
    def createServiceItem(def serviceName){
        def author = Profile.build()

		def serviceItem = new ServiceItem(svcProps)
        serviceItem.title = serviceName
        serviceItem.uuid = Utils.generateUUID()
        serviceItem.owners = [author]
		if (!serviceItem.validate())
        {
            serviceItem.errors.each{
                println "Error on save: ${it}"
            }
        }

		return serviceItem
    }

    @Ignore
    void testListByDate() {
        // First serviceItem was already created in setup
        assertNotNull serviceItem
        assertNotNull serviceItem.createdDate
        def firstDate = serviceItem.createdDate

        try {
            Thread.currentThread().sleep(3000)
        } catch (Exception e) {}


        def desktopApps = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)

        serviceItem = ServiceItem.build(title: "Test111", owners: [mpTest1], types: desktopApps)
        assertNotNull ServiceItem.get(serviceItem.id)
        assertNotNull serviceItem.createdDate
        def secondDate = serviceItem.createdDate

        try {
            Thread.currentThread().sleep(1000)
        } catch (Exception e) {}

        def params = ['editedSinceDate':firstDate]
        def r = serviceItemService.list(params)
        assertNotNull r
        assertEquals("Expected 2 service items, got ${r.size()}: ${r} / ${r.collect {it.editedDate.time}}", 2, r.size())


        params = ['editedSinceDate':secondDate]
        r = serviceItemService.list(params)
        assertNotNull r
        assertEquals("Expected 1 service item, got ${r.size()}: ${r} / ${r.collect {it.editedDate.time}}", 1, r.size())
    }

    @Ignore
    void testChangingAllListingsInsideAndOutside() {
        ServiceItem.build(title:"In-Out Test 1", owners: [testUser1], isOutside: true).save()
        ServiceItem.build(title:"In-Out Test 2", owners: [testUser1], isOutside: false).save()
        ServiceItem.build(title:"In-Out Test 3", owners: [testUser1], isOutside: true).save()
        ServiceItem.build(title:"In-Out Test 4", owners: [testUser1], isOutside: null).save(flush:true)

        def adminUser = Profile.build(username: 'admin', displayName: 'admin', createdDate: new Date())
        switchAdmin(adminUser.username)

        // find IDs of items w/ null isOutside, and IDs of items w/ non-null isOutside.
        def affectedIds = ServiceItem.findAll("from ServiceItem where isOutside != null")*.id

        serviceItemService.makeListingsOutsideOrInside(false)

        def allItems = ServiceItem.list()
        def affectedItems = allItems.findAll({item -> affectedIds.contains(item.id)})

        assert affectedItems.size() == affectedIds.size()
        assert affectedItems.every({it.isOutside == false})

        serviceItemService.makeListingsOutsideOrInside(true)

        allItems = ServiceItem.list()
        affectedItems = allItems.findAll({item -> affectedIds.contains(item.id)})

        assert affectedItems.size() == affectedIds.size()
        assert affectedItems.every({it.isOutside == true})
    }

    @Ignore
    void testBindOwners() {

        Profile user2 = new Profile(username: 'user2', displayName: 'Marketplace Tester 2', createdDate: new Date())
        Profile user3 = new Profile(username: 'user3', displayName: 'Marketplace Tester 3', createdDate: new Date())
        user2.save()
        user3.save()

        Map params = [owners: ['user2', 'user3'] as String[]]

        serviceItemService.bindOwners(params, serviceItem)

        assertEquals([user2, user3], serviceItem.owners)

    }

    @Ignore
    void testBindInvalidOwners() {

        Profile user4 = new Profile(username: 'user4', displayName: 'Marketplace Tester 4', createdDate: new Date())
        user4.save()

        Map params = [owners: ['user4', 'user5'] as String[]]

        shouldFail(grails.validation.ValidationException) {
            serviceItemService.bindOwners(params, serviceItem)
        }
    }
}
