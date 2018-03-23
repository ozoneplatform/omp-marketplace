package marketplace.dataexchange

import marketplace.Helper
import marketplace.RelationshipService
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.ExportSettings
import spock.lang.Specification

class ExportSettingsSpec extends Specification{

    void testDefaults() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([:], null)

        then:
        settings.includeCategories
        ExportSettings.Profiles.ALL == settings.includeProfiles
        settings.includeServiceItems
        settings.includeTypes
        settings.includeStates
        settings.includeCustomFieldDefs
        settings.includeComments
        null == settings.editedSinceDate
        null == settings.serviceItemIds
    }

    void testCategoriesOnly() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([categories: true], null)
        then:
        settings.includeCategories
        ExportSettings.Profiles.NONE == settings.includeProfiles
        !settings.includeServiceItems
        !settings.includeTypes
        !settings.includeStates
        !settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testStatesOnly() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([states: true], null)
        then:
        !settings.includeCategories
        ExportSettings.Profiles.NONE == settings.includeProfiles
        !settings.includeServiceItems
        !settings.includeTypes
        settings.includeStates
        !settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testProfilesOnly() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([profiles: true], null)
        then:
        !settings.includeCategories
        ExportSettings.Profiles.ALL == settings.includeProfiles
        !settings.includeServiceItems
        !settings.includeTypes
        !settings.includeStates
        !settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testServiceItemsOnly() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([serviceItems: true], null)
        then:
        !settings.includeCategories
        ExportSettings.Profiles.NONE == settings.includeProfiles
        settings.includeServiceItems
        !settings.includeTypes
        !settings.includeStates
        !settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testTypesOnly() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([types: true], null)
        then:
        !settings.includeCategories
        ExportSettings.Profiles.NONE == settings.includeProfiles
        !settings.includeServiceItems
        settings.includeTypes
        !settings.includeStates
        !settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testCustomFieldDefsOnly() {
        when:
        ExportSettings settings = ExportSettings.createFromParams([customFieldDefs: true], null)
        then:
        !settings.includeCategories
        ExportSettings.Profiles.NONE == settings.includeProfiles
        !settings.includeServiceItems
        !settings.includeTypes
        !settings.includeStates
        settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testLastPull() {
        when:
        final String dateStr = "1999-12-31T23:59:59Z"
        ExportSettings settings = ExportSettings.createFromParams([lastPull: dateStr], null)
        then:
        Helper.parseExternalDate(dateStr) == settings.editedSinceDate
    }

    void testSpecifiedServiceItems() {
        when:
        List<Integer> ids = [1, 17, 9876]
        def relationship = Mock(RelationshipService) {
            getAllRequiredChildren(*_) >> { List<Long> L ->
                return L[0]
            }
        }
        JSONObject json = new JSONObject()
        json.put("serviceItems", new JSONArray(ids))
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], relationship)
        then:
        ids.equals(settings.serviceItemIds)
    }

    void testJsonReplacesParameters() {
        when:
        JSONObject json = new JSONObject([types: true])
        // Simulate a request that has categories specified in the request, but types specified in the JSON
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString(), categories: true, states: false], null)
        then:
        // If JSON is present, then all other request parameters should be ignored
        settings.includeTypes
        settings.includeCategories
        ExportSettings.Profiles.ALL == settings.includeProfiles
        settings.includeServiceItems
        settings.includeStates
        settings.includeCustomFieldDefs
        settings.includeComments
    }

    void testNoProfiles() {
        when:
        JSONObject json = new JSONObject()
        setExportProfiles(json, "none")
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], null)
        then:
        settings.includeTypes
        settings.includeCategories
        ExportSettings.Profiles.NONE == settings.includeProfiles
        settings.includeServiceItems
        settings.includeStates
        settings.includeCustomFieldDefs
        settings.includeComments
    }

    void testAssociatedProfiles() {
        when:
        JSONObject json = new JSONObject()
        setExportProfiles(json, "associated")
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString(), categories: true], null)
        then:
        ExportSettings.Profiles.ASSOCIATED == settings.includeProfiles
    }

    void testIncludeComments() {
        when:
        JSONObject json = new JSONObject()
        setExportRatings(json, false)
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], null)
        then:
        settings.includeTypes
        settings.includeCategories
        ExportSettings.Profiles.ALL == settings.includeProfiles
        settings.includeServiceItems
        settings.includeStates
        settings.includeCustomFieldDefs
        !settings.includeComments
    }

    void testIncludeRequired() {
        when:
        List<Long> id = [0]
        def relationship = Mock(RelationshipService) {
            getAllRequiredChildren(*_) >> { List<Long> L ->
                assert id == L[0]
                return [0, 1]
            }
        }

        JSONObject json = new JSONObject([serviceItem: true])
        json["serviceItems"] = new JSONArray(id)
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], relationship)
       then:
       2 == settings.serviceItemIds.size()
    }

    private void setExportProfiles(JSONObject json, String value) {
        if (json.isNull("options")) {
            json.put("options", new JSONObject())
        }
        json.options.put("exportProfiles", value)
    }

    private void setExportRatings(JSONObject json, boolean value) {
        if (json.isNull("options")) {
            json.put("options", new JSONObject())
        }
        json.options.put("exportRatings", value)
    }
}
