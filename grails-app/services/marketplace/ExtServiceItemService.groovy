package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.domain.ValidationException
import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.utils.User
import ozone.utils.Utils

import java.text.ParseException
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import org.springframework.transaction.annotation.Transactional

class ExtServiceItemService {

    def serviceItemService
    def accountService
    def marketplaceApplicationConfigurationService
    def customFieldDefinitionService
    def changeLogService
    def JSONDecoratorService
    def owfWidgetTypesService
    def profileService

    static final String CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL = "fieldType"
    static final String VALID_CUSTOM_FIELD_CFD_MSG = "Add a valid Custom Field JSON field 'customFieldDefinitionUuid', 'name' or 'customFieldDefinitionId'."
    static final String VALID_CUSTOM_FIELD_DD_VALUE_MSG = "Add a valid Custom Field JSON field 'value' or 'fieldValueId'."
    static final String VALID_CUSTOM_FIELD_TEXT_VALUE_MSG = "Add a valid Custom Field JSON field 'value'."

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    @Transactional
    def disable(def itemId, def userName) {
        doEnable(false, itemId, userName)
    }

    @Transactional
    def enable(def itemId, def userName) {
        doEnable(true, itemId, userName)
    }

    @Transactional
    def doEnable(boolean enableFl, def itemId, def userName) {
        def extSvc = ExtServiceItem.get(itemId)
        if (extSvc) {
            log.debug("retrieved ${extSvc}")
        }
        //Do Enable - will throw exception if extSvc does not exist
        serviceItemService.doEnable(extSvc, userName, enableFl)
    }

    // External Admins can set the approval status to approved or pending; Admins or
    // Users can only set the approval status to pending.
    @Transactional(readOnly = true)
    protected boolean approvalStatusOKForCreate(String approvalStatus) {
        boolean returnValue = false
        if (accountService.isExternAdmin()) {
            returnValue = (approvalStatus == Constants.APPROVAL_STATUSES["APPROVED"] ||
                approvalStatus == Constants.APPROVAL_STATUSES["PENDING"])
        } else {
            returnValue = (approvalStatus == Constants.APPROVAL_STATUSES["PENDING"])
        }

        return returnValue
    }

    /**
     * The public-facing create method.
     *
     */
    @Transactional(noRollbackFor = [ValidationException])
    def create(def json, def username = accountService.getLoggedInUsername(), boolean isImport = false, def contextPath = null) {
        JSONDecoratorService.preProcessJSON(json)
        def extSvc = new ExtServiceItem()
        bindFromJSON(json, extSvc, username, true, isImport, contextPath)
        if (json.serviceItem.approvalStatus == null) {
            // if no approval status is specified, the approval status will be set to pending
            extSvc.approvalStatus = Constants.APPROVAL_STATUSES["PENDING"]
        } else {
            if (approvalStatusOKForCreate(json.serviceItem.approvalStatus)) {
                extSvc.approvalStatus = json.serviceItem.approvalStatus
            } else {
                throw new PermissionException("Invalid approvalStatus of ${json.serviceItem.approvalStatus} specified")
            }
        }
        // if uuid is passed in then don't generate a new one!
        if (!extSvc.uuid) {
            extSvc.uuid = Utils.generateUUID();
        }

        save(extSvc, username, true)

        updateEnabled(json, extSvc, username)

        return extSvc
    }

    /**
     * The public-facing update method.
     *
     */
    @Transactional(noRollbackFor = [ValidationException])
    def update(def itemId, def json, def username = accountService.getLoggedInUsername(), def contextPath = null) {
        JSONDecoratorService.preProcessJSON(json)
        def extSvc = ExtServiceItem.get(itemId)
        if (extSvc) {
            log.debug("retrieved ${extSvc}")
        } else {
            log.debug("Unable to locate serviceItem $itemId")
        }

        //Check permissions - will throw exception if extSvc does not exist
        serviceItemService.checkPermissionToEdit(extSvc, username)

        // Cannot update approval status
        if (json?.serviceItem?.approvalStatus) {
            log.warn("Ignoring approval status for listing [${extSvc.uuid}]; not permitted to modify")
            json.serviceItem.remove('approvalStatus')
        }

        // bindFromJSON ignores approvalStatus which is the desired behavior
        bindFromJSON(json, extSvc, username, false, contextPath)

        // We want to trigger an event to update the lastUpdated timestamp
        // for the ExtServiceItem.  This might result in a few false positives
        // when the associated ServiceItem and/or ExtContext aren't actually
        // updated but we'd rather take a more conservative approach and make
        // sure the timestamp gets updated. (comment from Bruce Cohen)
        //extSvc.version++

        def wasUpdated = save(extSvc, username, false)

        if (updateEnabled(json, extSvc, username)) {
            wasUpdated = true
        }

        return wasUpdated
    }

