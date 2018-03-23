package marketplace.controller

import grails.test.mixin.TestFor
import marketplace.Types
import marketplace.TypesController
import marketplace.TypesService
import ozone.marketplace.domain.ValidationException;


class TypesControllerSpec extends MarketplaceAdminControllerSpec {

    def typesServiceMock

	TypesControllerSpec() {
        super(TypesController.class)
    }

    @Override
    void setUp() {
        Object.setUp()
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
        Object.testDelete()
    }

    @Override
    void testDeleteFailure() {
        typesServiceMock.demand.delete(0..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
        Object.testDeleteFailure()
    }
}
