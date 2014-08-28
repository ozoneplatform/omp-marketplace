package marketplace

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import grails.converters.JSON
import ozone.security.authentication.OWFUserDetailsImpl
import ozone.security.authorization.target.OwfGroup

import java.text.SimpleDateFormat

import ozone.marketplace.domain.ValidationException


class ImportService {

	def marketplaceApplicationConfigurationService
    def accountService

    boolean transactional = false
    def config = ConfigurationHolder.config
    def importExecutorService

    /**
     * Execute the given ImportTask immediately
     * @param task
     * @return
     */
    @Transactional(noRollbackFor=ValidationException.class)
    public ImportStatus execute(ImportTask task, def contextPath=null) {
        runAsFullAdmin {
            task.json = updateJSON(task.json)
            return importExecutorService.importFile(task, contextPath)
        }
    }

    public def runAsFullAdmin(def closure) {
        // Save off the original authentication, to be restored after we've finished
        def originalAuthentication = accountService.getAuthentication()

        // Create an authentication that has both internal and external admin privileges
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>(2)
        auths << new GrantedAuthorityImpl(Constants.EXTERNADMIN)
        auths << new GrantedAuthorityImpl(Constants.ADMIN)

        Collection<OwfGroup> groups = new ArrayList<OwfGroup>()

        def authUser = new OWFUserDetailsImpl(accountService.getLoggedInUsername(), "", auths, groups)
        def authToken = new UsernamePasswordAuthenticationToken(authUser, "", auths)
        SecurityContextHolder.context.authentication = authToken

        // Do the thing that the caller wanted
        def returnValue = closure()

        // Restore the original authentication
        SecurityContextHolder.context.authentication = originalAuthentication

        return returnValue
    }

    /**
     * Finds all the duplicates for each item in the JSON. For Service Items a
     * "hasDuplicate" boolean field is added to the JSON telling if the Service Item
     * already exists in the system. All other items add an extra "needsResolving"
     * boolean field to tell if the item isn't in the system and needs to be resolved.
     * @param json
     * @return
     */
    JSONObject findDupes(JSONObject json) {
        if (json?.has("serviceItems")){
            def svc = json.getJSONArray("serviceItems")
            svc.each {
                it.put("hasDuplicate", ServiceItem.findDuplicates(it))
            }
        }

        if(json?.has("states")) {
            def states = json.getJSONArray("states")
            states.each {
                it.put("needsResolving", !State.findDuplicates(it))
            }
        }

        if(json?.has("categories")) {
            def categories = json.getJSONArray("categories")
            categories.each {
                it.put("needsResolving", !Category.findDuplicates(it))
            }
        }

        if(json?.has("types")) {
            def types = json.getJSONArray("types")
            types.each {
                it.put("needsResolving", !Types.findDuplicates(it))
            }
        }

        if(json?.has("customFieldDefs")) {
            def cfd = json.getJSONArray("customFieldDefs")
            cfd.each {
                it.put("needsResolving", !CustomFieldDefinition.findDuplicates(it))
            }
        }

        if (json?.has('contactTypes')) {
            def contactTypes = json.getJSONArray('contactTypes')
            contactTypes.each {
                it.put('needsResolving', !ContactType.findDuplicates(it))
            }
        }

        return json
    }

    /**
     * Generates a JSON list of agencies like you find for types and categories
     * at the end of an import file. Matches with agencies in this system by name
     * @param json the contents of an import file
     * @return a JSON list that can be added to the end of the import file contents
     */
    JSONArray generateAgencyList(JSONObject json) {
        // Iterate through all the service items and collect all the agency names used.
        Set nameSet = json.serviceItems.collect(new HashSet()) {
            it.isNull('agency') ? "" : it.agency
        }

        // Grab the list of our agencies
        Set ourAgencies = Agency.findAll()

        // Now create the JSON
        return nameSet.collect(new JSONArray()) { name ->
            Agency localAgency = ourAgencies.find { it.title == name }

            new JSONObject([
                id: localAgency ? localAgency.id : name,
                name:name,
                needsResolving: !localAgency
            ])
        }
    }

