package marketplace

import grails.gorm.validation.ConstrainedProperty
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import org.grails.web.json.JSONObject
import spock.lang.Specification

class StateSpec extends Specification implements DomainConstraintsUnitTest<State> {
    def state
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

    void setup() {
//        FakeAuditTrailHelper.install()

//        mockForConstraintsTests(State)
        state = new State()
    }

    void testBlankConstraints(){
        expect:
        propertyIsRequired('title')
        propertyValueIsInvalid('title',"", ConstrainedProperty.BLANK_CONSTRAINT)
//        TestUtil.assertPropertyBlank('title',state)
    }

    void testNullConstraints(){
        expect:
        propertyIsRequired('title')
        propertyValueIsInvalid('title',null, ConstrainedProperty.NULLABLE_CONSTRAINT)
//        TestUtil.assertPropertyRequired('title',state)
    }

    void testSizeContraints(){
        expect:
        propertyHasMaxSize('title', 50)
        propertyHasMaxSize('description', 250)
//        TestUtil.checkSizeConstraintProperty('title',state, 50)
//        TestUtil.checkSizeConstraintProperty('description',state, 250)
    }

    void testFindDuplicates(){
        setup:
        def testState = new State(title: "state1", uuid: 1234)
        mockDomain(State, [testState])

        when:
        def duplicateUuidState = new JSONObject(title: "state2", uuid: "1234")
        then:
        assert testState.uuid == duplicateUuidState.uuid
        assert State.findDuplicates(duplicateUuidState)

        when:
        def duplicateTitleState = new JSONObject(title: "state1", uuid: "4321")
        then:
        assert testState.title == duplicateTitleState.title
        assert State.findDuplicates(duplicateTitleState)

        when:
        def uniqueState = new JSONObject(title: "state3", uuid: "4321")
        then:
        assert testState.uuid != uniqueState.uuid
        assert testState.title != uniqueState.title
        assert !State.findDuplicates(uniqueState)
    }
}
