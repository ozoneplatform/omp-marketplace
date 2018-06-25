package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * A Domain class representing a pair of screenshots.  This pair should consist of
 * a large version and a small version of the same image.  The large version is optional.
 * If it is not present, the small version should be used for both
 */
class Screenshot extends AuditStamped implements Serializable, ToJSON {

    static searchable = {
        root false
        smallImageUrl index: 'not_analyzed', excludeFromAll: true
        largeImageUrl index: 'not_analyzed', excludeFromAll: true
        only = ['smallImageUrl', 'largeImageUrl']
    }

//    private static smallImageUrlValidator =
//        ValidationUtil.&validateUrlConstraint.curry('screenshot.smallImageUrl.url.invalid')
//
//    private static largeImageUrlValidator =
//        ValidationUtil.&validateUrlConstraint.curry('screenshot.largeImageUrl.url.invalid')

    static bindableProperties = ['smallImageUrl', 'largeImageUrl']
    static modifiableReferenceProperties = []

    static belongsTo = [serviceItem: ServiceItem]

    String smallImageUrl
    String largeImageUrl

    static constraints = {
        largeImageUrl(blank:false, nullable:true, maxSize:Constants.MAX_URL_SIZE,
                validator: { val, obj ->
                    def valid = ValidationUtil.&validateUrlConstraint
                    return valid != null
                            //.curry('screenshot.largeImageUrl.url.invalid')
                   // println(x)
                }
        )
        smallImageUrl(blank:false, nullable:false, maxSize:Constants.MAX_URL_SIZE,
            validator: { val, obj ->
                def valid = ValidationUtil.&validateUrlConstraint
                return valid != null
                        //.curry('screenshot.smallImageUrl.url.invalid')
            }
        )
    }

    public String getLargeImageUrl() {
        this.largeImageUrl == null ? this.smallImageUrl : this.largeImageUrl
    }

    @Override
    JSONObject asJSON() {
        new JSONObject([id           : id,
                        smallImageUrl: smallImageUrl,
                        largeImageUrl: getLargeImageUrl()])
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
