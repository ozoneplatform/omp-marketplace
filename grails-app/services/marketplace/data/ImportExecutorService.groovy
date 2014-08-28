package marketplace.data

import org.hibernate.FlushMode
import ozone.marketplace.dataexchange.CategoryImporter
import ozone.marketplace.dataexchange.CustomFieldDefImporter
import ozone.marketplace.dataexchange.AgencyImporter
import ozone.marketplace.dataexchange.ContactTypeImporter
import ozone.marketplace.dataexchange.ProfileImporter
import ozone.marketplace.dataexchange.ServiceItemImporter
import ozone.marketplace.dataexchange.StateImporter
import ozone.marketplace.dataexchange.TypeImporter

import marketplace.ImportStatus
import marketplace.ImportTask
import marketplace.ImportTaskResult
import marketplace.ServiceItem
import marketplace.TransactionUtils
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import ozone.marketplace.dataexchange.TagImporter
import ozone.marketplace.domain.ValidationException
import ozone.decorator.JSONDecoratorException

class ImportExecutorService {
    def transactional = false
    def sessionFactory
    def config

    def accountService
    def extServiceItemService
    def customFieldDefinitionService
    def relationshipService
    def serviceItemService
    def JSONDecoratorService
    def marketplaceApplicationConfigurationService
    def itemCommentService
    def profileService

    transient boolean managingHbmSession = false


    void preExecute() {
        // Ensure we have a Hibernate Session bound to the thread
        //   This may not be the case if we are running from a scheduler thread
        managingHbmSession = TransactionUtils.ensureSession(sessionFactory, log)

        log.debug "Cur user roles: ${accountService.getLoggedInUserRoles()}"

    }

    void postExecute() {
        if (managingHbmSession &&
            TransactionSynchronizationManager.getResource(sessionFactory) != null) {
            // Unbind session from thread
            SessionHolder sessionHolder =
                (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory)
            if (log.isDebugEnabled()) {
                def session = sessionHolder.getSession()
                log.debug "Closing thread-bound Hibernate Session"
                log.debug "---Session Details ---\nSession [${Integer.toHexString(session.hashCode())}]\nSessionFactory [${Integer.toHexString(session.getSessionFactory().hashCode())}] \nTransaction: ${session.getTransaction()}"
            }

            SessionFactoryUtils.closeSession(sessionHolder.getSession());

            managingHbmSession = false
        }
    }


    @Transactional(noRollbackFor = [ValidationException])
    public ImportStatus importFile(ImportTask task, def contextPath) {
        log.debug("Executing file import: ${task}")
        preExecute()
        def stats = executeImport(task, contextPath)
        postExecute()
        return stats
    }

    /**
     * Execute the actual import.  Returns an ImportStatus object summarizing results.
     * @param ImportTask task
     * @param params String or simple Map
     * @return ImportStatus
     */
    @Transactional(noRollbackFor = [ValidationException])
    ImportStatus executeImport(ImportTask task, def contextPath = null) {
        def json // The content of what we intend to import
        def result  //  [success: boolean, data: String]
        ImportStatus procResult = null

        // Workaround for Optimistic locking exception
        ImportTask.withSession { session ->
            try {
                session.flush()
            }
            catch (Exception exc) {
                log.error "Error clearing HBM session: $exc"
            }
        }

        log.debug "Executing import task: ${task.prettyPrint()}"

        json = task.json

        log.debug "Post data-collection process =================== "

        if (json?.size() > 0) {
            try {
                // PARSE
                procResult = parseAndSave(task, json, contextPath)
            }
            catch (JSONDecoratorException jsonException) {
                procResult = new ImportStatus(success: false, messages: [jsonException.message])
            }
            catch (Exception e) {
                e.printStackTrace()
                log.error "Error processing data: " + e
                if (log.isDebugEnabled()) {
                    e.printStackTrace()
                }
                String eMsg = e.toString()
                /*
                 * this is server-specific text; valid for now but not future-proof
                if (eMsg.indexOf("JSONObject text must begin with")>0) {
                    // Response was probably HTML
                }
                */
                if (eMsg.indexOf("Unauthorized") > 0 && eMsg.indexOf("401") > 0) {
                    // Looks like a 401 error
                    result = [success: false, data: "Error processing: 401 Unauthorized received from server; check certificate"]
                } else {
                    // Errp...default to send back what we have
                    result = [success: false, data: "Error processing: $e"]
                }
            }
        } else {
            result = [success: false, data: "Import file contains no JSON"]
        }

        log.debug " :::: executeImport:  Before bridge:: $procResult"
        // Temporary? bridge between import result styles, moving towards ImportStatus
        if (procResult) {
            result = [success: procResult.success, data: procResult.summaryMessage]
        } else {
            procResult = new ImportStatus(success: result.success, messages: [result.data])
        }

        createResult(task, result.success, result.data)
        log.debug " :::: executeImport:  returning: ${procResult.asJSON()}"
        return procResult
    }