    // Enables or disables the specified ExtServiceItem based on the specified JSON.
    // Returns true if the extServiceItem is updated; otherwise, returns false.
    @Transactional(readOnly = true)
    def updateEnabled(def json, def extSvc, username) {
        log.debug("updateEnabled2: isEnabled = ${json?.serviceItem?.isEnabled} extSvc.isHidden = ${extSvc.isHidden}")

        if (json?.serviceItem?.isEnabled != null) {
            // Groovy toBoolean() returns true for several string representations of true
            if (json.serviceItem.getString("isEnabled").toBoolean()) {
                if (extSvc.isHidden != 0) {
                    enable(extSvc.id, username)
                    return true
                }
            } else {
                if (extSvc.isHidden != 1) {
                    disable(extSvc.id, username)
                    return true
                }
            }
        }
        return false
    }

    /**
     * The public-facing get method.
     *
     */
    @Transactional(readOnly = true)
    def extServiceItems(params) {
        log.info params
        def model = [:]
        def c = ExtServiceItem.createCriteria()

        def list = c.list {
            and {
                if (params.id) {
                    def paramsId = params.id as Long
                    eq('id', paramsId)
                }
                if (params.systemUri)
                    eq('systemUri', params.systemUri)
                if (params.externalId)
                    eq('externalId', params.externalId)
            }
        }
        model.put("extServiceItemList", list)
        model.put("listSize", list.size())
        model.put("params", params)

        return model
    }

    /*
      * Process a JSON description of an ExtServiceItem.  This should work for
      * a create or an update.  If the given ExtServiceItem already has values
      * set for properties, this shouldn't touch them unless the incoming JSON
      * has those properties set.   This lets us perform updates with JSON that
      * is pretty sparse with only the properties we want to update
      */

    @Transactional(readOnly = true)
    def bindFromJSON(def json, ExtServiceItem extSvc, def username, def createFlag,
                     def importFlag = false, def contextPath = null) {
        log.debug "bindFromJSON - json = ${json}"
        return bindFromJSON2(json?.serviceItem, extSvc, username, createFlag, importFlag, contextPath)
    }

    @Transactional(readOnly = true)
    // TODO Combine bindFromJSON and bindFromJSON2
    def bindFromJSON2(def json, ExtServiceItem extSvc, def username, def createFlag,
                      def importFlag = false, def contextPath = null) {
        log.debug "bindFromJSON2 - json = ${json}"
        boolean isDirty = bindServiceItemFromJSON(json, extSvc, username, createFlag, importFlag, contextPath)

        [
            "externalViewUrl",
            "systemUri",
            "externalId",
            "externalEditUrl"
        ].
            each(JSONUtil.optStr.curry(json, extSvc))

        return isDirty
    }

    /*
     * Save the given ExtServiceItem.
     *
     * Returns true if listing was saved or false if there were no changes that needed to
     * be saved
     */

    @Transactional(noRollbackFor = [ValidationException])
    private def save(ExtServiceItem extSvc, def username, def createFl) {
        def returnValue = false

        extSvc.owfProperties?.owfWidgetType =
            extSvc.owfProperties?.owfWidgetType ?: owfWidgetTypesService.defaultOwfWidgetType

        if (!extSvc.owners) {
            def Profile = Profile.findByUsername(username)
            extSvc.owners = [Profile]
        }

        def autoApprove = false
        if (createFl) {
            if (accountService.isExternAdmin()) {
                if (extSvc.approvalStatus == Constants.APPROVAL_STATUSES["APPROVED"]) {
                    log.debug "ExtServiceItem will be auto approved"
                    autoApprove = true
                }
            }

        }
        extSvc.validate()
        if (extSvc.hasErrors()) {
            throw new ValidationException(fieldErrors: extSvc.errors,
                message: 'error saving ExtServiceItem')
        }

        // Our JSON binding may have left us with CustomFields that have null
        // for a value.  If we see any of these buggers, remove them
        customFieldDefinitionService.removeNullCustomFields(extSvc)

        if (!createFl) {
            serviceItemService.cleanBeforeSave(extSvc)
        }

        if (createFl || serviceItemService.isDirty(extSvc)) {
            if (!createFl) {
                serviceItemService.touch(extSvc)
            }
            returnValue = true
            serviceItemService.save(extSvc, username)
        }

        if (autoApprove) {
            log.debug "auto approving service item id = ${extSvc.id} for user ${username}"
            serviceItemService.autoApprove(extSvc, username)
            makeApprovedListingInside(extSvc, username)
        }

        return returnValue
    }

