package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(ItemComment)
class ItemCommentSpec extends Specification implements DomainConstraintsUnitTest<ItemComment>{
    def itemComment
    List<Class> getDomainClasses() {[ItemComment]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    void setup() {
//        FakeAuditTrailHelper.install()

//        mockForConstraintsTests(ItemComment)
        itemComment = new ItemComment()
    }

	void testNullable() {
        expect:
        propertyValueIsValid('rate', null)
        propertyValueIsValid('text', null)
//		itemComment = new ItemComment(text: null, rate: null)
//
//		assertFalse itemComment.validate()
//
//		assertNotSame "rate should be allowed to be nullable.", 'nullable', itemComment.errors['rate']
//		assertNotSame "text should be allowed to be nullable.", 'nullable', itemComment.errors['text']
	}

    void testRateMinMaxContraints() {
        expect:
        numberPropertyHasMinMaxValue('rate',1,5)
//        itemComment = new ItemComment()
//        TestUtil.checkMinMaxConstraintProperty('rate', itemComment, 5, 1)

    }
}
