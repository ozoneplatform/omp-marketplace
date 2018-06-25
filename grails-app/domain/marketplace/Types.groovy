package marketplace

import org.grails.web.json.JSONObject

import marketplace.JSONUtil as JS
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import ozone.utils.Utils


class Types extends AuditStamped implements Serializable, ToJSON {
    static searchable = {
        root false
        title index: 'analyzed', excludeFromAll: false
        ozoneAware index: 'not_analyzed', excludeFromAll: true
        id index: 'analyzed', excludeFromAll: false
        only = ['id', 'title', 'ozoneAware']
    }

    static bindableProperties = [
        'title', 'description',
        'ozoneAware', 'hasLaunchUrl',
        'hasIcons', 'image',
        'uuid'
    ]

    static modifiableReferenceProperties = ['image']

    String title
    String description
    boolean ozoneAware = false
    boolean hasLaunchUrl = true
    boolean hasIcons = false
    Images image
    String uuid
    Boolean isPermanent = false

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID();
        }
    }

    static constraints = {
        title(blank: false, nullable: false, maxSize: 50)
        description(nullable: true, maxSize: 250)
        image(nullable: true)
        typeId(nullable: true)
        uuid(nullable: true, unique: true)
        isPermanent(nullable: true)
    }

    static mapping = {
        cache true
    }

    static transients = ['sortTypeTitle', 'typeId', 'iconImageJSON']

    String creatorNameDisplay() {
        def returnVal = findCreatedByProfile()?.username
        if (returnVal == null) {
            // TODO: get this string from properties file
            returnVal = 'System'
        }

        return returnVal
    }

    String editorNameDisplay() {
        def returnVal = findEditedByProfile()?.username
        if (returnVal == null) {
            // TODO: get this string from properties file
            returnVal = 'System'
        }

        return returnVal
    }

    String toString() { title }

    String prettyPrint() {
        toString()
    }

    String titleDisplay() {
        if (title.size() > 13) {
            return title.substring(0, 13) + "...";
        } else {
            return title;
        }
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    Date prettyDate() {
        if (editedDate) {
            Calendar act = Calendar.getInstance()
            act.setTime(editedDate)
            Calendar now = Calendar.getInstance()
            Calendar firstThing = Calendar.getInstance()
            firstThing.set(Calendar.HOUR_OF_DAY, 0)
            firstThing.set(Calendar.MINUTE, 0)
            firstThing.set(Calendar.SECOND, 0)
            firstThing.set(Calendar.MILLISECOND, 0)

            //long DAY = 24 * 60 * 60 * 1000

            long lact = act.getTimeInMillis()
            long lnow = now.getTimeInMillis()
            long lfirstThing = firstThing.getTimeInMillis()

            long todayDiff = lnow - lfirstThing
            long actDiff = lnow - lact

            if (actDiff > todayDiff) {
                act.set(Calendar.HOUR_OF_DAY, 0)
                act.set(Calendar.MINUTE, 0)
                act.set(Calendar.SECOND, 0)
                act.set(Calendar.MILLISECOND, 0)
            }
            return act.getTime()
        } else {
            return null
        }
    }

    @Override
    JSONObject asJSON() {
        marshall([id          : id,
                  uuid        : uuid,
                  title       : title,
                  description : description,
                  ozoneAware  : ozoneAware,
                  hasLaunchUrl: hasLaunchUrl,
                  hasIcons    : hasIcons,
                  isPermanent : isPermanent])
    }

    public String getIconUrl(String contextPath) {
        "${contextPath}/images/types/$id"
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
            "ozoneAware",
            "hasLaunchUrl",
            "hasIcons",
            "isPermanent"
        ].each(JS.optBoolean.curry(json, this))

        // TODO: figure out how to handle images

        [
            "editedDate"
        ].each(JS.optDate.curry(json, this))
    }

    Long getTypeId() {
        id
    }

    String getSortTypeTitle() {
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
        if (obj instanceof Types) {
            Types other = (Types) obj
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
