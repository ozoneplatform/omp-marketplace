package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import ozone.marketplace.domain.ValidationException;

@TestMixin(IntegrationTestMixin)
class StateServiceTests {

	def stateService
	def serviceItemService

	void testSoftDelete() {
		boolean veThrown = false
		try{
			stateService.delete(427358374529952)
		}
		catch(ValidationException ve){
			veThrown = true
			assert 'objectNotFound' == ve.message
		}
		assert true ==  veThrown
	}

    void testParseEditedDate() {
        def params = [:]
        assert null == stateService.parseEditedSinceDate(params)

        params = ['editedSinceDate':'unparseable string']
        assert null == stateService.parseEditedSinceDate(params)

        params = ['editedSinceDate':'2012-03-02T12:58:38Z']
        def r = stateService.parseEditedSinceDate(params)
        assert null != r
        assert true ==  r instanceof Date

        params = ['editedSinceDate':'2012-03-02T12:58:38UTC']
        r = stateService.parseEditedSinceDate(params)
        assert null != r
        assert true ==  r instanceof Date

        params = ['editedSinceDate':'2012-03-02T12:58:38EST']
        r = stateService.parseEditedSinceDate(params)
        assert null != r
        assert true ==  r instanceof Date

        params = ['editedSinceDate':new Date()]
        r = stateService.parseEditedSinceDate(params)
        assert null != r
        assert true ==  r instanceof Date

        params = ['editedSinceDate':new Integer(23)]
        assert null == stateService.parseEditedSinceDate(params)

    }

    void testListByDate() {
        State state = new State(title: 'AAA').save()
        assert null != State.get(state.id)
        assert null != state.createdDate
        def firstDate = state.createdDate

        try {
            Thread.currentThread().sleep(3000)
        } catch (Exception e) {}

        state = new State(title: 'BBB').save()
        assert null != State.get(state.id)
        assert null != state.createdDate
        def secondDate = state.createdDate

        try {
            Thread.currentThread().sleep(1000)
        } catch (Exception e) {}

        def params = ['editedSinceDate':firstDate]
        def r = stateService.list(params)
        assert null != r
        assert 2 == r.size()

        params = ['editedSinceDate':secondDate]
        r = stateService.list(params)
        assert null != r
        assert 1 == r.size()
    }

	void testDelete() {
		State state = new State(title: 'state').save()
		state.save(flush:true)
		assert null != State.get(state.id)
		stateService.delete(state.id)
		State stateAfterSoftDelete = State.get(state.id)
		assert null == stateAfterSoftDelete
	}

    void testAuditTrail() {
        def startTime = new Date()
        Thread.sleep(1000)

        State state = new State(title: 'state').save()
        state.save(flush:true)

        state = State.get(state.id)
        assert null != state
        def createdDate1 = state.createdDate
        def editedDate1 = state.editedDate

        assert state.metaClass.hasProperty(state, "createdBy")
        assert state.metaClass.hasProperty(state, "createdDate")
        assert state.metaClass.hasProperty(state, "editedBy")
        assert state.metaClass.hasProperty(state, "editedDate")

        assert state.createdDate.after(startTime)
        assert state.editedDate.after(startTime)

        state.title = 'bozo5'
        Thread.sleep(1000)
        state.save(flush:true)
        state = State.get(state.id)
        assert state.createdDate.equals(createdDate1)
        assert state.editedDate.after(editedDate1)
    }


	void testCreateRequired(){
        def numStates = State.count()

        // check required states were created during bootstrap as expected.
        assert numStates > 0

        // check no new states are created if createRequired runs again
	    stateService.createRequired()
	    assert numStates == State.count()
	}

	//Check that getAllStates returns records
	void testGetAllStates(){
		assert true ==  stateService.getAllStates().size() > 0
	}
}
