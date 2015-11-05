package marketplace

import grails.test.mixin.TestFor
import org.hibernate.SessionFactory
import org.hibernate.classic.Session
import org.springframework.dao.DataIntegrityViolationException
import ozone.marketplace.domain.ValidationException

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(IntentDataTypeService)
class IntentDataTypeServiceTests {
    private static final long BAD_ID = -1L

    IntentDataTypeService testService

    void setUp() {
        FakeAuditTrailHelper.install()

        mockDomain(IntentDataType)
        testService = new IntentDataTypeService()
    }

    void testCreate() {
        def params = [title: 'title']
        def dataType = testService.create(params)
        assert dataType instanceof IntentDataType
    }

    void testLookupById() {
        def dataType = new IntentDataType(title: 'lookupTitle')
        dataType.save()
        def lookup = testService.lookupById(dataType.id)
        assert lookup == dataType
    }

    void testLookupByBadId() {
        def lookup = testService.lookupById(BAD_ID)
        assert lookup == null
    }

    void testCountTypesEmpty() {
        assert testService.countTypes() == 0
    }

    void testCountTypes() {
        new IntentDataType(title: 'countOne').save()
        new IntentDataType(title: 'countTwo').save()
        assert testService.countTypes() == 2
    }

    void testDelete() {
        def dataType = new IntentDataType(title: 'deleteTitle').save()
        def testId = dataType.id
        testService.delete(testId)
        assert IntentDataType.get(testId) == null
    }

    void testDeleteOfUnfound() {
        def message = shouldFail(ValidationException) {
            testService.delete(BAD_ID)
        }
        assert message == 'objectNotFound'
    }

    void testDeleteWithDataIntegrityViolation() {
        def sessionMock = mockFor(Session)
        def sessionFactoryMock = mockFor(SessionFactory)
        sessionMock.demand.setFlushMode {}
        sessionFactoryMock.demand.getCurrentSession { sessionMock.createMock() }
        testService.sessionFactory = sessionFactoryMock.createMock()

        def dataType = new IntentDataType(title: 'dataIntegrityTitle').save()
        dataType.metaClass.delete = { Map map ->
            throw new DataIntegrityViolationException('exception test')
        }

        def message = shouldFail(ValidationException) {
            testService.delete(dataType.id)
        }
        assert message == 'delete.failure'
    }

    void testGetAllDataTypes() {
        def lowTitle = 'getAllA'
        new IntentDataType(title: 'getAllZ').save()
        new IntentDataType(title: lowTitle).save()
        def dataTypes = testService.getAllDataTypes()
        assert dataTypes.size() == 2
        assert dataTypes[0].title == lowTitle
    }
}
