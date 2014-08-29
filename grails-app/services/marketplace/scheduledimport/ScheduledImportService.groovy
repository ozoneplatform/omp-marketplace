package marketplace.scheduledimport

import java.lang.reflect.UndeclaredThrowableException

import org.springframework.validation.Errors
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation
import org.springframework.security.core.context.SecurityContextHolder as SCH

import grails.validation.ValidationException

import marketplace.ImportTask

import marketplace.AccountService
import marketplace.AuditLoggingService
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
import marketplace.CustomField
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
import static ozone.utils.Utils.singleOrCollectionDo


/**
 * This class re-implements Scheduled Import for OMP 7.16.  It is separate from
 * the other import code (ImportExecutorService, etc) so that it can avoid the
 * complexities of interactive metadata mapping
 */

//each item import should be in its own transaction
@Transactional(propagation=Propagation.NOT_SUPPORTED)
class ScheduledImportService {

    private static enum CreationStatus { CREATED, UPDATED, NOT_UPDATED }

    ScheduledImportHttpService scheduledImportHttpService

    AccountService accountService
    AuditLoggingService AuditLoggingService
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
        ImportTask task = ImportTask.get(importTaskId)
        task.runs.size() //init lazy collection; not sure why this is necessary but it doesn't
                         //work if this collection isn't initialized in this method
        executeScheduledImport(task)
    }

    public void executeScheduledImport(ImportTask task) {
        accountService.loginSystemUser()

        log.info "Executing scheduled import [${task.name}]"
        auditLoggingService.logImport(task)

        ImportStatus importStatus = new ImportStatus()
        ScheduledImportData importData

        try {
            importData = scheduledImportHttpService.retrieveRemoteImportData(task)
        }
        catch (Exception e) {
            String message = "Error during import retrieval: ${exceptionToMessage(e)}"
            importStatus.messages << message
            log.error message, e
        }

        if (importData) {
            importProfiles(importData.profiles, importStatus)
            importCategories(importData.categories, importStatus)
            importTypes(importData.types, importStatus)
            importStates(importData.states, importStatus)
            importCustomFieldDefinitions(importData.customFieldDefs, importStatus)
            importServiceItems(importData.serviceItems, importData.relationships, importStatus)
            importRelationships(importData.relationships, importStatus)
        }

        boolean anyFailures = importStatus.messages.size() ||
            importStatus.entities.any { it.failed > 0 }

        if (anyFailures) {
            importStatus.success = false
        }

        recordImportResult(task, importStatus)

        task.save(failOnError:true, flush:true)
        SCH.clearContext()
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
                def creationStatus = fn(it)

                singleOrCollectionDo(creationStatus) { cs ->
                    switch (cs) {
                        case CreationStatus.NOT_UPDATED:
                            summary.notUpdated++
                            break
                        case CreationStatus.UPDATED:
                            summary.updated++
                            break
                        case CreationStatus.CREATED:
                            summary.created++
                            break
                    }
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
                summary.messages << "Error ${exceptionToMessage(e)} when importing $it"
                summary.failed++

                log.error "Unexpected exception during import", e
            }
        }
    }

    /**
     * template method for import items of a specific type.
     * @param dtoClass The class of item being imported
     * @param service The RestService for importing the items
     * @param summary The summary object to update with the results
     * @param items the items to import
     * @param equivalenceField The field to check for existing items (defaults to 'uuid')
     */
    private <T> void importUsingService(Class<T> dtoClass,
            RestService<T> service, ImportStatus.Summary summary, Collection<T> items,
            String equivalenceField='uuid', boolean skipValidation=false) {

        runAndCatchErrors(items, summary) { item ->
            importOneUsingService(dtoClass, service, item, equivalenceField, skipValidation)
        }
    }

    private <T> CreationStatus importOneUsingService(Class<T> dtoClass,
            RestService<T> service, T item, String equivalenceField='uuid',
            boolean skipValidation=false) {
        def existing = null
        if (item.hasProperty(equivalenceField)) {
            existing = dtoClass."findBy${equivalenceField.capitalize()}"(item[equivalenceField])
        }

        if (existing != null) {

            if (existing.editedDate < item.editedDate) {
                item.id = existing.id
                service.updateById(existing.id, item, skipValidation)
                return CreationStatus.UPDATED
            }
            else {
                return CreationStatus.NOT_UPDATED
            }
        }
        else {
            service.createFromDto(item, skipValidation)
            return CreationStatus.CREATED
        }
    }

    private void importProfiles(Collection<Profile> data, ImportStatus status) {
        //don't try to import System user
        data = data.grep { it.username != Profile.SYSTEM_USER_NAME }

        importUsingService(Profile, profileRestService, status.profiles, data, 'username')
    }

    private void importCategories(Collection<Category> data, ImportStatus status) {
        resolveCreatedAndEditedBy(data)
        importUsingService(Category, categoryRestService, status.categories, data)
    }

    private void importTypes(Collection<Types> data, ImportStatus status) {
        removeImagesFromTypes(data)
        resolveCreatedAndEditedBy(data)

        importUsingService(Types, typeRestService, status.types, data)
    }

    private void importStates(Collection<State> data, ImportStatus status) {
        resolveCreatedAndEditedBy(data)
        importUsingService(State, stateRestService, status.states, data)
    }

    /**
     * Replace DTOs with references to the actual persisted type
     * obj (necessary to link by id)
     * @param items Items to go through and replace DTO subobjects
     * @param referencedClass The Class of the subobject being replaced
     * @param referenceProperty The property on each item in items on which the DTO is attached
     * @param equivalenceField The field the use to resolve references (defaults to uuid)
     */
    private <T> void resolveReferences(Collection items, Class<T> referencedClass,
            String referenceProperty, String equivalenceField='uuid') {

        Map<Object, T> equivalenceFieldMap = [:]
        referencedClass.list().each { equivalenceFieldMap.put(it[equivalenceField], it) }

        items.each {
            def referencedItem = it[referenceProperty]
            if (referencedItem instanceof Collection) {
                List<T> newCollection = []

                referencedItem.each { newCollection << equivalenceFieldMap[it[equivalenceField]] }

                referencedItem.clear()
                referencedItem.addAll(newCollection)
            }
            else if (referencedItem != null) {
                it[referenceProperty] = equivalenceFieldMap[referencedItem[equivalenceField]]
            }
        }
    }

    private void resolveCreatedAndEditedBy(Collection items) {
        resolveReferences(items, Profile, 'createdBy', 'username')
        resolveReferences(items, Profile, 'editedBy', 'username')
    }

    private void importCustomFieldDefinitions(
            Collection<CustomFieldDefinition> customFieldDefs, ImportStatus status) {
        resolveReferences(customFieldDefs, Types, 'types')
        resolveCreatedAndEditedBy(customFieldDefs)

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

            return importOneUsingService(CustomFieldDefinition, service, customFieldDef)
        }
    }

    /**
     * Import agencies and associate the persisted Agency objects with their ServiceItems
     */
    private void importAgenciesFromServiceItems(Collection<ServiceItem> serviceItems,
            ImportStatus status) {

        Collection<Agency> agenciesAlreadySeen = new HashSet()

        runAndCatchErrors(serviceItems, status.agencies) { si ->
            Agency dto = si.agency
            if (dto) {
                Agency existing =
                    Agency.findByTitle(dto.title)

                if (existing != null) {
                    if (existing.editedDate < dto.editedDate) {
                        dto.id = existing.id
                        si.agency = agencyRestService.updateById(existing.id, dto, false)
                        agenciesAlreadySeen << si.agency
                        return CreationStatus.UPDATED
                    }
                    else {
                        si.agency = existing

                        //do not increase the NOT_UPDATED count if its an agency that was
                        //in fact created or updated on an earlier listing in this same import
                        return agenciesAlreadySeen.contains(existing) ? null :
                            CreationStatus.NOT_UPDATED
                    }
                }
                else {
                    si.agency = agencyRestService.createFromDto(dto, false)
                    agenciesAlreadySeen << si.agency
                    return CreationStatus.CREATED
                }
            }
        }
    }

    private Collection<ServiceItem> resolveCustomFields(Collection<ServiceItem> serviceItems,
            ImportStatus.Summary summary) {

        resolveCreatedAndEditedBy(serviceItems)

        //first, resolve the CustomFieldDefs
        resolveReferences(serviceItems.collect { it.customFields }.flatten() - null,
            CustomFieldDefinition, 'customFieldDefinition')

        //now switch to the appropriate subclass for each CustomField
        return serviceItems.collect { si ->
            ListIterator<CustomField> listIter = si.customFields?.listIterator()

            try {
                while (listIter?.hasNext()) {
                    CustomField customField = listIter.next()
                    Class<? extends CustomField> fieldClass =
                        customField.customFieldDefinition.styleType.fieldClass

                    CustomField newCustomField = fieldClass.newInstance()

                    newCustomField.customFieldDefinition = customField.customFieldDefinition
                    newCustomField.fieldValueText = customField.fieldValueText

                    listIter.set(newCustomField)
               }
            }
            catch (ValidationException ve) {
                summary.messages << "Validation Error when resolving custom fields on $si: $ve"
                summary.failed++

                log.error "Validation Error when resolving custom fields on $si", e

                return null
            }
            catch (Exception e) {
                summary.messages << "Error ${exceptionToMessage(e)} when importing $si"
                summary.failed++

                log.error "Unexpected exception during import", e

                return null
            }
            return si
        } - null
    }

    /**
     * This method adds DTOs for any relationships that were already on this serviceItem, which
     * still should be.  This prevents spurious "no longer requires" entries in the changelog
     */
    private void resolveExistingRelationships(Collection<ServiceItem> serviceItems,
            Collection<Relationship> relationships) {
        serviceItems.each { si ->

            //when importing from a 7.16, relationships may be present on the
            //serviceItem json, but they will be id refs (from the other system's db)
            //not uuid refs so they are useless here
            si.relationships.clear()

            //the existing copy of this service item in the database
            ServiceItem existingServiceItem = ServiceItem.findByUuid(si.uuid)

            if (existingServiceItem) {
                //the listings already required by this one
                Collection<String> existingRelatedItems = existingServiceItem.relationships
                    .grep { it.relationshipType == RelationshipType.REQUIRE }
                    .collect { it.relatedItems }.flatten()

                //the uuids of service item DTOs for this service item,
                //from the relationships section of the import
                Collection<ServiceItem> relatedUuids = relationships.grep {
                    it.owningEntity.uuid == si.uuid
                }.collect { it.relatedItems }.flatten()
                .collect { it.uuid }

                //the relationship object on this service item DTO to add to
                Relationship relationshipToAddTo = si.relationships.find {
                    it.relationshipType == RelationshipType.REQUIRE
                }

                if (!relationshipToAddTo) {
                    relationshipToAddTo = new Relationship()
                    si.addToRelationships(relationshipToAddTo)
                }

                //add a service item DTO for each relationship that is both in the
                //import and already existing
                relationshipToAddTo.relatedItems.addAll(
                    existingRelatedItems.grep { it.uuid in relatedUuids })
            }
        }
    }

    private void importServiceItems(Collection<ServiceItem> serviceItems,
            Collection<Relationship> relationships, ImportStatus status) {
        //import agencies based on information in the service items
        importAgenciesFromServiceItems(serviceItems, status)

        resolveReferences(serviceItems, Types, 'types')
        resolveReferences(serviceItems, Category, 'categories')
        resolveReferences(serviceItems, State, 'state')
        resolveReferences(serviceItems, Profile, 'owners', 'username')

        //if any serviceItems have errors in resolveCustomFields, they are removed
        //from the returned list
        serviceItems = resolveCustomFields(serviceItems, status.serviceItems)

        resolveExistingRelationships(serviceItems, relationships)

        importUsingService(ServiceItem, serviceItemRestService, status.serviceItems, serviceItems,
            'uuid', true)
    }

    private void importRelationships(Collection<Relationship> relationships,
            ImportStatus status) {

        Relationship.withTransaction {
            runAndCatchErrors(relationships, status.relationships) { relationshipDto ->
                Collection<CreationStatus> retval = []
                ServiceItem si = ServiceItem.findByUuid(relationshipDto.owningEntity.uuid)

                if (si) {
                    Relationship relationship = si.relationships.find {
                        it.relationshipType == RelationshipType.REQUIRE
                    }

                    if (!relationship) {
                        relationship = new Relationship()
                        si.addToRelationships(relationship)
                    }

                    //prevents "collection was not processed by flush()" exception from findByUuid below
                    Relationship.withSession { it.flush() }

                    //don't use resolveReferences to resolve related items; that would pull
                    //all ServiceItems into memory
                    Collection<ServiceItem> relatedItems = relationshipDto.relatedItems.collect {
                        ServiceItem item = ServiceItem.findByUuid(it.uuid)
                        if (!item) {
                            throw new IllegalArgumentException(
                                "Non-existant Related Item with uuid $it.uuid")
                        }

                        return item
                    }

                    relatedItems.each { relatedItem ->
                        if (relationship.relatedItems.contains(relatedItem)) {
                            return CreationStatus.NOT_UPDATED
                        }
                        else {
                            relationship.addToRelatedItems(relatedItem)
                            return CreationStatus.CREATED
                        }
                    }

                    return retval
                }
                else
                    throw new IllegalArgumentException("Non-existant Owning Listing with uuid " +
                        relationshipDto.owningEntity.uuid)
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

    private String exceptionToMessage(Exception e) {
        if (e instanceof UndeclaredThrowableException) {
            e = e.cause
        }

        return e.message ?: e.toString()
    }
}
