package marketplace

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(IntentDataType)
class IntentDataTypeTests {
    IntentDataType dataType

    void setUp() {
        FakeAuditTrailHelper.install()

        mockDomain(IntentDataType)
        def uniqueDataType = new IntentDataType(title: 'unique title', description: 'unique description', uuid: 'unique uuid')
        mockForConstraintsTests(IntentDataType, [uniqueDataType])
        dataType = new IntentDataType(title: 'test title', description: 'test description', uuid: 'test uuid')
    }

    void testTitleDoesNotValidateWhenNull() {
        dataType.title = null
        assertFalse dataType.validate()
    }

    void testTitleDoesNotValidateWhenBlank() {
        dataType.title = ' '
        assertFalse dataType.validate()
    }

    void testTitleValidatesWhen1Character() {
        dataType.title = 't'
        assertTrue dataType.validate()
    }

    void testTitleValidatesWhen256Characters() {
        dataType.title = 't'*256
        assertTrue dataType.validate()
    }

    void testTitleDoesNotValidateWhen257Characters() {
        dataType.title = 't'*257
        assertFalse dataType.validate()
    }

    void testDescriptionValidatesWhenNull() {
        dataType.description = null
        assertTrue dataType.validate()
    }

    void testDescriptionValidatesWhen1Character() {
        dataType.description = 'd'
        assertTrue dataType.validate()
    }

    void testDescriptionValidatesWhen256Characters() {
        dataType.description = 'd'*256
        assertTrue dataType.validate()
    }

    void testDescriptionDoesNotValidateWhen257Characters() {
        dataType.description = 'd'*257
        assertFalse dataType.validate()
    }

    void testUuidValidatesWhenNull() {
        dataType.uuid = null
        assertTrue dataType.validate()
    }

    void testUuidDoesNotValidateWhenNotUnique() {
        def testDataType = new IntentDataType(title: 'test', description: 'test', uuid: 'unique uuid')
        assertFalse testDataType.validate()
    }

    void testUuidIsGeneratedOnSave() {
        dataType.uuid = null
        dataType.save(flush: true)
        assert dataType.uuid != null
    }

    void testEqualsAndHashCode() {
        dataType.save()
        def other = IntentDataType.get(dataType.id)
        assert other == dataType
        assert other.hashCode() == dataType.hashCode()
    }

    void testNotEqual() {
        def other = new IntentDataType(title: 'other')
        assert other != dataType
    }

    void testToString() {
        dataType.save()
        assert dataType.toString() == dataType.title
    }

    void testPrettyPrint() {
        dataType.save()
        assert dataType.prettyPrint() == 'test title'
    }

    void testTitleDisplayShortTitle() {
        dataType.title = '1'
        assert dataType.titleDisplay() == '1'
    }

    void testTitleDisplay12Chars() {
        def twelve = '123456789012'
        dataType.title = twelve
        assert dataType.titleDisplay() == twelve
    }

    void testTitleDisplayShortenedTitle() {
        dataType.title = '1234567890123'
        assert dataType.titleDisplay() == '123456789012...'
    }

    void testAsJSON() {
        def json = dataType.asJSON()
        assert json.toString().indexOf('"id":null') > 0
        assert json.toString().indexOf('"title":"test title"') > 0
        assert json.toString().indexOf('"description":"test description"') > 0
        assert json.toString().indexOf('"uuid":"test uuid"') > 0
    }

    void testAsJSONRef() {
        def json = dataType.asJSONRef()
        assert json.toString().indexOf('"id":null') > 0
        assert json.toString().indexOf('"title":"test title"') > 0
        assert json.toString().indexOf('"uuid":"test uuid"') > 0
    }

    void testFindDuplicates(){
        def testDataType = new IntentDataType(title: "dataType1", uuid: "1234")
        mockDomain(IntentDataType, [testDataType])

        def duplicateUuidDataType = new JSONObject(title: "dataType2", uuid: "1234")
        assertEquals testDataType.uuid, duplicateUuidDataType.uuid
        assertTrue IntentDataType.findDuplicates(duplicateUuidDataType)

        def duplicateTitleDataType = new JSONObject(title: "dataType1", uuid: "4321")
        assertEquals testDataType.title, duplicateTitleDataType.title
        assertTrue IntentDataType.findDuplicates(duplicateTitleDataType)

        def uniqueDataType = new JSONObject(title: "dataType3", uuid: "4321")
        assertFalse(testDataType.uuid == uniqueDataType.uuid)
        assertFalse(testDataType.title == uniqueDataType.title)
        assertFalse IntentDataType.findDuplicates(uniqueDataType)
    }
}
