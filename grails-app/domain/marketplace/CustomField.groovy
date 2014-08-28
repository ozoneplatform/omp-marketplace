package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

@gorm.AuditStamp
class CustomField implements Serializable {

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

    static transients = ['fieldValueText', 'customFieldName', 'empty']

    static mapping = {
        batchSize 50
        cache true
        tablePerHierarchy false
    }

    String getFieldValueText(){
        return null
    }

    boolean isEmpty() {
        return !(fieldValueText as boolean)
    }

    def asJSON() {
        return new JSONObject(
            class: this.getClass().name, //facilitates subclasses
            customFieldDefinition: customFieldDefinition?.asJSONRef(),

            //TODO all of the rest of these fields are redundant
            //and only here for backwards compatibility.  They
            //can be removed once the non-rest calls are gone
            customFieldDefinitionId: customFieldDefinition?.id,
            customFieldDefinitionUuid: customFieldDefinition?.uuid,
            fieldType: customFieldDefinition?.styleType?.name(),
            label: customFieldDefinition?.label,
            name: customFieldDefinition?.name,
            section: customFieldDefinition?.section
        )
    }

    String getCustomFieldName() {
        return this.customFieldDefinition.name
    }

    boolean equals(other) {
        this.customFieldDefinition.equals(other?.customFieldDefinition)
    }
}
