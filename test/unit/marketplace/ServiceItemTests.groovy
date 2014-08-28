package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil
import org.codehaus.groovy.grails.web.json.JSONObject
import org.junit.Before
import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(ServiceItem)
class ServiceItemTests {
    def serviceItem

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(ServiceItem)
        serviceItem = new ServiceItem()
    }

    void testApprovedNotNull() {
        assert serviceItem.approvalStatus != null, "approval status should be initialized set"
        assertEquals "approval status should be initialized to Pending", serviceItem.approvalStatus, Constants.APPROVAL_STATUSES["IN_PROGRESS"]
    }

    void testIsHiddenNotNull() {
        assert serviceItem.isHidden != null, "isHidden should be initialized set"
        assertEquals "isHidden should be initialized to 0", serviceItem.isHidden, new Integer(0)
    }

    void testAvgRateNotNull() {
        assert serviceItem.avgRate != null, "avgRate should be initialized set"
        assertEquals "avgRate should be initialized to 0", serviceItem.avgRate as Integer, new Integer(0)
    }

    //AML-
    void testIsOutsideNull() {
        assert serviceItem.isOutside == null, "isOutside should be null"
    }

    /**
     * class ServiceItem {
     *      ...
     *      title blank: false
     *      ...
     * }
     */
    void testTitleNotBlank(){
        serviceItem = new ServiceItem()
        serviceItem.title = ''
        assertFalse serviceItem.validate()
        assertEquals 'title is blank.', 'blank', serviceItem.errors['title']

        serviceItem.title = "Not Blank Title For Test"
        assertFalse serviceItem.validate() //Other items haven't been set yet
        assertNotSame "title is not allowed to be blank.", 'blank', serviceItem.errors['title']
    }

    /**
     * class ServiceItem {
     *      ...
     *      description maxSize: 500
     *      ...
     * }
     */
    void testDescriptionSizeContraints(){
        TestUtil.checkSizeConstraintProperty('description',serviceItem, 4000)
    }

    /**
     * class ServiceItem {
     *      ...
     *      requirements maxSize: 250
     *      ...
     * }
     */
    void testRequirementsSizeContraints(){
        TestUtil.checkSizeConstraintProperty('requirements',serviceItem, 1000)
    }

    /**
     * class ServiceItem {
     *      ...
     *      dependencies maxSize: 250
     *      ...
     * }
     */
    void testDependenciesSizeContraints(){
        TestUtil.checkSizeConstraintProperty('dependencies',serviceItem, 1000)
    }

    /**
     * class ServiceItem {
     *      ...
     *      organization maxSize: 40
     *      ...
     * }
     */
    void testOrganizationSizeContraints(){
        TestUtil.checkSizeConstraintProperty('organization',serviceItem, 256)
    }

    /**
     * class ServiceItem {
     *      ...
     *      description validator: {
     *          if (it.size() > 500)
     *          {
     *              return ["serviceItem.description.maxsize",500]
     *          }
     *          else
     *              return true
     *      }
     *      ...
     * }
     */
    void testDescriptionCustomValidator(){
        serviceItem = new ServiceItem(description: TestUtil.getStringOfLength(4001))
        assertFalse serviceItem.validate()
        assertEquals 'description fails validation of size 4001.', 'maxSize', serviceItem.errors['description']

        serviceItem = new ServiceItem(description: TestUtil.getStringOfLength(4000))
        assertFalse serviceItem.validate() //Other items haven't been set yet
        assertNotSame "description should pass validation of size 4000", 'maxSize', serviceItem.errors['description']
    }

    /**
     * class ServiceItem {
     *      ...
     *      launchUrl nullable: true
     *      docUrl nullable: true
     *      ...
     *      installUrl nullable: true
     *      ...
     *      requirements nullable: true
     *      dependencies nullable: true
     *      organization nullable: true
     *      ...
     *      emailAddress nullable: true
     *      ...
     * }
     */
    void testNullable(){
        serviceItem = new ServiceItem(launchUrl: null,
            installUrl: null,
            requirements: null,
            dependencies: null,
            organization: null)

        assertFalse serviceItem.validate()

        assertNotSame "launchUrl should be allowed to be nullable.", 'nullable', serviceItem.errors['launchUrl']
        assertNotSame "installUrl should be allowed to be nullable.", 'nullable', serviceItem.errors['installUrl']
        assertNotSame "requirements should be allowed to be nullable.", 'nullable', serviceItem.errors['requirements']
        assertNotSame "dependencies should be allowed to be nullable.", 'nullable', serviceItem.errors['dependencies']
        assertNotSame "organization should be allowed to be nullable.", 'nullable', serviceItem.errors['organization']
    }

    void testLaunchURLValid() {
        serviceItem = new ServiceItem(launchUrl: "https://www.foo.com")
        serviceItem.validate()
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "https://192.168.20.28:8443")
        assertFalse serviceItem.validate()
        serviceItem.errors.allErrors.each {
                println it
        }
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "http://localhost/widgetA")
        assertFalse serviceItem.validate()
        serviceItem.errors.allErrors.each {
            println it
        }
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "http://localhost:8080/widgetA")
        assertFalse serviceItem.validate()
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "http://my-machine/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        assertFalse serviceItem.validate()
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "http://pctina/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        assertFalse serviceItem.validate()
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "http://pctina:8080/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        assertFalse serviceItem.validate()
        assertNull serviceItem.errors['launchUrl']

        serviceItem = new ServiceItem(launchUrl: "http://pctina:80805/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        assertFalse serviceItem.validate()
        assertNull serviceItem.errors['launchUrl']

        // too many digits in port
        /*
        serviceItem = new ServiceItem(launchUrl: "http://pctina:808056/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        assertFalse serviceItem.validate()
        assertNotNull serviceItem.errors['launchUrl']

        // No port #, though indicates port
        serviceItem = new ServiceItem(launchUrl: "http://pctina:/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        assertFalse serviceItem.validate()
        assertNotNull serviceItem.errors['launchUrl']
         */

    }

    void testLaunchable() {
        def type = new Types(hasLaunchUrl: true)
        def state = new State(isPublished: true)
        ServiceItem serviceItem = new ServiceItem(
            launchUrl: "https://www.foo.com",
            approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"],
            types: type,
            state: state
        )

        // Verify a valid launchable item returns true for isLaunchable
        assertTrue serviceItem.isLaunchable()

        // Verify that a non-published service item is not launchable
        serviceItem.state.isPublished = false
        assertFalse serviceItem.isLaunchable()
        serviceItem.state.isPublished = true

        // Verify that a non-approved service item is not launchable
        serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["SUBMITTED"]
        assertFalse serviceItem.isLaunchable()
        serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]

        // Verify that a malformed URL is not launchable
        serviceItem.launchUrl = 'abcd://not.a.working.url.com'
        assertFalse serviceItem.isLaunchable()
    }

    void testTextCustomFieldsCustomValidator(){
        mockForConstraintsTests(TextCustomField)
        def type = new Types()
        def cfd = new TextCustomFieldDefinition(id:1, types: [type])
        def cf = new TextCustomField(customFieldDefinition: cfd, value:'123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890')
        serviceItem.customFields= [cf]
        assertFalse serviceItem.validate()
        println cf.customFieldDefinition.id
        println serviceItem.customFields[0].errors
        assertEquals 'maxSize', serviceItem.customFields[0].errors['value']
    }

    void testFindDuplicates(){
        def uuid = '12345678-1234-1234-1234-123456789012'

        def testOwfProperties1 = new OwfProperties(universalName: "name1")
        mockDomain(OwfProperties)

        def testServiceItem = new ServiceItem(
            owfProperties: testOwfProperties1,
            uuid: uuid,
            owners: [new Profile(username: 'admin')],
            types: new Types(title: 'test'),
            launchUrl: 'https:///',
            title: 'service item'
        )
        mockDomain(ServiceItem)
        testServiceItem.save(failOnError: true)

        def duplicateUuidServiceItem = new JSONObject(owfProperties: new JSONObject(universalName: "name2"), uuid: uuid)
        assertEquals testServiceItem.uuid, duplicateUuidServiceItem.uuid
        assertTrue ServiceItem.findDuplicates(duplicateUuidServiceItem)

        def duplicateUniversalNameServiceItem = new JSONObject(owfProperties: new JSONObject(universalName: "name1"), uuid: "4321")
        assertEquals testServiceItem.owfProperties?.universalName, duplicateUniversalNameServiceItem.owfProperties?.universalName
        assertTrue ServiceItem.findDuplicates(duplicateUniversalNameServiceItem)

        def uniqueServiceItem = new JSONObject(owfProperties: new JSONObject(universalName: "name3"), uuid: "4321")
        assertFalse(testServiceItem.uuid == uniqueServiceItem.uuid)
        assertFalse(testServiceItem.owfProperties?.universalName == uniqueServiceItem.owfProperties?.universalName)
        assertFalse ServiceItem.findDuplicates(uniqueServiceItem)

        def nullUniversalNameServiceItem = new JSONObject(owfProperties: new JSONObject(universalName: null), uuid: "4321")
        assertFalse(testServiceItem.uuid == nullUniversalNameServiceItem.uuid)
        assertNull nullUniversalNameServiceItem.owfProperties?.universalName
        assertFalse ServiceItem.findDuplicates(nullUniversalNameServiceItem)
    }

	void testUpdateInsideOutsideFlagToggleToTrue(){
		ServiceItem item = new ServiceItem()

		item.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ALL_INSIDE)
		assertThat(item.isOutside, is(Boolean.FALSE))  //changed based on global setting to false
		item.isOutside = null

		item.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ADMIN_SELECTED)
		assertThat(item.isOutside, is(nullValue()))  //Not modified
		item.isOutside = null

		item.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ALL_OUTSIDE)
		assertThat(item.isOutside, is(Boolean.TRUE))  //changed to true

	}

    //TODO see if there is a way to get getPersistentValue
    //working in grails 2 unit tests
    //void testModifiedForChangelog() {
        //def dirtyProperty
        //def oldValue = 'old'
        //def newValue = 'new'
        //def serviceItemDirty
        //def owfPropertiesDirty
        //def categoryDirty

        //ServiceItem.metaClass.isDirty = {
            //serviceItemDirty
        //}

        //ServiceItem.metaClass.getDirtyPropertyNames = {
            //[dirtyProperty]
        //}

        //ServiceItem.metaClass.getPersistentValue = { prop ->
            //if (prop == dirtyProperty) {
                //oldValue
            //}
            //else if (prop == 'owfProperties') {
                //new OwfProperties(
                    //universalName: dirtyProperty == 'universalName' ? oldValue : newValue
                //)
            //}
        //}

        //OwfProperties.metaClass.isDirty = {
            //owfPropertiesDirty
        //}

        //OwfProperties.metaClass.getDirtyPropertyNames = {
            //[dirtyProperty]
        //}

        //OwfProperties.metaClass.getPersistentValue = {
            //oldValue
        //}

        //Category.metaClass.isDirty = {
            //categoryDirty
        //}

        //Category.metaClass.getDirtyPropertyNames = {
            //[dirtyProperty]
        //}

        //Category.metaClass.getPersistentValue = {
            //oldValue
        //}

        //def testServiceItem = new ServiceItem(
            //title: newValue,
            //owfProperties: new OwfProperties(
                //universalName: newValue
            //),
            //categories: [new Category(title: newValue)]
        //)

        ////test detection of dirty properties directly on the service item
        //serviceItemDirty = false
        //owfPropertiesDirty = false
        //categoryDirty = false
        //assertFalse testServiceItem.modifiedForChangeLog()

        //serviceItemDirty = true
        //dirtyProperty = 'version'   //a property that should be ignored
        //assertFalse testServiceItem.modifiedForChangeLog()

        //dirtyProperty = 'title'
        //assertTrue testServiceItem.modifiedForChangeLog()

        //oldValue = 'new' //supposedly dirty, but the value hasn't actually changed
        //assertFalse testServiceItem.modifiedForChangeLog()
        //oldValue = 'old'

        ////test detection of dirty properties on the owfProperties
        //serviceItemDirty = false
        //owfPropertiesDirty = true
        //dirtyProperty = 'version'
        //assertFalse testServiceItem.modifiedForChangeLog()

        //dirtyProperty = 'universalName'
        //assertTrue testServiceItem.modifiedForChangeLog()

        //oldValue = 'new' //supposedly dirty, but the value hasn't actually changed
        //assertFalse testServiceItem.modifiedForChangeLog()
        //oldValue = 'old'

        ////test that subobjects are checked.  Using category as an example but recommendedLayouts
        ////and customFields should also work
        //owfPropertiesDirty = false
        //categoryDirty = true
        //dirtyProperty = 'title'
        //assertTrue testServiceItem.modifiedForChangeLog()

        //oldValue = 'new' //supposedly dirty, but the value hasn't actually changed
        //assertFalse testServiceItem.modifiedForChangeLog()
        //oldValue = 'old'
    //}

    void testProcessCustomFieldTypes() {
        def type = new Types(title: 'test type')

        def si = new ServiceItem(types: type)

        def customFieldDefinitions = [
            new CustomFieldDefinition(
                name: 'customfield def 1',
                types: [type]
            ),
            new CustomFieldDefinition( name: 'customfield def 2')
        ]

        def dropDownCustomFieldMock = mockFor(DropDownCustomField)

        //test that marshallAllFieldValues gets called on the dropDownCustomField
        dropDownCustomFieldMock.demand.marshallAllFieldValues(1..1) {}

        si.customFields = [
            new TextCustomField(
                customFieldDefinition: customFieldDefinitions[1],
                value: 'test'
            ),
            new DropDownCustomField(
                customFieldDefinition: customFieldDefinitions[0]
            )
        ]

        si.processCustomFields()

        //check that only the customField that is valid for this type was kept
        assert si.customFields.size() == 1
        assert si.customFields[0].customFieldDefinition.types.contains(type)
    }

    void testCheckOwfProperties() {
        def awareType = new Types(ozoneAware: true)
        def unawareType = new Types(ozoneAware: false)

        def si = new ServiceItem(types: awareType)

        assertNull si.owfProperties

        si.checkOwfProperties()
        assertNotNull si.owfProperties
        assert si.owfProperties instanceof OwfProperties

        si.types = unawareType

        si.checkOwfProperties()
        assertNull si.owfProperties
    }

    void testUpdateInsideOutsideFlag() {
        def si = new ServiceItem()

        assertNull si.isOutside

        si.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ALL_INSIDE)
        assertFalse si.isOutside

        si.updateInsideOutsideFlag(null)
        assertFalse si.isOutside

        si.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ALL_OUTSIDE)
        assertTrue si.isOutside

        si.updateInsideOutsideFlag(null)
        assertTrue si.isOutside
    }
}
