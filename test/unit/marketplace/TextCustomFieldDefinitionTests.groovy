package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(TextCustomFieldDefinition)
class TextCustomFieldDefinitionTests {

	def textCustomFieldDefinition
	def final DEFAULT_STYLE_TYPE = Constants.CustomFieldDefinitionStyleType.TEXT
	def final STYLE_TYPE_INVALID_MSG = 'textCustomFieldDefintion.styleType.notCorrect'


    void setUp() {
        FakeAuditTrailHelper.install()

		mockForConstraintsTests(TextCustomFieldDefinition)
		textCustomFieldDefinition = new TextCustomFieldDefinition()
    }

    void testDefaultStyleTypeSet() {
		assertEquals "Style Type of ${DEFAULT_STYLE_TYPE} Not Set in Default Constructor.", DEFAULT_STYLE_TYPE, textCustomFieldDefinition.styleType
	}

	void testStyleTypeRequired() {
		textCustomFieldDefinition.styleType = null
		TestUtil.assertPropertyRequired('styleType', textCustomFieldDefinition)
	}

	void testStyleTypeValidatorSet(){
		textCustomFieldDefinition.styleType = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
		assertFalse textCustomFieldDefinition.validate()
		assertEquals "styleType should return '${STYLE_TYPE_INVALID_MSG}' when styleType is not '${DEFAULT_STYLE_TYPE}'", STYLE_TYPE_INVALID_MSG, textCustomFieldDefinition.errors['styleType']
	}
}
