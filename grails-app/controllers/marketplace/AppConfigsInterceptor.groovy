package marketplace

import grails.util.Environment

import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.apache.commons.lang.StringEscapeUtils
import static ozone.marketplace.enums.MarketplaceApplicationSetting.*
import grails.util.Holders
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper
import ozone.marketplace.util.event.LogInEvent

import java.util.concurrent.Callable
import org.ozoneplatform.appconfig.server.service.api.ApplicationConfigurationService
import org.springframework.security.access.AccessDeniedException
import marketplace.*

class AppConfigsInterceptor {

    int order = 50

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    AppConfigsInterceptor() {
        match(action: '*').excludes(action:'(performDoList|image|listAsJSON|setResultUiViewSettings|handle|getSearchResults|delete*|get*|update*)')
    }

    boolean before() { true }

    boolean after() {
        if(controllerName in ['config','preferences','jaxrs', 'theme','itemComment']){
            return true
        }

        if(!model) model = [:]

        model.isFranchiseStore        = marketplaceApplicationConfigurationService.isFranchiseStore()
        model.footerFeaturedText      = marketplaceApplicationConfigurationService.valueOf(STORE_FOOTER_FEATURED_TITLE)
        model.footerFeaturedContent   = marketplaceApplicationConfigurationService.valueOf(STORE_FOOTER_FEATURED_CONTENT)
        model.logoUrl                 = marketplaceApplicationConfigurationService.valueOf(STORE_LOGO)
        model.openSearchTitleMessage = marketplaceApplicationConfigurationService.valueOf(OPEN_SEARCH_TITLE_MESSAGE)
        model.allowImageUpload = marketplaceApplicationConfigurationService.valueOf(ALLOW_IMAGE_UPLOAD)
        model.contactEmailAddress = marketplaceApplicationConfigurationService.valueOf(STORE_CONTACT_EMAIL)
        model.defaultAffiliatedMarketplaceTimeout = marketplaceApplicationConfigurationService.valueOf(AMP_SEARCH_DEFAULT_TIMOUT)
        model.ownerCanEditTheirApprovedListings = marketplaceApplicationConfigurationService.valueOf(ALLOW_OWNER_TO_EDIT_APPROVED_LISTING)
        true
    }

    void afterView() {
        // no-op
    }
}
