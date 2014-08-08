package marketplace.scheduledimport

import marketplace.ImportTask

import marketplace.rest.ProfileRestService
import marketplace.rest.CategoryRestService
import marketplace.rest.TypeRestService
import marketplace.rest.StateRestService
import marketplace.rest.ServiceItemRestService
import marketplace.rest.AgencyRestService

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

    private <T> void importUsingService(Class<T> dtoClass,
            RestService<T> service, JSONArray items) {

        items.each {
            T dto = dtoClass.instantiate(it)
            Long existingId = null
            if (dto.uuid && dtoClass.metaClass.respondsTo('findByUuid', String)) {
                existingId = dtoClass.findByUuid(dto.uuid)?.id
            }

            if (existingId != null) {
                dto.id = existingId
                service.updateById(existingId, dto)
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
    private importCustomFieldDefinitions =
        this&.importUsingService.curry(CustomFieldDefinition, customFieldDefinitionRestService)

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
            T dto = new ServiceItem(siJson)
            Long existingId = ServiceItem.findByUuid(dto.uuid)?.id

            if (existingId != null) {
                dto.id = existingId
                serviceItemRestService.updateById(existingId, dto)
            }
            else {
                serviceItemRestService.createFromDto(dto)
            }
        }
    }
}
