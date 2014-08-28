package marketplace

import grails.test.mixin.TestFor

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(IntentDirection)
class IntentDirectionTests {
    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(IntentDirection)
    }

    void testBlankTitleIsInvalid() {
        def blankDirection = new IntentDirection()
        assertFalse blankDirection.validate()
    }

    void testInvalidTitleIsRejected() {
        def invalidDirectionTitle = new IntentDirection(title: "Wrong")
        assertFalse invalidDirectionTitle.validate()
    }

    void testValidTitleIsAccepted() {
        def validDirectionTitle = new IntentDirection(title: "Send")
        assertTrue validDirectionTitle.validate()
    }
}