    /**
     * Updates the JSON in two ways
     * First, runs through the arrays defining types, states, categories, and custom fields, and pulls out any that are being removed or mapped
     * away.  Also any that are moot because they are already defined in the system and don't have newer data are pulled out.
     * Second, runs through service items and modifies the uuids of the references to types, states, categories, and custom fields to the new uuids.
     * Also changes uuids in the custom fields' reference to types.
     * **Only the uuids are changed.  Other ancillary data like type name or custom field style type might be outdated.**
     * This should not be a problem as import only needs the uuid.  Doesn't even use the database id if the uuid is specified.
     * @param json
     * @return
     */
    JSONObject updateJSON(JSONObject json) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.OPT_DATE_FORMAT, Locale.US)
        dateFormat.setCalendar Calendar.getInstance(new SimpleTimeZone(0, "GMT"))

		Map typeMapping = updateJsonTypes(json, dateFormat)

        updateJsonStates(json, dateFormat)
        updateJsonCategories(json, dateFormat)
        updateJsonCustomFieldDefs(json, typeMapping, dateFormat)
        updateJsonAgencies(json)
        updateJsonContactTypes(json)

        return json
    }

    private void updateJsonContactTypes(JSONObject json) {
        def contactTypes = json.has("contactTypes") ? json.getJSONArray("contactTypes") : []
        def serviceItems = json.has("serviceItems") ? json.getJSONArray("serviceItems") : []

        //find contact types that need removing
        def removeFromJson = new HashSet()
        def removeFromServiceItem = new HashSet()
        def contactTypeMap = [:]

        contactTypes?.each { contactType ->
            if (contactType.mapsTo == 'remove') {
                removeFromServiceItem << contactType
                removeFromJson << contactType
            } else if (contactType.mapsTo != 'create') {
                contactTypeMap.put(contactType.title, contactType)

                //we don't want to create this new type since we are mapping it
                removeFromJson << contactType
            }
        }

        //remove contact types
        contactTypes?.removeAll(removeFromJson)

        def titlesToRemove = removeFromServiceItem*.title
        serviceItems?.each { si ->
            //remove contacts referencing types that will be removed
            si.contacts?.removeAll { contact -> titlesToRemove.contains(contact.type?.title) }

            //add local ids for existing contact types.  New contact types will be untouched
            //and will get resolved by title later
            si.contacts?.each { contact ->
                def type = contactTypeMap[contact.type.title]
                contact.type.id = type?.mapsTo
            }
        }
    }

	/**
	 * Updates the JSON with the new state mapping.
	 * First, runs through the array of states and pulls out any that are already defined in the system and don't have newer data.
	 * Second, runs through service items and updates the references to states.
	 * @param json JSON object representing an import.  Will be modified by this method
	 */
	private void updateJsonStates(JSONObject json, SimpleDateFormat dateFormat) {

		// Run through all the declared states, and figure out if these types need to stay in the JSON or get removed.
		// Collect the ones that stay in the newStates set.
        def states = json.has("states")? json.getJSONArray("states") : []
		def si = json.has("serviceItems")? json.getJSONArray("serviceItems") : []
        def newStates = []
        def removeStateDupes = []

        states.each { state ->
            if (state.needsResolving == true) {
                newStates.push(state)
            } else {
                // Remove the state if it's an old duplicate or keep it if it's newer
                // than the version in the system
                def stateDate = dateFormat.parse(state.editedDate)
                def stateInSystem = State.findByUuid(state.uuid)
                if (stateInSystem.equals(null)) {
                    stateInSystem = State.findByTitle(state.title)
                }
                state.uuid = stateInSystem.uuid
                if (stateDate > stateInSystem.editedDate) {
                    newStates.push(state)
                } else {
                    removeStateDupes.push(state)
                }
            }
        }

        json.states = newStates

		// Change the type, state, categories, and custom fields in the service items to match
		// what the data changed to from above
		si.each{serviceItem->

			if (!serviceItem.isNull("state") && !serviceItem.state.isNull("id")) {
                def foundState = false

                // Finds the service item state and changes its uuid just in case it was changed
                // if it was a duplicate that was newer than the one in the system
                for (stateElement in 0..<json.states.size()) {
                    if (serviceItem.state.id == json.states[stateElement].id) {
                        foundState = true
                        serviceItem.state.uuid = json.states[stateElement].uuid
                    }
                }

                // If the state wasn't found in the json then it was a duplicate and can be found in the system
                if (foundState == false) {
                    for (element in 0..<removeStateDupes.size()) {
                        if (removeStateDupes[element].id == serviceItem.state.id) {
                            serviceItem.state = (State.findByUuid(removeStateDupes[element].uuid)).asJSONRef()
                            break
                        }
                    }
                }
            } else {
                serviceItem.state = (State.findByTitle("Active"))?.asJSONRef()
            }
        }
	}

	/**
	 * Updates the JSON with the new type mapping.
	 * First, runs through the array of types and pulls out any that are being removed or mapped away.
	 * Also any that are moot because they are already defined in the system and don't have newer data are pulled out.
	 * Second, runs through service items and modifies the uuids of the references to types to the new uuids.
	 * If a service item has its type removed, it removes the entire service item from the JSON
	 * **Only the uuids are changed.  Other ancillary data like type name might be outdated.**
	 * This should not be a problem as import only needs the uuid.  Doesn't even use the database id if the uuid is specified.
	 * @param json JSON object representing an import.  Will be modified by this method
	 * @return a mapping of type ids in the import file to uuids of the types on the server that they get mapped to
	 */
	private Map updateJsonTypes(JSONObject json, SimpleDateFormat dateFormat) {

		// Run through all the declared types, and figure out if these types need to stay in the JSON or get removed.
		// Collect the ones that stay in the newTypes set.
		// Create a map of old types to new types.
		def types = json.has("types")? json.getJSONArray("types") : []
		List newTypes = []
		Map typeMapping = new HashMap()

		types.each { type ->
			if (type.needsResolving == false) {
				// Remove the type if it's an old duplicate or keep it if it's newer
				// than the version in the system
				def typeDuplicateDate = dateFormat.parse(type.editedDate)
				def typeInSystem = Types.findByUuid(type.uuid)
				if (typeInSystem.equals(null)) {
					typeInSystem = Types.findByTitle(type.title)
				}
				type.uuid = typeInSystem.uuid
				if (typeDuplicateDate > typeInSystem.editedDate) {
					typeMapping.put(type.id, typeInSystem.uuid);
					newTypes.push(type)
				} else {
					typeMapping.put(type.id, typeInSystem.uuid);
				}
			} else if (type.mapsTo == 'create') {
				typeMapping.put(type.id, type.uuid);
				newTypes.push(type)
			} else {
				Types newType = Types.findById(type.mapsTo)
				typeMapping.put(type.id, newType.uuid);
			}
		}

		json.types = newTypes

		// Remap each types in each service items.  Then pull out any service items that have had their type removed
        def serviceItems = json.has("serviceItems")? json.getJSONArray("serviceItems") : []
		serviceItems.each {serviceItem->
			serviceItem.types.uuid = typeMapping.get(serviceItem.types.id)
		}

		return typeMapping

	}

	/**
	 * Updates the JSON with the new categories mapping.
	 * First, runs through the array of category definitions and pulls out any that are being removed or mapped away.
	 * Also any that are moot because they are already defined in the system and don't have newer data are pulled out.
	 * Second, runs through service items and modifies the uuids of the references to categories to the new uuids.
	 * **Only the uuids are changed.  Other ancillary data like category name might be outdated.**
	 * This should not be a problem as import only needs the uuid.  Doesn't even use the database id if the uuid is specified.
	 * @param json JSON object representing an import.  Will be modified by this method
	 */
	private void updateJsonCategories(JSONObject json, SimpleDateFormat dateFormat) {
		// Run through all the declared categories, and figure out if these categories need to stay in the JSON or get removed.
		// Collect the ones that stay in the newCategories set.
		// Create a map of old categories to new categories.
		def categories = json.has("categories")? json.getJSONArray("categories") : []
		def serviceItems = json.has("serviceItems")? json.getJSONArray("serviceItems") : []
		def newCategories = []
		Map categoryMapping = new HashMap()

		categories.each { category ->
			if (category.needsResolving == false) {
				// Remove the category if it's an old duplicate or keep it if it's newer
				// than the version in the system
				def catDuplicateDate = dateFormat.parse(category.editedDate)
				def catInSystem = Category.findByUuid(category.uuid)
				if (catInSystem.equals(null)) {
					catInSystem = Category.findByTitle(category.title)
				}
				category.uuid = catInSystem.uuid
				if (catDuplicateDate > catInSystem.editedDate) {
					categoryMapping.put(category.id, catInSystem.uuid);
					newCategories.push(category)
				} else {
					categoryMapping.put(category.id, catInSystem.uuid);
				}
			} else if (category.mapsTo == 'create') {
				categoryMapping.put(category.id, category.uuid);
				newCategories.push(category)
			} else if (category.mapsTo == 'remove'){
				categoryMapping.put(category.id, null);
			} else {
				// Change the category to the mapping the user specified and keep the old category ID in mapsTo
				// for service items to use to change their category
				Category newCategory = Category.findById(category.mapsTo)
				categoryMapping.put(category.id, newCategory.uuid);
			}
		}

		json.categories = newCategories

		// Remap each category in each service item.
		serviceItems.each{serviceItem->
			// Changes the service items categories if needed.
			// We just update the categories' uuids.  Any other fields in the category JSON section
			// may be stale, but they are not looked at during import.
			// Make sure as we change the uuid that there isn't already a category with that uuid.
			List categoriesUsed = []
			serviceItem.categories.each{category->
				String newUuid = categoryMapping.get(category.id)
				if (newUuid && !categoriesUsed.contains(newUuid)) {
					category.uuid = newUuid
					categoriesUsed.add(newUuid)
				}
				else {
					category.uuid = null
				}
			}
			// Drop any JSON for categories that were removed or had multiple categories with the same uuid
			serviceItem.categories.removeAll {category->category.uuid==null}
		}

	}

	/**
	 * Updates the JSON with the new custom field definition mapping.
	 * First, runs through the array of field defs and pulls out any that are being removed.
	 * Also any that are moot because they are already defined in the system and don't have newer data are pulled out.
	 * Second, runs through service items and modifies the uuids of the references to field defs to the new uuids.
	 * **Only the uuids are changed.  Other ancillary data like style type name might be outdated.**
	 * This should not be a problem as import only needs the uuid.  Doesn't even use the database id if the uuid is specified.
	 * @param json JSON object representing an import.  Will be modified by this method
	 */
	private void updateJsonCustomFieldDefs(JSONObject json, Map typeMapping, SimpleDateFormat dateFormat) {

		// Run through all the declared field defs, and figure out if they need to stay in the JSON or get removed.
		// Collect the ones that stay in the newCFDs set.
		// Create a map of old field defs to new ones
		def cfds = json.has("customFieldDefs")? json.getJSONArray("customFieldDefs") : []
		def serviceItems = json.has("serviceItems")? json.getJSONArray("serviceItems") : []
		List newCFDs = []
		Map cfdMapping = new HashMap()

		cfds.each { customField ->
			if (customField.needsResolving == false) {
				// Remove the custom field if it's an old duplicate or keep it if it's newer
				// than the version in the system
				def cfdDuplicateDate = dateFormat.parse(customField.editedDate)
				def cfdInSystem = CustomFieldDefinition.findByUuid(customField.uuid)
				if (cfdInSystem.equals(null)) {
					def allNames = CustomFieldDefinition.findAllByName(customField.name);
					allNames.each {
						if (customField.fieldType == "DROP_DOWN") {
							if ((it.styleType.styleTypeName == "Drop Down") && (customField.isMultiSelect == it.isMultiSelect)) {
								cfdInSystem = it
							}
						} else if (Constants.CustomFieldDefinitionStyleType."${customField.fieldType}".styleTypeName == it.styleType.styleTypeName) {
								cfdInSystem = it
						}
					}
				}
				customField.uuid = cfdInSystem.uuid
				if (cfdDuplicateDate > cfdInSystem.editedDate) {
					cfdMapping.put(customField.id, cfdInSystem.uuid);
					newCFDs.push(customField)
				} else {
					customField.uuid = cfdInSystem.uuid
					cfdMapping.put(customField.id, cfdInSystem.uuid);
				}
			} else if (customField.mapsTo == 'create') {
				cfdMapping.put(customField.id, customField.uuid);
				newCFDs.push(customField)
			} else {
				cfdMapping.put(customField.id, null);
			}
		}

		json.customFieldDefs = newCFDs

		// Remap all types in each field def.  Then pull out any field defs that have had all their types removed
		newCFDs.each { customField ->
			Set usedTypes = new HashSet()
			if (customField.allTypes == false) {
				// Update the types that the custom field is tied to
				customField.types?.each { type ->
					String newUuid = typeMapping[type.id]
					if (newUuid && !usedTypes.contains(newUuid)) {
						type.uuid = newUuid
						usedTypes.add(newUuid)
					}
					else {
						type.uuid = null
					}
				}
				// Drop any types that were removed or occur multiple times due to two types mapped to the same type
				customField.types?.removeAll { it.uuid.equals(null) }
			}
		}

		// Remap each types in each service items.  Then pull out any service items that have had their type removed
		serviceItems.each{serviceItem->
			// Change the service items custom fields if needed
			serviceItem.customFields.each{customField->
				// We just update the uuid.  Any other fields in this JSON section that refer to the custom field definition
				// may be stale, but they are not looked at during import.
				customField.customFieldDefinitionUuid = cfdMapping.get(customField.customFieldDefinitionId)
			}
			// Remove any custom field entries that refer to custom field definitions that were dropped
			Closure cfdWithoutUuid = {cfd->cfd.customFieldDefinitionUuid == null}
			serviceItem.customFields.removeAll(cfdWithoutUuid);
		}

	}

    private void updateJsonAgencies(JSONObject json) {
        // Run through all the declared agencies and create a map of old agencies to new agencies.
        // We don't modify the JSON list of agencies, as it is ignored during import.
        def agencies = json.has("agencies") ?
            json.getJSONArray("agencies") : []

        //a mapping from agency title in the import to an object representing
        //the agency locally.  This object will be one of the following three things:
        // null, signifying that the agency is not being kept locally
        // an object containing an id, indicating a mapping to an existing agency
        // an object containing a title, indicating that the agency will be newly created
        // with that title
        Map<String, JSONObject> agencyMapping = [:]

        //a set of agencies to remove from the JSON, either because we are ignoring them,
        //or because we are mapping them to other, existing agencies
        Set<JSONObject> toRemove = new HashSet()

        //agencies that we are creating new.  These are tracked so that they
        //can have their iconUrls filled in
        Set<String> newAgencyTitles = new HashSet()
        Long noAgencyMappedToId = null

        agencies.each { agency ->

            //normalize "name" to "title"
            agency.title = agency.name
            agency.remove('name')


            agency.iconUrl = agency.iconUrl


            // The special "no agency" case.
            if (agency.title == "") {
                //mapping 'no agency' to one of our existing agencies
                if (agency.mapsTo != "create" && agency.mapsTo != "remove") {
                    //mapsTo holds the title of the destination agency
                    noAgencyMappedToId = agency.mapsTo as Long
                }

                toRemove << agency
            }

            //agency already exists in this system
            else if (agency.needsResolving == false) {
                agencyMapping.put(agency.title, agency) //agency should have the id
            }

            //agency is to be created in this system
            else if (agency.mapsTo == 'create') {
                //save the agency so that we can fill in it's
                //icon as we go through the service items later
                newAgencyTitles << agency.title
                agencyMapping.put(agency.title, agency)
                agency.remove('id') //the id will usually be the name, which isn't right
            }

            //service items with this agency in the JSON will be stored
            //with no agency
            else if (agency.mapsTo == 'remove') {
                agencyMapping.put(agency.title, null);
                toRemove << agency
            }

            //mapping explicitly specified
            else {
                agencyMapping.put(agency.title, [id: agency.mapsTo])
                toRemove << agency
            }
        }


        //remove the 'no agency' agency
        agencies.removeAll(toRemove)

        def serviceItems = json.has("serviceItems") ? json.getJSONArray("serviceItems") : []


        serviceItems.each { serviceItem->
            //service item does not have an agency
            if (serviceItem.isNull("agency") || serviceItem.agency.trim().isEmpty()) {
                    serviceItem.agency = noAgencyMappedToId != null ?
                        new JSONObject(id: noAgencyMappedToId) : null
            }

            //service item has an agency
            else {
                //at this point serviceItem.agency is a string containing the agency title
                JSONObject agency = agencyMapping.get(serviceItem.agency)

                //removal
                if (agency == null) {
                    serviceItem.agency = null
                }
                else {
                    //replace the title string with an object holding the title and,
                    //if it is known, the id
                    serviceItem.agency = agency
                }
            }

            serviceItem.remove('agencyIcon')
        }
    }

    /**
     * For each service item, the indices (the service item's index in the json) of its required
     * service items are added to the json
     * @param json
     * @return
     */
    JSONObject getRequiredListings(JSONObject json) {
        def relationships = json.has("relationships")? json.getJSONArray("relationships") : []

        // A mapping of a service item's uuid to its index in the json
        def indexMapping = [:]

        for (index in 0..<json.serviceItems.size()) {
            indexMapping.put(json.serviceItems[index].uuid, index)

            // Adds the field requires to the json that holds the indices of the service items it requires
            json.serviceItems[index].put("requires", new JSONArray())
        }

        relationships.each { serviceItemRelationships ->
            def siRequirements = new JSONArray()
            def siIndex = indexMapping[serviceItemRelationships.serviceItem.uuid]

            if (!siIndex.equals(null)) {
                serviceItemRelationships.requires.each {
                    def uuid = it.uuid
                    if (!indexMapping[uuid].equals(null)) {
                        siRequirements.put(indexMapping[uuid])
                    }
                }
                json.serviceItems[siIndex].requires = siRequirements
            }
        }

        return json
    }
}
