package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(CustomFieldDefinition)
class CustomFieldDefinitionTests {
    def customFieldDefinition

    void setUp() {
        FakeAuditTrailHelper.install()
    }

    void testBlankConstraints(){
        mockForConstraintsTests(CustomFieldDefinition)
        customFieldDefinition = new CustomFieldDefinition()
        TestUtil.assertPropertyBlank('name',customFieldDefinition)
        TestUtil.assertPropertyBlank('label',customFieldDefinition)
    }

    void testSizeContraints(){
        mockForConstraintsTests(CustomFieldDefinition)
        customFieldDefinition = new CustomFieldDefinition()
        TestUtil.checkSizeConstraintProperty('name',customFieldDefinition, 50)
        TestUtil.checkSizeConstraintProperty('label',customFieldDefinition, 50)
        TestUtil.checkSizeConstraintProperty('tooltip',customFieldDefinition, 50)
    }

    void testBelongsToType(){
        mockForConstraintsTests(CustomFieldDefinition)
        customFieldDefinition = new CustomFieldDefinition()
        Types myType = new Types(id: 1, title:'My type')
        Types notMyType = new Types(id: 2, title:'Not my type')
        def cfd = new CustomFieldDefinition(types: [myType], label: 'My Label')
        assertTrue cfd.belongsToType(myType)
        assertFalse cfd.belongsToType(notMyType)
    }

    void testFindDuplicates(){
        def testDropDownCFD = new DropDownCustomFieldDefinition(
            uuid: "1234",
            label: 'label1',
            name: "name1",
            styleType: Constants.CustomFieldDefinitionStyleType.DROP_DOWN,
            isMultiSelect: true,
            fieldValues: [new FieldValue(displayText: 'value1')]
        )

        def testTextCFD = new TextCustomFieldDefinition(
            uuid: "4321",
            name: "name1",
            label: 'label2',
            styleType: Constants.CustomFieldDefinitionStyleType.TEXT
        )

        mockDomain(DropDownCustomFieldDefinition)
        mockDomain(TextCustomFieldDefinition)
        [testTextCFD, testDropDownCFD].each { it.save(failOnError: true) }

        def duplicateUuidCFD = new JSONObject(uuid: "1234")
        assertEquals testDropDownCFD.uuid, duplicateUuidCFD.uuid
        assertTrue CustomFieldDefinition.findDuplicates(duplicateUuidCFD)

        def duplicateTextCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "TEXT")
        assertEquals testTextCFD.name, duplicateTextCFD.name
        assertEquals testTextCFD.styleType.styleTypeName, Constants.CustomFieldDefinitionStyleType."${duplicateTextCFD.fieldType}".styleTypeName
        assertTrue CustomFieldDefinition.findDuplicates(duplicateTextCFD)

        def uniqueFieldTypeCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "TEXT_AREA")
        assertEquals testTextCFD.name, uniqueFieldTypeCFD.name
        assertTrue(testTextCFD.styleType.styleTypeName != Constants.CustomFieldDefinitionStyleType."${uniqueFieldTypeCFD.fieldType}".styleTypeName)
        assertFalse CustomFieldDefinition.findDuplicates(uniqueFieldTypeCFD)

        def uniqueDropDownCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "DROP_DOWN", isMultiSelect: false)
        assertEquals testDropDownCFD.name, uniqueDropDownCFD.name
        assertEquals testDropDownCFD.styleType.styleTypeName, Constants.CustomFieldDefinitionStyleType."${uniqueDropDownCFD.fieldType}".styleTypeName
        assertTrue(testDropDownCFD.isMultiSelect != uniqueDropDownCFD.isMultiSelect)
        assertFalse CustomFieldDefinition.findDuplicates(uniqueDropDownCFD)

        def duplicateDropDownCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "DROP_DOWN", isMultiSelect: true)
        assertEquals testDropDownCFD.name, duplicateDropDownCFD.name
        assertEquals testDropDownCFD.styleType.styleTypeName, Constants.CustomFieldDefinitionStyleType."${duplicateDropDownCFD.fieldType}".styleTypeName
        assertEquals testDropDownCFD.isMultiSelect, duplicateDropDownCFD.isMultiSelect
        assertTrue CustomFieldDefinition.findDuplicates(duplicateDropDownCFD)
    }
}
