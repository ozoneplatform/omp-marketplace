package marketplace

import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional
import ozone.marketplace.domain.ValidationException

class IntentDataTypeService extends MarketplaceService {

    def sessionFactory
    def grailsApplication

    @Transactional
    def create(paramMap) {
        new IntentDataType(paramMap)
    }

    @Transactional(readOnly = true)
    def lookupById(id) {
        IntentDataType.get(id)
    }

    @Transactional(readOnly = true)
    def list(params) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = IntentDataType.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = IntentDataType.list(params)
        }
        return results
    }

    @Transactional(readOnly = true)
    def countTypes() {
        IntentDataType.count()
    }

    @Transactional
    def delete(id) {
        IntentDataType dataType
        dataType = IntentDataType.get(id)
        if (!dataType) {
            throw new ValidationException(message: 'objectNotFound', args: [id])
        }
        try {
            dataType.delete(flush: true)
        }
        catch (DataIntegrityViolationException e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${dataType}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [dataType?.toString(), message])
        }
    }

    @Transactional(readOnly = true)
    def getAllDataTypes() {
        IntentDataType.list(sort: 'title', order: 'asc')
    }

    @Transactional
    def createRequired() {
        log.info "Loading Intent Data Types"

        def intentDataTypesInConfig = grailsApplication.config.marketplace.metadata.intentDataTypes

        if (intentDataTypesInConfig) {
            intentDataTypesInConfig.each { dataType ->
                if (!IntentDataType.findByTitle(dataType.title)) {
                    new IntentDataType(dataType).save()
                }
            }
        } else {
            log.info "Intent Data Types metadata info was not found in the loaded config"
        }
    }

}
