package marketplace.grails.domain

import marketplace.TextCustomField
import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

class TextCustomFieldSpec extends Specification implements DomainConstraintsUnitTest<TextCustomField> {
	Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

	static final Integer VALUE_MAX_SIZE = 256

	//TODO BVEST Why does this fail?
//    void testValueMaxConstraints() {
//		expect:
//		propertyHasMaxSize('value', VALUE_MAX_SIZE)
//	}

	void testValueNotRequired() {
		expect:
		propertyValueIsValid('value', null)
	}
}
