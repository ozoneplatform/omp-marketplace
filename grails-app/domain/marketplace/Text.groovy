package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject;

@gorm.AuditStamp
class Text implements Serializable {

    //TODO: This class and its associated controller/service are uneeded

    String name
    String value
    Boolean readOnly = false

    static constraints = {
        name(blank: false, nullable: false, maxSize: 50, unique: true)
        value(blank: true, nullable: true, maxSize: 250)
    }

    static mapping = {
        cache true
    }

    String toString() { "$name" }

    String prettyPrint() {
        toString()
    }

    void scrubCR() {
        if (this.value) {
            this.value = this.value.replaceAll("\r", "")
        }
    }

    static getByName(nameIn) {

        def returnValue

        returnValue = Text.createCriteria().get {
            eq("name", nameIn)
            cacheable(true)
            cacheRegion('marketplace.ReferenceQueryCache')
        }

        return returnValue

    }

    def asJSON() {
        return new JSONObject(
            id: id,
            name: name,
            value: value,
            readOnly: readOnly
        )
    }
}
