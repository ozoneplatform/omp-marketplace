package marketplace

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject


class ImportStatus {

    class Summary {
        String name
        int created
        int updated
        int notUpdated
        int failed
        def messages = []

        Summary(String n) {
            name = n
        }

        def asJSON() {
            def jsonObj = new JSONObject(
                name: name,
                created: created,
                updated: updated,
                notUpdated: notUpdated,
                failed: failed,
                messages: ((messages == null) ? new JSONArray() : new JSONArray(messages?.collect { it }))
            )
        }
    }

    boolean success = true   // did it work
    def messages = [] // general messages
    def categories = new Summary("categories")
    def types = new Summary("types")
    def states = new Summary("states")
    def customFieldDefs = new Summary("customFieldDefs")
    def profiles = new Summary("profiles")
    def serviceItems = new Summary("serviceItems")
    def relationships = new Summary("relationships")
    def agencies = new Summary('agencies')
    def contactTypes = new Summary('contactTypes')
    def tags = new Summary('tags')
    def entities = [ categories, types, states, customFieldDefs,
                    profiles, serviceItems, relationships,
                    agencies, contactTypes]

    def asJSON() {
        def currJSON = new JSONObject(
            success: success,
            messages: ((messages == null) ? new JSONArray() : new JSONArray(messages?.collect { it })),
            categories: categories.asJSON(),
            types: types.asJSON(),
            states: states.asJSON(),
            customFieldDefs: customFieldDefs.asJSON(),
            profiles: profiles.asJSON(),
            serviceItems: serviceItems.asJSON(),
            relationships: relationships.asJSON(),
            agencies: agencies.asJSON(),
            contactTypes: contactTypes.asJSON(),
            tags: tags.asJSON()
        )
        return currJSON
    }

    def String toString() {
        return asJSON()
    }

   	/**
   	 * Create a summary message from success and each sub-status
   	 * @return
   	 */
   	public String getSummaryMessage() {
       def msgSummary = []
       int actionCount = 0
       int successCount = 0
       int failCount = 0
       if (messages?.size() > 0) msgSummary << messages
       entities.each {
           actionCount += getActionCount(it)
           successCount += getSuccessCount(it)
           failCount += getFailCount(it)
           if (it.created > 0) msgSummary << "${it.created} " + (it.name?:"things") + " created"
           if (it.updated > 0) msgSummary << "${it.updated} " + (it.name?:"things") + " updated"
           if (it.notUpdated > 0) msgSummary << "${it.notUpdated} " + (it.name?:"things") + " were not updated"
           if (it.failed > 0) msgSummary << "${it.failed} " + (it.name?:"things") + " failed"
           if (it.failed > 0) {
               this.success = false
               if (it.messages?.size() > 0) {
                   msgSummary << it.messages
               }
           }
       }
       if (actionCount <= 0 && msgSummary.size() <= 0) msgSummary << "No changes"

       def header
       if ((success && failCount > 0) || (!success && successCount > 0)) {
           header = "Completed with errors: "
       }
       else if (success) {
           header = "Success: "
       }
       else {
           header = "Failed: "
       }
       return header + msgSummary.join('; ')
   	}

    int getActionCount(Summary s) {
        if (!s) return 0
        return s.created + s.updated + s.notUpdated + s.failed
    }

    int getSuccessCount(Summary s) {
        if (!s) return 0
        return s.created + s.updated //+ s.notUpdated ?
    }

    int getFailCount(Summary s) {
        if (!s) return 0
        return s.failed
    }

}
