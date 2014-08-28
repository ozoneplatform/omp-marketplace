package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(ItemComment)
class ItemCommentTests {
    def itemComment

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(ItemComment)
        itemComment = new ItemComment()
    }

	void testNullable() {
		itemComment = new ItemComment(text: null, rate: null)

		assertFalse itemComment.validate()

		assertNotSame "rate should be allowed to be nullable.", 'nullable', itemComment.errors['rate']
		assertNotSame "text should be allowed to be nullable.", 'nullable', itemComment.errors['text']
	}

    void testRateMinMaxContraints() {

        itemComment = new ItemComment()
        TestUtil.checkMinMaxConstraintProperty('rate', itemComment, 5, 1)

    }
}
