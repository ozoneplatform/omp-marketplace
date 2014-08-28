package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(FieldValue)
class FieldValueTests {

	def fieldValue
	static final Integer DISPLAY_TEXT_MAX_SIZE = 255

    void setUp() {
        FakeAuditTrailHelper.install()

		mockForConstraintsTests(FieldValue)
		fieldValue = new FieldValue()
    }

    void testDisplayTextRequired() {
		TestUtil.assertPropertyRequired('displayText', fieldValue)
    }

	void testDisplayTextNotBlank() {
		TestUtil.assertPropertyBlank('displayText', fieldValue)
	}

	void testDisplayTextMaxContraints() {
		TestUtil.checkSizeConstraintProperty('displayText', fieldValue, DISPLAY_TEXT_MAX_SIZE, null)
	}
}
