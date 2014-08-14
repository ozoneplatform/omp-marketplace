package marketplace.scheduledimport

import org.springframework.validation.Errors
import org.springframework.transaction.annotation.Transactional

import org.hibernate.SessionFactory

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

import marketplace.TransactionUtils
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
import marketplace.ImportStatus
import marketplace.ImportTaskResult
import marketplace.Constants
import ozone.marketplace.enums.RelationshipType


/**
 * This class re-implements Scheduled Import for OMP 7.16.  It is separate from
 * the other import code (ImportExecutorService, etc) so that it can avoid the
 * complexities of interactive metadata mapping
 */
class ScheduledImportService {

    private static enum CreationStatus { CREATED, UPDATED, NOT_UPDATED }

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

    SessionFactory sessionFactory

    /**
     * Executes the import task with the corresponding id
     */
    public void executeScheduledImport(Long importTaskId) {
        TransactionUtils.ensureSession(sessionFactory, log)
        ImportTask task = ImportTask.get(importTaskId)
        task.runs.size() //init lazy collection; not sure why this is necessary but it doesn't
                         //work if this collection isn't initialized in this method
        executeScheduledImport(task)
    }

    @Transactional(noRollbackFor=ValidationException)
    public void executeScheduledImport(ImportTask task) {

        log.info "Executing scheduled import [${task.name}]"
        ImportStatus importStatus = new ImportStatus()

        ScheduledImportData importData
        try {
            importData = scheduledImportHttpService.retrieveRemoteImportData(task)
        }
        catch (Exception e) {
            importStatus.messages << "Error during import retrieval: $e.message"
        }

        if (importData) {
            importProfiles(importData.profiles, importStatus)
            importCategories(importData.categories, importStatus)
            importTypes(importData.types, importStatus)
            importStates(importData.states, importStatus)
            importCustomFieldDefinitions(importData.customFieldDefs, importStatus)
            importServiceItems(importData.serviceItems, importStatus)
            importRelationships(importData.relationships, importStatus)
        }

        boolean anyFailures = importStatus.messages.size() ||
            importStatus.entities.any { it.failed > 0 }

        if (anyFailures) {
            importStatus.success = false
        }

        recordImportResult(task, importStatus)

        TransactionUtils.closeAndUnbindSession(sessionFactory)
    }

    private void removeImagesFromTypes(Collection<Types> types) {
        types.each { it.image = null }
    }

    /**
     * Runs the closure on each item in the Collection, and updates the summary accordingly.
     * An error in one item does not stop processing
     * of the rest of the items
     */
    private void runAndCatchErrors(Collection items, ImportStatus.Summary summary, Closure fn) {
        items.collect {
            try {
                CreationStatus cs = fn(it)
                switch (cs) {
                    case CreationStatus.NOT_UPDATED:
                        summary.notUpdated++
                    case CreationStatus.UPDATED:
                        summary.updated++
                    case CreationStatus.CREATED:
                        summary.created++
                }
            }
            catch (ValidationException ve) {
                summary.messages += ve.errors.allErrors.collect { it.toString() }
                summary.failed++
            }
            catch (IllegalArgumentException ie) {
                summary.messages << "Invalid input: $ie for item $it"
                summary.failed++
            }
            catch (Exception e) {
                summary.messages << "Error $e when importing $it"
                summary.failed++
            }
        }
    }

    //template method for import items of a specific type
    private <T> CreationStatus importUsingService(Class<T> dtoClass,
            RestService<T> service, ImportStatus.Summary summary, Collection<T> items) {

        runAndCatchErrors(items, summary) {
            def existing = null
            if (it.hasProperty('uuid')) {
                existing = dtoClass.findByUuid(it.uuid)
            }

            if (existing != null) {

                if (existing.editedDate < it.editedDate) {
                    it.id = existing.id
                    service.updateById(existing.id, it)
                    return CreationStatus.UPDATED
                }
                else {
                    return CreationStatus.NOT_UPDATED
                }
            }
            else {
                service.createFromDto(it)
                return CreationStatus.CREATED
            }
        }
    }

    private void importProfiles(Collection<Profile> data, ImportStatus status) {
        importUsingService(Profile, profileRestService, status.profiles, data)
    }

    private void importCategories(Collection<Category> data, ImportStatus status) {
        importUsingService(Category, categoryRestService, status.categories, data)
    }

    private void importTypes(Collection<Types> data, ImportStatus status) {
        //TODO is this the proper way of handling this
        removeImagesFromTypes(data)

        importUsingService(Types, typeRestService, status.types, data)
    }

    private void importStates(Collection<State> data, ImportStatus status) {
        importUsingService(State, stateRestService, status.states, data)
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

    private void importCustomFieldDefinitions(
            Collection<CustomFieldDefinition> customFieldDefs, ImportStatus status) {
        resolveReferencesByUuid(customFieldDefs, Types, 'types')

        runAndCatchErrors(customFieldDefs, status.customFieldDefs) { customFieldDef ->
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
    private void importAgenciesFromServiceItems(Collection<ServiceItem> serviceItems,
            ImportStatus status) {
        runAndCatchErrors(serviceItems, status.agencies) { si ->
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

    private void importServiceItems(Collection<ServiceItem> serviceItems, ImportStatus status) {
        //import agencies based on information in the service items
        importAgenciesFromServiceItems(serviceItems, status)
        resolveReferencesByUuid(serviceItems, Types, 'types')
        resolveReferencesByUuid(serviceItems, Category, 'categories')
        resolveReferencesByUuid(serviceItems, State, 'state')
        resolveReferencesByUuid(serviceItems, Profile, 'owners')
        resolveReferencesByUuid(serviceItems.collect { it.customFields }.flatten(),
            CustomFieldDefinition, 'customFieldDefinition')

        runAndCatchErrors(serviceItems, status.serviceItems) { si ->
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

    private void importRelationships(Collection<Relationship> relationships,
            ImportStatus status) {
        runAndCatchErrors(relationships, status.relationships) { relationshipDto ->
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

    private void recordImportResult(ImportTask task, ImportStatus status) {
        ImportTaskResult result = new ImportTaskResult(
            result: status.success,
            message: status.summaryMessage
        )

        task.addToRuns(result)
        task.lastRunResult = result
    }
}
