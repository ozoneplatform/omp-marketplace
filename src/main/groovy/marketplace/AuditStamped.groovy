package marketplace

import javax.annotation.Nullable

import org.grails.web.json.JSONObject


abstract class AuditStamped {

    Long createdBy
    Date createdDate = new Date()

    Long editedBy
    Date editedDate = new Date()

    static mapping = {
        createdBy column: 'created_by_id'
        editedBy column: 'edited_by_id'
    }

    static constraints = {
        createdBy nullable: true, bindable: false
        createdDate nullable: true, editable: false, bindable: false

        editedBy nullable: true, bindable: false
        editedDate nullable: true, editable: false, bindable: false
    }

    @Nullable
    Profile findCreatedByProfile() {
        if (!createdBy) return null

        Profile.findById(createdBy)
    }

    @Nullable
    Profile findEditedByProfile() {
        if (!editedBy) return null

        Profile.findById(editedBy)
    }

    protected JSONObject marshall(Map map) {
        appendAuditStamp(new JSONObject(map))
    }

    protected JSONObject appendAuditStamp(JSONObject jsonObject) {
        jsonObject.put("createdDate", createdDate ?: new Date())
        if (createdBy) {
            Profile createdBy_ = findCreatedByProfile()
            jsonObject.put("createdBy", new JSONObject(id: createdBy_?.id, name: createdBy_?.display(), username: createdBy_?.username))
        }

        jsonObject.put("editedDate", editedDate ?: new Date())
        if (editedBy) {
            Profile editedBy_ = findEditedByProfile()
            jsonObject.put("editedBy", new JSONObject(id: editedBy_?.id, name: editedBy_?.display(), username: editedBy_?.username))
        }

        jsonObject
    }

}