    /**
     * Given String input in standard OMP JSON format.  Provides for an overridable pre-processing
     * on each contained service item, as an opportunity for different implementations to ensure
     * the data is correct.
     * @param input
     * @return
     */
    @Transactional(noRollbackFor = [ValidationException])
    def parseAndSave(ImportTask task, JSONObject json, def contextPath = null) {
        JSONDecoratorService.preProcessJSON(json)
        def stats = new ImportStatus()

        def categoryImporter = new CategoryImporter()
        categoryImporter.importFromJSONArray(task.json.categories, stats.categories)

        def stateImporter = new StateImporter()
        stateImporter.importFromJSONArray(task.json.states, stats.states)

        def typeImporter = new TypeImporter()
        typeImporter.importFromJSONArray(task.json.types, stats.types)

        def customFieldImporter = new CustomFieldDefImporter()
        customFieldImporter.importFromJSONArray(task.json.customFieldDefs, stats.customFieldDefs)

        def agencyImporter = new AgencyImporter()
        agencyImporter.importFromJSONArray(task.json.agencies, stats.agencies)

        def contactTypeImporter = new ContactTypeImporter()
        contactTypeImporter.importFromJSONArray(task.json.contactTypes, stats.contactTypes)

        def profileImporter = new ProfileImporter()
        profileImporter.loadProfiles(task.json.profiles, stats.profiles, task.json.options.importAllProfiles)

        //This is used by other tasks as well so there may not be service items in it
        if(task.json.has('serviceItems')){
            def tags = new HashSet()
            task.json.getJSONArray("serviceItems").each{
                if(it.has('tags')) {
                    it.getJSONArray('tags')?.each{
                        tags << it.tag
                    }
                }
            }

            def tagImporter = new TagImporter()
            tagImporter.importFromJSONArray(tags, stats.tags)
        }

        def serviceItemImporter = new ServiceItemImporter(profileImporter, task.json.options.importRatings ?: false, contextPath)
        serviceItemImporter.JSONDecoratorService = JSONDecoratorService
        serviceItemImporter.accountService = accountService
        serviceItemImporter.serviceItemService = serviceItemService
        serviceItemImporter.extServiceItemService = extServiceItemService
        serviceItemImporter.marketplaceApplicationConfigurationService = marketplaceApplicationConfigurationService
        serviceItemImporter.itemCommentService = itemCommentService
        serviceItemImporter.profileService = profileService
        def updatedOrCreatedServiceItems = serviceItemImporter.importFromJSONArray(task.json.serviceItems, stats.serviceItems)

        // We only update relationships where the parent serviceItem is being updated based
        // on the editedDate.
        importRelationships(json.relationships, stats, updatedOrCreatedServiceItems)

        log.debug "Import Results:: ${stats.getSummaryMessage()}"
        return stats
    }


