package marketplace

import grails.test.mixin.TestFor

import ozone.marketplace.domain.ValidationException

@TestFor(StateController)
class StateControllerTests extends MarketplaceAdminControllerTests {

    def stateServiceMock

    StateControllerTests() {
        super(StateController.class)
    }

    @Override
    void setUp() {
        super.setUp()

        stateServiceMock = mockFor(StateService)
        controller.stateService =  stateServiceMock.createMock()
        stateServiceMock.demand.with {
            list(0..1) { params -> State.list() }
            countTypes(0..1) { -> State.count() }
        }
        controller.metaClass.postUpdateDomain = { -> }
    }

    @Override
    void testDelete() {
        stateServiceMock.demand.delete(0..1) { id -> }
        super.testDelete()
    }

    @Override
    void testDeleteFailure() {
        stateServiceMock.demand.delete(0..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
        super.testDeleteFailure()
    }
}
