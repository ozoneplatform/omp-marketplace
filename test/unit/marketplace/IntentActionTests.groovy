package marketplace

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(IntentAction)
class IntentActionTests {

    IntentAction defaultAction

    void setUp() {
        FakeAuditTrailHelper.install()

        def existingAction = new IntentAction(title: "existing action", description: "description", uuid: "existing uuid")
        mockForConstraintsTests(IntentAction)
        mockDomain(IntentAction, [existingAction])
        defaultAction = new IntentAction(title: "title", description: "description", uuid: "some uuid")
    }

    void testDefaultPassesValidation() {
        assertTrue defaultAction.validate()
    }

    void testTitleCannotBeNull() {
        defaultAction.title = null
        assertFalse defaultAction.validate()
    }

    void testTitleCannotBeBlank() {
        defaultAction.title = " "
        assertFalse defaultAction.validate()
    }

    void testMinimumTitleLength() {
        defaultAction.title = "t"
        assertTrue defaultAction.validate()
    }

    void testMaximumTitleLength() {
        defaultAction.title = "t"*256
        assertTrue defaultAction.validate()
    }

    void testTitleCannotExceedMaxLength() {
        defaultAction.title = "t"*257
        assertFalse defaultAction.validate()
    }

    void testUuidIsUnique() {
        //this list is for some reason necessary, maybe it flushes
        //the session?
        IntentAction.list()

        defaultAction.uuid = "existing uuid"
        assertFalse defaultAction.validate()
    }

    void testUuidIsNullable() {
        defaultAction.uuid = null
        assertTrue defaultAction.validate()
    }

    void testNeverEqual() {
        def otherAction = new IntentAction(title: "title", description: "description", uuid: "some uuid*")
        defaultAction.save()
        otherAction.save()
        assert defaultAction != otherAction
    }

    void testEquality() {
        defaultAction.save()
        def retrievedAction = IntentAction.get(defaultAction.id)
        assert defaultAction == retrievedAction
        assert defaultAction.hashCode() == retrievedAction.hashCode()
    }

    void testGeneratesUuid() {
        defaultAction.uuid = null
        defaultAction.save(flush: true)
        assert defaultAction.uuid != null
    }

    void testDescriptionIsNullable() {
        defaultAction.description = null
        assertTrue defaultAction.validate()
    }

    void testMaxDescriptionLength() {
        defaultAction.description = "t"*256
        assertTrue defaultAction.validate()
    }

    void testDescriptionCannotExceedMaxLength() {
        defaultAction.description = "t"*257
        assertFalse defaultAction.validate()
    }

    void testFindDuplicates(){
        def testAction = new IntentAction(title: "action1", uuid: "1234")
        mockDomain(IntentAction, [testAction])

        def duplicateUuidAction = new JSONObject(title: "action2", uuid: "1234")
        assertEquals testAction.uuid, duplicateUuidAction.uuid
        assertTrue IntentAction.findDuplicates(duplicateUuidAction)

        def duplicateTitleAction = new JSONObject(title: "action1", uuid: "4321")
        assertEquals testAction.title, duplicateTitleAction.title
        assertTrue IntentAction.findDuplicates(duplicateTitleAction)

        def uniqueAction = new JSONObject(title: "action3", uuid: "4321")
        assertFalse(testAction.uuid == uniqueAction.uuid)
        assertFalse(testAction.title == uniqueAction.title)
        assertFalse IntentAction.findDuplicates(uniqueAction)
    }
}
