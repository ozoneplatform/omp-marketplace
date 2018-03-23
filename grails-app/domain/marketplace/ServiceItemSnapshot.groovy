package marketplace

import org.grails.web.json.JSONObject

//TODO Is ther a reason to have this class? title is generally just the title of the serviceItem,
//it seems redundant
class ServiceItemSnapshot implements Serializable, ToJSON {

    ServiceItem serviceItem
    String title

    static belongsTo = ModifyRelationshipActivity

    static constraints = {
        serviceItem(nullable: true)
    }

    @Override
    JSONObject asJSON() {
        new JSONObject([title: title,
                        id   : serviceItem?.id])
    }

    boolean equals(other) {
        other instanceof ServiceItemSnapshot && this.title == other.title &&
            this.serviceItem == other.serviceItem
    }
}
