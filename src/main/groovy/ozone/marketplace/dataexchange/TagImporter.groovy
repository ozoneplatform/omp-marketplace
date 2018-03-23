package ozone.marketplace.dataexchange

import org.grails.web.json.JSONObject

import marketplace.Tag
/**
 * To change this template use File | Settings | File Templates.
 */
class TagImporter extends AbstractImporter{

    public TagImporter() {
        super("tag")
    }

    def findExistingObject(def json) {
        return Tag.findByTitle(json.title)
    }

    def createNewObject() {
        return new Tag()
    }

    //incoming icons should never override icons for existing tags
    @Override
    protected boolean shouldUpdate(JSONObject a, def b) {
        false
    }

    //titles are unique, stable identifiers of tags
    protected def getIdentifier(def tag) { tag?.title }
}
