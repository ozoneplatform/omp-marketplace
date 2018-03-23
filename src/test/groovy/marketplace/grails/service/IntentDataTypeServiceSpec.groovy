package marketplace.grails.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.IntentDataType
import marketplace.IntentDataTypeService

import ozone.marketplace.domain.ValidationException
import spock.lang.Specification

class IntentDataTypeServiceSpec
        extends Specification
        implements ServiceUnitTest<IntentDataTypeService>, DataTest {

    private static final long BAD_ID = -1L

    void setup() {
        mockDomain(IntentDataType)
    }

    void testCreate() {
        when:
        def params = [title: 'title']
        def dataType = service.create(params)

        then:
        dataType instanceof IntentDataType
    }

    void testLookupById() {
       when:
       def dataType = new IntentDataType(title: 'lookupTitle')
       dataType.save()
       def lookup = service.lookupById(dataType.id)

       then:
       lookup == dataType
    }

    void testLookupByBadId() {
        when:
        def lookup = service.lookupById(BAD_ID)

        then:
        lookup == null
    }

    void testCountTypesEmpty() {
        expect:
        service.countTypes() == 0
    }

    void testCountTypes() {
        when:
        new IntentDataType(title: 'countOne').save()
        new IntentDataType(title: 'countTwo').save(flush: true)

        then:
        service.countTypes() == 2
    }

    void testDelete() {
        given:
        def dataType = new IntentDataType(title: 'deleteTitle').save(flush: true)

        assert IntentDataType.count() == 1

        when:
        def testId = dataType.id
        service.delete(testId)

        then:
        IntentDataType.count() == 0
        !IntentDataType.get(testId)
    }

    void testDeleteOfUnfound() {
        when:
        service.delete(BAD_ID)

        then:
        def ex = thrown(ValidationException)
        ex.message == 'objectNotFound'
    }

//TODO BVBEST come back
//    void testDeleteWithDataIntegrityViolation() {
//        setup:
//        def sessionMock = Mock(Session) {
//            setFlushMode(*_) >> {}
//        }
//        testService.sessionFactory = Mock(SessionFactory) {
//            getCurrentSession(*_) >> { sessionMock}
//        }
//        def dataType = new IntentDataType(title: 'dataIntegrityTitle').save()
//        dataType.metaClass.delete = { Map map ->
//            throw new DataIntegrityViolationException('exception test')
//        }
//        when:
//        testService.delete(dataType.id)
//        then:
//        def ex = thrown(ValidationException)
//        ex.message == 'delete.failure'
//
//    }

    void testGetAllDataTypes() {
        given:
        def lowTitle = 'getAllA'
        new IntentDataType(title: 'getAllZ').save()
        new IntentDataType(title: lowTitle).save(flush: true)

        when:
        def dataTypes = service.getAllDataTypes()

        then:
        dataTypes.size() == 2
        dataTypes[0].title == lowTitle
    }

}
