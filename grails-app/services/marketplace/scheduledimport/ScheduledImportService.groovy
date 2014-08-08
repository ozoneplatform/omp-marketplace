package marketplace.scheduledimport

import marketplace.ImportTask

import marketplace.rest.RestService
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

import marketplace.CustomFieldDefinition
import marketplace.ServiceItem
import marketplace.Types
import marketplace.Agency
import marketplace.Category
import marketplace.State
import marketplace.Relationship
import ozone.marketplace.enums.RelationshipType


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

        ScheduledImportData importData = scheduledImportHttpService.retrieveRemoteImportData(task)

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
            RestService<T> service, Collection<T> items) {

        items.each {
            T existing = null
            if (it.hasProperty('uuid')) {
                existing = dtoClass.findByUuid(it.uuid)
            }

            if (existing != null) {
                service.updateById(existing.id, it)
            }
            else {
                service.createFromDto(it)
            }
        }
    }

    private importProfiles = this.&importUsingService.curry(Profile, profileRestService)
    private importCategories = this.&importUsingService.curry(Category, categoryRestService)
    private importTypes = this.&importUsingService.curry(Types, typeRestService)
    private importStates = this.&importUsingService.curry(State, stateRestService)
    private importAgencies = this.&importUsingService.curry(Agency, agencyRestService)

    private void importCustomFieldDefinitions(Collection<CustomFieldDefinition> customFieldDefs) {
        customFieldDefs.each { customFieldDef ->

            RestService<? extends CustomFieldDefinition> service

            //determine which service to use
            switch (customFieldDef.getClass()) {
                case TextCustomFieldDefinition:
                    service = textCustomFieldDefinitionRestService
                    break
                case TextAreaCustomFieldDefinition:
                    service = textAreaCustomFieldDefinitionRestService
                    break
                case DropDownCustomFieldDefinition:
                    service = dropDownCustomFieldDefinitionRestService
                    break
                case ImageURLCustomFieldDefinition:
                    service = imageURLCustomFieldDefinitionRestService
                    break
                case CheckBoxCustomFieldDefinition:
                    service = checkBoxCustomFieldDefinitionRestService
                    break
            }

            CustomFieldDefinition existing =
                customFieldDef.getClass().findByUuid(customFieldDef.uuid)

            if (existing != null) {
                service.updateById(existing.id, customFieldDef)
            }
            else {
                service.createFromDto(customFieldDef)
            }
        }
    }

    private void importServiceItems(Collection<ServiceItem> serviceItems) {
        Set<Agency> agencies = serviceItems.collect { it.agency } - null

        //import agencies based on information in the service items
        importAgencies(agencies)

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

    private void importRelationships(Collection<Relationship> relationships) {
        relationships.each { relationshipDto ->
            ServiceItem si = serviceItemRestService.getById(relationshipDto.owningEntity.id)

            Relationship relationship = si.relationships.find {
                relationshipType == RelationshipType.REQUIRE
            }

            if (!relationship) {
                relationship = new Relationship(relationshipType: RelationshipType.REQUIRE)
                si.addToRelationships(relationship)
            }

            relationship.relatedItems.addAll(relationshipDto.relatedItems)
        }
    }
}
