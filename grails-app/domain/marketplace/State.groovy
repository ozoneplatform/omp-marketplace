package marketplace

import org.grails.web.json.JSONObject

import marketplace.JSONUtil as JS
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import ozone.utils.Utils


class State extends AuditStamped implements Serializable, ToJSON {

    static bindableProperties = ['title', 'description', 'isPublished', 'uuid']
    static modifiableReferenceProperties = []

    static searchable = {
        root false
        title index: 'analyzed'
        isPublished index: 'not_analyzed', excludeFromAll: true
        only = ['id', 'title', 'isPublished']
    }

    String title
    String description
    boolean isPublished = false

    String uuid

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID();
        }
    }

    static constraints = {
        title(blank: false, nullable: false, maxSize: 50)
        description(maxSize: 250, nullable: true)
        uuid(nullable: true, unique: true)
        stateId(nullable: true)
    }

    static mapping = {
        cache true
    }

    static transients = ['sortStateTitle', 'stateId']

    String toString() { title }

    String prettyPrint() {
        toString()
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    @Override
    JSONObject asJSON() {
        marshall([id         : id,
                  title      : title,
                  description: description,
                  isPublished: isPublished,
                  uuid       : uuid])
    }

    def asJSONRef() {
        return new JSONObject(
            id: id,
            uuid: uuid,
            title: title
        )
    }

    String titleDisplay() {
        if (title.size() > 12) {
            return title.substring(0, 12) + "...";
        } else {
            return title;
        }
    }

    private bindFromJSON(JSONObject json) {
        [
            "title",
            "description",
            "uuid"
        ].each(JS.optStr.curry(json, this))

        [
            "isPublished"
        ].each(JS.optBoolean.curry(json, this))

        [
            "editedDate"
        ].each(JS.optDate.curry(json, this))
    }

    Long getStateId() {
        id
    }

    String getSortStateTitle() {
        title?.toLowerCase()
    }

    @Override
    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(id)
            .append(title)
            .append(description)
            .append(version)
            .append(uuid)
        def code = builder.toHashCode()
        return code;
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof State) {
            State other = (State) obj
            EqualsBuilder builder = new EqualsBuilder()
            builder.append(id, other.id)
                .append(uuid, other.uuid)
                .append(version, other.version)
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
