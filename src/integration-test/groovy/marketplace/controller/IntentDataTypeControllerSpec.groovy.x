package marketplace.controller

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import marketplace.IntentDataTypeController
import marketplace.grails.controller.MarketplaceAdminControllerSpec

@Integration
@Rollback
class IntentDataTypeControllerSpec extends MarketplaceAdminControllerSpec implements ControllerUnitTest<IntentDataTypeController>{
    def mockIntentDataTypeService

//    IntentDataTypeControllerTests() {
//        super(IntentDataTypeController)
//        this.tooLongTitle = "t"*257
//    }

    @Override
    void setup() {
        //TODO BVEST Revisit
        //super.setup()
//        mockIntentDataTypeService = Mock(IntentDataTypeService) {
//            list(*_) >> {params -> IntentDataType.list()}
//            countTypes(*_) >> { -> IntentDataType.count()}
//            lookupById(*_) >> { id -> IntentDataType.get(id)}
//            create(*_) >> {params -> new IntentDataType(params)}
//        }
//        mockIntentDataTypeService.demand.with {
//            list(0..1) { params -> IntentDataType.list() }
//            countTypes(0..1) { -> IntentDataType.count() }
//            lookupById(0..1) { id -> IntentDataType.get(id) }
//            create(0..1) { params -> new IntentDataType(params) }
//        }
//        controller.intentDataTypeService = mockIntentDataTypeService
    }

    @Override
    protected void mocksForTestDelete() {
//        mockIntentDataTypeService = Mock() {
//            delete(*_) >> { id -> }
//        }
    }

    @Override
    protected void mocksForTestDeleteFailure() {
//        mockIntentDataTypeService = Mock() {
//            delete(*_) >> { id -> throw new ValidationException(message: validationExceptionMessage)}
//        }
//        mockIntentDataTypeService.demand.delete(1..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
    }
}
