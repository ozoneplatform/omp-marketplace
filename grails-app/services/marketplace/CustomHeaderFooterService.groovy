package marketplace

import marketplace.configuration.MarketplaceApplicationConfigurationService

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

class CustomHeaderFooterService {

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    def hasCustomHeader() {
        customHeaderUrl.size() > 0 && customHeaderHeight > 0
    }

    def hasCustomFooter() {
        customFooterUrl.size() > 0 && customFooterHeight > 0
    }

    def hasCustomHeaderOrFooter() {
        hasCustomFooter() || hasCustomHeader()
    }

    def getCssImportsAsList() {
        def cssConfigItem = marketplaceApplicationConfigurationService.getApplicationConfiguration(CUSTOM_CSS_IMPORTS)?.value

        cssConfigItem?.size() > 0 ? cssConfigItem.split(",") : []
    }

    def getJsImportsAsList() {
        def jsConfigItem = marketplaceApplicationConfigurationService.getApplicationConfiguration(CUSTOM_JS_IMPORTS)?.value

        jsConfigItem?.size() > 0 ? jsConfigItem.split(",") : []
    }

    def getCustomHeaderUrl() {
        def headerConfig = marketplaceApplicationConfigurationService.getApplicationConfiguration(CUSTOM_HEADER_URL)?.value

        headerConfig?.size() > 0 ? headerConfig : ""
    }

    def getCustomFooterUrl() {
        def footerConfig = marketplaceApplicationConfigurationService.getApplicationConfiguration(CUSTOM_FOOTER_URL)?.value

        footerConfig?.size() > 0 ? footerConfig : ""

    }

    def getCustomHeaderHeight() {
        def heightConfig = marketplaceApplicationConfigurationService.getApplicationConfiguration(CUSTOM_HEADER_HEIGHT)?.value

        heightConfig?.size() > 0 ? heightConfig.toInteger() : 0
    }

    def getCustomFooterHeight() {
        def heightConfig = marketplaceApplicationConfigurationService.getApplicationConfiguration(CUSTOM_FOOTER_HEIGHT)?.value

        heightConfig?.size() > 0 ? heightConfig.toInteger() : 0
    }
}
