package marketplace

import grails.core.GrailsApplication
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import ozone.marketplace.domain.ValidationException
import spock.lang.Specification

//@TestFor(IntentActionService)
class IntentActionServiceSpec extends Specification implements ServiceUnitTest<IntentActionService>, DataTest{

    IntentActionService service
    IntentAction action
    def mockGrailsApp

    @Rule
    public final ExpectedException exception = ExpectedException.none()

    void setup() {
        //FakeAuditTrailHelper.install()

       // mockForConstraintsTests(IntentAction)
        mockDomain(IntentAction)

//        mockGrailsApp = mockFor(GrailsApplication)

        action =  new IntentAction(title: "title").save()
        assert !action?.hasErrors()

        service = new IntentActionService(grailsApplication: Mock(GrailsApplication))
        //service.grailsApplication = Mock(GrailsApplication)
    }

    //TODO BVEST Revisit
//    void testDeleteIntentActionSucceeds() {
//        println('B: ' + action.id)
//        when:
//      //  action.delete()
//        service.deleteIntentActionById(action.id)
//
//        def deletedAction = IntentAction.get(action.id)
//        then:
//        assert !deletedAction
//    }

    void testDeleteIntentActionFailsWithInvalidObject() {
//        exception.expect(ValidationException)
        when:
        service.deleteIntentActionById(-1)
        then:
        thrown(ValidationException)
    }

    //TODO BVEST Revisit
//    void testDeleteIntentActionFailsWithDataError() {
//        setup:
////        def sessionMock = Mock(Session) {
////
////        }
////        def sessionFactoryMock = Mock(SessionFactory)
////        sessionMock.demand.setFlushMode(1) {}
////        sessionFactoryMock.demand.getCurrentSession(1) { sessionMock.createMock() }
//        service.sessionFactory = Mock(SessionFactory) {
//            getCurrentSession(1) >> Mock(Session) {
//                setFlushMode(1) >> {}
//            }
////            sessionFactoryMock.createMock()
//        }
//        Mock(IntentAction) {
//            delete(_) >> { Map map -> throw new DataIntegrityViolationException('surprise!')}
//        }
////        IntentAction.metaClass.delete = { Map map ->
////            throw new DataIntegrityViolationException("surprise!")
////        }
//        when:
//        service.deleteIntentActionById(action.id)
//
//        then:
//        thrown(ValidationException)
//
////        exception.expect(ValidationException)
////        exception.expectMessage("delete.failure")
////        service.deleteIntentActionById(action.id)
//    }

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
