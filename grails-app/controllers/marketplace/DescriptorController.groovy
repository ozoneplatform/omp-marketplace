package marketplace

import ozone.marketplace.enums.MarketplaceApplicationSetting
import org.codehaus.groovy.grails.web.json.JSONObject

import grails.converters.JSON

import javax.servlet.http.HttpServletResponse

class DescriptorController extends BaseMarketplaceRestController {

    def relationshipService
    def marketplaceApplicationConfigurationService


    private def serviceItemToDescriptor(ServiceItem serviceItem) {
        def descriptorMap = [:]
        if (serviceItem.owfProperties.stackDescriptor && !serviceItem.owfProperties.stackDescriptor.isEmpty()) {
            descriptorMap = serviceItemToStackDescriptor(serviceItem)
        } else {
            descriptorMap = serviceItemToWidgetDescriptor(serviceItem)
        }
        descriptorMap
    }

    private def serviceItemToStackDescriptor(ServiceItem serviceItem) {
        return new JSONObject(serviceItem.owfProperties.stackDescriptor)
    }

    private def serviceItemToWidgetDescriptor(ServiceItem serviceItem) {
        def descriptorMap = [:]

        def intentAsDescriptor = {Intent intent ->
            [
                action: intent.action.title,
                dataTypes: [intent.dataType.title]
            ]
        }

        def sendIntents = serviceItem.owfProperties?.intents?.findAll { it.send  }?.collect {intentAsDescriptor(it)}
        def rcvIntents = serviceItem.owfProperties?.intents?.findAll { it.receive }?.collect {intentAsDescriptor(it)}

        descriptorMap.with {
            widgetUuid = serviceItem.uuid
            widgetGuid = serviceItem.uuid
            displayName = serviceItem.title
            description = serviceItem.description
            widgetVersion = serviceItem.versionName
            widgetUrl = serviceItem.launchUrl
            imageUrlLarge = serviceItem.imageLargeUrl
            imageUrlMedium = serviceItem.imageMediumUrl
            imageUrlSmall = serviceItem.imageSmallUrl

            universalName = serviceItem.owfProperties.universalName
            visible = serviceItem.owfProperties.visibleInLaunch
            singleton = serviceItem.owfProperties.singleton
            background = serviceItem.owfProperties.background
            mobileReady = serviceItem.owfProperties.mobileReady
            height = serviceItem.owfProperties.height
            width = serviceItem.owfProperties.width
            widgetTypes = [serviceItem.owfProperties.owfWidgetType]
            descriptorUrl = serviceItem.owfProperties.descriptorUrl

            defaultTags = serviceItem.categories.collect { it.title }
            editedBy = serviceItem?.editedBy?.username
            editedDte = serviceItem?.editedDate
            intents = [
                    send: sendIntents,
                    receive: rcvIntents
            ]
            directRequired = this.relationshipService.getRequiresItems(serviceItem.id, false, "").collect { it.uuid }
        }

        descriptorMap
    }

    def storeDescriptor = {
        def url = request.getRequestURL().toString() - request.getServletPath()
        def logo = url + '/public/' + marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OPEN_SEARCH_SITE_ICON)
        def name = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_NAME)
        def widgetSize = 200;

        def storeData = [
            height: widgetSize,
            visible: true,
            widgetUrl: url,
            singleton: false,
            imageUrlMedium: logo,
            imageUrlSmall: logo,
            background: false,
            mobileReady: false,
            width: widgetSize,
            widgetTypes: ["marketplace"],
            displayName: name
         ]  as JSONObject

         def json = [status:HttpServletResponse.SC_OK, data:storeData]

        render(view:"/descriptor/descriptor", model:[jsonData:json])
    }

    def getListAsJSON = {
        log.debug "getListAsJSON: params -> " + params
        def results = []
        ServiceItem.list().each {
            if(it.isOWFCompatible() && !it.isHidden()) {
                log.debug "getListAsJSON: search results -> " + it.prettyPrint()
                serviceItemToWidgetDescriptor(it)
            }
        }

        renderResult(results, results.size(), HttpServletResponse.SC_OK)
    }

    def getItemAsJSON = {

        def results = []
        def serviceItem = ServiceItem.findByUuid(params.id)
        if (serviceItem?.isOWFCompatible() && !serviceItem?.isHidden()) {
            log.debug "getItemAsJSON: search results -> " + serviceItem.prettyPrint()
            results << serviceItemToDescriptor(serviceItem)
        }

        renderResult(results, results.size(), HttpServletResponse.SC_OK)
    }
}
