package marketplace.grails.controller

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DataTest
import grails.testing.mixin.integration.Integration
import grails.testing.web.controllers.ControllerUnitTest
import marketplace.IntentActionController
import marketplace.IntentActionService

@Integration
@Rollback
class IntentActionControllerSpec extends MarketplaceAdminControllerSpec implements DataTest{

//    IntentActionControllerTests() {
//        super(IntentActionController.class)
//        this.tooLongTitle = "t"*257
//    }

    void setup() {
        //super.setup()
        this.tooLongTitle = "t"*257
        controller.intentActionService = Mock(IntentActionService)
    }

    //@Override
    void testDelete() {
        given:
        controller.intentActionService = Mock(IntentActionService) {
            deleteIntentActionById() >> {id -> }
        }
//        intentActionServiceMock.demand.deleteIntentActionById(0..1) { id ->  }
        expect:
        super.testDelete()
    }

//    @Override
//    void testDeleteFailure() {
//        intentActionServiceMock.demand.deleteIntentActionById(0..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
//        super.testDeleteFailure()
//    }
}
