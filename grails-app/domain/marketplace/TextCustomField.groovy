package marketplace


class TextCustomField extends CustomField {

    String value

    static constraints = {
        value(maxSize: 256, nullable: true)
    }

    static mapping = {
        table 'text_cf'
        cache true
    }

    String toString() {
        "${customFieldDefinition?.label} = ${value}"
    }

    String prettyPrint() {
        toString()
    }

    String getFieldValueText() {
        return value
    }

    void setFieldValueText(String fieldValueText) {
        value = fieldValueText
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
