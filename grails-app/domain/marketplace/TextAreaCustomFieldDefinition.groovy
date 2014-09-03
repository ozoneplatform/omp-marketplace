package marketplace

class TextAreaCustomFieldDefinition extends CustomFieldDefinition {

    static bindableProperties = CustomFieldDefinition.bindableProperties
    static modifiableReferenceProperties = []

    static constraints = {
        styleType(nullable: false, validator: {
            if (it != Constants.CustomFieldDefinitionStyleType.TEXT_AREA) {
                return "textAreaCustomFieldDefintion.styleType.notCorrect"
            } else {
                return true
            }
        })
    }

    static mapping = {
        table 'text_area_cfd'
        cache true
    }

    TextAreaCustomFieldDefinition() {
        this.styleType = Constants.CustomFieldDefinitionStyleType.TEXT_AREA
    }
}
