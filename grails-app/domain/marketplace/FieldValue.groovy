package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

/*
 * This Domain object will hold a list of values that correspond to:
 * - Drop Down Custom Field Values
 */
@gorm.AuditStamp
class FieldValue implements Serializable {

    static bindableProperties = ['displayText', 'isEnabled']
    static modifiableReferenceProperties = []

	String displayText

	Integer isEnabled = 1

    FieldValue(String displayText) {
        this.displayText = displayText
    }

    FieldValue() {}


	static belongsTo = [customFieldDefinition: DropDownCustomFieldDefinition]

    static constraints = {
		displayText(blank:false,nullable: false, maxSize:255)
    }

	static mapping = {
		batchSize:50
		cache true
	}

	static dolist(def customFieldDefId) {
		def fvList = FieldValue.createCriteria().list(sort:'displayText', order:'asc') {
			 customFieldDefinition {
				 eq('id', Long.valueOf(customFieldDefId))
			 }
			 eq("isEnabled", new Integer(1))
			 cacheable(true)
			 cacheRegion('org.hibernate.cache.StandardQueryCache')
		 }
		 return fvList
	}

	String toString() { "id:${id}:value:${displayText}" }

	String prettyPrint() {
		"${displayText}"
	}

	def asJSON() {
		return new JSONObject(
			id: id,
			displayText: displayText,
			isEnabled: isEnabled,
			createdDate: AdminObjectFormatter.standardDateDisplay(createdDate),
			customFieldDefinitionId: customFieldDefinition?.id
		)
	}

	def asJSONRef() {
		return new JSONObject(
			displayText: displayText,
			isEnabled: isEnabled,
		)
	}
}
