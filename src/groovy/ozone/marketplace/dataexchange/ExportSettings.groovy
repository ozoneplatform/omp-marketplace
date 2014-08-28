package ozone.marketplace.dataexchange

import grails.converters.JSON
import marketplace.Helper
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Simple class to hold settings that determine what will be exported.
 */
class ExportSettings {
    enum Profiles { NONE, ALL, ASSOCIATED }
    Profiles includeProfiles = Profiles.NONE
    boolean includeStates = false
    boolean includeTypes = false
    boolean includeCategories = false
    boolean includeCustomFieldDefs = false
    boolean includeServiceItems = false
    boolean includeComments = false
    boolean includeRequired = false
    boolean includeContactTypes = false
    Date editedSinceDate = null
    List<Long> serviceItemIds

    ExportSettings() {}

    /**
     * Creates an ExportSettings object from the params that are passed to an export action
     * @param params the map of params that were passed to the export action
     * @return an ExportSettings that has the corresponding flags set
     */
    static ExportSettings createFromParams(def params, def relationshipService) {
        ExportSettings newSettings = new ExportSettings(params, relationshipService)
        return newSettings
    }

    /**
     * Do not call this directly; use createFromParams() instead for the more descriptive name
     */
    private ExportSettings(def params, def relationshipService) {
        // If JSON was provided, just use that
        if (params.containsKey('json')) {
            JSONObject json = JSON.parse(params.json)
            applyJSONSettings(json, relationshipService)
        } else {
            // If there was no JSON, check the legacy parameters
            applyParamSettings(params)
        }

        if (params.containsKey('lastPull')) {
            editedSinceDate = Helper.parseExternalDate(params.lastPull)
        }
    }

    private void applyJSONSettings(JSONObject json, def relationshipService) {
        // Default to everything; this is different than when the params object is used directly
        includeProfiles = Profiles.ALL
        includeStates = true
        includeTypes = true
        includeCategories = true
        includeCustomFieldDefs = true
        includeServiceItems = true
        includeComments = true
        includeRequired = true
        includeContactTypes = true

        if (!json.isNull("serviceItems")) {
            serviceItemIds = json.optJSONArray("serviceItems")

            List<Long> serviceItemIdList = serviceItemIds.collect{it as Long}
            if (serviceItemIds == null || serviceItemIds.isEmpty()) {
                // The serviceItems key was not an array, so it must be a true/false
                // to indicate that all listings should be included/excluded
                includeServiceItems = json.serviceItems
            } else {
                serviceItemIds = relationshipService.getAllRequiredChildren(serviceItemIdList)
            }
        }
        JSONObject options = json.optJSONObject("options")
        if (!options.equals(null)) {
            if (!options?.isNull("exportProfiles")) {
                if (options.exportProfiles == "all" || options.exportProfiles == true) {
                    includeProfiles = Profiles.ALL
                } else if (options.exportProfiles == "none" || options.exportProfiles == false) {
                    includeProfiles = Profiles.NONE
                } else if (options.exportProfiles == "associated") {
                    includeProfiles = Profiles.ASSOCIATED
                }
            }
            if (!options.isNull("exportRatings")) {
                includeComments = options.exportRatings
            }
        }
    }

    private void applyParamSettings(def params) {
        applySimpleIncludeSettings(params)
        if (params.containsKey('serviceItems')) {
            includeServiceItems = params.serviceItems
        }

        // If nothing was explicitly selected, then select everything. We can't make everything
        // default to true because if only one thing is selected then everything else should be false
        boolean nothingIncluded = ![
                includeProfiles != Profiles.NONE,
                includeStates,
                includeTypes,
                includeCategories,
                includeCustomFieldDefs,
                includeServiceItems,
                includeComments].any()
        if (nothingIncluded) {
            includeProfiles = Profiles.ALL
            includeStates = true
            includeTypes = true
            includeCategories = true
            includeCustomFieldDefs = true
            includeServiceItems = true
            includeComments = true
        }

    }

    private void applySimpleIncludeSettings(def options) {
        if (options.containsKey('profiles')) {
            includeProfiles = (options.profiles ? Profiles.ALL : Profiles.NONE)
        }
        if (options.containsKey('states')) {
            includeStates = options.states
        }
        if (options.containsKey('types')) {
            includeTypes = options.types
        }
        if (options.containsKey('categories')) {
            includeCategories = options.categories
        }
        if (options.containsKey('customFieldDefs')) {
            includeCustomFieldDefs = options.customFieldDefs
        }
        if (options.containsKey('comments')) {
            includeComments = options.comments
        }
    }
}
