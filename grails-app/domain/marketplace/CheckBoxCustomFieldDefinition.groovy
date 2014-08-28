package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

class CheckBoxCustomFieldDefinition extends CustomFieldDefinition {

    boolean selectedByDefault = false

    static constraints = {
        styleType(nullable: false, validator: {
            if (it != Constants.CustomFieldDefinitionStyleType.CHECK_BOX) {
                return "CheckBoxCustomFieldDefintion.styleType.notCorrect"
            } else {
                return true
            }
        })
    }

    static mapping = {
        table 'check_box_cfd'
        cache true
    }

    CheckBoxCustomFieldDefinition() {
        this.styleType = Constants.CustomFieldDefinitionStyleType.CHECK_BOX
    }

    def asJSON() {
        def jsonObject = super.asJSON()
        jsonObject.putAll(
            selectedByDefault: selectedByDefault
        )
        return jsonObject
    }

    def bindFromJSON(JSONObject json) {
        super.bindFromJSON(json)
        this.selectedByDefault = json.selectedByDefault as boolean
    }

}
