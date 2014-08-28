package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

//TODO Is ther a reason to have this class? title is generally just the title of the serviceItem,
//it seems redundant
class ServiceItemSnapshot implements Serializable {

    ServiceItem serviceItem
    String title

    static belongsTo = ModifyRelationshipActivity

    static constraints = {
        serviceItem(nullable: true)
    }

    JSONObject asJSON() {
        return new JSONObject(
            title: title,
            id: serviceItem?.id
        )
    }

    boolean equals(other) {
        other instanceof ServiceItemSnapshot && this.title == other.title &&
            this.serviceItem == other.serviceItem
    }
}
