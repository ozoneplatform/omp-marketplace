package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.utils.Utils
import marketplace.JSONUtil as JS
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.EqualsBuilder

@gorm.AuditStamp
class Category implements Serializable {
    static searchable = {
        root false
        title index: 'analyzed'
        only = ['id', 'title']
    }

    static bindableProperties = ['title', 'description']
    static modifiableReferenceProperties = []

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
        description(nullable: true, maxSize: 250)
        categoryId(nullable: true)
        uuid(nullable: true, unique: true)
    }
    static mapping = {
        cache true
        batchSize 50
    }

    static transients = ['sortCategoryTitle', 'categoryId']

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

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    def asJSON() {
        return new JSONObject(
            id: id,
            uuid: uuid,
            title: title,
            description: description
        )
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
        ].each(JS.optStr.curry(json, this))

        [
            "editedDate"
        ].each(JS.optDate.curry(json, this))
    }

    Long getCategoryId() {
        id
    }

    String getSortCategoryTitle() {
        title
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
        if (obj instanceof Category) {
            Category other = (Category) obj
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
