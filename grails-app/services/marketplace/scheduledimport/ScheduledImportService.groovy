package marketplace.scheduledimport

import marketplace.ImportTask

import marketplace.rest.ProfileRestService
import marketplace.rest.CategoryRestService
import marketplace.rest.TypeRestService
import marketplace.rest.StateRestService
import marketplace.rest.ServiceItemRestService
import marketplace.rest.AgencyRestService
import marketplace.rest.DropDownCustomFieldDefinitionRestService
import marketplace.rest.TextCustomFieldDefinitionRestService
import marketplace.rest.TextAreaCustomFieldDefinitionRestService
import marketplace.rest.ImageURLCustomFieldDefinitionRestService
import marketplace.rest.CheckBoxCustomFieldDefinitionRestService

/**
 * This class re-implements Scheduled Import for OMP 7.16.  It is separate from
 * the other import code (ImportExecutorService, etc) so that it can avoid the
 * complexities of interactive metadata mapping
 */
class ScheduledImportService {

    ScheduledImportHttpService scheduledImportHttpService

    ProfileRestService profileRestService
    CategoryRestService categoryRestService
    TypeRestService typeRestService
    StateRestService stateRestService
    ServiceItemRestService serviceItemRestService
    AgencyRestService agencyRestService

    DropDownCustomFieldDefinitionRestService dropDownCustomFieldDefinitionRestService
    TextCustomFieldDefinitionRestService textCustomFieldDefinitionRestService
    TextAreaCustomFieldDefinitionRestService textAreaCustomFieldDefinitionRestService
    ImageURLCustomFieldDefinitionRestService imageURLCustomFieldDefinitionRestService
    CheckBoxCustomFieldDefinitionRestService checkBoxCustomFieldDefinitionRestService

    /**
     * Executes the import task with the corresponding id
     */
    public void executeScheduledImport(int importTaskId) {
        executeScheduledImport(ImportTask.get(importTaskId))
    }

    public void executeScheduledImport(ImportTask task) {
        log.info "Executing scheduled import [${task.name}]"

        JSONObject json = scheduledImportHttpService.retrieveRemoteImportData(task)

        importProfiles(json.profiles)
        importCategories(json.categories)
        importTypes(json.types)
        importStates(json.states)
        importCustomFieldDefinitions(json.customFieldDefs)
        importServiceItems(json.serviceItems)
        importRelationships(json.relationships)
    }

    //template method for import items of a specific type
    private <T> void importUsingService(Class<T> dtoClass,
            RestService<T> service, JSONArray items) {

        items.each {
            T existing = null
            if (it.uuid && dtoClass.metaClass.respondsTo('findByUuid', String)) {
                existing = dtoClass.findByUuid(it.uuid)
            }

            if (existing != null) {
                service.update(existing, it)
            }
            else {
                service.createFromDto(dto)
            }
        }
    }

    private importProfiles = this&.importUsingService.curry(Profile, profileRestService)
    private importCategories = this&.importUsingService.curry(Category, categoryRestService)
    private importTypes = this&.importUsingService.curry(Types, typeRestService)
    private importStates = this&.importUsingService.curry(State, stateRestService)
    private importAgencies = this&.importUsingService.curry(Agency, agencyRestService)

    private void importCustomFieldDefinitions(JSONArray customFieldDefs) {
        customFieldDefs.each { customFieldDef ->
            //determine which style of custom field this is
            CustomFieldDefinitionStyleType styleType =
                Constants.CustomFieldDefinitionStyleType.values().find {
                    it.styleTypeName() == customFieldDef.fieldType
                }

            RestService<? extends CustomFieldDefinition> service

            //determine which service to use
            switch (styleType) {
                case Constants.CustomFieldDefinitionStyleType.TEXT:
                    service = textCustomFieldDefinitionRestService
                    break
                case Constants.CustomFieldDefinitionStyleType.TEXT_AREA:
                    service = textAreaCustomFieldDefinitionRestService
                    break
                case Constants.CustomFieldDefinitionStyleType.DROP_DOWN:
                    service = dropDownCustomFieldDefinitionRestService
                    break
                case Constants.CustomFieldDefinitionStyleType.IMAGE_URL:
                    service = imageURLCustomFieldDefinitionRestService
                    break
                case Constants.CustomFieldDefinitionStyleType.CHECK_BOX:
                    service = checkBoxCustomFieldDefinitionRestService
                    break
            }

            CustomFieldDefinition existing =
                styleType.fieldDefinitionClass.findByUuid(customFieldDef.uuid)

            if (existing != null) {
                service.update(existing, customFieldDef)
            }
            else {
                service.createFromDto(styleType.fieldDefinitionClass.instantiate(customFieldDef))
            }
        }
    }

    private void importServiceItems(JSONArray serviceItems) {
        Set<Agency> agencies = serviceItems.collect { si ->
            (si.agencyIcon && si.agency) ?
                new Agency(
                    iconUrl: si.agencyIcon,
                    title: si.agency
                ) :
                null
        } - null

        //import agencies based on information in the service items
        importAgencies(new JSONArray(agencies.collect { it.asJSON() }))

        serviceItems.each { siJson ->
            ServiceItem existing = ServiceItem.findByUuid(siJson.uuid)

            if (existing != null) {
                serviceItemRestService.update(existing, siJson)
            }
            else {
                serviceItemRestService.createFromDto(new ServiceItem(siJson))
            }
        }
    }
}
