package marketplace

import grails.gorm.validation.ConstrainedProperty
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

class TextCustomFieldDefinitionSpec extends Specification implements DomainConstraintsUnitTest<TextCustomFieldDefinition> {
	Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

	def textCustomFieldDefinition
	def final DEFAULT_STYLE_TYPE = Constants.CustomFieldDefinitionStyleType.TEXT
	def final STYLE_TYPE_INVALID_MSG = 'textCustomFieldDefintion.styleType.notCorrect'


    void setup() {
		textCustomFieldDefinition = new TextCustomFieldDefinition()
    }

    void testDefaultStyleTypeSet() {
		expect:
		initializedPropertyIsRequired('styleType', DEFAULT_STYLE_TYPE)
	}

	void testStyleTypeRequired() {
		expect:
		propertyValueIsInvalid('styleType', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
	}

	void testStyleTypeValidatorSet(){
		given:
		textCustomFieldDefinition.styleType = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
		expect:
		!textCustomFieldDefinition.validate()
		assert null != textCustomFieldDefinition.errors['styleType'].codes.find { it == STYLE_TYPE_INVALID_MSG}
	}
}
