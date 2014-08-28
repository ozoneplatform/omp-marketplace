package marketplace

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

class DropDownCustomFieldDefinition extends CustomFieldDefinition {

    def beforeValidate() {
        //beforeValidate isn't automatically called on child
        //objects
        fieldValues*.beforeValidate()

        super.beforeValidate()
    }

    List<FieldValue> fieldValues

    Boolean isMultiSelect = false

    static constraints = {
        styleType(nullable: false, validator: {
            if (it != Constants.CustomFieldDefinitionStyleType.DROP_DOWN) {
                return "dropDownCustomFieldDefinition.styleType.notCorrect"
            } else {
                return true
            }
        })
        fieldValues(nullable: true,
            validator: {
                val, obj ->
                    val.any { it.isEnabled == 1 }
            })
        isMultiSelect(nullable: true)
    }

    static mapping = {
        table 'drop_down_cfd'
        fieldValues cache: true
        fieldValues batchSize: 50
        fieldValues cascade: 'all-delete-orphan'
        cache true
    }

    static hasMany = [fieldValues: FieldValue]

    DropDownCustomFieldDefinition() {
        this.styleType = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
    }

    def fieldValueListSafeUpdate(List<FieldValue> newFieldValues) {

        //determine if each of fieldValues are in newFieldValues - if not, disable that fieldValue
        fieldValues.each { existingFieldValue ->
            FieldValue newFieldValue = newFieldValues.find {
                it.displayText == existingFieldValue.displayText
            }
            if (!newFieldValue) {
                existingFieldValue.isEnabled = 0
            }
        }

        //determine if each of newFieldValues exists and is enabled - if not, create or enable it as appropriate
        newFieldValues.each { newFieldValue ->
            FieldValue existingField = fieldValues.find {
                it.displayText == newFieldValue.displayText
            }
            if (!existingField) {
                addToFieldValues(newFieldValue)
            } else if (existingField.isEnabled != 1) {
                existingField.isEnabled = 1
            }
        }
    }

    def asJSON() {
        def currJSON = super.asJSON()
        currJSON.put('fieldValues', new JSONArray(fieldValues?.collect { it.asJSONRef() }))
        currJSON.put('isMultiSelect', isMultiSelect)
        return currJSON
    }

//    This is partially handled in the ImportExportService because of complications having to do with
//    handling FieldValues that may be in use by a ServiceItem.
    def bindFromJSON(JSONObject json) {
        super.bindFromJSON(json)
        this.isMultiSelect = json.isMultiSelect as boolean
    }
}
