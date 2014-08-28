package marketplace

import org.springframework.dao.DataIntegrityViolationException
import ozone.marketplace.domain.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import org.springframework.transaction.annotation.Transactional

class IntentActionService {

    def sessionFactory
    def grailsApplication

    @Transactional(readOnly = false)
    def deleteIntentActionById(id) {
        def action = IntentAction.get(id)
        if (!action) {
            throw new ValidationException(message: "objectNotFound", args: [id])
        }

        try {
            action.delete(flush: true)
        }
        catch (DataIntegrityViolationException e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${action}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [action?.toString(), message])
        }
    }

    @Transactional(readOnly = false)
    def createRequired() {
        log.info "Loading Intent Actions"

        def intentActionsInConfig = grailsApplication.config.marketplace.metadata.intentActions

        if (intentActionsInConfig) {
            intentActionsInConfig.each {
                if (!IntentAction.findByTitle(it.title)) {
                    new IntentAction(it).save()
                }
            }
        } else {
            log.error "Intent Actions metadata info was not found in the loaded config"
        }
    }
}
