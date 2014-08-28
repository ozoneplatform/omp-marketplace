package marketplace

class ImageURLCustomFieldDefinition extends CustomFieldDefinition {

    static constraints = {
        styleType(nullable: false, validator: {
            if (it != Constants.CustomFieldDefinitionStyleType.IMAGE_URL) {
                return "imageURLCustomFieldDefintion.styleType.notCorrect"
            } else {
                return true
            }
        })
    }

    static mapping = {
        table 'image_url_cfd'
        cache true
    }

    ImageURLCustomFieldDefinition() {
        this.styleType = Constants.CustomFieldDefinitionStyleType.IMAGE_URL
    }
}
