package ozone.marketplace.dataexchange

import org.grails.web.json.JSONObject

import marketplace.Agency
/**
 * To change this template use File | Settings | File Templates.
 */
class AgencyImporter extends AbstractImporter{

    public AgencyImporter() {
        super("agency")
    }

    def findExistingObject(def json) {
        return Agency.findByTitle(json.title)
    }

    def createNewObject() {
        return new Agency()
    }

    //incoming icons should never override icons for existing agencies
    @Override
    protected boolean shouldUpdate(JSONObject a, def b) {
        false
    }

    //titles are unique, stable identifiers of agencies
    protected def getIdentifier(def agency) { agency?.title }
}
