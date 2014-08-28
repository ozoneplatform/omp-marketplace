package marketplace

import grails.test.mixin.TestMixin

import ozone.marketplace.domain.ValidationException

class IntentActionControllerTests extends MarketplaceAdminControllerTests {

    def intentActionServiceMock

    IntentActionControllerTests() {
        super(IntentActionController.class)
        this.tooLongTitle = "t"*257
    }

    @Override
    void setUp() {
        super.setUp()

        intentActionServiceMock = mockFor(IntentActionService)
        controller.intentActionService = intentActionServiceMock.createMock()
    }

    @Override
    void testDelete() {
        intentActionServiceMock.demand.deleteIntentActionById(0..1) { id ->  }
        super.testDelete()
    }

    @Override
    void testDeleteFailure() {
        intentActionServiceMock.demand.deleteIntentActionById(0..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
        super.testDeleteFailure()
    }
}
