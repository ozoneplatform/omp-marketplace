package marketplace

import ozone.marketplace.enums.MarketplaceApplicationSetting;

class AdministrationController extends BaseMarketplaceRestController {


    def index = {
        render(view:'administration', model:[storeLogo:marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_LOGO)])
    }

    def partnerStores = {
        render(view: 'partner-stores')
    }

}
