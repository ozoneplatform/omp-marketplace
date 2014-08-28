package marketplace

class TextAreaCustomField extends CustomField {

    String value

    static constraints = {
        value(maxSize: 4000, nullable: true)
    }

    static mapping = {
        table 'text_area_cf'
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

    void scrubCR() {

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
