package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(ScoreCardItem)
class ScoreCardItemTests {
    def scoreCardItem

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(ScoreCardItem)
        scoreCardItem = new ScoreCardItem()
    }

    void testQuestionNotNull() {

		//Test that 'question' fails validation if its blank
		scoreCardItem = new ScoreCardItem()
		assertFalse scoreCardItem.validate()
		assertEquals  'nullable', scoreCardItem.errors['question']
    }

	void testQuestionNotBlank() {
		scoreCardItem = new ScoreCardItem()
        scoreCardItem.question =  "  "
		assertFalse scoreCardItem.validate()
		assertEquals  'blank', scoreCardItem.errors['question']
	}

	void testQuestionSizeContraints(){
		scoreCardItem = new ScoreCardItem()
		TestUtil.checkSizeConstraintProperty('question',scoreCardItem, 250)
	}


	void testDescriptionNotNull() {

		//Test that 'description' fails validation if its blank
		scoreCardItem = new ScoreCardItem()
		assertFalse scoreCardItem.validate()
		assertEquals  'nullable', scoreCardItem.errors['description']

	}

	void testDescriptionNotBlank() {
		scoreCardItem = new ScoreCardItem()
        scoreCardItem.description = "  "
		assertFalse scoreCardItem.validate()
		assertEquals  'blank', scoreCardItem.errors['description']
	}


	void testDescriptionSizeContraints(){
		scoreCardItem = new ScoreCardItem()
		TestUtil.checkSizeConstraintProperty('description',scoreCardItem, 500)
	}
}
