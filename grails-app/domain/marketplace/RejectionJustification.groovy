package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

@gorm.AuditStamp
class RejectionJustification implements Serializable {

    String title
    String description

    static constraints = {
    	title(blank: false, nullable: false, maxSize: 50)
    	description(nullable: true, maxSize: 250)
    }

    static mapping = {
        cache true
    }

    String toString() { "$title" }

    String prettyPrint() {
        toString()
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    def asJSON() {
        return new JSONObject(
            id: id,
            title: title,
            description: description
        )
    }
}
