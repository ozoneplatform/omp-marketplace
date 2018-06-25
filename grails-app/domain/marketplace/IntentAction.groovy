package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import ozone.utils.Utils


class IntentAction extends AuditStamped implements Serializable, ToJSON {
    static searchable = {
        root false
        title index: 'analyzed'
        only = ['title']
    }

    String title
    String description
    String uuid

    static constraints = {
        title blank: false, maxSize: 256
        description nullable: true, maxSize: 256
        uuid nullable: true, unique: true
    }

    static mapping = {
        cache true
    }

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID();
        }
    }

    String prettyPrint() {
        toString()
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    String toString() { "$title" }

    @Override
    JSONObject asJSON() {
        marshall([id         : id,
                  uuid       : uuid,
                  title      : title,
                  description: description])
    }

    def asJSONRef() {
        return new JSONObject(
            id: id,
            uuid: uuid,
            title: title
        )
    }

    def bindFromJSON(JSONObject json) {
        [
            "title",
            "description",
            "uuid"
        ].each(JSONUtil.optStr.curry(json, this))

        [
            "editedDate"
        ].each(JSONUtil.optDate.curry(json, this))
    }

    @Override
    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(id)
            .append(title)
            .append(description)
            .append(uuid)
        def code = builder.toHashCode()
        return code;
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof IntentAction) {
            IntentAction other = (IntentAction) obj
            EqualsBuilder builder = new EqualsBuilder()
            builder.append(id, other.id)
                .append(uuid, other.uuid)
                .append(description, other.description)
                .append(title, other.title)
            return builder.isEquals();
        }
        return false;
    }

    static boolean findDuplicates(def obj) {
        if (obj?.has('uuid')) {
            def allUuids = findAllByUuid(obj.uuid);
            if (allUuids.size() == 0) {
                if (obj.has('title')) {
                    def allTitles = findAllByTitle(obj.title);
                    return (allTitles.size() > 0) ?: false;
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
