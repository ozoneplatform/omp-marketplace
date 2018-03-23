package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Rollback
@Integration
class StateSetupTests extends Specification{
    //  After BootStrap, there should be 7 states setup...
    void testLoadStates() {
        when:
    	State.list()
        then:
        5 == State.count()
    }

    //-----------------------------
    //After BootStrap... find state
    //-----------------------------
    void performTitleFind(idVal, title){
    	def s = State.findByTitle(title)
    	assert s != null, "State with title:${title} not found, id should've been ${idVal}"
        //Test to make sure title is what was expected
        assert title == s.title
    }

    void testFindActive(){
        expect:
        performTitleFind(true, "Active")
    }

    void testFindBeta(){
        expect:
        performTitleFind(2, "Beta")
    }

    void testFindDeprecated(){
        expect:
        performTitleFind(3, "Deprecated")
    }

    void testFindPlanned(){
        expect:
        performTitleFind(4, "Planned")
    }

    void testFindRetired(){
        expect:
        performTitleFind(5, "Retired")
    }
}
