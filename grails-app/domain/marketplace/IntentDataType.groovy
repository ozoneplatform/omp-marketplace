package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import ozone.utils.Utils


class IntentDataType extends AuditStamped implements Serializable, ToJSON {
    static searchable = {
        root false
        title index: 'analyzed'
        only = ['title']
    }

    private static final long serialVersionUID = 1L
    private static final int MAX_TITLE_SIZE = 256
    private static final int MAX_DESCRIPTION_SIZE = MAX_TITLE_SIZE

    String title
    String description
    String uuid

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID()
        }
    }

    static constraints = {
        title blank: false, maxSize: MAX_TITLE_SIZE, nullable: false
        description maxSize: MAX_DESCRIPTION_SIZE, nullable: true
        uuid nullable: true, unique: true
    }

    static mapping = {
        cache true
        batchSize 50
    }

    @Override
    JSONObject asJSON() {
        marshall([id         : id,
                  uuid       : uuid,
                  title      : title,
                  description: description])
    }

    def asJSONRef() {
        new JSONObject(
            id: id,
            uuid: uuid,
            title: title
        )
    }

    String toString() { "$title" }

    String prettyPrint() {
        title
    }

    String titleDisplay() {
        final max_size = 12
        if (title.size() > max_size) {
            title[0..(max_size - 1)] + '...'
        } else {
            title
        }
    }

    void scrubCR() {
        final cr = "\r"
        final replacement = ''
        title = title.replaceAll(cr, replacement)
        if (description) {
            description = description.replaceAll(cr, replacement)
        }
    }

    @Override
    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(id)
            .append(title)
            .append(description)
            .append(version)
            .append(uuid)
        return builder.toHashCode()
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof IntentDataType) {
            IntentDataType other = (IntentDataType) obj
            EqualsBuilder builder = new EqualsBuilder()
            builder.append(id, other.id)
                .append(uuid, other.uuid)
                .append(version, other.version)
                .append(description, other.description)
                .append(title, other.title)
            return builder.isEquals()
        }
        return false
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
