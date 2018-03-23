package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.Category
import marketplace.Constants
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.FieldValue
import marketplace.PermissionException
import marketplace.Profile
import marketplace.Relationship
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.State
import marketplace.TextCustomField
import marketplace.TextCustomFieldDefinition
import marketplace.Types
import marketplace.controller.MarketplaceIntegrationTestCase
import spock.lang.Ignore
import org.apache.commons.lang.time.FastDateFormat
import org.springframework.web.context.request.RequestContextHolder as RCH

import ozone.marketplace.enums.DefinedDefaultTypes;
import ozone.utils.Utils
import ozone.marketplace.domain.ValidationException

@Ignore
@Integration
@Rollback
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

    void setup() {
//        super.setUp()

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
            releasedDate: new Date(),
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

    @Ignore
    void testGetSession() {
        when:
    	def retVal = serviceItemService.getSession()
    	then:
        RCH.currentRequestAttributes().getSession() == retVal
    }

    @Ignore
    void testSave(){
        when:
		//Test the save method
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        then:
        //Test to make sure save was successful
        serviceItem.title == ServiceItem.findByTitle("Test000").title
		//Test the audit trail
		1 == serviceItem.serviceItemActivities.size()
		serviceItem.lastActivity.action == Constants.Action.MODIFIED
    }

    @Ignore
    void testSaveReturnsIdentity(){
        when:
        //Test the save method
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        then:
        //Test to make sure save was successful
        serviceItem.title == ServiceItem.findByTitle("Test000").title
        //Test the audit trail
        1 == serviceItem.serviceItemActivities.size()
        serviceItem.lastActivity.action == Constants.Action.MODIFIED
        null != serviceItem.id
    }

    @Ignore
	void testSavingAListingThatIDoNotOwn() {
        when:
		def userLewis = Profile.build(username: 'rLewis', displayName: 'Ray Lewis').save()
		switchUser(userLewis.username)
		ServiceItem si = ServiceItem.build(title:"Ray's Listing",owners: [userLewis]).save()

		def userBigBen = Profile.build(username: 'bigben', displayName: 'Big Ben').save()
		si.title = "Steel me a Listing"
		switchUser(userBigBen.username)
        serviceItemService.save(si)

        then:
        thrown(PermissionException)
	}

    @Ignore
	void testSaveWithInvalidCustomFields(){
        when:
		CustomFieldDefinition cfd = CustomFieldDefinition.build(allTypes: false, types: [Types.build()])
		CustomField cf = TextCustomField.build(customFieldDefinition: cfd)
        // cf has value of null and cfd has a bunch of null fields...
		serviceItem.customFields = [cf]
		serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		ServiceItem si = ServiceItem.get(serviceItem.id)

        then:
        0 == si.customFields.size()
	}

    // tests the logic that makes sure only custom fields with a service type that matches
    // the service item's service type are saved with the service item.
    @Ignore
    void testCustomFields() {
        when:
        def desktopAppType = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)
        then:
        null != desktopAppType

        when:
        def webAppType = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title)
        then:
        null != webAppType

        when:
        def widgetType = new Types(title: "Widget")
        then:
        null != widgetType

        when:
        def plugInType = new Types(title: "plugin")
        then:
        null != plugInType

        when:
        CustomFieldDefinition cfd1 = CustomFieldDefinition.build(
            name: 'height', label: 'The Height', description: 'How tall this is',
            allTypes: false, types: [desktopAppType, webAppType, widgetType]);
        CustomField customField1 = TextCustomField.build(value: '5 8', customFieldDefinition: cfd1)
        CustomFieldDefinition cfd2 = CustomFieldDefinition.build(
            name: 'height2', label: 'The Height2', description: 'How tall this is2',
            allTypes: false, types: [desktopAppType, plugInType]);
        CustomField customField2 = TextCustomField.build(value: '6 4', customFieldDefinition: cfd2)

        serviceItem.customFields = [customField1, customField2]

        // serviceItem has type rest so all custom fields should be saved
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		ServiceItem si = ServiceItem.get(serviceItem.id)
		then:
        2 == si.customFields.size()

        when:
        serviceItem.types = plugInType
        // customField2 should not be saved since it does not have type plugIn
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		si = ServiceItem.get(serviceItem.id)
		then:
        1 == si.customFields.size()

        when:
        // change types on the customFieldDefinition for customField2
        // so customField2 will not get saved with the serviceItem
        cfd2.types = [desktopAppType]
        cfd2.save()
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
 		si = ServiceItem.get(serviceItem.id)
		then:
        0 == si.customFields.size()
    }

    @Ignore
    void testDelete(){
        expect:
        serviceItem.validate() //Should be true...

        when:
        //Save should pass...
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        then:
        //Now should have a valid id
        serviceItem.id != null

        when:
        Long currentServiceItemId = new Long(serviceItem.id)

        //Make sure the delete worked
		def checkItem = ServiceItem.get(currentServiceItemId)

        //Test delete method
        serviceItemService.delete(currentServiceItemId)

        then:
        //Make sure the delete worked
        null == ServiceItem.get(currentServiceItemId)

    }

    @Ignore
	void testDeleteWithComments(){
        when:
		switchUser("mpTest1")

		serviceItemService.save(serviceItem)
		itemCommentService.saveItemComment([username: mpTest1.username, commentTextInput: "Comment 1", serviceItemId: "${serviceItem.id}"])

		def params = [ id: "${serviceItem.id}" ]
		def comments = itemCommentService.getServiceItemComments(params)

        then:
		1 == comments.size()

        when:
        serviceItemService.delete(serviceItem.id)
		then:
        null == ServiceItem.get(serviceItem.id)
	}

    @Ignore
	void testDeleteWithRelationship(){
        when:
		switchUser(testUser1.username)
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])

		def rel  = Relationship.findByOwningEntity(parent)

        then:
        3 == rel.relatedItems.size()

        when:
        serviceItemService.delete(parent.id)

        then:
        null == ServiceItem.get(parent.id)
		null == Relationship.findByOwningEntity(parent)

        when:
		def r1RemoveActivity = getActivity(requires1,Constants.Action.REMOVERELATEDTOITEM)
		def r2RemoveActivity = getActivity(requires2,Constants.Action.REMOVERELATEDTOITEM)
		def r3RemoveActivity = getActivity(requires3,Constants.Action.REMOVERELATEDTOITEM)

        then:
		null != r1RemoveActivity
		null != r2RemoveActivity
		null != r3RemoveActivity
		1 == r1RemoveActivity.items.size()
		1 == r2RemoveActivity.items.size()
		1 == r3RemoveActivity.items.size()
		"Parent" == r1RemoveActivity.items[0].title
		"Parent" == r2RemoveActivity.items[0].title
		"Parent" == r3RemoveActivity.items[0].title
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
		si.serviceItemActivities.findAll { it.action == action}.max { it.activityTimestamp }
	}

    @Ignore
	void testDeleteInRelationships(){
        when:
		switchUser(testUser1.username)
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem parent2 = ServiceItem.build(title:"Parent 2",owners: [testUser1]).save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])
		relationshipService.addOrRemoveRequires(parent2.id,[requires1.id,requires2.id,requires3.id])

		def rel  = Relationship.findByOwningEntity(parent)
		then:
        3 == rel.relatedItems.size()

        when:
        rel  = Relationship.findByOwningEntity(parent2)
		then:
        3 == rel.relatedItems.size()

        when:
		serviceItemService.delete(requires2.id)
		then:
        null == ServiceItem.get(requires2.id)

        when:
		rel = Relationship.findByOwningEntity(parent)
		then:
        2 == rel.relatedItems.size()
		"Requires 1" == rel.relatedItems[0].title
		"Requires 3" == rel.relatedItems[1].title

        when:
		def parentRemoveActivity = getActivity(parent,Constants.Action.REMOVERELATEDITEMS)
		then:
        null != parentRemoveActivity
		1 == parentRemoveActivity.items.size()
		"Requires 2" == parentRemoveActivity.items[0].title

        when:
		rel = Relationship.findByOwningEntity(parent2)
		then:
        2 == rel.relatedItems.size()
		"Requires 1" == rel.relatedItems[0].title
		"Requires 3" == rel.relatedItems[1].title

        when:
		def parent2RemoveActivity = getActivity(parent2,Constants.Action.REMOVERELATEDITEMS)
		then:
        null != parent2RemoveActivity
        1 == parent2RemoveActivity.items.size()
		"Requires 2" == parent2RemoveActivity.items[0].title
	}

    @Ignore
    void testUpdate(){
        expect:
        serviceItem.validate() //Should be true...

        when:
		//Save should pass...
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        then:
        //Now should have a valid id
        serviceItem.id != null

        when:
        //Test Update method...
        serviceItem.title = "NEW_TITLE_FOR_TEST"
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Make sure the update was a success..
        String currentServiceItemTitle = "NEW_TITLE_FOR_TEST"
        then:
        serviceItem.title == ServiceItem.findByTitle(currentServiceItemTitle).title

        //Check to make sure approved is 0
        serviceItem.approvalStatus == Constants.APPROVAL_STATUSES["IN_PROGRESS"]
        serviceItem.lastActivity.action == Constants.Action.MODIFIED

		//Test Audit trail (double modified)
		2 == serviceItem.serviceItemActivities.size()
		serviceItem.lastActivity.action == Constants.Action.MODIFIED

	}

    @Ignore
    void testOwfWidgetTypeAddedWhenListingEditedToOwfCompatibleType(){
        expect:
        serviceItem.validate() //Should be true...

        when:
		//Save should pass...
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        then:
        //Now should have a valid id
        serviceItem.id != null

        when:
        // update the type
        serviceItem.types = widgetType
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)

        //Make sure the update was a success..
        def updatedItem = ServiceItem.findByTitle('Test000')

        then:
        updatedItem.types.title == widgetType.title

        // Make sure the OWF widget type is standard, the default.
        updatedItem.owfProperties.owfWidgetType == 'standard'
	}

	private void doApprove(){
        when:
        def adminUser = Profile.build(username: 'admin', displayName: 'admin', createdDate: new Date())
        switchAdmin(adminUser.username)

        String sessionUsername = serviceItemService.getSession().username

		//Set approved equal to 0
		serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["IN_PROGRESS"]
		then:
        serviceItem.validate() //Should be true...

        when:
        //Save should pass...
		serviceItemService.save(serviceItem, sessionUsername)

        then:
		//Now should have a valid id
		serviceItem.id != null

        when:
        serviceItemService.setInsideOutside(serviceItem, sessionUsername, true)

		//Perform the approve method...
		serviceItemService.approve(serviceItem, sessionUsername)

        then:
		//Check to make sure approved is 1
		serviceItem.approvalStatus == Constants.APPROVAL_STATUSES["APPROVED"]
	}

    @Ignore
    void testApprove(){
        when:
        //Approve Service Item
		doApprove()

        then:
        //Test Audit trail
		serviceItem.lastActivity.action == Constants.Action.APPROVED
    }

    @Ignore
	void testApprovalDate(){
        when:
		//Approve Service Item
		doApprove()

        then:
		//Test Approved Date on Index Exists
//		"ServiceItem ${serviceItem}, approvalDate is NULL.",
        serviceItem?.approvalDate != null

        and:
		//Test Approved Date on Index is equal to Last Activity Date
		if((serviceItem?.approvalDate != null) && (serviceItem?.lastActivityDate != null)){
			expect:
            dateFormatter.format(serviceItem.approvalDate) == dateFormatter.format(serviceItem.lastActivityDate)
		}

		//Test Approved Date query is valid
		ServiceItem.withNewSession { session ->
			def approvedServiceItemActivity = ServiceItemActivity.findByServiceItemAndAction(serviceItem, Constants.Action.APPROVED)
			for(asia in approvedServiceItemActivity){
				if((serviceItem?.approvalDate != null) && (asia?.activityTimestamp != null)){
                    expect:
					dateFormatter.format(serviceItem?.approvalDate) == dateFormatter.format(asia?.activityTimestamp)
				}
				break
			}
		}
	}

    @Ignore
    void testCreate(){
        when:
        // create a serviceItem that has not been persisted. Note the build method from the
        // build-test-data does persist the object.
        def newServiceItem = createServiceItem('bruja 99')
        then:
        null == newServiceItem.id

        when:
        serviceItemService.save(newServiceItem,serviceItemService.getSession().username)

        then:
        1 == newServiceItem.serviceItemActivities.size()
		newServiceItem.lastActivity.action == Constants.Action.CREATED
	}

    // Test changing the type with both text and dropdown custom fields
    @Ignore
    void testChangeType() {
        when:
        def desktopAppsType = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)

        then:
        null != desktopAppsType

        when:
        def webApps = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title)
        then:
        null != webApps

        when:
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
        then:
        !dropDownField1.isEmpty()

        when:
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
		then:
        3 == si.customFields.size()

        when:
        si.types = webApps
        si.addToCustomFields(textField2)
        si.addToCustomFields(dropDownField2)
        si.addToCustomFields(imageURLField1)
        si.addToCustomFields(textAreaField1)
        // textField1 and dropDownField1 should not be saved since it does not have type widgetType
        serviceItemService.save(serviceItem,serviceItemService.getSession().username)
		si = ServiceItem.get(serviceItem.id)
		then:
        4 == si.customFields.size()
        //si.customFields.each{ logIt(it) }
        si.customFields.contains(textField2)
        si.customFields.contains(dropDownField2)
        si.customFields.contains(imageURLField1)
        si.customFields.contains(textAreaField1)
        !si.customFields.contains(textField1)
        !si.customFields.contains(dropDownField1)
        !si.customFields.contains(checkboxField1)
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
        expect:
        // First serviceItem was already created in setup
        null != serviceItem
        null != serviceItem.createdDate

        when:
        def firstDate = serviceItem.createdDate

        try {
            Thread.currentThread().sleep(3000)
        } catch (Exception e) {}


        def desktopApps = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)

        serviceItem = ServiceItem.build(title: "Test111", owners: [mpTest1], types: desktopApps)
        then:
        null != ServiceItem.get(serviceItem.id)
        null != serviceItem.createdDate

        when:
        def secondDate = serviceItem.createdDate

        try {
            Thread.currentThread().sleep(1000)
        } catch (Exception e) {}

        def params = ['editedSinceDate':firstDate]
        def r = serviceItemService.list(params)
        then:
        null != r
        2 == r.size()
