package marketplace

import ozone.marketplace.domain.ValidationException

class ImageURLCustomField extends CustomField {

    String value

    static constraints = {
        value(maxSize: Constants.MAX_URL_SIZE, nullable: true)
    }

    static mapping = {
        table 'image_url_cf'
        cache true
    }

    String toString() {
        "${customFieldDefinition?.label} = ${value}"
    }

    String prettyPrint() {
        toString()
    }

    void setFieldValueText(String valueIn) {
        setValue(valueIn)
    }

    public void setValue(String valueIn) {
        if (valueIn != null && valueIn.trim().size() > 0 && !valueIn.matches(Constants.URL_PATTERN)) {
            throw new ValidationException(message: "Image URL custom field '${customFieldDefinition?.name}' requires a properly formatted URL.")
        }
        value = valueIn
    }

    String getFieldValueText() {
        return value
    }

    void scrubCR() {
        if (this.value) {
            this.value = this.value.replaceAll("\r", "")
        }
    }

    def asJSON() {
        def jsonObject = super.asJSON()
        jsonObject.putAll(
            id: id,
            value: value
        )
        return jsonObject
    }
}
