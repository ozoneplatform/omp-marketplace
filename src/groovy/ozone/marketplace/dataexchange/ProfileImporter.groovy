package ozone.marketplace.dataexchange

import marketplace.Profile
import marketplace.ImportStatus

class ProfileImporter extends AbstractImporter {
    def profiles
    def importAll
    ImportStatus.Summary stats

    public ProfileImporter() {
        super("profile")
    }

    def findExistingObject(def json) {
        return Profile.findByUsername(json.username)
    }

    def createNewObject() {
        return new Profile()
    }

    def loadProfiles(def profiles, ImportStatus.Summary stats, def importAll) {
        this.profiles = profiles
        this.stats = stats
        this.importAll = importAll
        if (importAll) {
            return importFromJSONArray(profiles, stats)
        }
        return []
    }

    protected def getIdentifier(def object) {
        // Profiles are best identified by the username
        return object?.username
    }

}
