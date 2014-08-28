package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

/**
 * Created by IntelliJ IDEA.
 * User: tpanning
 * Date: 4/17/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
@TestMixin(IntegrationTestMixin)
class IntentDirectionServiceTests {

    def intentDirectionService

    void testMarketplaceRequiredIntentDirectionsCreated() {
        def numDirections = IntentDirection.count()

        // check that a direction was created for each title
        assert IntentDirection.DIRECTION_TITLES.size() == IntentDirection.count()

        // check no new directions are created if createRequired runs again
        intentDirectionService.createRequired()
        assert numDirections == IntentDirection.count()
    }
}
