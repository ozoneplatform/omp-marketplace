package marketplace

import grails.test.mixin.TestFor
import marketplace.testutil.FakeAuditTrailHelper
import org.hibernate.classic.Session
import org.hibernate.SessionFactory
import org.junit.Test
import ozone.marketplace.domain.ValidationException

@TestFor(TypesService)
public class TypesServiceTests {

    Types types1
    Types types2


    private static Expando createCountCriteria(Integer count) {
        def criteria = new Expando()
        criteria.get = {Closure -> count}

        return criteria
    }

    public void setUp() {
        FakeAuditTrailHelper.install()

        def sessionMock = mockFor(Session)
        def sessionFactoryMock = mockFor(SessionFactory)

        service.sessionFactory = sessionFactoryMock.createMock()
        sessionMock.demand.setFlushMode(0..99) {}
        sessionFactoryMock.demand.getCurrentSession(0..99) { sessionMock.createMock() }

        types1 = new Types(title: "Types 1")
        types2 = new Types(title: "Types 2")

        mockDomain(Types, [types1, types2])
        mockDomain(ServiceItem)
        mockDomain(CustomFieldDefinition)

    }

    public void testDelete() {
        //mock criteria to return no serviceItems of the given type or cf definition
        ServiceItem.metaClass.static.createCriteria = { createCountCriteria(0) }
        CustomFieldDefinition.metaClass.static.createCriteria = { createCountCriteria(0) }

        service.delete(types1.id)

        assert service.getAllTypes()  == [types2]
    }

    @Test(expected=ValidationException.class)
    public void testDeleteTypesAssociatedWithCustomFieldDefinitionFails() {
        //mock criteria to return a cf definition with the given type
        ServiceItem.metaClass.static.createCriteria = { createCountCriteria(0) }
        CustomFieldDefinition.metaClass.static.createCriteria = { createCountCriteria(1) }

        service.delete(types1.id)
        assert Types.list() as Set == [types1, types2] as Set
    }

    @Test(expected=ValidationException.class)
    public void testDeleteTypesWithAssociatedServiceItemFails() {
        //mock criteria to return a cf definition with the given type
        ServiceItem.metaClass.static.createCriteria = { createCountCriteria(1) }
        CustomFieldDefinition.metaClass.static.createCriteria = { createCountCriteria(0) }

        service.delete(types1.id)
        assert Types.list() as Set == [types1, types2] as Set
    }

    @Test(expected=ValidationException.class)
    public void testDeletePermanentTypeFails() {
        //mock criteria to return no serviceItems of the given type or cf definition
        ServiceItem.metaClass.static.createCriteria = { createCountCriteria(0) }
        CustomFieldDefinition.metaClass.static.createCriteria = { createCountCriteria(0) }

        types1.isPermanent = true
        types1.save()

        service.delete(types1.id)
        assert Types.list() as Set == [types1, types2] as Set
    }

    @Test(expected=ValidationException.class)
    public void testDeleteInvalidObjectFails() {
        //mock criteria to return no serviceItems of the given type or cf definition
        ServiceItem.metaClass.static.createCriteria = { createCountCriteria(0) }
        CustomFieldDefinition.metaClass.static.createCriteria = { createCountCriteria(0) }

        service.delete(-1)
        assert Types.list() as Set == [types1, types2] as Set
    }

    public void testCreateRequired() {
        String requiredTitle = "Required Type"
        grailsApplication.config.marketplace.metadata.types = [[title: requiredTitle,
                                                                description: "",
                                                                ozoneAware: false,
                                                                hasLaunchUrl: false,
                                                                hasIcons: false,
                                                                isPermanent: false]]

        service.createRequired()

        assert Types.list()*.title as Set == [types1.title, types2.title, requiredTitle] as Set
    }

    public void testGetAllTypes() {
        assert service.getAllTypes() as Set == [types1, types2] as Set
    }
}