package marketplace

import grails.test.mixin.TestFor

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(Intent)
class IntentTests {
    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(Intent)
    }

    void testBlankIsInvalid() {
        def blankIntent = new Intent()
        assertFalse blankIntent.validate()
    }

    void testValidIsAccepted() {
        def dataType = new IntentDataType(title: "unique title")
        def action = new IntentAction(title: "unique action")
        def validIntent = new Intent(dataType: dataType, action: action, send: true, receive: false)
        assertTrue validIntent.validate()
    }
}