//        ("Expected 2 service items, got ${r.size()}: ${r} / ${r.collect {it.editedDate.time}}", 2, r.size())

        when:
        params = ['editedSinceDate':secondDate]
        r = serviceItemService.list(params)
        then:
        null != r
        1 == r.size()
//        assertEquals("Expected 1 service item, got ${r.size()}: ${r} / ${r.collect {it.editedDate.time}}", 1, r.size())
    }

    @Ignore
    void testChangingAllListingsInsideAndOutside() {
        when:
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

        then:
        affectedItems.size() == affectedIds.size()
        affectedItems.every({it.isOutside == false})

        when:
        serviceItemService.makeListingsOutsideOrInside(true)

        allItems = ServiceItem.list()
        affectedItems = allItems.findAll({item -> affectedIds.contains(item.id)})

        then:
        affectedItems.size() == affectedIds.size()
        affectedItems.every({it.isOutside == true})
    }

    @Ignore
    void testBindOwners() {
        when:

        Profile user2 = new Profile(username: 'user2', displayName: 'Marketplace Tester 2', createdDate: new Date())
        Profile user3 = new Profile(username: 'user3', displayName: 'Marketplace Tester 3', createdDate: new Date())
        user2.save()
        user3.save()

        Map params = [owners: ['user2', 'user3'] as String[]]

        serviceItemService.bindOwners(params, serviceItem)

        then:
        [user2, user3].equals(serviceItem.owners)

    }

    @Ignore
    void testBindInvalidOwners() {
        when:
        Profile user4 = new Profile(username: 'user4', displayName: 'Marketplace Tester 4', createdDate: new Date())
        user4.save()

        Map params = [owners: ['user4', 'user5'] as String[]]
        serviceItemService.bindOwners(params, serviceItem)

        then:
        thrown(grails.validation.ValidationException)
    }
}