    private makeApprovedListingInside(def serviceItem, def username) {
        if (marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.INSIDE_OUTSIDE_BEHAVIOR)
            == Constants.INSIDE_OUTSIDE_ADMIN_SELECTED) {
            serviceItemService.setInsideOutside(serviceItem, username, false)
        }
    }

    // We want to consider two lists of RecommendedLayouts to be equal if they have the same elements
    // but are in a different order. This is because the list is always stored in alphabetical order and
    // may not be sent that way in the json.
    private boolean areEqual(def layouts1, def layouts2) {
        def returnValue = false

        if (layouts1 == null && layouts2 == null) {
            returnValue = true
        } else {
            if (layouts1 == null || layouts2 == null) {
                returnValue = false
            } else {
                returnValue = (layouts1.containsAll(layouts2) && layouts2.containsAll(layouts1))
            }
        }

        return returnValue
    }

    /*
     * Build a ServiceItem from JSON.
     *
     *  Updating rating, related, itemComments, rejectionListings,
     *  and serviceItemActivities, approvalStatus by external systems is not
     *  supported
     *
     *  NOTE: We do minimal validation in this method.  It is entirely possible
     *  for the updates to the ServiceItem to produce a ServiceItem which does
     *  not validate.  In addition to the normal validation rules, we may have
     *  a Type or State set to null. We'll let the calling method deal with that
     */

    def bindServiceItemFromJSON(JSONObject json, ServiceItem svc, def username,
                                def createFlag, def importFlag, def contextPath = null) {
        log.debug "bindServiceItemFromJSON - json = ${json}"

        // AML-1546 - added isOutside
        def originalVersion = svc.version ?: -1

        [
                "title",
                "description",
                "launchUrl",
                "installUrl",
                "versionName",
                "requirements",
                "dependencies",
                "organization",
                "imageSmallUrl",
                "imageMediumUrl",
                "imageLargeUrl",
                "uuid"
        ].each(JSONUtil.optStr.curry(json, svc))

        svc.isOutside = json.isOutside
        svc.opensInNewBrowserTab = !!json.opensInNewBrowserTab

        if (importFlag) {
            JSONUtil.optStr(json, svc, "approvalStatus")
        }

        // Extract Date-based properties.
        // We're not allowing external systems to set the 'createdDate' property
        if (json.has('releaseDate')) {
            if (json.releaseDate == null || json.releaseDate == JSONObject.NULL) {
                svc.releaseDate = null
            }

            else {
                try {
                    svc.setReleaseDate(json.releaseDate)
                } catch (ParseException pe) {
                    try {
                        JSONUtil.optDate(json, svc, 'releaseDate')
                    }
                    catch (ParseException pe2) {
                        throw new ValidationException(message: "Release Date sent " +
                            "(${json?.releaseDate}) " +
                            "must have time format of ${Constants.OPT_DATE_FORMAT} or " +
                            "${Constants.RELEASE_DATE_FORMAT_STRING}"
                        )
                    }
                }
            }
        }

        if (json?.has('agency')) {
            Agency agency = lookup(json.agency, Agency)
            svc.agency = agency
        }

        // Done simple properties, lets go after referenced objects
        if (json?.has("state")) {
            def state = lookup(json?.state, State)
            //log.debug "lookup(${json?.state})= ${state}"
            svc.state = state
        }

        if (json?.has("types")) {
            def types = lookup(json?.types, Types)
            //log.debug "lookup(${json?.types})= ${types}"
            if (types) {
                svc.types = types

                if (svc.imageLargeUrl.equals(null) && (svc.types.hasIcons == true)) {
                    svc.imageLargeUrl = contextPath + "/images/types/" + svc.types.id
                }

                if (svc.imageMediumUrl.equals(null) && (svc.types.hasIcons == true)) {
                    svc.imageMediumUrl = contextPath + "/images/types/" + svc.types.id
                }

                if (svc.imageSmallUrl.equals(null) && (svc.types.hasIcons == true)) {
                    svc.imageSmallUrl = contextPath + "/images/types/" + svc.types.id
                }
            } else {
                throw new ValidationException(message: "ExtServiceItem type cannot be null")
            }
        }

        if (json?.has("techPocs")) {
            svc.techPocs = json.techPocs
        } else if (json?.has("techPoc") && !json.techPoc.equals(null)) {
            svc.techPocs = [json.techPoc]
        }

        if (json?.has("docUrls") && !json.isNull("docUrls")) {
            json.docUrls.each { docUrl ->
                svc.addToDocUrls(new ServiceItemDocumentationUrl(name: docUrl.name, url: docUrl.url))
            }
        } else if (json?.has("docUrl") && !json.docUrl.equals(null)) {
            svc.addToDocUrls(new ServiceItemDocumentationUrl(name: json.docUrl, url: json.docUrl))
        }

        if (json?.has('screenshot1Url') && !json.screenshot1Url.equals(null)) {
            svc.addScreenshot(new Screenshot(smallImageUrl: json.screenshot1Url))
        }
        if (json?.has('screenshot2Url') && !json.screenshot2Url.equals(null)) {
            svc.addScreenshot(new Screenshot(smallImageUrl: json.screenshot2Url))
        }

        if (json?.has('contacts')) {
            json.contacts.each {
                if (it) {
                    ContactType type = lookup(it.type, ContactType)

                    def contact = new Contact(
                        type: type,
                        securePhone: it.securePhone,
                        unsecurePhone: it.unsecurePhone,
                        email: it.email,
                        name: it.name,
                        organization: it.organization
                    )
                    svc.addToContacts(contact)
                }
            }
        }

        Profile currentUser = profileService.currentUserProfile

        if(json?.has('tags')){

            //Loop through the tags json and create ServiceItemTag objects in memory
            def jsonTagList = json.tags.collect{
                Tag tag = Tag.findByTitle(it['tag'].title)
                new ServiceItemTag(tag: tag ?: new Tag(title: it['tag'].title), serviceItem: svc, createdBy: currentUser)
            }
            //For each ServiceItemTag in the list add it to svc if that list does not contain it
            jsonTagList.each{ ServiceItemTag serviceItemTag ->
                if(!svc.tags?.contains(serviceItemTag)){
                    svc.addToTags(serviceItemTag)
                }
            }
            //Now loop through the existing tags and add them to a list to remove if they are in the svc but not in the list
            def tagsToRemove = []
            svc.tags?.each{
                //In the servie item but not in the jcon list means to remove it
                if(!jsonTagList.contains(it)){
                    tagsToRemove << it
                }
            }
            tagsToRemove.each{ svc.removeFromTags(it) }
        }


        def owfPropertiesKey = "owfProperties"
        if (json?.has(owfPropertiesKey)) {
            if (!svc.owfProperties) {
                log.debug("creating new OwfProperties")
                // OwfProperties belongsTo the serviceItem so it will get saved when the serviceItem is saved
                svc.owfProperties = new OwfProperties()
            }
            log.debug("setting OwfProperties to ${json.owfProperties}")
            JSONUtil.optBoolean(json.get(owfPropertiesKey), svc.owfProperties, 'singleton')
            JSONUtil.optBoolean(json.get(owfPropertiesKey), svc.owfProperties, 'visibleInLaunch')
            JSONUtil.optBoolean(json.get(owfPropertiesKey), svc.owfProperties, 'background')
            JSONUtil.optBoolean(json.get(owfPropertiesKey), svc.owfProperties, 'mobileReady')
            JSONUtil.optInt(json.get(owfPropertiesKey), svc.owfProperties, 'height')
            JSONUtil.optInt(json.get(owfPropertiesKey), svc.owfProperties, 'width')
            JSONUtil.optStr(json.get(owfPropertiesKey), svc.owfProperties, 'stackContext')
            JSONUtil.optStr(json.get(owfPropertiesKey), svc.owfProperties, 'stackDescriptor')
            JSONUtil.optStr(json.get(owfPropertiesKey), svc.owfProperties, 'universalName')
            JSONUtil.optStr(json.get(owfPropertiesKey), svc.owfProperties, 'descriptorUrl')

            if (json?.has("intents")) {
                svc.owfProperties?.intents?.clear()

                json.intents.each {
                    def dataType = lookup(it.dataType, IntentDataType)
                    if (dataType == null && it.dataType?.title != null) {
                        dataType = IntentDataType.findByTitle(it.dataType.title)
                        if (dataType == null) {
                            if (it.dataType?.description.equals(null)) {
                                dataType = new IntentDataType(title: it.dataType.title)
                            } else {
                                dataType = new IntentDataType(title: it.dataType.title, description: it.dataType?.description)
                            }
                        }
                    }
                    def action = lookup(it.action, IntentAction)
                    if (action == null && it.action?.title != null) {
                        action = IntentAction.findByTitle(it.action.title)
                        if (action == null) {
                            if (it.action?.description.equals(null)) {
                                action = new IntentAction(title: it.action.title)
                            } else {
                                action = new IntentAction(title: it.action.title, description: it.action?.description)
                            }
                        }
                    }
                    if (action != null && dataType != null) {
                        def intent = new Intent(dataType: dataType, action: action, send: it.send, receive: it.receive)
                        // Use find-by-example to see if this intent already exists, otherwise use the newly created intent
                        intent = Intent.find(intent) ?: intent
                        svc.owfProperties.addToIntents(intent)
                        dataType.save()
                        action.save()
                        intent.save()
                    }
                }
            }
        }

        if (json?.has("categories")) {
            def categories = json?.categories.collect {
                def category = lookup(it, Category)
                //log.debug "lookup(${it})= ${category}"
                return category
            }
            // remove any nulls
            categories = categories.grep { it }
            def newIds = categories.collect { it.id }
            def oldIds = svc.categories.collect { it.id }
            if (newIds != oldIds) {
                log.debug("updating categories. Was ${svc.categories}. Now is ${categories}.")
                svc.categories = categories
            }
        }

        // Referenced enums
        if (json?.has("recommendedLayouts")) {
            def recommendedLayouts = json?.recommendedLayouts.collect {
                def layout = lookupEnum(RecommendedLayout, "description", it)
                log.debug "lookupEnum(${it})= ${layout}"
                return layout
            }
            // remove any nulls
            recommendedLayouts = recommendedLayouts.grep { it }
            if (!areEqual(svc.recommendedLayouts, recommendedLayouts)) {
                log.debug("updating recommendedLayouts. Was ${svc.recommendedLayouts}. Now is ${recommendedLayouts}.")
                svc.recommendedLayouts = recommendedLayouts
            }
        }


        boolean isDirty = bindCustomFieldsFromJSON(json?.customFields, svc, createFlag, originalVersion, importFlag)

        // On an import we set the isHidden flag and save. For the Add ExtServiceItem REST call we enable or
        // disable the listing separately so we generate a ServiceItemActivity.
        if (importFlag) {
            if (json?.isEnabled != null) {
                if (json.getString("isEnabled").toBoolean()) {
                    svc.isHidden = 0
                } else {
                    svc.isHidden = 1
                }
            }
        }

        def jsonOwners = null
        if (json?.has("owners")) {
            jsonOwners = json.owners
        } else if (json?.has("owner")) { // For files exported before marketplace 7.4
            jsonOwners = [json.owner]
        }
        if (jsonOwners) {
            svc.owners = []
            jsonOwners.each { ownerJson ->
                Profile owner = lookup(ownerJson.username, Profile, "username")
                if (owner) {
                    svc.owners << owner
                } else {
                    log.debug("Owner's profile does not exist.  Creating it with username '${ownerJson.username}'.")
                    User user = new User(username: ownerJson.username, name: ownerJson.name)
                    svc.owners << profileService.createProfile(user, new Date())
                }
            }
        }
        return isDirty
    }

    /*
    * If no custom fields are specified in the json, then leave the custom fields as they are but still
    * check that all required fields have values. Bruce's original version of this method removed any
    * custom fields which were not specified in the json which doesn't fit with how we handle custom fields.
    *
    * @param json
    * @param svc
    * @return
    */

    private bindCustomFieldsFromJSON(def json, def svc, def createFlag, def originalVersion, def isImport = false) {
        boolean isDirty = false

        // Retrieve list of Custom Field Definitions allowed for Item's Type
        def allowedCFDs = getAllowedCFDs(svc)

        // Build list of Custom Fields for Item
        //svc.customFields = (svc.customFields) ?: []
        for (def cfjson in json) {
            def cf, cfd
            // Figure out what CustomFieldDefinition we're referencing in the
            // incoming JSON
            def passedCFD = lookupCFD(cfjson)

            log.debug "passedCFD = ${passedCFD} - types = ${passedCFD?.types}. Null here?"

            if (!passedCFD) {
                log.warn "Incoming JSON referenced non-existing CustomFieldDefinition: ${cfjson}"
                throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, there does NOT exist a CustomFieldDefinition, ${VALID_CUSTOM_FIELD_CFD_MSG}")
            }

            log.debug "customField in JSON refers to: ${passedCFD.name}"

            // Only add Type appropriate Custom Fields to Item.
            // Ignore all others and warn.
            cfd = allowedCFDs.find { it?.id == passedCFD?.id }

            if (!cfd) {
                log.warn "Custom Field Definition ${passedCFD?.name} not allowed for this type: [${svc?.types}]"
                throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, Custom Field Definition '${passedCFD?.name}' not allowed for this type: [${svc?.types}], ${VALID_CUSTOM_FIELD_CFD_MSG}")
            }

            //Look up custom field for this service item with that definition
            cf = getCustomFieldToUpdate(svc, passedCFD)

            //Update Custom Field
            def fieldChange = false
            try {
                fieldChange = updateCustomField(cfjson, cf, passedCFD)
            } catch (ValidationException e) {
                if (isImport) {
                    // We don't care about validation exceptions on import; they just mean that a required field
                    // doesn't have a value, which is fine (it was probably grandfathered-in when the custom
                    // field was made required.
                    // But as a general rule, we'll log the change (even if we changed from nothing to nothing
                    fieldChange = true
                } else {
                    throw e
                }
            }
            if (!createFlag && fieldChange) {
                // ChangeDetails for custom fields are not created in the listener because they need special handling.
                changeLogService.saveChangeDetail(svc, fieldChange.fieldName, fieldChange.oldValue, fieldChange.newValue,
                    originalVersion + 1)
                isDirty = true
            }

            //Update Service Item Custom Fields
            updateServiceItemCustomFields(svc, cf)

        }
        if (!isImport) {
            //Validate all Required Custom Fields are indeed set
            validateRequiredCustomFields(svc, allowedCFDs)
        }
        log.debug "svc.customFields = ${svc.customFields}"

        return isDirty
    }

    /***
     * Validates Required Custom Fields
     * @param svc - Service Item which has Custom Fields List to check
     * @param allowedCFDs - The allowed custom field definitions...
     * @return
     */
    private def validateRequiredCustomFields(ServiceItem svc, List<CustomFieldDefinition> allowedCFDs) {
        //Loop through allowedCFDs to make sure any required ones got values!
        allowedCFDs.each { cfd ->
            if (cfd.isRequired) {
                def customField = svc.customFields.find { it?.customFieldDefinition.id == cfd.id }
                if (customField) {
                    log.debug "required custom field ${cfd.name}, with cfd id ${cfd.id}, has value ${customField.value}"
                } else {
                    if (cfd?.instanceOf(DropDownCustomFieldDefinition)) {
                        throw new ValidationException(message: "DROP_DOWN custom field '${cfd?.name}' requires a valid fieldValueId (id of field value) or value, where fieldValueId or value is an ENABLED fieldValue Option from ${FieldValue.dolist(cfd.id)}, for the Custom Field Definition id ${cfd.id}, ${VALID_CUSTOM_FIELD_DD_VALUE_MSG}")
                    } else {
                        throw new ValidationException(message: "${cfd.name} with Custom Field Definition id ${cfd.id} requires a value")
                    }
                }
            }
        }
    }

    /***
     *  Updates the Service Item's Custom Fields List with Custom Field
     * @param svc - Service Item to Store Custom Field to Custom Field List
     * @param cf - Custom Field to Store
     * @return
     */
    private def updateServiceItemCustomFields(ServiceItem svc, CustomField cf) {
        if (cf.id == null || !svc.customFields?.contains(cf)) {
            // Now insert new CustomFields into customFields map
            if (svc.customFields == null) {
                svc.customFields = []
            }
            svc.customFields.add cf
        } //else {
        //Update the existing one...
        //This is necessary as well if we have converted a Base CustomField to a TextCustomField
// TODO: decide whether or not we need this. I think it is making the collection dirty.
//			for(int idx = 0; idx < svc.customFields.size(); idx++){
//				if(svc.customFields[idx]?.id == cf.id){
//					svc.customFields[idx] = cf
//					break
//				}
//			}
        //}
    }

    /***
     * Update the Custom Field value
     *
     * @param cfjson - Custom Field JSON object
     * @param cf - Custom Field to Update
     * @param passedCFD - Custom Field Definition passed from JSON
     * @return
     */
    private def updateCustomField(def cfjson, CustomField cf, CustomFieldDefinition passedCFD) {
        boolean isValid = false
        def validationExceptionMsg = "Error Creating Custom Field."

        def fieldName = cf?.customFieldDefinition?.name
        def oldValue = null
        def newValue = null

        // DropDown is treated as a special case because of the ability to
        // reference fieldValueId in addition to value
        if (cf.instanceOf(DropDownCustomField)) {
            validationExceptionMsg = "DROP_DOWN custom field '${cf?.customFieldDefinition?.name}' requires a valid fieldValueId (id of field value) or value, where fieldValueId or value is an ENABLED fieldValue Option from ${FieldValue.dolist(passedCFD?.id)}, for the Custom Field Definition id ${passedCFD?.id}, ${VALID_CUSTOM_FIELD_DD_VALUE_MSG}"
            oldValue = cf.fieldValueText
            // This is the case where the json has the id of the field value
            if (cfjson?.has("fieldValueId")) {
                def fieldValueId = extractId(cfjson, "fieldValueId")
                if (StringUtils.isNotBlank(fieldValueId?.toString())) {
                    def fieldValue = FieldValue.get(fieldValueId)
                    if ((fieldValue != null) && (fieldValue.isEnabled == 1)) {
                        cf.setValue(fieldValue)
                        newValue = cf.fieldValueText
                        isValid = true
                    }
                }
            }

            if ((!isValid) && cfjson?.has("fieldValue")) {
                def values = []
                if (cfjson.get("fieldValue") instanceof JSONObject) {
                    log.debug "CFD is instance of JSONObject so pull value field"
                    values << cfjson?.fieldValue?.value
                } else {
                    log.debug "CFD is instance of JSONArray so pull array item value fields"
                    for (v in cfjson.fieldValue) {
                        values << v.value.toString()
                    }
                }


                cf.setValue((String[]) values)
                isValid = true
            }

            // This is the case where the json has the value of the field value (e.g. 'blue' for 'color')
            if ((!isValid) && (cfjson?.has("value"))) {
                // Distinguish between a single value and a list of values
                cf.setValue(cfjson.value)
                //ONUtil.optStr(cfjson, cf, "value")
                newValue = cf.fieldValueText
                // Make sure the custom field has a value if it is required.
                if ((cf.customFieldDefinition?.isRequired) && (cf.isEmpty())) {
                    validationExceptionMsg = "Required value cannot be null/blank. " + validationExceptionMsg
                }
                //TODO clarify the meaning of the line below (MS)
                if ((cf.value == null) || (cf.value.isEnabled == 1)) {
                    isValid = true
                }
            }
        } else {
            // All other CustomFields
            validationExceptionMsg = "Custom field '${cf?.customFieldDefinition?.name}' requires a valid value, for the Custom Field Definition id ${passedCFD?.id}, ${VALID_CUSTOM_FIELD_TEXT_VALUE_MSG}"
            if (cfjson?.has("value")) {
                oldValue = cf.getFieldValueText()
                newValue = JSONUtil.getStr(cfjson, "value")

                if (oldValue == newValue) {
                    log.debug "oldValue and newValue are the same"
                } else {
                    cf.setValue(newValue)
                    def cf2 = CustomField.get(cf.id)
                    log.debug "cf: set value to ${newValue}. cf.id = ${cf.id} cf = ${cf} - cf.getClass = ${cf.getClass()}"
                    if (cf2)
                        log.debug "cf2: cf2 = ${cf2} - cf2.getClass = ${cf2.getClass()}"
                }
                // Make sure the custom field has a value if it is required.
                if ((cf.customFieldDefinition?.isRequired) && cf.isEmpty()) {
                    validationExceptionMsg = "${cf?.customFieldDefinition?.name} requires a value"
                } else {
                    isValid = true
                }
            }
        }

        boolean valueChanged = !(newValue == oldValue)
        log.debug "set value for cf: ${cf}. newValue = ${newValue} oldValue = $oldValue}. valueChanged = ${valueChanged}}"

        // Make sure cf is valid
        if (!isValid) {
            throw new ValidationException(message: validationExceptionMsg)
        }

        if (valueChanged) {
            return [fieldName: fieldName, oldValue: oldValue, newValue: newValue]
        }

        return null
    }

    /****
     * Get the Custom Field to Update
     * @param svc - Service Item which will/does contain the Custom Field to Update
     * @param passedCFD - Custom Field Definition passed from JSON
     * @return
     */
    private CustomField getCustomFieldToUpdate(ServiceItem svc, CustomFieldDefinition passedCFD) {
        CustomField cf = null
        //Let's Look for a Custom Field in the Service Item's Custom Fields list that matches the
        //Passed Custom Field Definition
        if ((svc.customFields != null) && (svc.customFields.size() > 0)) {
            for (CustomField svc_cf : svc.customFields) {
                if (svc_cf?.customFieldDefinition?.id == passedCFD?.id) {
                    cf = svc_cf
                    break
                }
            }
        }

        //if no cf to update found..create a new one...
        if (!cf) {
            cf = passedCFD.styleType.newField(passedCFD.properties + [customFieldDefinition: passedCFD])
        }
        return cf
    }

    /****
     * Get Allowed Custom Field Definitions based on Types defined on Service Item
     *
     * @param svc - Service item containing the types
     * @return
     */
    private getAllowedCFDs(ServiceItem svc) {
        def allowedCFDs = customFieldDefinitionService.findByType(svc?.types)
        return allowedCFDs
    }

    /***
     * Lookup the Custom Field Definition by the Custom Field JSON Object
     *
     * @param cfjson - Custom Field JSON Object
     * @return
     */
    private def lookupCFD(def cfjson) {
        def styleTypeText = cfjson?.has(CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL) ? extractAlt(cfjson, CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL) : null

        def altNameStr = "name"
        def altCFDIdStr = "customFieldDefinitionId"

        def json_cfd_name = cfjson?.has(altNameStr) ? extractAlt(cfjson, altNameStr) : null
        def json_cfd_id = cfjson?.has(altCFDIdStr) ? extractId(cfjson, altCFDIdStr) : null
        def json_cfd_uuid = extractUuid(cfjson, 'customFieldDefinitionUuid')
        if ((json_cfd_uuid == null) && (json_cfd_id == null) && (json_cfd_name == null)) {
            throw new ValidationException(message: "In Custom Field JSON {${cfjson}},${VALID_CUSTOM_FIELD_CFD_MSG}")
        }

        def cfd = null
        // TODO: need to add uuid check here!!!!
        // Precedent Order 1: Attempt to retrieve object by uuid
        if (json_cfd_uuid) {
            log.debug("lookup custom field def with uuid = ${json_cfd_uuid}")
            cfd = CustomFieldDefinition.findByUuid(json_cfd_uuid)

            // If a uuid is specified and we don't find it, then we fail.
            if (cfd == null) {
                throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, there does NOT exist a CustomFieldDefinition with uuid '${json_cfd_uuid}'")
            }
        }

        //Precedent Order 2: if cfd_id passed in, retrieve CFD by CFD id
        if ((cfd == null) && (json_cfd_id != null) && !StringUtils.isBlank(json_cfd_id.toString())) {
            cfd = CustomFieldDefinition.get(json_cfd_id)

            // If a database id is specified and we don't find it, we will try the name if there is one. Leaving this
            // in for backward compatibility. Seems inconsistent.
            if ((cfd == null) && (json_cfd_name == null)) { //Basically, if cf_id is all they passed in...
                throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, there does NOT exist a CustomFieldDefinition with (customFieldDefintionId) id '${json_cfd_id}'")
            }
        }

        //Precedent Order 3: if cfd_name passed in, retrieve CFD by Name
        if ((cfd == null) && (json_cfd_name != null)) {
            def cfds = CustomFieldDefinition.findAllByName(json_cfd_name)

            def cfdsMsg = new StringBuffer()
            cfds.eachWithIndex { cfdObj, idx ->
                cfdsMsg.append("id:${cfdObj.id}|types:${cfdObj.types}")
            }

            if (cfds.size == 1)
                cfd = cfds[0]
            else if (!cfds || cfds == 0)
                throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, there does NOT exist a CustomFieldDefinition with name '${json_cfd_name}', ${VALID_CUSTOM_FIELD_CFD_MSG}")
            else if (cfds.size > 1)
                throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, Non-Unique Name Error, Multiple (${cfds?.size}) Custom Field Definitions with name '${json_cfd_name}' found: [${cfdsMsg.toString()}], ${VALID_CUSTOM_FIELD_CFD_MSG}")

        }

        // If a field type was provided in the json, verify that it matches the field type in the database.
        if ((cfd instanceof DropDownCustomFieldDefinition) &&
            (StringUtils.isNotBlank(styleTypeText)) &&
            (!(styleTypeText?.equals(Constants.CustomFieldDefinitionStyleType.DROP_DOWN.name())))) {
            throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, Drop Down Custom Field has '${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}' JSON field as type '${Constants.CustomFieldDefinitionStyleType.DROP_DOWN.name()}'; however the Custom Field is not of this type.")
        } else if ((cfd instanceof TextCustomFieldDefinition) &&
            (StringUtils.isNotBlank(styleTypeText)) &&
            (!(styleTypeText?.equals(Constants.CustomFieldDefinitionStyleType.TEXT.name())))) {
            throw new ValidationException(message: "In Custom Field JSON {${cfjson}}, Text Custom Field has '${CUSTOM_FIELD_JSON_STYLE_TYPE_LABEL}' JSON field as type '${Constants.CustomFieldDefinitionStyleType.TEXT.name()}'; however the Custom Field is not of this type.")
        }

        return cfd
    }

    /*
      * Because we're nice, we want to allow for a couple of different ways to
      * reference to an existing object in incoming JSON.  We want to support the
      * following scenarios:
      * 1) JSON has an nested definition with database id in it
      * {
      *    "state": {
      *    	"id": 3
      *    	"title": "Deprecated"
      *    }
      * }
      * In this case we look up state.id in the database
      *
      * 2) JSON has just a database id
      * {
      * 	"state": 3
      * }
      *
      * 3) JSON has nested definition with no database id in it
      * {
      * 	"state": {
      * 		"title": "Deprecated"
      * 	}
      * }
      * In this case we look up state by title, using findByXXXX and returns
      * the first State object if there are more than one with the given title
      *
      * 4) JSON has just a title
      * {
      * 	"state": "Deprecated"
      * }
      * In this case we look up the state by title, using findByXXXX and returns
      * the first State object if there are more than one with the given title
      *
      */

    def lookup(def fromJSON, def domainClass, def altStr = "title",
               def idStr = "id") {
        // First attempt to retrieve object by uuid
        def uuid = extractUuid(fromJSON)
        log.debug("lookup ${domainClass} uuid = ${uuid}")
        if (uuid != null) {
            def returnVal = domainClass.findByUuid(uuid)
            if (returnVal == null) {
                log.warn("unable to find ${domainClass} for uuid = ${uuid}")
            }

            return returnVal
        }
        // Next attempt to retrieve object by database id
        def id = extractId(fromJSON, idStr)
        log.debug("id=${id}")
        if (id != null) {
            def returnVal = domainClass.get(id)
            return returnVal
        }
        // Since we don't have a database id, try the 'alt' property instead
        def alt = extractAlt(fromJSON, altStr)
        log.debug("alt=${alt}")

        if (alt != null) {
            def altStrCap = WordUtils.capitalize(altStr)
            def method = "findBy${altStrCap}"
            def returnVal = domainClass."${method}"(alt)
            return returnVal
        }

        return null;
    }

    private lookupEnum(def enumClass, def prop, def value) {
        for (en in enumClass) {
            if (en."${prop}" == value) {
                return en
            }
        }
        return null
    }

    private extractUuid(def fromJSON, def idStr = 'uuid') {
        return (fromJSON instanceof JSONObject) ? fromJSON.opt(idStr) : null
    }

    private extractId(def fromJSON, def idStr) {
        // Handle three cases:
        // 1) fromJSON is itself an integer database id
        // 2) fromJSON.id exists and is the database id
        // 3) neither 1) or 2)
        return (fromJSON instanceof Integer) ? fromJSON :
            (fromJSON instanceof JSONObject) ? fromJSON.opt(idStr) : null
    }

    private extractAlt(def fromJSON, def altStr) {
        // Handle two cases:
        // 1) fromJSON is itself a string
        // 2) fromJSON.alt exists
        // 3) neither 1) or 2)
        return (fromJSON instanceof String) ? fromJSON :
            (fromJSON instanceof JSONObject) ? fromJSON.opt(altStr) : null
    }

    @Transactional(readOnly = true)
    def getServiceItemListing(def params) {

        if (params.sorttype) {
            session.sorttype = params.sorttype
        }

        if ((!params.id) || params.id.equalsIgnoreCase("null"))
            return null
        def id = Long.valueOf(params.id)
        return ExtServiceItem.get(id)
    }
}
