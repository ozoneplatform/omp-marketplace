package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(DropDownCustomFieldDefinition)
class DropDownCustomFieldDefinitionTests {

	DropDownCustomFieldDefinition dropDownCustomFieldDefinition
    FieldValue field1
    FieldValue field2
    FieldValue fieldWithSameTextAs2
    List<FieldValue> fieldList

	def final DEFAULT_STYLE_TYPE = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
	def final STYLE_TYPE_INVALID_MSG = 'dropDownCustomFieldDefinition.styleType.notCorrect'

    void setUp() {
        FakeAuditTrailHelper.install()

		mockForConstraintsTests(DropDownCustomFieldDefinition)
        mockDomain(CustomFieldDefinition)
        mockDomain(FieldValue)
        field1 = new FieldValue(displayText: "field 1")
        field2 = new FieldValue(displayText: "field 2")
        fieldList = [field1, field2]
        fieldWithSameTextAs2 = new FieldValue(displayText: "field 2")
		dropDownCustomFieldDefinition = new DropDownCustomFieldDefinition()
		dropDownCustomFieldDefinition.fieldValues = new ArrayList<FieldValue>()
    }

    void testDefaultStyleTypeSet() {
		assertEquals "Style Type of ${DEFAULT_STYLE_TYPE} Not Set in Default Constructor.", DEFAULT_STYLE_TYPE, dropDownCustomFieldDefinition.styleType
	}

	void testFieldValuesNotRequired() {
		dropDownCustomFieldDefinition.fieldValues = null
		TestUtil.assertPropertyNotRequired('fieldValues', dropDownCustomFieldDefinition,  "dropDownCustomFieldDefinition.fieldValues should not be required. (nullable:true)")
	}

	void testStyleTypeRequired() {
		dropDownCustomFieldDefinition.styleType = null
		TestUtil.assertPropertyRequired('styleType', dropDownCustomFieldDefinition)
	}

	void testStyleTypeValidatorSet(){
		dropDownCustomFieldDefinition.styleType = Constants.CustomFieldDefinitionStyleType.TEXT
		assertFalse dropDownCustomFieldDefinition.validate()
		assert dropDownCustomFieldDefinition.errors['styleType'] != null
	}

    void testFieldValueSafeUpdateCanSetTheListOfFieldValues() {
        initializeDropDownCustomField()

        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)
        assertEquals(1, field1.isEnabled)
        assertEquals(1, field2.isEnabled)
        assert dropDownCustomFieldDefinition.fieldValues as List == fieldList as List
    }

    void testFieldValueSafeUpdateDisablesButDoesNotDeleteAFieldWhenItDoesNotAppearInTheNewValues() {
        initializeDropDownCustomField()
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)

        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1])
        assert dropDownCustomFieldDefinition.fieldValues as List == fieldList as List
        assertEquals(0, field2.isEnabled)
    }

    void testFieldValueSafeUpdateReenablesADisabledFieldWhenItAppearsInTheNewValueList() {
        initializeDropDownCustomField()
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1])

        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)
        assertEquals(1, field2.isEnabled)
    }

    void testFieldValueSafeUpdateTreatsFieldValuesWithEquivalentDisplayTextAsEquivalentFieldValues() {
        initializeDropDownCustomField()
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate(fieldList)
        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1])

        dropDownCustomFieldDefinition.fieldValueListSafeUpdate([field1, fieldWithSameTextAs2])
        assertEquals(1, field2.isEnabled)
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
