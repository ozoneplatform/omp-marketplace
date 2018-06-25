package marketplace.grails.domain

import grails.gorm.validation.ConstrainedProperty
import marketplace.Constants
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomFieldDefinition
import marketplace.FieldValue
import marketplace.TextCustomFieldDefinition
import marketplace.Types
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import org.grails.web.json.JSONObject
import spock.lang.Specification

class CustomFieldDefinitionSpec extends Specification implements DomainConstraintsUnitTest<CustomFieldDefinition> {
    List<Class> getDomainClasses() {[CustomFieldDefinition,TextCustomFieldDefinition,DropDownCustomFieldDefinition]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

    void testBlankConstraints(){
        expect:
        propertyValueIsInvalid('name', "", ConstrainedProperty.BLANK_CONSTRAINT)
        propertyValueIsInvalid('label', "", ConstrainedProperty.BLANK_CONSTRAINT)
    }

    void testSizeContraints(){
        expect:
        propertyHasMaxSize('name', 50)
        propertyHasMaxSize('label', 50)
        propertyHasMaxSize('tooltip', 50)
    }

    void testBelongsToType(){
        given:
        Types myType = new Types(id: 1, title:'My type')
        Types notMyType = new Types(id: 2, title:'Not my type')
        def cfd = new CustomFieldDefinition(types: [myType], label: 'My Label')

        expect:
        cfd.belongsToType(myType) == true
        cfd.belongsToType(notMyType) == false
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

        mockDomain(DropDownCustomFieldDefinition, [testDropDownCFD])
        mockDomain(TextCustomFieldDefinition, [testTextCFD])
        [testTextCFD, testDropDownCFD].each { it.save(failOnError: true) }

        when:
        def duplicateUuidCFD = new JSONObject(uuid: "1234")
        then:
        testDropDownCFD.uuid == duplicateUuidCFD.uuid
        CustomFieldDefinition.findDuplicates(duplicateUuidCFD) == true

        when:
        def duplicateTextCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "TEXT")
        then:
        testTextCFD.name == duplicateTextCFD.name
        testTextCFD.styleType.styleTypeName == Constants.CustomFieldDefinitionStyleType."${duplicateTextCFD.fieldType}".styleTypeName
        CustomFieldDefinition.findDuplicates(duplicateTextCFD) == true

        when:
        def uniqueFieldTypeCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "TEXT_AREA")
        then:
        testTextCFD.name == uniqueFieldTypeCFD.name
        testTextCFD.styleType.styleTypeName != Constants.CustomFieldDefinitionStyleType."${uniqueFieldTypeCFD.fieldType}".styleTypeName
        CustomFieldDefinition.findDuplicates(uniqueFieldTypeCFD) == false

        when:
        def uniqueDropDownCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "DROP_DOWN", isMultiSelect: false)
        then:
        testDropDownCFD.name == uniqueDropDownCFD.name
        testDropDownCFD.styleType.styleTypeName == Constants.CustomFieldDefinitionStyleType."${uniqueDropDownCFD.fieldType}".styleTypeName
        testDropDownCFD.isMultiSelect != uniqueDropDownCFD.isMultiSelect
        CustomFieldDefinition.findDuplicates(uniqueDropDownCFD) == false

        when:
        def duplicateDropDownCFD = new JSONObject(uuid: "9876", name: "name1", fieldType: "DROP_DOWN", isMultiSelect: true)
        then:
        testDropDownCFD.name == duplicateDropDownCFD.name
        testDropDownCFD.styleType.styleTypeName == Constants.CustomFieldDefinitionStyleType."${duplicateDropDownCFD.fieldType}".styleTypeName
        testDropDownCFD.isMultiSelect == duplicateDropDownCFD.isMultiSelect
        CustomFieldDefinition.findDuplicates(duplicateDropDownCFD) == true
    }
}
