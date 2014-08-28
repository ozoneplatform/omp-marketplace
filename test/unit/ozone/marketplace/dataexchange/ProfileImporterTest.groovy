package ozone.marketplace.dataexchange

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import marketplace.ImportStatus
import marketplace.Profile

import marketplace.testutil.FakeAuditTrailHelper

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

@TestMixin(DomainClassUnitTestMixin)
class ProfileImporterTest {

    ProfileImporter profileImporter

    void setUp() {
        FakeAuditTrailHelper.install()

        mockDomain(Profile)
        profileImporter = new ProfileImporter()
    }

    void testLoadProfiles() {
        JSONArray profilesJSON = makeProfilesJSON()
        ImportStatus stats = new ImportStatus()

        profileImporter.loadProfiles(profilesJSON, stats.profiles, false)
        assertEquals 0, stats.profiles.failed
        assertEquals 0, stats.profiles.notUpdated
        assertEquals 0, stats.profiles.updated
        assertEquals 0, stats.profiles.created

        stats = new ImportStatus()
        profileImporter.loadProfiles(profilesJSON, stats.profiles, true)
        assertEquals 0, stats.profiles.failed
        assertEquals 0, stats.profiles.notUpdated
        assertEquals 0, stats.profiles.updated
        assertEquals 1, stats.profiles.created
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
