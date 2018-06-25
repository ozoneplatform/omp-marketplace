package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


class ServiceItemDocumentationUrl implements Serializable, ToJSON {

    static searchable = {
        root false
        only = ['name', 'url']
    }

    static bindableProperties = ['name', 'url']
    static modifiableReferenceProperties = []

    String name
    String url

    static belongsTo = [serviceItem: ServiceItem]

    static constraints = {
        name(maxSize: 255, nullable: false)
        url(maxSize: Constants.MAX_URL_SIZE, nullable: false)
    }

    @Override
    JSONObject asJSON() {
        new JSONObject([name: name,
                        url : url])
    }

    def asJSONRef() {
        asJSON()
    }

    @Override
    int hashCode() {
        new HashCodeBuilder()
            .append(name)
            .append(url)
            .toHashCode()
    }

    @Override
    boolean equals(other) {
        // Since docUrls is a lazy loaded collection, the instances could be
        // hibernate proxies, so use the GORM 'instanceOf' method
        Boolean sameType
        try {
            sameType = other.instanceOf(ServiceItemDocumentationUrl)
        } catch(MissingMethodException mme) {
            sameType = false
        }

        if(sameType) {

            return new EqualsBuilder()
                        .append(name, other.name)
                        .append(url, other.url)
                        .isEquals()
        }
        return false
    }

    @Override
    String toString() {
        "$name: $url"
    }
}
