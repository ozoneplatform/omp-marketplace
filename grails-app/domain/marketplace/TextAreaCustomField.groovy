package marketplace

import org.grails.web.json.JSONObject


class TextAreaCustomField extends CustomField implements ToJSON {

    def value

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

    void setFieldValueText(String fieldValueText) {
        value = fieldValueText
    }

    void scrubCR() {

    }

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()
        json.putAll([id   : id,
                     value: value])
        json
    }

}
