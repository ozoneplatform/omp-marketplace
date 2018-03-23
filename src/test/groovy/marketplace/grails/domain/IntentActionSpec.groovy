package marketplace

import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import org.grails.web.json.JSONObject
//import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(IntentAction)
class IntentActionSpec extends Specification implements DomainConstraintsUnitTest<IntentAction>{
    List<Class> getDomainClasses() {[IntentAction]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    IntentAction defaultAction

    void setup() {
//        FakeAuditTrailHelper.install()

        def existingAction = new IntentAction(title: "existing action", description: "description", uuid: "existing uuid")
//        mockForConstraintsTests(IntentAction)
//        mockDomain(IntentAction, [existingAction])
        defaultAction = new IntentAction(title: "title", description: "description", uuid: "some uuid")
    }

    void testDefaultPassesValidation() {
        expect:
        defaultAction.validate()
    }

    void testTitleCannotBeNull() {
        when:
        defaultAction.title = null
        then:
        !defaultAction.validate()
    }

    void testTitleCannotBeBlank() {
        when:
        defaultAction.title = ""
        then:
        !defaultAction.validate()
    }

    void testMinimumTitleLength() {
        when:
        defaultAction.title = "t"
        then:
        defaultAction.validate()
    }

    void testMaximumTitleLength() {
        expect:
        propertyHasMaxSize('title', 256)
    }

//    void testTitleCannotExceedMaxLength() {
//        defaultAction.title = "t"*257
//        assertFalse defaultAction.validate()
//    }

    void testUuidIsUnique() {
        given:
        def existingAction = new IntentAction(title: "existing action", description: "description", uuid: "existing uuid")

        when:
        existingAction.save()
        then:
        IntentAction.count() == old(IntentAction.count()) + 1

        when:
        defaultAction.uuid = "existing uuid"

        then:
        !defaultAction.validate()
        IntentAction.count() == old(IntentAction.count())
    }

    void testUuidIsNullable() {
        expect:
        propertyValueIsValid('uuid', null)
//        defaultAction.uuid = null
//        assertTrue defaultAction.validate()
    }

    void testNeverEqual() {
        when:
        def otherAction = new IntentAction(title: "title", description: "description", uuid: "some uuid*")

        then:
        defaultAction.save()
        IntentAction.count() == old(IntentAction.count()) + 1
        otherAction.save()

        assert defaultAction != otherAction
    }

    void testEquality() {
        when:
        defaultAction.save()
        def retrievedAction = IntentAction.get(defaultAction.id)

        then:
        assert defaultAction == retrievedAction
        assert defaultAction.hashCode() == retrievedAction.hashCode()
    }

    void testGeneratesUuid() {
        when:
        defaultAction.uuid = null
        defaultAction.save(flush: true)

        then:
        assert defaultAction.uuid != null
    }

    void testDescriptionIsNullable() {
        expect:
        propertyValueIsValid('description', null)
//        defaultAction.description = null
//        assertTrue defaultAction.validate()
    }

    void testMaxDescriptionLength() {
        expect:
        propertyHasMaxSize('description', 256)
//        defaultAction.description = "t"*256
//        assertTrue defaultAction.validate()
    }

//    void testDescriptionCannotExceedMaxLength() {
//        defaultAction.description = "t"*257
//        assertFalse defaultAction.validate()
//    }

    void testFindDuplicates(){
        def testAction = new IntentAction(title: "action1", uuid: "1234")
        testAction.save()
//        mockDomain(IntentAction, [testAction])

        when:
        def duplicateUuidAction = new JSONObject(title: "action2", uuid: "1234")

        then:
        assert testAction.uuid == duplicateUuidAction.uuid
        assert IntentAction.findDuplicates(duplicateUuidAction) == true

        when:
        def duplicateTitleAction = new JSONObject(title: "action1", uuid: "4321")
        then:
        assert testAction.title == duplicateTitleAction.title
        assert IntentAction.findDuplicates(duplicateTitleAction) == true

        when:
        def uniqueAction = new JSONObject(title: "action3", uuid: "4321")
        then:
        assert testAction.uuid != uniqueAction.uuid
        assert testAction.title != uniqueAction.title
        assert IntentAction.findDuplicates(uniqueAction) == false
    }
}
