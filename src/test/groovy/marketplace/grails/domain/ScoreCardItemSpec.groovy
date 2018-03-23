package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(ScoreCardItem)
class ScoreCardItemSpec extends Specification implements DomainConstraintsUnitTest<ScoreCardItem> {
    def scoreCardItem
	List<Class> getDomainClasses() {[ScoreCardItem]}

	Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    void setup() {
        //FakeAuditTrailHelper.install()

//        mockForConstraintsTests(ScoreCardItem)
        scoreCardItem = new ScoreCardItem()
    }

    void testQuestionNotNull() {
		//Test that 'question' fails validation if its blank
		expect:
		!scoreCardItem.validate()
		propertyValueIsInvalid('question', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
    }

	void testQuestionNotBlank() {
		setup:
        scoreCardItem.question =  "  "
		expect:
		!scoreCardItem.validate()
		propertyValueIsInvalid('question', "", ConstrainedProperty.BLANK_CONSTRAINT)

	}

	void testQuestionSizeContraints(){
		expect:
		propertyHasMaxSize('question', 250)
//		scoreCardItem = new ScoreCardItem()
//		TestUtil.checkSizeConstraintProperty('question',scoreCardItem, 250)
	}


	void testDescriptionNotNull() {
		//Test that 'description' fails validation if its blank
		expect:
		!scoreCardItem.validate()
		propertyValueIsInvalid('description', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
	}

	void testDescriptionNotBlank() {
		setup:
        scoreCardItem.description = "  "

		expect:
		!scoreCardItem.validate()
		propertyValueIsInvalid('description', "", ConstrainedProperty.BLANK_CONSTRAINT)
	}


	void testDescriptionSizeContraints(){
		expect:
		propertyHasMaxSize('description', 500)
//		scoreCardItem = new ScoreCardItem()
//		TestUtil.checkSizeConstraintProperty('description',scoreCardItem, 500)
	}
}
