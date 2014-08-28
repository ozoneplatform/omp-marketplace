package marketplace

import gorm.AuditStamp
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * A Domain class representing a pair of screenshots.  This pair should consist of
 * a large version and a small version of the same image.  The large version is optional.
 * If it is not present, the small version should be used for both
 */
@AuditStamp
class Screenshot implements Serializable {

    static searchable = {
        root false
        smallImageUrl index: 'not_analyzed', excludeFromAll: true
        largeImageUrl index: 'not_analyzed', excludeFromAll: true
        only = ['smallImageUrl', 'largeImageUrl']
    }

    private static smallImageUrlValidator =
        ValidationUtil.&validateUrlConstraint.curry('screenshot.smallImageUrl.url.invalid')

    private static largeImageUrlValidator =
        ValidationUtil.&validateUrlConstraint.curry('screenshot.largeImageUrl.url.invalid')

    static bindableProperties = ['smallImageUrl', 'largeImageUrl']
    static modifiableReferenceProperties = []

    static belongsTo = [serviceItem: ServiceItem]

    String smallImageUrl
    String largeImageUrl

    static constraints = {
        smallImageUrl(blank:false, nullable:false, maxSize:Constants.MAX_URL_SIZE,
            validator: smallImageUrlValidator)
        largeImageUrl(blank:false, nullable:true, maxSize:Constants.MAX_URL_SIZE,
            validator: largeImageUrlValidator)
    }

    public String getLargeImageUrl() {
        this.largeImageUrl == null ? this.smallImageUrl : this.largeImageUrl
    }

    JSONObject asJSON() {
        new JSONObject([
            id: this.id,
            smallImageUrl: this.smallImageUrl,
            largeImageUrl: this.getLargeImageUrl()
        ])
    }

    @Override
    boolean equals(other) {
        // Since screenshots is a lazy loaded collection, the instances could be
        // hibernate proxies, so use the GORM 'instanceOf' method
        Boolean sameType
        try {
            sameType = other.instanceOf(Screenshot)
        } catch(MissingMethodException mme) {
            sameType = false
        }

        if(sameType) {
            return new EqualsBuilder()
                        .append(smallImageUrl, other.smallImageUrl)
                        .append(largeImageUrl, other.largeImageUrl)
                        .isEquals()
        }
        return false
    }

    @Override
    int hashCode() {
        return new HashCodeBuilder()
                    .append(smallImageUrl)
                    .append(largeImageUrl)
                    .toHashCode()
    }

    @Override
    String toString() {
        "$largeImageUrl, $smallImageUrl"
    }
}
