package ozone.marketplace.dataexchange

import marketplace.Types
import org.grails.web.json.JSONObject

class TypeImporter extends AbstractImporter {
    public TypeImporter() {
        super("type")
    }

    def findExistingObject(def json) {
        return Types.findByUuid(json.uuid)
    }

    def createNewObject() {
        return new Types()
    }

    protected boolean shouldUpdate(JSONObject json, def existingMatch) {
        if (existingMatch.isPermanent) {
            return false
        }
        return super.shouldUpdate(json, existingMatch)
    }
}
