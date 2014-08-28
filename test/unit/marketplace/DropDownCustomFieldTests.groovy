package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(DropDownCustomField)
class DropDownCustomFieldTests {

	def dropDownCustomField

    void setUp() {
        FakeAuditTrailHelper.install()

		mockForConstraintsTests(DropDownCustomField)
		dropDownCustomField = new DropDownCustomField()
    }

	void testValueNotRequired() {
		dropDownCustomField.value = null
		TestUtil.assertPropertyNotRequired('value', dropDownCustomField,  "dropDownCustomField.value should not be required. (nullable:true)")
	}
}
