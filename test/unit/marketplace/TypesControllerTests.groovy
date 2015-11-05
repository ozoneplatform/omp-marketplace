package marketplace

import grails.test.mixin.TestFor

import ozone.marketplace.domain.ValidationException;

@TestFor(TypesController)
class TypesControllerTests extends MarketplaceAdminControllerTests {

    def typesServiceMock

	TypesControllerTests() {
        super(TypesController.class)
    }

    @Override
    void setUp() {
        super.setUp()
        typesServiceMock = mockFor(TypesService)
        controller.typesService =  typesServiceMock.createMock()
        typesServiceMock.demand.with {
            list(0..1) { params -> Types.list() }
            countTypes(0..1) { -> Types.count() }
            getItemFromParams(0..1) { params, request -> [domain: Types.get(params.id), isNewVersion: false] }
            buildItemFromParams(0..1) {params, request -> new Types(params) }
        }
        controller.metaClass.postUpdateDomain = { -> }
    }

    @Override
    void testDelete() {
        typesServiceMock.demand.delete(0..1) { id -> }
        super.testDelete()
    }

    @Override
    void testDeleteFailure() {
        typesServiceMock.demand.delete(0..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
        super.testDeleteFailure()
    }
}
