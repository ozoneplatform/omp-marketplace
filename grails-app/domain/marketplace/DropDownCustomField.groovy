package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.StringUtils

import ozone.marketplace.domain.ValidationException


class DropDownCustomField extends CustomField implements ToJSON {

    FieldValue value
   //TODO BVEST followup
    List<FieldValue> fieldValueList

    static bindableProperties = ['value'] + CustomField.bindableProperties
    static modifiableReferenceProperties = []

    static constraints = {
        value(nullable: true)
    }

    static mapping = {
        table 'drop_down_cf'
        cache true
        fieldValueList(column: 'drop_down_cf_field_value_id')
    }

    static hasMany = [fieldValueList: FieldValue]

    private void setFieldValue(def newValue) {
        boolean newValueIsValid = false
        if (newValue == null) {
            this.value = null
            newValueIsValid = true
        } else if (newValue instanceof String) {
            if (StringUtils.isBlank(newValue)) {
                this.value = null
                newValueIsValid = true
            } else if (!(customFieldDefinition instanceof DropDownCustomFieldDefinition)) {
                //if the CustomFieldDefinition isn't in place or is just a placeholder,
                //findFieldValueForString won't work right.  In that case just save a dto;
                //It should get marshalled properly later on in marshallAllFieldValues
                this.value = new FieldValue(displayText: newValue)
                newValueIsValid = true
            }
            else {
                FieldValue fieldValue = findFieldValueForString(newValue)
                if (fieldValue) {
                    this.value = fieldValue
                    newValueIsValid = true
                }
            }
        } else if (newValue instanceof FieldValue) {
            this.value = newValue
            newValueIsValid = true
        }

        if (!newValueIsValid) {
            def validationExceptionMsg = "DROP_DOWN custom field '${this.customFieldDefinition?.name}', with value provided, '${newValue}', requires a fieldValue with a valid Field Value Option value, from ${FieldValue.dolist(this.customFieldDefinition?.id)}"
            throw new ValidationException(message: validationExceptionMsg)
        }
    }

    String toString() {
        "${customFieldDefinition?.label} = ${value ?: (fieldValueList ? fieldValueList*.displayText.join(',') : '')}"
    }

    String prettyPrint() {
        toString()
    }

    void setFieldValueText(String value) {
        //if it is a multiselect field, split on commas.
        //this method may either call setValue(String) or setValue(String[])
        def val = customFieldDefinition.isMultiSelect ? value.split(',') as String[] : value
        setValue(val)
    }

    //this method will only ever be called if there is only one selection
    public void setValue(String newValue) {
        removeAllFromFieldValueList()
        this.fieldValueList = null
        this.setFieldValue(newValue)
    }

    public void setValue(FieldValue newValue) {
        this.setFieldValue(newValue)
    }

    public void setValue(String[] newValue) {
        if (newValue) {
            // Remove empty values from the list
            newValue = trimValueList(newValue)
            switch (newValue.size()) {
                case 0:
                    setValue((String) null)
                    break
                case 1:
                    setValue((String) newValue[0])
                    break
                default:
                    if (!((DropDownCustomFieldDefinition) this.customFieldDefinition).isMultiSelect)
                        throw new ValidationException(message: "Cannot assign multiple values to a single-value drop-down field ${this.customFieldDefinition.name}")
                    this.@value = null
                    removeAllFromFieldValueList()
                    newValue.each {
                       if (!(this.customFieldDefinition instanceof DropDownCustomFieldDefinition)) {
                            //if the CustomFieldDefinition isn't in place or is just a
                            //placeholder, findFieldValueForString won't work right.  In that
                            //case just save the string; It should get marshalled properly later
                            //on in marshallAllFieldValues
                            addToFieldValueList(it)
                        }
                        else {
                            FieldValue fieldValue = findFieldValueForString(it)
                            if (fieldValue) {
                                addToFieldValueList(fieldValue)
                            } else {
                                def validationExceptionMsg = "DROP_DOWN custom field '${this.customFieldDefinition?.name}', with value provided, '${it}', requires a fieldValue with a valid Field Value Option value, from ${FieldValue.dolist(this.customFieldDefinition?.id)}"
                                throw new ValidationException(message: validationExceptionMsg)
                            }
                        }
                    }
            }
        }
    }

    /**
     * Go through value and fieldValueList and ensure that all
     * of the FieldValues are fully marshalled and aren't just
     * DTOs
     */
    void marshallAllFieldValues() {
        if (value) {
            if (value instanceof String) {
                value = new FieldValue(displayText: value)
            }

            if (value.customFieldDefinition == null) {
                def val = findFieldValueForString(value.displayText)
                if (!val) {
                    throw new IllegalArgumentException(
                        "Invalid DropDownCustomField value ${value.displayText}")
                }
                else {
                    value = val
                }
            }
            else {
                value?.refresh()
            }
        }

        if (fieldValueList) {
            def listIter = fieldValueList.listIterator()
            def currValue

            while (listIter.hasNext()) {
                currValue = listIter.next()

                if (currValue instanceof String) {
                    currValue = new FieldValue(displayText: currValue)
                }

                if (currValue.customFieldDefinition == null) {
                    def val = findFieldValueForString(currValue.displayText)
                    if (!val) {
                        throw new IllegalArgumentException(
                            "Invalid DropDownCustomField value ${currValue.displayText}")
                    }
                    else {
                        listIter.set(val)
                    }
                }
                else {
                    currValue?.refresh()
                }
            }
        }
    }

    FieldValue findFieldValueForString(String strValue) {
        FieldValue.findByDisplayTextAndCustomFieldDefinition(strValue, this.customFieldDefinition)
    }

    String getFieldValueText() {
        return this.@value ? this.@value?.displayText : (this.@fieldValueList ? this.@fieldValueList*.displayText.join(',') : '')
    }

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()
        json.putAll([id        : id,
                     value     : getFieldValueText(),
                     fieldValue: value ? fieldValueAsJSON() :
                                 fieldValueList ? fieldValueListAsJSON() : EMPTY_JSON_OBJECT])
        json
    }

    def fieldValueAsJSON() {
        value ?
            new JSONObject(
                id: value?.id,
                value: value?.displayText,
                isEnabled: value?.isEnabled
            ) :
            EMPTY_JSON_OBJECT
    }

    def fieldValueListAsJSON() {
        fieldValueList ?
            fieldValueList.collect {
                new JSONObject(
                    id: it?.id,
                    value: it?.displayText,
                    isEnabled: it?.isEnabled
                )
            } :
            [EMPTY_JSON_OBJECT]
    }

    private void removeAllFromFieldValueList() {
        if (this.fieldValueList) {
            def copy = []
            copy.addAll(fieldValueList)
            copy?.each { removeFromFieldValueList(it) }
        }
    }

    private List trimValueList(valueList) {
        valueList.findAll { it as boolean }
    }
}
