package marketplace

import org.apache.commons.lang.exception.ExceptionUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.apache.commons.lang.StringUtils
import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.marketplace.enums.RelationshipType
import ozone.utils.Utils;

import grails.converters.JSON

class RelationshipController extends BaseMarketplaceRestController {
    def config = ConfigurationHolder.config

    def genericQueryService
    def relationshipService
    def serviceItemService

    def index = {}

	def getListings = {
		def opts = [:]
		opts.max = params.max ?: 10
		opts.offset = params.offset ?: 0

		if(StringUtils.isNotEmpty(params.query) && (!params.query.equals(message(code: "relationship.searchEmptyText")))){
			opts.title = "*" + params.query + "*"
		}

		try{
			if (params.id){
				//def parent = ServiceItem.findById(params.id)
				opts.id = "<> " + params.id
				//relatedItems = Relationship.findByOwningEntityAndRelationshipType(parent, RelationshipType.REQUIRE)?.relatedItems ?: []
			}
			def itemsModel = genericQueryService.serviceItems(opts)
			def items = itemsModel['serviceItemList']
			def totalItems = itemsModel['listSize']
			def sizeOfItems = items.size()
			def numberOfListingsMessage
			if (sizeOfItems < totalItems){
				numberOfListingsMessage = message(code: "message.NumberOfListingsDisplayedRefineSearch", args: [sizeOfItems,totalItems])
			}
			else{
				numberOfListingsMessage = message(code: "message.NumberOfListingsDisplayed", args: [sizeOfItems])
			}

            render ([
                success: true,
                totalCount: totalItems,
                msg: numberOfListingsMessage,
                data: items.collect { si ->
                    def imageUrl = si.imageSmallUrl ?: getImageUrl(si.id, request)
                    return [
                        id: si.id,
                        title: si.title,
                        imageURL: imageUrl,
                        versionName: si.versionName
                    ]
                }
            ] as JSON)
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error("The following error has occurred: ${message}", e)
            render ([
                success: false,
                totalCount: 0,
                msg: e.getMessage()
            ] as JSON)
        }
    }

    /**
     * Obtains best available image URL for the given service item
     * @param serviceItemId
     * @param request
     * @return
     */
    String getImageUrl(long serviceItemId, def request) {
        def params = [serviceItemId: serviceItemId, getSmallUrl: true, contextPath: request.contextPath]
        params.serviceItem = (ServiceItem) ServiceItem.get(serviceItemId)
        def imageMap = serviceItemService.getServiceItemIconImage(params)
        def imageUrl = imageMap.url
        return imageUrl
    }

	def getRelatedItems = {
		try{
			def items = []
			if (params.id){
				def parent = serviceItemService.getServiceItem(params.id)
				Relationship rel = Relationship.findByOwningEntityAndRelationshipType(parent,RelationshipType.REQUIRE)
				if (rel){
					items = rel.relatedItems?.findAll { it != null }
				}
			}
            def imageUrl
			render ([
                success: true,
				totalCount: items.size(),
				data: items.collect { si ->
                    imageUrl = si.imageSmallUrl ?: getImageUrl(si.id, request)
                    return [
                        id: si.id,
                        title: si.title,
                        imageURL: imageUrl,
                        versionName: si.versionName
                    ]
                }
             ] as JSON)
		}
		catch (Exception e){
			render ([
				success: false,
				msg: e.getMessage()
			] as JSON)
		}
	}

    /**
     * Same as the above method except that only items that satisfy the OWF specific criteria
     * are returned.
     */
    def getOWFRequiredItems = {
        try {
            log.debug "getRequiredItems: params = ${params}"
            def model = relationshipService.getRequiredItems(params.id, session.isAdmin, session.username,
                { itemIn -> (itemIn.isOWFCompatible() && !itemIn.isHidden()) })
            //TODO: remove after OWF is fixed to accept descriptions longer than 255 characters
            if (params.version == '6.0.1-GA-v1') {
                model = model.collect { def json ->
                    if (json.description?.size() > 255) json.description = Utils.ellipsizeString(json.description, 255)
                    if (json.versionName == null) json.versionName = ''
                    json
                }
            }

            model = model.collect { def json ->
              if(json.versionName == null) json.versionName = ''
              json
            }
            def total = model.size()
            renderResult(model, total, 200)
        }
        catch (AccessControlException ae) {
            handleExpectedException(ae, "getOWFRequiredItems", 403)
        }
        catch (Exception e) {
            handleException(e, "getRequiredItems")
        }
    }
}