    @Transactional
    def createResult(ImportTask task, Boolean success, String msg) {

        log.debug "Creating ImportTaskResult of success [$success] for message of size [${msg?.size()}]"
        // Create result record
        ImportTaskResult result = new ImportTaskResult()
        result.runDate = new Date()
        result.result = success
        result.message = (msg.size() <= ImportTaskResult.MESSAGE_MAX_SIZE ? msg :
            msg[0..ImportTaskResult.MESSAGE_MAX_SIZE - 4] + '...')

        // Workaround for Optimistic locking exception
        ImportTask.withSession { session ->
            try {
                if (session.isDirty()) {
                    log.debug "Flushing ImportTask session [0x${Integer.toHexString(session.hashCode())}]...."
                    session.flush()
                }
                log.debug "Clearing ImportTask session [0x${Integer.toHexString(session.hashCode())}]...."
                session.clear()
            }
            catch (Exception exc) {
                log.error "Error clearing HBM session: ${exc.message}"
            }
        }

        // Ensure ImportTask is most recent in GORM
        task = ImportTask.get(task.id)
        result.task = task
        task.addToRuns(result);
        task.lastRunResult = result;
        def saveStatus = null
        try {
            saveStatus = task.save(flush: true)
        }
        catch (Exception e) {
            log.warn "Error saving ImportTask: ${e.message}"
            // try again
            task = ImportTask.get(task.id)
            result.task = task
            task.addToRuns(result);
            task.lastRunResult = result;
            saveStatus = task.save(flush: true)
        }
        return saveStatus
    }

    // =============================================================================
    //   JSON Parsing logic
    // =============================================================================


    @Transactional(noRollbackFor = [ValidationException])
    def importRelationships(def json, ImportStatus stats, updatedOrCreatedServiceItems) {
        log.debug("importRelationships: ${json}")

        json?.each {
            if (updatedOrCreatedServiceItems.contains(it.serviceItem.uuid)) {
                importRelationship(it, stats)
            } else {
                log.info("not updating relationships for serviceItem.uuid = ${it.serviceItem.uuid}")
                stats.relationships.notUpdated += 1

                //it is possible that this serviceItem failed validation, triggering the
                //session clearing at the end of the importListings method.  If this happened,
                //exceptions will be thrown shortly after execution passes through this block
                //unless the session is flushed.
                sessionFactory.currentSession.flush()
            }
        }
    }

    /**
     */
    @Transactional(noRollbackFor = [ValidationException])
    def importRelationship(def json, ImportStatus stats) {
        log.debug("importRelationship: ${json}")
        def requiredIds = []

        def parent = null
        // Set flush mode to manual as a workaround for OP-3289 (http://grails.1312388.n4.nabble.com/Dreaded-quot-collection-was-not-processed-by-flush-quot-error-td4639206.html) (MS)
        ServiceItem.withSession { session ->
            session.flushMode = FlushMode.MANUAL
            parent = ServiceItem.findByUuid(json.serviceItem?.uuid)
        }

        if (parent == null) {
            log.warn("unable to find parent serviceItem with uuid = ${json.serviceItem.uuid}")
            stats.relationships.failed += 1
            stats.relationships.messages << "Could not find parent in relationship, uuid = ${json.serviceItem.uuid}"
            return
        }

        boolean foundAllChildren = true
        json.requires.each {
            log.debug("it.title = ${it.title} it.uuid = ${it.uuid}")
            def requiredItem = ServiceItem.findByUuid(it.uuid)
            if (requiredItem != null) {
                requiredIds << requiredItem.id
            } else {
                foundAllChildren = false
                log.info("unable to find required serviceItem.uuid = ${it.uuid} for parent.uuid = ${json.serviceItem.uuid}")
            }
        }
        if (requiredIds.size() > 0) {
            def updated = relationshipService.addOrRemoveRequires(parent.id, requiredIds)
            if (updated) {
                stats.relationships.updated += 1
            }
        } else {
            stats.relationships.notUpdated += 1
        }
    }
}

