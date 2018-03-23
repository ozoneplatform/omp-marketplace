package marketplace

import grails.gorm.validation.ConstrainedProperty
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import org.grails.datastore.mapping.model.MappingContext
import org.grails.orm.hibernate.cfg.HibernatePersistentEntity
import org.grails.web.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import static org.junit.Assert.*

class ServiceItemSpec extends Specification implements DomainConstraintsUnitTest<ServiceItem>{
    Class<?>[] getDomainClassesToMock() {
        [ServiceItem,TextCustomField]
    }
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

    @Autowired
    TextCustomField textCustomField

    def serviceItem

    void setup() {
//        FakeAuditTrailHelper.install()

//        mockForConstraintsTests(ServiceItem)
        serviceItem = new ServiceItem()
    }

    void testApprovedNotNull() {
        expect:
        propertyValueIsInvalid('approvalStatus', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
        assert serviceItem.approvalStatus == Constants.APPROVAL_STATUSES["IN_PROGRESS"]
//        initializedPropertyIsRequired('approvalStatus', Constants.APPROVAL_STATUSES["IN_PROGRESS"])
   }

    void testIsHiddenNotNull() {
        expect:
        propertyValueIsInvalid('isHidden', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
        assert serviceItem.isHidden == 0
    }

    void testAvgRateNotNull() {
        expect:
        propertyValueIsInvalid('avgRate', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
        assert serviceItem.avgRate == 0
//        assert serviceItem.avgRate != null, "avgRate should be initialized set"
//        assertEquals "avgRate should be initialized to 0", serviceItem.avgRate as Integer, new Integer(0)
    }

    //AML-
    void testIsOutsideNull() {
        expect:
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
        expect:
        propertyIsRequired('title')
        propertyValueIsValid('title','Not Blank')
//        serviceItem.title = ''
//        assertFalse serviceItem.validate()
//        assertEquals 'title is blank.', 'blank', serviceItem.errors['title']
//
//        serviceItem.title = "Not Blank Title For Test"
//        assertFalse serviceItem.validate() //Other items haven't been set yet
//        assertNotSame "title is not allowed to be blank.", 'blank', serviceItem.errors['title']
    }

    /**
     * class ServiceItem {
     *      ...
     *      description maxSize: 4000
     *      ...
     * }
     */
    void testDescriptionSizeContraints(){
        expect:
        propertyHasMaxSize('description', 4000)
//        TestUtil.checkSizeConstraintProperty('description',serviceItem, 4000)
    }

    /**
     * class ServiceItem {
     *      ...
     *      requirements maxSize: 1000
     *      ...
     * }
     */
    void testRequirementsSizeContraints(){
        expect:
        propertyHasMaxSize('requirements', 1000)
//        TestUtil.checkSizeConstraintProperty('requirements',serviceItem, 1000)
    }

    /**
     * class ServiceItem {
     *      ...
     *      dependencies maxSize: 1000
     *      ...
     * }
     */
    void testDependenciesSizeContraints(){
        expect:
        propertyHasMaxSize('dependencies', 1000)
//        TestUtil.checkSizeConstraintProperty('dependencies',serviceItem, 1000)
    }

    /**
     * class ServiceItem {
     *      ...
     *      organization maxSize: 256
     *      ...
     * }
     */
    void testOrganizationSizeContraints(){
        expect:
        propertyHasMaxSize('organization', 256)
//        TestUtil.checkSizeConstraintProperty('organization',serviceItem, 256)
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
    //Commented out due to no custom validator on description field
//    void testDescriptionCustomValidator(){
//        serviceItem = new ServiceItem(description: TestUtil.getStringOfLength(4001))
//        assertFalse serviceItem.validate()
//        assertEquals 'description fails validation of size 4001.', 'maxSize', serviceItem.errors['description']
//
//        serviceItem = new ServiceItem(description: TestUtil.getStringOfLength(4000))
//        assertFalse serviceItem.validate() //Other items haven't been set yet
//        assertNotSame "description should pass validation of size 4000", 'maxSize', serviceItem.errors['description']
//    }

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
        setup:
        serviceItem = new ServiceItem(launchUrl: null,
            installUrl: null,
            requirements: null,
            dependencies: null,
            organization: null)

        expect:
        !serviceItem.validate()
        propertyValueIsValid('launchUrl', null)
        propertyValueIsValid('installUrl', null)
        propertyValueIsValid('requirements', null)
        propertyValueIsValid('dependencies', null)
        propertyValueIsValid('organization', null)
    }

    void testLaunchURLValid() {
        expect:
        propertyValueIsValid('launchUrl', "https://www.foo.com")
        propertyValueIsValid('launchUrl', "https://192.168.20.28:8443")
        propertyValueIsValid('launchUrl', "http://localhost/widgetA")
        propertyValueIsValid('launchUrl', "http://localhost:8080/widgetA")
        propertyValueIsValid('launchUrl', "http://my-machine/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        propertyValueIsValid('launchUrl', "http://pctina/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        propertyValueIsValid('launchUrl', "http://pctina:8080/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")
        propertyValueIsValid('launchUrl', "http://pctina:80805/owf/examples/fake-widgets/img/fakeWidgets/fakeWidget8.png")

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
        setup:
        def type = new Types(hasLaunchUrl: true)
        def state = new State(isPublished: true)
        ServiceItem serviceItem = new ServiceItem(
            launchUrl: "https://www.foo.com",
            approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"],
            types: type,
            state: state
        )

        // Verify a valid launchable item returns true for isLaunchable
        expect:
        serviceItem.isLaunchable()

        // Verify that a non-published service item is not launchable
        when:
        serviceItem.state.isPublished = false
        then:
        !serviceItem.isLaunchable()

        when:
        serviceItem.state.isPublished = true
        // Verify that a non-approved service item is not launchable
        serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["SUBMITTED"]
        then:
        !serviceItem.isLaunchable()

        when:
        serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]
        // Verify that a malformed URL is not launchable
        serviceItem.launchUrl = 'abcd://not.a.working.url.com'
        then:
        !serviceItem.isLaunchable()
    }

    //TODO BVEST Revisit
//    void testTextCustomFieldsCustomValidator(){
//        setup:
//        def type = new Types()
//        def cfd = new TextCustomFieldDefinition(id:1, types: [type])
//        textCustomField = new TextCustomField(customFieldDefinition: cfd, value:'123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890')
//        serviceItem.customFields= [textCustomField]
//
//        expect:
//        textCustomField.validate()
//        !serviceItem.validate()
//    }

    //TODO BVEST Move to integration
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
        setup:
		ServiceItem item = new ServiceItem()
        when:
		item.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ALL_INSIDE)
		then:
        assert item.isOutside == Boolean.FALSE //changed based on global setting to false

        when:
        item.isOutside = null
        item.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ADMIN_SELECTED)

        then:
        assert item.isOutside == null  //Not modified

        when:
        item.isOutside = null
		item.updateInsideOutsideFlag(Constants.INSIDE_OUTSIDE_ALL_OUTSIDE)
        then:
        assert item.isOutside == Boolean.TRUE //changed to true

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
