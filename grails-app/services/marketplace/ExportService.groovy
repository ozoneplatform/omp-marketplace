package marketplace

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.ExportSettings

class ExportService {

    boolean transactional = true
    def grailsApplication
    def stateService
    def typesService
    def categoryService
    def profileService
    def customFieldDefinitionService
    def serviceItemService
    def relationshipService

    def contactTypeRestService

    def retrieveStates(def params) {
        params.sort = 'title'
        log.debug "retrieveStates: params = ${params}"
//        Date lastPull
//        if(paramsIn.lastPull){
//            def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//            lastPull = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(paramsIn.lastPull)
//            log.debug "lastPull = ${lastPull} ${lastPull.getClass()}"
//            log.debug "lastPull = ${dateFormatter.format(lastPull)}"
//        }

//        def now = new Date()
//        def aWhileBack = now - 5

        stateService.list()

        //def tmpDate = Date.parse("yyyy-MM-dd'T'HH:mm:ssZ", "2012-02-15T18:48:28Z")
//        def tmpDate = Date.parse("yyyy-MM-dd", "2012-01-15")
//        def tmpParams = [sort: 'title', lastExportDate: lastPull]

        def results = stateService.list(params)
        log.debug "returning ${results.size()} states"
        return results
    }

    def retrieveTypes(def params) {
        params.sort = 'title'
        return typesService.list(params)
    }

    def retrieveCategories(def params) {
        params.sort = 'title'
        return categoryService.list(params)
    }

    def retrieveProfiles(def params) {
        def tmpParams = [sort: 'username']
        if (params.editedSinceDate) {
            tmpParams.editedSinceDate = params.editedSinceDate
        }
        if (grailsApplication.config.marketplace.maxProfilesToExport) {
            log.warn("retrieveProfiles: maxProfiles = ${grailsApplication.config.marketplace.maxProfilesToExport}")
            tmpParams.max = grailsApplication.config.marketplace.maxProfilesToExport
        }
        log.info("retrieveProfiles: params = ${tmpParams}")
        return profileService.list(tmpParams)
    }

    def retrieveCustomFieldDefinitions(def params) {
        params.sort = 'name'
        return customFieldDefinitionService.list(params)
    }

    def retrieveContactTypes() {
        return contactTypeRestService.getAll(null, null)
    }

    def retrieveServiceItems(ExportSettings settings) {

        List<ServiceItem> serviceItems = []
        if (settings.serviceItemIds?.size() > 0) {
            // If specific service items were listed, we'll ignore the editedSinceDate and just
            // return them in the order they were requested
            serviceItems = ServiceItem.getAll(settings.serviceItemIds)
        } else {
            def tmpParams = [sort: 'title']
            if (grailsApplication.config.marketplace.maxListingsToExport) {
                log.warn("retrieveServiceItems: maxListings = ${grailsApplication.config.marketplace.maxListingsToExport}")
                tmpParams.max = grailsApplication.config.marketplace.maxListingsToExport
            }
            if (settings.editedSinceDate) {
                tmpParams.editedSinceDate = settings.editedSinceDate
            }
            log.info("retrieveServiceItems: params = ${tmpParams}")
            serviceItems = serviceItemService.list(tmpParams)
        }
        return serviceItems
    }

    def retrieveExportData(ExportSettings settings, boolean isAdmin, String username) {
        def searchParams = [:]
        if (settings.editedSinceDate) {
            searchParams.editedSinceDate = settings.editedSinceDate
        }
        def model
        def total

        model = [:]

        if (settings.includeStates) {
            model.states = retrieveStates(searchParams)
        }
        if (settings.includeTypes) {
            model.types = retrieveTypes(searchParams)
        }
        if (settings.includeCategories) {
            model.categories = retrieveCategories(searchParams)
        }
        if (settings.includeProfiles != ExportSettings.Profiles.NONE) {
            model.profiles = retrieveProfiles(searchParams)
            log.debug "exporting ${model.profiles.size()} profiles"
        }
        if (settings.includeCustomFieldDefs) {
            model.customFieldDefs = retrieveCustomFieldDefinitions(searchParams)
        }
        if (settings.includeContactTypes) {
            model.contactTypes = retrieveContactTypes()
        }
        if (settings.includeServiceItems) {
            def serviceItems = retrieveServiceItems(settings)
            log.debug "exporting ${serviceItems.size()} serviceItems"

            // ServiceItems don't automatically put their comments in JSON, so convert the ServiceItems to JSON
            // and then add the comments if requested.
            model.serviceItems = serviceItems.collect { si ->
                JSONObject siJson = si.asLegacyJSON()
                if (settings.includeComments) {
                    siJson.put("itemComments", new JSONArray(si.itemComments.collect { comment -> comment.asJSON() }))
                }

                return siJson
            }

            // Grab the relationships
            def relationships = []
            serviceItems.each {
                //log.debug "${it.getClass()} - ${it.title} ---------------------- "
                def requiresItems = relationshipService.getRequiresItems(it.id, isAdmin, username)
                def items2 = []
                requiresItems.each { it2 ->
                    def tmpItem = [title: it2.title, uuid: it2.uuid]
                    //log.debug "${requiresItems.getClass()} - ${requiresItems}"
                    items2 << tmpItem
                }
                def relationship = [serviceItem: [title: it.title, uuid: it.uuid], requires: items2]
                //log.debug "relationship = ${relationship}"
                relationships << relationship
            }
            model.relationships = relationships
        }

        // May need to drop any unnecessary profiles
        if (settings.includeProfiles == ExportSettings.Profiles.ASSOCIATED) {
            pruneProfiles(model)
        }

        total = 0


        log.debug "model.getClass() = ${model.getClass()}"
        return model
    }

	/**
	 * Figure out which profiles are referenced by the service items and remove any that aren't
	 */
	private void pruneProfiles(Map model) {

		// Run through all the service items and collect a list of all the owners and commenters.
		Set usedProfiles = new HashSet()
		model.serviceItems?.each {si->
			si.owners?.collect(usedProfiles) {it.id};
			si.itemComments?.collect(usedProfiles) {it.author?.id}

            //the first rejectionListing is exported with the JSON, so we want to keep that
            //rejection listing's author
            if (si.rejectionListing) {
                usedProfiles << si.rejectionListing.author.id
            }
		}
		usedProfiles.remove(null)

		// Now remove all profiles from the profile list that aren't in the set of useful profiles
		Closure profileNotUsed = {profile-> !usedProfiles.contains(profile.id)}
		model.profiles.removeAll(profileNotUsed)
	}
}
