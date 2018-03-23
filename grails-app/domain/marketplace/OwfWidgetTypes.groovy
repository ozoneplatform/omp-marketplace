package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import ozone.utils.Utils

/**
 * The type of widget of a ServiceItem representing an OWF widget.
 */
class OwfWidgetTypes extends AuditStamped implements Serializable, ToJSON {

    //TODO: This class and its associated controller/service appear to be unused

    static searchable = {
        root false
        title index: 'not_analyzed', excludeFromAll: false
        only = ['title']
    }

    String title
    String description
    String uuid

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID();
        }
    }

    static constraints = {
        title(blank: false, nullable: false, maxSize: 50)
        uuid(nullable: true, unique: true)
    }

    static mapping = {
        cache true
        batchSize 50
    }

    static transients = ['owfWidgetTypeId']

    String toString() { "$title" }

    String prettyPrint() {
        toString()
    }

    String titleDisplay() {
        if (title.size() > 12) {
            return title.substring(0, 12) + "...";
        } else {
            return title;
        }
    }

    Long getOwfWidgetTypeId() {
        id
    }

    @Override
    JSONObject asJSON() {
        marshall([id         : id,
                  uuid       : uuid,
                  title      : title,
                  description: description])
    }

    @Override
    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(id)
            .append(title)
            .append(version)
            .append(uuid)
        def code = builder.toHashCode()
        return code;
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof OwfWidgetTypes) {
            OwfWidgetTypes other = (OwfWidgetTypes) obj
            EqualsBuilder builder = new EqualsBuilder()
            builder.append(id, other.id)
                .append(uuid, other.uuid)
                .append(version, other.version)
                .append(title, other.title)
            return builder.isEquals();
        }
        return false;
    }
}
