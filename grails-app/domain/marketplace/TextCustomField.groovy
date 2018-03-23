package marketplace

import org.grails.web.json.JSONObject


class TextCustomField extends CustomField implements ToJSON {

    def value

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

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()
        json.putAll([id   : id,
                     value: value])
        json
    }
}
