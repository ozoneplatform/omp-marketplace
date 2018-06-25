package marketplace.dataexchange

import grails.testing.gorm.DataTest
import marketplace.ImportStatus
import marketplace.Profile
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.ProfileImporter
import spock.lang.Specification

class ProfileImporterTest extends Specification implements DataTest{

    ProfileImporter profileImporter

    void setup() {
//        FakeAuditTrailHelper.install()

        mockDomain(Profile)
        profileImporter = new ProfileImporter()
    }

    void testLoadProfiles() {
        when:
        JSONArray profilesJSON = makeProfilesJSON()
        ImportStatus stats = new ImportStatus()

        profileImporter.loadProfiles(profilesJSON, stats.profiles, false)
        then:
        0 == stats.profiles.failed
        0 == stats.profiles.notUpdated
        0 == stats.profiles.updated
        0 == stats.profiles.created

        when:
        stats = new ImportStatus()
        profileImporter.loadProfiles(profilesJSON, stats.profiles, true)
        then:
        0 == stats.profiles.failed
        0 == stats.profiles.notUpdated
        0 == stats.profiles.updated
        1 == stats.profiles.created
    }

    private JSONArray makeProfilesJSON() {
        return new JSONArray([
            new JSONObject(
                id: 1,
                username: "testUser1",
                displayName: "Test User 1"
            )
        ])
    }
}
