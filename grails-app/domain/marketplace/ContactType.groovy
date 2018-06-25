package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


class ContactType extends AuditStamped implements Serializable, ToJSON {

    static searchable = {
        root false
        title excludeFromAll: true
        only = ['title']
    }

    String title
    Boolean required = false

    static constraints = {
        title blank: false, nullable: false, maxSize: 50, unique: true
        required nullable: false
    }

    final static bindableProperties = ['title', 'required']
    final static modifiableReferenceProperties = []

    @Override
    int hashCode() {
        new HashCodeBuilder()
            .append(title)
            .append(required)
            .toHashCode()
    }

    @Override
    boolean equals(other) {
        if (other instanceof ContactType) {
            return new EqualsBuilder()
                .append(title, other.title)
                .append(required, other.required)
                .isEquals()
        }
        return false
    }

    @Override
    JSONObject asJSON() {
        marshall([id      : id,
                  title   : title,
                  required: required])
    }

    String prettyPrint() {
        toString()
    }

    @Override
    String toString() {
        "title: $title, required: $required"
    }

    void scrubCR() {
        //Don't think we really need to do anything here, but the MarketplaceAdminController calls it
    }

    static boolean findDuplicates(def obj) {
        obj?.title && findByTitle(obj.title)
    }

    void bindFromJSON(JSONObject obj) {
        this.with {
            title = obj.title
            required = obj.required
        }
    }

    def beforeDelete() {
        ContactType.withNewSession {
            Contact.findAllByType(this)*.delete()
        }
    }
}
