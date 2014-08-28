package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(TextCustomField)
class TextCustomFieldTests {

	def textCustomField
	static final Integer VALUE_MAX_SIZE = 256


    void setUp() {
        FakeAuditTrailHelper.install()

		mockForConstraintsTests(TextCustomField)
		textCustomField = new TextCustomField()
    }

    void testValueMaxContraints() {
		TestUtil.checkSizeConstraintProperty('value', textCustomField, VALUE_MAX_SIZE, null)
	}

	void testValueNotRequired() {
		textCustomField.value = null
		TestUtil.assertPropertyNotRequired('value', textCustomField,  "textCustomField.value should not be required. (nullable:true)")
	}
}
