package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.commons.ApplicationHolder
import gorm.AuditStamp

@AuditStamp
class AffiliatedMarketplace implements Serializable {

    Images icon
    String name
    String serverUrl
    Long timeout

    /** Deactivate to avoid searching this marketplace **/
    Integer active = 1

    static mapping = {
        cache true
        icon cascade: "delete"
    }

    static constraints = {
        name(blank: false, nullable: false, maxSize: 50)
        timeout(blank: true, nullable: true)
        icon(nullable: true)
        active(nullable: false)
        serverUrl(blank: false, nullable: false, maxSize: Constants.MAX_URL_SIZE, validator: { val, obj ->
            if (!val || 0 == val.trim().size()) {
                return [
                    'affiliatedMarketplace.serverUrl.required'
                ]
            }
            if (val != null && val.trim().size() > 0 && !val.matches(/(.*)(:\/\/)(.*)/)) {
                return [
                    'affiliatedMarketplace.serverUrl.invalid'
                ]
            }
        })
    }

    String toString() { "$id $name : $serverUrl" }

    String prettyPrint() {
        toString()
    }

    protected getAffiliatedMarketplaceService() {
        ApplicationHolder.application.mainContext.affiliatedMarketplaceService
    }

    def asJSON(def contextPath, def isHtmlEncoded = false) {
        def iconImageMap = getAffiliatedMarketplaceService().getMarketplaceIconImage([contextPath: contextPath, iconId: icon?.id])
        return new JSONObject(
            id: id,
            name: isHtmlEncoded ? name.encodeAsHTML() : name,
            serverUrl: isHtmlEncoded ? serverUrl.encodeAsHTML() : serverUrl,
            timeout: timeout,
            icon: new JSONObject(
                url: "${iconImageMap?.url}",
                contentType: "${iconImageMap?.contentType}",
                imageSize: iconImageMap?.imageSize,
                id: iconImageMap?.imageId
            ),
            active: active
        )
    }
}
