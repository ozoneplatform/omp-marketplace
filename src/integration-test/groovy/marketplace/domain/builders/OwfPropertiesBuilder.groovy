package marketplace.domain.builders

import marketplace.OwfProperties
import marketplace.ServiceItem


class OwfPropertiesBuilder implements Builder<OwfProperties> {

    String owfWidgetType
    Boolean singleton
    Boolean visibleInLaunch
    Boolean background
    Boolean mobileReady
    String universalName
    String stackContext

    ServiceItem serviceItem

    OwfProperties build() {
        if (!owfWidgetType) owfWidgetType = OwfProperties.DEFAULT_WIDGET_TYPE
        if (!singleton) singleton = false
        if (!visibleInLaunch) visibleInLaunch = true
        if (!background) background = false
        if (!mobileReady) mobileReady = false

        new OwfProperties([owfWidgetType  : owfWidgetType,
                           singleton      : singleton,
                           visibleInLaunch: visibleInLaunch,
                           background     : background,
                           mobileReady    : mobileReady,
                           universalName  : universalName,
                           serviceItem    : serviceItem,
                           stackContext   : stackContext])
    }

}
