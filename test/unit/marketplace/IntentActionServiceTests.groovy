package marketplace

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.SessionFactory
import org.hibernate.classic.Session
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.springframework.dao.DataIntegrityViolationException
import ozone.marketplace.domain.ValidationException

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(IntentActionService)
class IntentActionServiceTests {

    def service, action, mockGrailsApp

    @Rule
    public final ExpectedException exception = ExpectedException.none()

    @Override
    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(IntentAction)
        mockDomain(IntentAction)

        mockGrailsApp = mockFor(GrailsApplication)

        action =  new IntentAction(title: "title").save()
        assert !action?.hasErrors()

        service = new IntentActionService()
        service.grailsApplication = mockGrailsApp.createMock()
    }

    void testDeleteIntentActionSucceeds() {
        service.deleteIntentActionById(action.id)

        def deletedAction = IntentAction.get(action.id)
        assert !deletedAction
    }

    void testDeleteIntentActionFailsWithInvalidObject() {
        exception.expect(ValidationException)
        service.deleteIntentActionById(-1)
    }

    void testDeleteIntentActionFailsWithDataError() {

        def sessionMock = mockFor(Session)
        def sessionFactoryMock = mockFor(SessionFactory)
        sessionMock.demand.setFlushMode(1) {}
        sessionFactoryMock.demand.getCurrentSession(1) { sessionMock.createMock() }
        service.sessionFactory = sessionFactoryMock.createMock()
        IntentAction.metaClass.delete = { Map map ->
            throw new DataIntegrityViolationException("surprise!")
        }

        exception.expect(ValidationException)
        exception.expectMessage("delete.failure")
        service.deleteIntentActionById(action.id)
    }

    void testCreateRequired() {
        mockGrailsApp.demand.getConfig(0..1) {
            def config = new ConfigObject()
            config.putAll([marketplace: [metadata: [intentActions: [[title: "test required"]]]]])

            config
        }

        service.createRequired()
        assert IntentAction.findByTitle("test required")
    }
}
