package ozone.marketplace.dataexchange

import marketplace.Constants
import marketplace.Helper
import marketplace.ImportStatus
import org.apache.commons.logging.LogFactory
import org.grails.web.json.JSONObject

import java.text.SimpleDateFormat

/**
 * Provides generic structure for importing objects from JSON. Matched objects, as defined by findExistingObject(),
 * will be updated if the import is newer. Objects that don't already exist will be created. The number of updated,
 * created, and failed imports are tracked.
 */
abstract class AbstractImporter {
    protected static final log = LogFactory.getLog(this)

    String domainName
    protected final SimpleDateFormat dateFormat

    protected AbstractImporter(String domainName) {
        dateFormat = new SimpleDateFormat(Constants.OPT_DATE_FORMAT, Locale.US)
        dateFormat.setCalendar Calendar.getInstance(new SimpleTimeZone(0, "GMT"))
        this.domainName = domainName
    }

    abstract def findExistingObject(def json)

    abstract def createNewObject()

    def importFromJSONArray(def jsonArray, ImportStatus.Summary stats) {
        def importedArray = []
        jsonArray?.each {
            def importedObjId = importFromJSON(it, stats)
            if (importedObjId != null) {
                importedArray << importedObjId
            }
        }
        return importedArray
    }

    def importFromJSON(def json, ImportStatus.Summary stats) {
        def actionVerb = "processing"
        log.debug("${actionVerb} ${domainName}: ${json}")
        def importedObj = null
        try {
            importedObj = findExistingObject(json)
            if (importedObj) {
                if (shouldUpdate(json, importedObj)) {
                    actionVerb = "updating"
                    log.debug("${actionVerb} ${domainName}: ${json.uuid}")
                    importedObj = updateFromJSONAndSave(importedObj, json)
                    stats.updated += 1
                }
                else {
                    actionVerb = "not updating"
                    stats.notUpdated += 1
                }
            }
            else {
                actionVerb = "creating"
                log.debug("${actionVerb} ${domainName}")
                importedObj = createFromJSONAndSave(json)
                stats.created += 1
            }
        } catch (Exception e) {
            stats.failed += 1
            if (log.isDebugEnabled()) {
                log.error Helper.getStackTrace(e)
            }
            stats.messages << handleException(e, actionVerb, json)
        }
        return getIdentifier(importedObj)
    }

    protected def createFromJSONAndSave(def json) {
        def newObject = createNewObject()
        updateFromJSONAndSave(newObject, json)
        return newObject
    }

    protected def updateFromJSONAndSave(def object, def json) {
        object.bindFromJSON(json)
        object.save(failOnError: true, flush: true  )
        return object
    }

    protected boolean shouldUpdate(JSONObject json, def existingMatch) {
        def val = json.get('editedDate')
        def jsonEditedDate = null
        if (val) {
            jsonEditedDate = dateFormat.parse(val)
        }
        return  jsonEditedDate > existingMatch.editedDate
    }

    /**
     * This should return the best unique identifier of an imported object.
     * @param object the object to identify, although it may be null if the import failed
     */
    protected def getIdentifier(def object) {
        return object?.uuid
    }

    @SuppressWarnings("UnusedMethodParameter") // the base class doesn't use the json parameter, but derived classes do
    protected def handleException(Exception e, def actionVerb, def json) {
        return "Error ${actionVerb} ${domainName}: " << e
    }

}
