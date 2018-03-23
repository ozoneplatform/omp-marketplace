package marketplace

import org.grails.web.json.JSONObject

class CheckBoxCustomFieldDefinition extends CustomFieldDefinition implements ToJSON {

    static bindableProperties = CustomFieldDefinition.bindableProperties + ['selectedByDefault']
    static modifiableReferenceProperties = []

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

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()
        json.putAll([selectedByDefault: selectedByDefault])
        json
    }

    def bindFromJSON(JSONObject json) {
        super.bindFromJSON(json)
        this.selectedByDefault = json.selectedByDefault as boolean
    }

}
