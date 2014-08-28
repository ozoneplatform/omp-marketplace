package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*

@TestMixin(IntegrationTestMixin)
class StateSetupTests {
    //  After BootStrap, there should be 7 states setup...
    void testLoadStates() {
    	State.list().each {println it.id + " " + it.title}
        assert 5 == State.count()
    }

    //-----------------------------
    //After BootStrap... find state
    //-----------------------------
    void performTitleFind(idVal, title){
    	def s = State.findByTitle(title)
    	assert s != null, "State with title:${title} not found, id should've been ${idVal}"
        println "State Found ${s.id}: ${s.title}"
        //Test to make sure title is what was expected
        assert title == s.title
    }

    void testFindActive(){
        println "FindActive"
        performTitleFind(true, "Active")
    }

    void testFindBeta(){
        println "FindBeta"
        performTitleFind(2, "Beta")
    }

    void testFindDeprecated(){
        println "FindDeprecated"
        performTitleFind(3, "Deprecated")
    }

    void testFindPlanned(){
        println "FindPlanned"
        performTitleFind(4, "Planned")
    }

    void testFindRetired(){
        println "FindRetired"
        performTitleFind(5, "Retired")
    }
}
