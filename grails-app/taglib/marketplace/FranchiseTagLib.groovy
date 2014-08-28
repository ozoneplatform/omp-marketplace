package marketplace

import ozone.marketplace.enums.MarketplaceApplicationSetting

class FranchiseTagLib {

    def accountService

    def marketplaceApplicationConfigurationService

    static namespace = "franchise"

    def renderIfScoreCardEnabled = { attrs, body ->
        if (isScoreCardFunctionalityEnabled()) {
            out << body()
        }
    }

    private boolean isScoreCardFunctionalityEnabled() {
        return marketplaceApplicationConfigurationService.is(MarketplaceApplicationSetting.SCORE_CARD_ENABLED)
    }


    def renderIfUserIsFromStoreAgency = { attrs, body ->
        def store = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_NAME)
        if (accountService.isUserFromStoreAgency(store)) {
            out << body()
        }
    }

    def renderIfInsideOutsideAdminSelected = { attrs, body ->
        if (isInsideOutsideAdminSelected()) {
            out << body()
        }
    }

    // Tag to render content that is Marketplace only and not available to a Franchise Store
    def renderMarketplaceOnly = { attrs, body ->
        if (!isFranchiseStoreEnabled()) {
            out << body()
        }
    }

    private boolean isFranchiseStoreEnabled() {
        return marketplaceApplicationConfigurationService.isFranchiseStore()
    }

    private boolean isInsideOutsideAdminSelected() {
        return Constants.INSIDE_OUTSIDE_ADMIN_SELECTED ==
            marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR)

    }
}
