package marketplace.franchise

import marketplace.BaseMarketplaceRestController
import marketplace.GenericQueryService

import ozone.marketplace.enums.MarketplaceApplicationSetting


class FranchiseReportingController extends BaseMarketplaceRestController {

    GenericQueryService genericQueryService

    static defaultAction = "show"

    def show = {

    }

    //Returns the information about this instance of marketplace/franchise store
    def getStoreAttributes = {

        def model = [:]

        model.put(FranchiseConstants.IS_FRANCHISE_STORE, marketplaceApplicationConfigurationService.isFranchiseStore())
        model.put(FranchiseConstants.STORE_AGENCY, marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_NAME))
        renderResult(model, model.size(), 200)
    }

}
