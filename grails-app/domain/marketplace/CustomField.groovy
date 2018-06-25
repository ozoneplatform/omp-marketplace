package marketplace

import org.grails.web.json.JSONObject


class CustomField extends AuditStamped implements Serializable, ToJSON {

    static EMPTY_JSON_OBJECT = new JSONObject()

    static bindableProperties = ['customFieldDefinition']
    static modifiableReferenceProperties = []

    static searchable = {
        root false
        fieldValueText excludeFromAll: false
        customFieldName index: "not_analyzed", excludeFromAll: true
        only = ['fieldValueText', 'customFieldName']
    }

    static belongsTo = ServiceItem

    CustomFieldDefinition customFieldDefinition

    /**
     * This field is only used when this class is used directly, generally as a DTO.
     * It should not be used by the actual subclass CustomField impls
     **/
    private String value

    static transients = ['fieldValueText', 'customFieldName', 'empty']

    static mapping = {
        batchSize 50
        cache true
        tablePerHierarchy false
    }

    String getFieldValueText(){
        return value
    }

    public void setValue(def value) {
        this.value = value == null ? null : value.toString()
    }

    boolean isEmpty() {
        return !(fieldValueText as boolean)
    }

    @Override
    JSONObject asJSON() {
        new JSONObject([id                       : id,
                        class                    : this.getClass().name, //facilitates subclasses
                        customFieldDefinition    : customFieldDefinition?.asJSONRef(),

                        //TODO all of the rest of these fields are redundant
                        //and only here for backwards compatibility.  They
                        //can be removed once the non-rest calls are gone
                        customFieldDefinitionId  : customFieldDefinition?.id,
                        customFieldDefinitionUuid: customFieldDefinition?.uuid,
                        fieldType                : customFieldDefinition?.styleType?.name(),
                        label                    : customFieldDefinition?.label,
                        name                     : customFieldDefinition?.name,
                        section                  : customFieldDefinition?.section])
    }

    String getCustomFieldName() {
        return this.customFieldDefinition.name
    }

    boolean equals(other) {
        this.customFieldDefinition.equals(other?.customFieldDefinition)
    }

    /**
     * Legacy import format compat
     */
    public void setCustomFieldDefinitionUuid(String uuid) {
        this.customFieldDefinition = new CustomFieldDefinition(uuid: uuid)
    }
}
