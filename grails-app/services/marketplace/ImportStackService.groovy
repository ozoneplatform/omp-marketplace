package marketplace

import grails.converters.JSON

import marketplace.configuration.MarketplaceApplicationConfigurationService

import ozone.utils.User

class ImportStackService {

    boolean transactional = true

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    RelationshipService relationshipService

    ServiceItemService serviceItemService

    /** Parse a stack descriptor into the stack and its component widgets
     @param exportedStack the file that OWF creates when the user does an "Export"
     @param owner the user that will be the owner of all of the widgets
     @return a list of new listings from the stack descriptor. The stack is always the first element, and if
      there are any new widgets described in the descriptor they are the following elements.
     */
    ServiceItem importStackDescriptor(def exportedStack, User owner) {

        def jsonText = (exportedStack =~ /(?s)var data = (.*stackContext.*);\s+var jsonData/)
        if (!jsonText) {
            throw new IllegalArgumentException("App to import does not match the expected format")
        }

        return importStack(jsonText[0][1], owner).item
    }

    /**
     * Creates a stack serviceItem from Json
     * @param json JSON representation of the stack
     * @param owner User creating the serviceItem
     * @return item (stack serviceItem), isNew (true if serviceItem did not already exist)
     */
    def importStack(def json, User owner) {
        // Parse the JSON
        def stackData = JSON.parse(json)
        def message = ""

        ServiceItem stack = serviceItemService.findByOwfProperty(stackData.stackContext, 'stackContext')

        if (stack) {
            message += "The App is already in the Store. "
            // Check to see if the JSON description has changed
            if (stack.owfProperties.stackDescriptor != json) {
                if (stack.isAuthor(owner.username)) {
                    // Repopulate with current JSON if owner is the author
                    stackFromJson(stack, stackData, json)
                    message += "The Store updated this App with the current App configuration. "
                } else {
                    message += "You are not the owner, so you cannot update the App listing with new information. "
                }

            }
            return [item: stack, isNew: false, msg: message]
        }
        stack = new ServiceItem()
        stack.owfProperties = new OwfProperties()
        stackFromJson(stack, stackData, json)
        serviceItemService.populateDefaults(stack, owner)
        return [item: stack, isNew: true, msg: "The App has been added to the store. Click " +
                "Submit to send the App for administrator approval. Edit the App by clicking Edit under the Admin tab. " +
            "View App details under the Details tab including all associated App Components. "]

    }

    /**
     * Creates a widget serviceItem from Json object
     * @param widgetData parsed JSON representation of the widget
     * @param owner User creating the serviceItem
     * @return item (serviceItem), isNew (true if serviceItem did not already exist)
     */
    def importWidget(def widgetData, User owner) {
        // If there is already a widget with that universal name, use the existing widget instead
        ServiceItem widget = serviceItemService.findByUniversalName(widgetData.universalName)
        if (widget) {
            Profile profile = Profile.findByUsername(owner.username)
            if (widget.isAuthor(profile)) {
                // Update the widget from JSON
                updateWidgetFromJson(widget, widgetData)

            }
            return ['item': widget, 'isNew': false]
        }
        widget = new ServiceItem()
        widgetFromJson(widget, widgetData)
        serviceItemService.populateDefaults(widget, owner)
        return ['item': widget, 'isNew': true]
    }

    /**
     * Imports and saves a widget and returns the saved item.
     * @param json JSON object representation of the widget
     * @param owner
     * @return serviceItem
     */
    ServiceItem importAndSaveWidget(def json, User owner) {

        ServiceItem serviceItem = (this.&importWidget(json, owner)).item
        Profile profile = Profile.findByUsername(owner.username)

        if (serviceItem.isAuthor(profile)) {
            serviceItemService.save(serviceItem, owner.username)
        }
        return serviceItem

    }

    def importComponentsFromStack(ServiceItem stack, User owner) {
        return importComponents(stack, JSON.parse(stack.owfProperties.stackDescriptor).widgets,
            owner)
    }

    /**
     * Create the widgets (components) associated with the imported stack and update relationships
     * @param data
     * @param owner
     * @return components List
     */
    def importComponents(def stack, def widgets, User owner) {
        def item
        if (widgets?.size() > 0) {
            List<ServiceItem> components = widgets.collect {
                // Import the widget and save it
                importAndSaveWidget(it, owner)
            }
            relationshipService.addOrRemoveRequiresWithProfile(stack.id, null, components*.id, false)
            return components

        } else {
            // Return null if no components
            return null
        }
    }

    /**
     * Populate serviceItem (stack) with JSON
     * @param stack serviceItem of type stack
     * @param json original JSON representation of the stack
     * @param data stack as JSON object (parsed)
     * @return updated serviceItem
     */
    def stackFromJson(def stack, def data, def json) {

        stack.with {
            title = maybeNull(data.name)
            description = maybeNull(data.description)
            imageLargeUrl = maybeNull(data.imageUrl)
            owfProperties.stackContext = maybeNull(data.stackContext)
            owfProperties.stackDescriptor = json
            types = Types.findByTitle("OZONE App")
        }
        return stack
    }

