package marketplace

import grails.test.mixin.TestMixin

import ozone.marketplace.domain.ValidationException

@TestFor(IntentDataTypeController)
class IntentDataTypeControllerTests extends MarketplaceAdminControllerTests {
    def mockIntentDataTypeService

    IntentDataTypeControllerTests() {
        super(IntentDataTypeController)
        this.tooLongTitle = "t"*257
    }

    @Override
    void setUp() {
        super.setUp()
        mockIntentDataTypeService = mockFor(IntentDataTypeService)
        mockIntentDataTypeService.demand.with {
            list(0..1) { params -> IntentDataType.list() }
            countTypes(0..1) { -> IntentDataType.count() }
            lookupById(0..1) { id -> IntentDataType.get(id) }
            create(0..1) { params -> new IntentDataType(params) }
        }
        controller.intentDataTypeService = mockIntentDataTypeService.createMock()
    }

    @Override
    protected void mocksForTestDelete() {
        mockIntentDataTypeService.demand.delete(1..1) { id ->  }
    }

    @Override
    protected void mocksForTestDeleteFailure() {
        mockIntentDataTypeService.demand.delete(1..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
    }
}
