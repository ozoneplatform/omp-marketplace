package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.IntentDirection
import spock.lang.Specification

/**
 * Created by IntelliJ IDEA.
 * User: tpanning
 * Date: 4/17/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Integration
@Rollback
class IntentDirectionServiceTests extends Specification{

    def intentDirectionService

    void testMarketplaceRequiredIntentDirectionsCreated() {
        when:
        def numDirections = IntentDirection.count()

        // check that a direction was created for each title
        then:
        IntentDirection.DIRECTION_TITLES.size() == IntentDirection.count()

        // check no new directions are created if createRequired runs again
        when:
        intentDirectionService.createRequired()
        then:
        numDirections == IntentDirection.count()
    }
}
