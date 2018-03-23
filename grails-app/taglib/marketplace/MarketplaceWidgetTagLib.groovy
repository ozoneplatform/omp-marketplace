package marketplace

import grails.converters.*
import ozone.marketplace.enums.MarketplaceApplicationSetting

class MarketplaceWidgetTagLib {

    static namespace = "mpwidget"

    ServiceItemService serviceItemService

    private def isInWidget() {
        return params.widget || session.widget
    }

    private boolean isInAmlWidget() {
        return true  //There was a db param for this:  is(MarketplaceApplicationSetting.FRANCHISE_UI_ENABLED) but its been removed
    }

    def isWidgetMode = { attrs, body ->
        return isInWidget()
    }

    def doInWidget = { attrs, body ->
        if (isInWidget()) {
            out << body()
        }
    }

    def skipInWidget = { attrs, body ->
        if (!isInWidget()) {
            out << body()
        }
    }

    def skipInAmlWidget = { attrs, body ->
        if (!isInAmlWidget()) {
            out << body()
        }
    }
    def doInAmlWidget = { attrs, body ->
        if (isInAmlWidget()) {
            out << body()
        }
    }

    def ifOwfCompatibleListing = { attrs, body ->
        def listingId = attrs.id
        ServiceItem listing = serviceItemService.getServiceItem(listingId)
        if (listing.isOWFCompatible() && !listing.isHidden()) {
            out << body()
        }
    }

    def getOwfCompatible = { attrs ->
        def listingId = attrs.id
        ServiceItem listing = serviceItemService.getServiceItem(listingId)
        out << (listing.isOWFCompatible() && !listing.isHidden())
    }

    def getBaseUrl = { attrs ->
        def marketplaceUrl
        if (request.requestURL) {
            marketplaceUrl = new String(request.requestURL)
            marketplaceUrl = marketplaceUrl.substring(0, marketplaceUrl.indexOf(new String(request.requestURI)))
            marketplaceUrl += request.contextPath
        } else {
            marketplaceUrl = g.resource(dir: request.contextPath, absolute: true)
        }
        out << marketplaceUrl
    }
}
