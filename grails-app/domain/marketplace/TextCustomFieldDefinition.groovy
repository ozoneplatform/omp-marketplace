package marketplace


class TextCustomFieldDefinition extends CustomFieldDefinition {

    static bindableProperties = CustomFieldDefinition.bindableProperties
    static modifiableReferenceProperties = []

    static constraints = {
        styleType(nullable: false, validator: {
            if (it != Constants.CustomFieldDefinitionStyleType.TEXT) {
                return "textCustomFieldDefintion.styleType.notCorrect"
            } else {
                return true
            }
        })
    }

    static mapping = {
        table 'text_cfd'
        cache true
    }

    TextCustomFieldDefinition() {
        this.styleType = Constants.CustomFieldDefinitionStyleType.TEXT
    }
}
