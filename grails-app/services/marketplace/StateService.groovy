package marketplace

import ozone.marketplace.domain.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import java.text.SimpleDateFormat
import org.springframework.transaction.annotation.Transactional
// import static org.codehaus.groovy.grails.commons.ConfigurationHolder.config
import grails.util.Holders

class StateService extends MarketplaceService {

    def sessionFactory
    def config = Holders.config

    @Transactional(readOnly = true)
    def listB(def params) {
        def results = State.list(params)
        def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
        results.each() {
            log.debug "    ${it} ${it.editedDate} ${it.editedDate.getClass()}"
            log.debug "    ${it} ${dateFormatter.format(it.editedDate)}"
        }
        return results
    }

    @Transactional(readOnly = true)
    def list(def params) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = State.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = State.list(params)
        }
        return results
    }

    def get(def params) {
        return State.get(params.id)
    }

    def countTypes() {
        return State.count()
    }

    /**
     * There are three cases to consider:
     * 1. State with no listing associated with it. Just delete it
     * 2. State with active listing associated with it. Throw error, don't delete it
     * 3. State with soft deleted listing associated with it. Throw error, don't delete it
     */
    @Transactional
    def delete(def id) {
        State st
        try {
            st = State.get(id)
            if (!st) {
                throw new ValidationException(message: "objectNotFound", args: [id])
            }
            def criteria = ServiceItem.createCriteria()
            def cnt = criteria.get {
                projections {
                    count('id')
                }
                state {
                    eq('id', new Long(id))
                }
            }
            if (cnt > 0) {
                throw new ValidationException(message: "delete.failure.serviceItem.exists", args: [st?.toString()])
            }
            st.delete(flush: true)
        }
        catch (ValidationException ve) {
            throw ve
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${st}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [st?.toString(), message])
        }
    }

    @Transactional(readOnly = true)
    def getAllStates() {
        State.list(sort: 'title', order: 'asc')
    }

    @Transactional(readOnly = true)
    def findByTitle(String title) {
        return State.findByTitle(title)
    }

    @Transactional
    def createRequired() {

        log.info "Loading states..."

        def statesInConfig = config.marketplace.metadata.states

        if (statesInConfig) {
            statesInConfig.each { item ->
                if (!State.findByTitle(item.title)) {
                    new State(title: item.title, description: item.description, isPublished: item.isPublished).save()
                }
            }
        } else {
            log.error "States metadata info was not found in the loaded config files."
        }
    }
}
