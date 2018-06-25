package marketplace

import grails.gorm.validation.ConstrainedProperty
import marketplace.grails.domain.DomainConstraintsUnitTest


import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(FieldValue)
class FieldValueSpec extends Specification implements DomainConstraintsUnitTest<FieldValue>{
	List<Class> getDomainClasses() {[FieldValue]}
	Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

	def fieldValue
	static final Integer DISPLAY_TEXT_MAX_SIZE = 255

    void setup() {
//        FakeAuditTrailHelper.install()
//
//		mockForConstraintsTests(FieldValue)
		//fieldValue = new FieldValue()
    }

    void testDisplayTextRequired() {
		expect:
		propertyIsRequired('displayText')
//		TestUtil.assertPropertyRequired('displayText', fieldValue)
    }

	void testDisplayTextNotBlank() {
		expect:
		propertyValueIsInvalid('displayText', '', ConstrainedProperty.BLANK_CONSTRAINT)
//		TestUtil.assertPropertyBlank('displayText', fieldValue)
	}

	void testDisplayTextMaxContraints() {
		expect:
		propertyHasMaxSize('displayText', DISPLAY_TEXT_MAX_SIZE)
//		TestUtil.checkSizeConstraintProperty('displayText', fieldValue, DISPLAY_TEXT_MAX_SIZE, null)
	}
}