    def intentsFromJson(data) {

        def intents = new LinkedHashMap()
        data.intents.send.each {
            def action = it.action
            it.dataTypes.each {
                //If we find the action in the Map then add dataType with send=true since we're doing send intents now
                if (intents[action]) {
                    intents[action].put(it, [send: true, receive: false])
                }
                //If no action found then add the action and the dataType as part of that action with appropriate send/receive
                else {
                    intents.put(action, [(it): [send: true, receive: false]])
                }
            }
        }

        data.intents.receive.each {
            def action = it.action
            it.dataTypes.each {
                if (intents[action]) {
                    //If we find the action and the dataType then it was created above in the send loop, so just set receive=true
                    if (intents[action][it]) {
                        intents[action][it]['receive'] = true
                    }
                    //If we did find the action but no dataType then we didn't create it yet so set send=false and receive=true
                    else {
                        intents[action].put(it, [send: false, receive: true])
                    }
                }
                //We didn't find any action so add it and the current dataType with appropriate send and receive vals
                else {
                    intents.put(action, [(it): [send: false, receive: true]])
                }
            }
        }

        def intentSet = [] as Set
        //Iterate over the map, create new intents, and aggregate them into a single set
        for (action in intents.keySet()) {
            for (dataType in intents[action].keySet()) {
                def dataTypeObj, actionObj

                //If dataType or action doesn't exist create a new one
                dataTypeObj = IntentDataType.findByTitle(dataType)
                if (!dataTypeObj) {
                    dataTypeObj = new IntentDataType(title: dataType)
                    dataTypeObj.save()
                }

                actionObj = IntentAction.findByTitle(action)
                if (!actionObj) {
                    actionObj = new IntentAction(title: action)
                    actionObj.save()
                }

                def intentData = intents[action][dataType]
                def newIntent = new Intent(dataType: dataTypeObj, action: actionObj, send: intentData["send"], receive: intentData["receive"])
                intentSet.add(newIntent)
            }
        }

        return intentSet

    }

    /**
     * Populate new serviceItem (widget) with JSON
     * @param widget serviceItem of type widget
     * @param data JSON object (parsed) representation of the widget
     * @return updated serviceItem
     */
    def widgetFromJson(def widget, def data) {

        def intentSet = intentsFromJson(data)

        widget.with {
            owfProperties = new OwfProperties()
            title = data.displayName
            types = Types.findByTitle("App Component")
            description = maybeNull(data.description)
            launchUrl = data.widgetUrl
            imageSmallUrl = data.imageUrlSmall
            imageMediumUrl = data.imageMediumUrl ?: data.imageUrlLarge
            imageLargeUrl = data.imageUrlLarge
            versionName = maybeNull(data.widgetVersion)
            owfProperties.with {
                universalName = maybeNull(data.universalName)
                singleton = data.singleton
                background = data.background
                it.mobileReady = !!data.mobileReady
                visibleInLaunch = data.visible
                descriptorUrl = maybeNull(data.descriptorUrl)
                width = data.width
                height = data.height
            }

            intentSet.each { owfProperties.addToIntents(it) }
        }

        return widget

    }

    /**
     * Populate existing serviceItem (widget) with JSON
     * @param widget serviceItem of type widget
     * @param data JSON object (parsed) representation of the widget
     * @return updated serviceItem
     */
    def updateWidgetFromJson(def widget, def data) {

        def intentSet = intentsFromJson(data)

        widget.with {
            title = data.displayName
            description = maybeNull(data.description)
            launchUrl = data.widgetUrl
            imageSmallUrl = data.imageUrlSmall
            imageLargeUrl = data.imageUrlLarge
            versionName = maybeNull(data.widgetVersion)
            owfProperties.with {
                universalName = maybeNull(data.universalName)
                singleton = data.singleton
                background = data.background
                mobileReady = !!data.mobileReady
                visibleInLaunch = data.visible
                descriptorUrl = maybeNull(data.descriptorUrl)
                width = data.width
                height = data.height
            }

            owfProperties.intents.clear()
            intentSet.each { owfProperties.addToIntents(it) }
        }

        return widget

    }




    private String maybeNull(def value) {
        // JSON.parse() creates a JSONObject.NULL when a JSON object is imported that has a null value for a key.
        // JSONObject.NULL == null, but JSONObject.toString() yields the string "null".
        return (value?.equals(null)) ? null : value;
    }

    /**
     * Check for invalid URLs so we can return to client before attempting a save.
     * @param url
     * @return
     */
    def isValidURL(def url) {
        def valid = true
        // This validator will accept a null URL - checks to see if a non-null URL validates
        if (url != null && !url.equals(null)) {

            if (url?.trim()?.size() > 0) {
                valid = url.matches(Constants.URL_PATTERN)
            }
        }

        return valid
    }


    def validateJson(def json) {

        def data = JSON.parse(json)

        def message = "All URLs must be fully qualified. "

        // Currently validating the three URLs which must meet domain constraints in the store but can be relative paths in OWF
        data.widgets.collect {

            if (!isValidURL(it.widgetUrl)) {
                throw new IllegalArgumentException(message + "App Component ${it.displayName}: widgetUrl '${it.widgetUrl}' is invalid.")

            }
            if (!isValidURL(it.imageUrlLarge)) {
                throw new IllegalArgumentException(message + "App Component ${it.displayName}: imageUrlSmall '${it.imageUrlLarge}' is invalid.")

            }
            if (!isValidURL(it.imageUrlSmall)) {
                throw new IllegalArgumentException(message + "App Component ${it.displayName}: imageUrlSmall '${it.imageUrlSmall}' is invalid.")

            }
        }

    }
}
