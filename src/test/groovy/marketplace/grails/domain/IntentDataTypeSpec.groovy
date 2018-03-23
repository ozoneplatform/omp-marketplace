package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import org.grails.web.json.JSONObject
//import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(IntentDataType)
class IntentDataTypeSpec extends Specification implements DomainConstraintsUnitTest<IntentDataType>{
    List<Class> getDomainClasses() {[IntentDataType]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    IntentDataType dataType

    void setup() {
//        FakeAuditTrailHelper.install()
//
//        mockDomain(IntentDataType)
        //def uniqueDataType = new IntentDataType(title: 'unique title', description: 'unique description', uuid: 'unique uuid')
//        mockForConstraintsTests(IntentDataType, [uniqueDataType])
        dataType = new IntentDataType(title: 'test title', description: 'test description', uuid: 'test uuid')
    }

    void testTitleDoesNotValidateWhenNull() {
        expect:
        propertyValueIsInvalid('title', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
//        dataType.title = null
//        assertFalse dataType.validate()
    }

    void testTitleConstraints() {
        expect:
        propertyValueIsInvalid('title', "", ConstrainedProperty.BLANK_CONSTRAINT)
        propertyHasMaxSize('title', 256)
//        dataType.title = ' '
//        assertFalse dataType.validate()
    }
//
//    void testTitleValidatesWhen1Character() {
//        dataType.title = 't'
//        assertTrue dataType.validate()
//    }
//
//    void testTitleValidatesWhen256Characters() {
//        dataType.title = 't'*256
//        assertTrue dataType.validate()
//    }
//
//    void testTitleDoesNotValidateWhen257Characters() {
//        dataType.title = 't'*257
//        assertFalse dataType.validate()
//    }

    void testDescriptionConstraints() {
        expect:
        propertyValueIsValid('description', null)
        propertyHasMaxSize('description', 256)
//        dataType.description = null
//        assertTrue dataType.validate()
    }

//    void testDescriptionValidatesWhen1Character() {
//        dataType.description = 'd'
//        assertTrue dataType.validate()
//    }
//
//    void testDescriptionValidatesWhen256Characters() {
//        dataType.description = 'd'*256
//        assertTrue dataType.validate()
//    }
//
//    void testDescriptionDoesNotValidateWhen257Characters() {
//        dataType.description = 'd'*257
//        assertFalse dataType.validate()
//    }

    void testUuidValidatesWhenNull() {
        expect:
        propertyValueIsValid('uuid', null)
//        dataType.uuid = null
//        assertTrue dataType.validate()
    }

    void testUuidDoesNotValidateWhenNotUnique() {
        given:
        dataType = new IntentDataType(title: 'test title', description: 'test description', uuid: 'unique uuid')
        when:
        dataType.save()
        then:
        IntentDataType.count() == old(IntentDataType.count()) + 1

        when:
        def testDataType = new IntentDataType(title: 'test', description: 'test', uuid: 'unique uuid')
        then:
        !testDataType.validate()
        !testDataType.save()
        IntentDataType.count() == old(IntentDataType.count())
//        assertFalse testDataType.validate()
    }

    void testUuidIsGeneratedOnSave() {
        given:
        dataType = new IntentDataType(title: 'test title', description: 'test description', uuid: null)

        when:
        dataType.save(flush: true)
        then:
        dataType.uuid != null
    }

    void testEqualsAndHashCode() {
        when:
        dataType.save()
        def other = IntentDataType.get(dataType.id)

        then:
        assert other == dataType
        assert other.hashCode() == dataType.hashCode()
    }

    void testNotEqual() {
        given:
        def other = new IntentDataType(title: 'other')

        expect:
        assert other != dataType
    }

    void testToString() {
        when:
        dataType.save()
        then:
        assert dataType.toString() == dataType.title
    }

    void testPrettyPrint() {
        when:
        dataType.save()
        then:
        assert dataType.prettyPrint() == 'test title'
    }

    void testTitleDisplayShortTitle() {
        when:
        dataType.title = '1'
        then:
        assert dataType.titleDisplay() == '1'
    }

    void testTitleDisplay12Chars() {
        given:
        def twelve = '123456789012'
        dataType.title = twelve

        expect:
        assert dataType.titleDisplay() == twelve
    }

    void testTitleDisplayShortenedTitle() {
        given:
        dataType.title = '1234567890123'
        expect:
        assert dataType.titleDisplay() == '123456789012...'
    }

    void testAsJSON() {
        given:
        def json = dataType.asJSON()
        expect:
        assert json.toString().indexOf('"id":null') > 0
        assert json.toString().indexOf('"title":"test title"') > 0
        assert json.toString().indexOf('"description":"test description"') > 0
        assert json.toString().indexOf('"uuid":"test uuid"') > 0
    }

    void testAsJSONRef() {
        given:
        def json = dataType.asJSONRef()
        expect:
        assert json.toString().indexOf('"id":null') > 0
        assert json.toString().indexOf('"title":"test title"') > 0
        assert json.toString().indexOf('"uuid":"test uuid"') > 0
    }

    void testFindDuplicates(){
        given:
        def testDataType = new IntentDataType(title: "dataType1", uuid: "1234")
        testDataType.save()
//        mockDomain(IntentDataType, [testDataType])

        when:
        def duplicateUuidDataType = new JSONObject(title: "dataType2", uuid: "1234")
        then:
        assert testDataType.uuid == duplicateUuidDataType.uuid
        assert IntentDataType.findDuplicates(duplicateUuidDataType) == true

        when:
        def duplicateTitleDataType = new JSONObject(title: "dataType1", uuid: "4321")
        then:
        assert testDataType.title == duplicateTitleDataType.title
        assert IntentDataType.findDuplicates(duplicateTitleDataType) == true

        when:
        def uniqueDataType = new JSONObject(title: "dataType3", uuid: "4321")
        then:
        assert testDataType.uuid != uniqueDataType.uuid
        assert testDataType.title != uniqueDataType.title
        assert IntentDataType.findDuplicates(uniqueDataType) == false
    }
}
