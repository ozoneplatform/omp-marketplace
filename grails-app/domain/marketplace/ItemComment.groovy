package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

@gorm.AuditStamp
class ItemComment implements Comparable, Serializable {

    static searchable = {
        root false
        only = ['text']
        text excludeFromAll: true
    }

    static bindableProperties = ['text', 'rate', 'serviceItem']
    static modifiableReferenceProperties = []

    String text
    Float rate

    static belongsTo = [serviceItem: ServiceItem, author: Profile]

    static mapping = {
        author fetch: 'join'
        cache true
        serviceItem index: 'itm_cmnt_svc_item_id_idx'
        author index: 'itm_cmnt_author_id_idx'
    }

    static constraints = {
        def maxLength = 4000
        rate(nullable: true, min: 1F, max: 5F)
        text(blank: true, nullable: true, maxSize: maxLength, validator: {
            if (it?.size() > maxLength) {
                return ["serviceItem.comment.maxsize", maxLength]
            } else
                return true
        })
    }

    int compareTo(obj) {
        int result = createdDate.compareTo(obj.createdDate)
        if (result > 0) {
            return -1
        } else if (result < 0) {
            return 1
        } else {
            if (id && obj.id) {
                return obj.id.compareTo(id)
            } else if (text && obj.text) {
                return text.compareTo(obj.text)
            } else if (rate && obj.rate) {
                return rate.compareTo(obj.rate)
            } else {
                return 0
            }
        }
    }

    void scrubCR() {
        if (this.text) {
            this.text = this.text.replaceAll("\r", "")
        }
    }

    def getTextAsHTML() {
        def text = this.text
        if (text) {
            text = text.replaceAll("\n", "<br/>")
            text = text.replaceAll(" ", "&nbsp;")
        }
        return text
    }

    String toString() {
        return author?.displayName + ": Rate " + rate + " Stars : " + text
    }

    String prettyPrint() {
        toString()
    }

    def asJSON() {
        JSONObject currJSON = new JSONObject(
            id: id,
            text: text,
            rate: rate,
            author: new JSONObject(
                id: author.id,
                username: author.username,
                displayName: author.displayName
            )
        )
        JSONUtil.addCreatedAndEditedInfo(currJSON, this)
        return currJSON
    }
}
