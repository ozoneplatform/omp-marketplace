package ozone.marketplace.dataexchange

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import marketplace.Helper
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import marketplace.RelationshipService


@TestMixin(GrailsUnitTestMixin)
class ExportSettingsTest {

    void testDefaults() {
        ExportSettings settings = ExportSettings.createFromParams([:], null)
        assertTrue settings.includeCategories
        assertEquals ExportSettings.Profiles.ALL, settings.includeProfiles
        assertTrue settings.includeServiceItems
        assertTrue settings.includeTypes
        assertTrue settings.includeStates
        assertTrue settings.includeCustomFieldDefs
        assertTrue settings.includeComments
        assertNull settings.editedSinceDate
        assertNull settings.serviceItemIds
    }

    void testCategoriesOnly() {
        ExportSettings settings = ExportSettings.createFromParams([categories: true], null)
        assertTrue settings.includeCategories
        assertEquals ExportSettings.Profiles.NONE, settings.includeProfiles
        assertFalse settings.includeServiceItems
        assertFalse settings.includeTypes
        assertFalse settings.includeStates
        assertFalse settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testStatesOnly() {
        ExportSettings settings = ExportSettings.createFromParams([states: true], null)
        assertFalse settings.includeCategories
        assertEquals ExportSettings.Profiles.NONE, settings.includeProfiles
        assertFalse settings.includeServiceItems
        assertFalse settings.includeTypes
        assertTrue settings.includeStates
        assertFalse settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testProfilesOnly() {
        ExportSettings settings = ExportSettings.createFromParams([profiles: true], null)
        assertFalse settings.includeCategories
        assertEquals ExportSettings.Profiles.ALL, settings.includeProfiles
        assertFalse settings.includeServiceItems
        assertFalse settings.includeTypes
        assertFalse settings.includeStates
        assertFalse settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testServiceItemsOnly() {
        ExportSettings settings = ExportSettings.createFromParams([serviceItems: true], null)
        assertFalse settings.includeCategories
        assertEquals ExportSettings.Profiles.NONE, settings.includeProfiles
        assertTrue settings.includeServiceItems
        assertFalse settings.includeTypes
        assertFalse settings.includeStates
        assertFalse settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testTypesOnly() {
        ExportSettings settings = ExportSettings.createFromParams([types: true], null)
        assertFalse settings.includeCategories
        assertEquals ExportSettings.Profiles.NONE, settings.includeProfiles
        assertFalse settings.includeServiceItems
        assertTrue settings.includeTypes
        assertFalse settings.includeStates
        assertFalse settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testCustomFieldDefsOnly() {
        ExportSettings settings = ExportSettings.createFromParams([customFieldDefs: true], null)
        assertFalse settings.includeCategories
        assertEquals ExportSettings.Profiles.NONE, settings.includeProfiles
        assertFalse settings.includeServiceItems
        assertFalse settings.includeTypes
        assertFalse settings.includeStates
        assertTrue settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testLastPull() {
        final String dateStr = "1999-12-31T23:59:59Z"
        ExportSettings settings = ExportSettings.createFromParams([lastPull: dateStr], null)
        assertEquals Helper.parseExternalDate(dateStr), settings.editedSinceDate
    }

    void testSpecifiedServiceItems() {
        List<Integer> ids = [1, 17, 9876]
        def relationship = mockFor(RelationshipService)
        relationship.demand.getAllRequiredChildren(1) { List<Integer> L ->
            return L
        }
        JSONObject json = new JSONObject()
        json.put("serviceItems", new JSONArray(ids))
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], relationship.createMock())
        assertEquals ids, settings.serviceItemIds
    }

    void testJsonReplacesParameters() {
        JSONObject json = new JSONObject([types: true])
        // Simulate a request that has categories specified in the request, but types specified in the JSON
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString(), categories: true, states: false], null)
        // If JSON is present, then all other request parameters should be ignored
        assertTrue settings.includeTypes
        assertTrue settings.includeCategories
        assertEquals ExportSettings.Profiles.ALL, settings.includeProfiles
        assertTrue settings.includeServiceItems
        assertTrue settings.includeStates
        assertTrue settings.includeCustomFieldDefs
        assertTrue settings.includeComments
    }

    void testNoProfiles() {
        JSONObject json = new JSONObject()
        setExportProfiles(json, "none")
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], null)
        assertTrue settings.includeTypes
        assertTrue settings.includeCategories
        assertEquals ExportSettings.Profiles.NONE, settings.includeProfiles
        assertTrue settings.includeServiceItems
        assertTrue settings.includeStates
        assertTrue settings.includeCustomFieldDefs
        assertTrue settings.includeComments
    }

    void testAssociatedProfiles() {
        JSONObject json = new JSONObject()
        setExportProfiles(json, "associated")
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString(), categories: true], null)
        assertEquals ExportSettings.Profiles.ASSOCIATED, settings.includeProfiles
    }

    void testIncludeComments() {
        JSONObject json = new JSONObject()
        setExportRatings(json, false)
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], null)
        assertTrue settings.includeTypes
        assertTrue settings.includeCategories
        assertEquals ExportSettings.Profiles.ALL, settings.includeProfiles
        assertTrue settings.includeServiceItems
        assertTrue settings.includeStates
        assertTrue settings.includeCustomFieldDefs
        assertFalse settings.includeComments
    }

    void testIncludeRequired() {
        List<Long> id = [0]
        def relationship = mockFor(RelationshipService)
        relationship.demand.getAllRequiredChildren(1) { List<Long> L ->
            assertEquals id, L
            return [0, 1]
        }

        JSONObject json = new JSONObject([serviceItem: true])
        json["serviceItems"] = new JSONArray(id)
        ExportSettings settings = ExportSettings.createFromParams([json: json.toString()], relationship.createMock())
        assertEquals 2, settings.serviceItemIds.size()
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
