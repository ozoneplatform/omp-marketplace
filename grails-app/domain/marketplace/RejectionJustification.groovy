package marketplace

import org.grails.web.json.JSONObject


class RejectionJustification extends AuditStamped implements Serializable, ToJSON {

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

    @Override
    JSONObject asJSON() {
        marshall([id         : id,
                  title      : title,
                  description: description])
    }

}
