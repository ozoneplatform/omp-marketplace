package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(DropDownCustomFieldDefinition)
class DropDownCustomFieldDefinitionSpec  extends Specification implements DomainConstraintsUnitTest<DropDownCustomFieldDefinition> {

	DropDownCustomFieldDefinition dropDownCustomFieldDefinition
    FieldValue field1
    FieldValue field2
    FieldValue fieldWithSameTextAs2
    List<FieldValue> fieldList
    List<Class> getDomainClasses() {[DropDownCustomFieldDefinition,CustomFieldDefinition,FieldValue]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

	def final DEFAULT_STYLE_TYPE = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
	def final STYLE_TYPE_INVALID_MSG = 'dropDownCustomFieldDefinition.styleType.notCorrect'

    void setup() {
        //FakeAuditTrailHelper.install()

		//mockForConstraintsTests(DropDownCustomFieldDefinition)
//        mockDomain(CustomFieldDefinition)
//        mockDomain(FieldValue)
        field1 = new FieldValue(displayText: "field 1")
        field2 = new FieldValue(displayText: "field 2")
        fieldList = [field1, field2]
        fieldWithSameTextAs2 = new FieldValue(displayText: "field 2")
		dropDownCustomFieldDefinition = new DropDownCustomFieldDefinition()
		dropDownCustomFieldDefinition.fieldValues = new ArrayList<FieldValue>()
    }

    void testDefaultStyleTypeSet() {
        expect:
        DEFAULT_STYLE_TYPE == dropDownCustomFieldDefinition.styleType
		//assertEquals "Style Type of ${DEFAULT_STYLE_TYPE} Not Set in Default Constructor.", DEFAULT_STYLE_TYPE, dropDownCustomFieldDefinition.styleType
	}

	void testFieldValuesNotRequired() {
        //TODO BVEST Doesn't like nulling fieldValues
        //expect:
       // propertyValueIsValid('fieldValues', new ArrayList<FieldValue>())
        //TestUtil.assertPropertyNotRequired('fieldValues', dropDownCustomFieldDefinition,  "dropDownCustomFieldDefinition.fieldValues should not be required. (nullable:true)")
	}

	void testStyleTypeRequired() {
        expect:
        propertyValueIsInvalid('styleType', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
//		dropDownCustomFieldDefinition.styleType = null
//		TestUtil.assertPropertyRequired('styleType', dropDownCustomFieldDefinition)
	}

	void testStyleTypeValidatorSet(){
		given:
        dropDownCustomFieldDefinition.styleType = Constants.CustomFieldDefinitionStyleType.TEXT
        expect:
        !dropDownCustomFieldDefinition.validate()
		dropDownCustomFieldDefinition.errors['styleType'] != null
	}

    void testFieldValueSafeUpdateCanSetTheListOfFieldValues() {
        given:
        initializeDropDownCustomField()

        when:
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)

        then:
        1 == field1.isEnabled
        1 == field2.isEnabled
        dropDownCustomFieldDefinition.fieldValues as List == fieldList as List
    }

    void testFieldValueSafeUpdateDisablesButDoesNotDeleteAFieldWhenItDoesNotAppearInTheNewValues() {
        given:
        initializeDropDownCustomField()
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)

        when:
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1])

        then:
        dropDownCustomFieldDefinition.fieldValues as List == fieldList as List
        0 == field2.isEnabled
    }

    void testFieldValueSafeUpdateReenablesADisabledFieldWhenItAppearsInTheNewValueList() {
        given:
        initializeDropDownCustomField()
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1])

        when:
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)

        then:
        1 == field2.isEnabled
    }

    void testFieldValueSafeUpdateTreatsFieldValuesWithEquivalentDisplayTextAsEquivalentFieldValues() {
        given:
        initializeDropDownCustomField()
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1])

        when:
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1, fieldWithSameTextAs2])

        then:
        1 == field2.isEnabled
    }

    private void initializeDropDownCustomField() {
        dropDownCustomFieldDefinition.with {
            styleType = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
            name = "a name"
            label = "a label"
        }
        dropDownCustomFieldDefinition.save()
    }
}
