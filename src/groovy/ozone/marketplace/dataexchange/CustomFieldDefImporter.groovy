package ozone.marketplace.dataexchange

import grails.util.Environment

import marketplace.Constants
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomFieldDefinition
import marketplace.FieldValue
import org.codehaus.groovy.grails.web.json.JSONObject

class CustomFieldDefImporter extends AbstractImporter {
    public CustomFieldDefImporter() {
        super("custom field definition")
    }

    def findExistingObject(def json) {
        def styleType = Constants.CustomFieldDefinitionStyleType.valueOf(json.fieldType)
        if (!styleType) {
            log.warn "Could not find CustomFieldDefinition for styleType: '${json.fieldType}'"
            throw new InvalidPropertiesFormatException("fieldType '${json.fieldType}' was not recognized")
        }
        return styleType.fieldDefinitionClass.findByUuid(json.uuid)
    }

    protected def createFromJSONAndSave(def json) {
        def styleType = Constants.CustomFieldDefinitionStyleType.valueOf(json.fieldType)
        if (!styleType) {
            log.warn "Could not find CustomFieldDefinition for styleType: '${json.fieldType}'"
            throw new InvalidPropertiesFormatException("fieldType '${json.fieldType}' was not recognized")
        }
        def newObject = styleType.fieldDefinitionClass.newInstance()
        newObject.bindFromJSON(json)
        if (newObject instanceof DropDownCustomFieldDefinition) {
            // Drop downs require custom handling for the field values
            json.fieldValues.each {
                FieldValue fieldValue = new FieldValue()
                fieldValue.displayText = it.displayText
                fieldValue.isEnabled = it.isEnabled as int
                fieldValue.setCustomFieldDefinition(newObject)
                setAuditStamp(fieldValue)
                newObject.addToFieldValues(fieldValue)
            }
        }
        newObject.save(failOnError: true, flush: true  )
        return newObject
    }

    def createNewObject() {
        // This is not used in this implementation because createFromJSONAndSave determines which class to instantiate based on the fieldType
        return null
    }

    protected def updateFromJSONAndSave(def object, def json) {
        object.bindFromJSON(json)
        if (object instanceof DropDownCustomFieldDefinition) {
            // For now, only add new field values (don't remove old ones)
            json.fieldValues.each { importedFieldValue ->
                def existingFieldValue = object.fieldValues.find { it.displayText == importedFieldValue.displayText }
                if (!existingFieldValue) {
                    FieldValue newFieldValue = new FieldValue()
                    newFieldValue.displayText = importedFieldValue.displayText
                    newFieldValue.isEnabled = importedFieldValue.isEnabled
                    newFieldValue.setCustomFieldDefinition(object)
                    object.addToFieldValues(newFieldValue)

                    setAuditStamp(newFieldValue)
                }
            }
        }
        object.save(failOnError: true, flush: true  )
        return object
    }

    protected boolean shouldUpdate(JSONObject json, def existingMatch) {
        // Never update permanent custom fields
        if (existingMatch.isPermanent) {
            return false
        }
        return super.shouldUpdate(json, existingMatch)
    }

    private void setAuditStamp(FieldValue val) {
        //unit tests don't call beforeValidate on
        //nested objects like they should, so the
        //audit-trail code doesn't fire
        if (Environment.current == Environment.TEST) {
            val.editedDate = val.createdDate = new Date()
        }
    }
}
