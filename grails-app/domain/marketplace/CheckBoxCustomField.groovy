package marketplace

import org.grails.web.json.JSONObject

import ozone.marketplace.domain.ValidationException

class CheckBoxCustomField extends CustomField implements ToJSON {

    boolean value

    static constraints = {
        value(maxSize: 2083, nullable: true)
    }

    static mapping = {
        table 'check_box_cf'
        cache true
    }

    void setFieldValueText(String fieldValueText) {
            setValue((String)fieldValueText)
    }

    void setValue(String valueIn) {
        if (valueIn == null) {
            value = false
        }
        // We are being stricter than the groovy toBoolean method
        else if (valueIn.equalsIgnoreCase('true')) {
            value = true
        } else if (valueIn.equalsIgnoreCase('false')) {
            value = false
        } else {
            throw new ValidationException(message: "CheckBox custom field '${customFieldDefinition?.name}' requires a value of 'true' or 'false'")
        }
    }

    def setValue(Boolean val) {
       this.value = val
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

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()
        json.putAll([id   : id,
                     value: value])
        json
    }

}
