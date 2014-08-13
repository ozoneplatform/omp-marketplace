package marketplace.scheduledimport

import org.springframework.validation.Errors
import org.springframework.transaction.annotation.Transactional
import grails.validation.ValidationException

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
import marketplace.Profile
import marketplace.Types
import marketplace.Agency
import marketplace.Category
import marketplace.State
import marketplace.Relationship
import marketplace.DropDownCustomFieldDefinition
import marketplace.TextCustomFieldDefinition
import marketplace.TextAreaCustomFieldDefinition
import marketplace.ImageURLCustomFieldDefinition
import marketplace.CheckBoxCustomFieldDefinition
import marketplace.Constants
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
    public void executeScheduledImport(Long importTaskId) {
        executeScheduledImport(ImportTask.get(importTaskId))
    }


    @Transactional(noRollbackFor=ValidationException)
    public void executeScheduledImport(ImportTask task) {
        log.info "Executing scheduled import [${task.name}]"

        ScheduledImportData importData = scheduledImportHttpService.retrieveRemoteImportData(task)


        Collection<Errors> errors = []

        errors << importProfiles(importData.profiles)
        errors << importCategories(importData.categories)
        errors << importTypes(importData.types)
        errors << importStates(importData.states)
        errors << importCustomFieldDefinitions(importData.customFieldDefs)
        errors << importServiceItems(importData.serviceItems)
        errors << importRelationships(importData.relationships)
    }

    private void removeImagesFromTypes(Collection<Types> types) {
        types.each { it.image = null }
    }

    /**
     * Runs the closure on each item in the Collection, and returns the aggregation
     * of all of the resulting Errors.  An error in one item does not stop processing
     * of the rest of the items
     * @return The aggregation of the Errors
     * @return null if there were no errors
     */
    private Errors runAndReturnErrors(Collection items, Closure fn) {
        Collection<Errors> errors = items.collect {
            try {
                fn(it)
                return null
            }
            catch (ValidationException e) {
                return e.errors
            }
        }

        //combine all errors into one object
        return errors.inject { acc, e ->
            if (acc == null) {
                return e
            }
            else {
                if (e != null) {
                    acc.addAllErrors(e)
                }

                return acc
            }
        }
    }

    //template method for import items of a specific type
    private <T> Errors importUsingService(Class<T> dtoClass,
            RestService<T> service, Collection<T> items) {

        runAndReturnErrors(items) {
            def existing = null
            if (it.hasProperty('uuid')) {
                existing = dtoClass.findByUuid(it.uuid)
            }

            if (existing != null) {
                it.id = existing.id
                service.updateById(existing.id, it)
            }
            else {
                service.createFromDto(it)
            }
        }
    }

    private Errors importProfiles(Collection<Profile> data) {
        importUsingService(Profile, profileRestService, data)
    }

    private Errors importCategories(Collection<Category> data) {
        importUsingService(Category, categoryRestService, data)
    }

    private Errors importTypes(Collection<Types> data) {
        //TODO is this the proper way of handling this
        removeImagesFromTypes(data)

        importUsingService(Types, typeRestService, data)
    }

    private Errors importStates(Collection<State> data) {
        importUsingService(State, stateRestService, data)
    }

    /**
     * Replace DTOs with references to the actual persisted type
     * obj (necessary to link by id)
     * @param items Items to go through and replace DTO subobjects
     * @param referencedClass The Class of the subobject being replaced
     * @param referenceProperty The property on each item in items on which the DTO is attached
     */
    private <T> void resolveReferencesByUuid(Collection items, Class<T> referencedClass,
            String referenceProperty) {

        Map<String, T> uuidMap = [:]
        referencedClass.list().each { uuidMap.put(it.uuid, it) }

        items.each {
            def referencedItem = it[referenceProperty]
            if (referencedItem instanceof Collection) {
                List<T> newCollection = []

                referencedItem.each { newCollection << uuidMap[it.uuid] }

                referencedItem.clear()
                referencedItem.addAll(newCollection)
            }
            else if (referencedItem != null) {
                it[referenceProperty] = uuidMap[referencedItem.uuid]
            }
        }
    }

    private Errors importCustomFieldDefinitions(
            Collection<CustomFieldDefinition> customFieldDefs) {
        resolveReferencesByUuid(customFieldDefs, Types, 'types')

        runAndReturnErrors(customFieldDefs) { customFieldDef ->
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

    /**
     * Import agencies and associate the persisted Agency objects with their ServiceItems
     */
    private Errors importAgenciesFromServiceItems(Collection<ServiceItem> serviceItems) {
        runAndReturnErrors(serviceItems) { si ->
            Agency dto = si.agency
            Agency existing =
                Agency.findByTitle(dto.title)

            if (existing != null) {
                dto.id = existing.id
                si.agency = agencyRestService.updateById(existing.id, dto)
            }
            else {
                si.agency = agencyRestService.createFromDto(dto)
            }
        }
    }

    private void resolveCategories(Collection<ServiceItem> si) {
        Collection<Category> categories = Category.list
    }

    private Errors importServiceItems(Collection<ServiceItem> serviceItems) {
        //import agencies based on information in the service items
        importAgenciesFromServiceItems(serviceItems)
        resolveReferencesByUuid(serviceItems, Types, 'types')
        resolveReferencesByUuid(serviceItems, Category, 'categories')
        resolveReferencesByUuid(serviceItems, State, 'state')
        resolveReferencesByUuid(serviceItems, Profile, 'owners')
        resolveReferencesByUuid(serviceItems.collect { it.customFields }.flatten(),
            CustomFieldDefinition, 'customFieldDefinition')

        return runAndReturnErrors(serviceItems) { si ->
            ServiceItem existing = ServiceItem.findByUuid(si.uuid)
            String actualApprovalStatus = si.approvalStatus
            ServiceItem result

            if (existing != null) {
                si.id = existing.id
                si.approvalStatus = existing.approvalStatus

                result = service.updateById(existing.id, si)
            }
            else {
                si.approvalStatus = Constants.APPROVAL_STATUSES['IN_PROGRESS']

                result = serviceItemRestService.createFromDto(si)
            }

            //update approvalStatus in a separate step to prevent validation problems
            serviceItemRestService.update(result, [approvalStatus: actualApprovalStatus], true)
        }
    }

    private Errors importRelationships(Collection<Relationship> relationships) {
        runAndReturnErrors(relationships) { relationshipDto ->
            ServiceItem si = ServiceItem.findByUuid(relationshipDto.owningEntity)

            if (si) {
                Relationship relationship = si.relationships.find {
                    relationshipType == RelationshipType.REQUIRE
                }

                if (!relationship) {
                    relationship = new Relationship()
                    si.addToRelationships(relationship)
                }

                relationship.relatedItems.addAll(relationshipDto.relatedItems)
            }
        }
    }
}
