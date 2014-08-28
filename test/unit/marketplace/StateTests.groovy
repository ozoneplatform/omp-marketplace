package marketplace

import ozone.utils.TestUtil;
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(State)
class StateTests {
    def state

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(State)
        state = new State()
    }

    void testBlankConstraints(){
        TestUtil.assertPropertyBlank('title',state)
    }

    void testNullConstraints(){
        TestUtil.assertPropertyRequired('title',state)
    }

    void testSizeContraints(){
        TestUtil.checkSizeConstraintProperty('title',state, 50)
        TestUtil.checkSizeConstraintProperty('description',state, 250)
    }

    void testFindDuplicates(){
        def testState = new State(title: "state1", uuid: 1234)
        mockDomain(State, [testState])

        def duplicateUuidState = new JSONObject(title: "state2", uuid: "1234")
        assertEquals testState.uuid, duplicateUuidState.uuid
        assertTrue State.findDuplicates(duplicateUuidState)

        def duplicateTitleState = new JSONObject(title: "state1", uuid: "4321")
        assertEquals testState.title, duplicateTitleState.title
        assertTrue State.findDuplicates(duplicateTitleState)

        def uniqueState = new JSONObject(title: "state3", uuid: "4321")
        assertFalse(testState.uuid == uniqueState.uuid)
        assertFalse(testState.title == uniqueState.title)
        assertFalse State.findDuplicates(uniqueState)
    }
}
