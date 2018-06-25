package marketplace

import org.grails.web.json.JSONObject

class ChangeDetail implements Serializable, ToJSON {

    final static Integer MAX_VALUE_LENGTH = 4000

    String fieldName
    String oldValue
    String newValue

    static belongsTo = [serviceItemActivity: ServiceItemActivity]

    static constraints = {
        fieldName(blank: false, nullable: false, maxSize:255)
        oldValue(nullable: true, maxSize: MAX_VALUE_LENGTH)
        newValue(nullable: true, maxSize: MAX_VALUE_LENGTH)
    }

    static mapping = {
        batchSize:50
        cache true
    }

    String toString() { "id:${id} field ${fieldName} was ${oldValue} now is ${newValue}" }

    @Override
    JSONObject asJSON() {
        new JSONObject([id         : id,
                        displayName: Constants.getSiListingName(fieldName),
                        fieldName  : fieldName,
                        oldValue   : oldValue,
                        newValue   : newValue])
    }

    public void setOldValue(String oldValue) {
        this.oldValue = truncateValue(oldValue)
    }

    public void setNewValue(String newValue) {
        this.newValue = truncateValue(newValue)
    }

    static String truncateValue(String fieldValue) {
        fieldValue && fieldValue.size() > MAX_VALUE_LENGTH ? fieldValue.substring(0, MAX_VALUE_LENGTH) : fieldValue
    }
}
