package ozone.marketplace.dataexchange

import marketplace.State

class StateImporter extends AbstractImporter {

    public StateImporter() {
        super("state")
    }

    def findExistingObject(def json) {
        return State.findByUuid(json.uuid)
    }

    def createNewObject() {
        return new State()
    }
}
