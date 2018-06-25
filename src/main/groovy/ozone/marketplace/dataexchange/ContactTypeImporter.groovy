package ozone.marketplace.dataexchange

import marketplace.ContactType

class ContactTypeImporter extends AbstractImporter{

    public ContactTypeImporter() {
        super("contact type")
    }

    def findExistingObject(def json) {
        return ContactType.findByTitle(json.title)
    }

    def createNewObject() {
        return new ContactType()
    }

    protected def getIdentifier(def contactType) {
        contactType?.title
    }
}
