package marketplace

import javax.annotation.Nonnull

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

import org.hibernate.FlushMode
import org.hibernate.SessionFactory

import org.apache.commons.lang.exception.ExceptionUtils

import ozone.marketplace.domain.ValidationException

import static com.google.common.base.Preconditions.checkNotNull


@Transactional
class StateService extends MarketplaceService {

    SessionFactory sessionFactory

    @ReadOnly
    State get(Map params) {
        State.get(params.id as Long)
    }

    @ReadOnly
    List<State> getAllStates() {
        State.list(sort: 'title', order: 'asc')
    }

    @ReadOnly
    List<State> list(Map params) {
        List<State> results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = State.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        }
        else {
            results = State.list(params)
        }
        return results
    }

    @Deprecated
    @ReadOnly
    List<State> listB(Map params) {
        list(params)
    }

    @ReadOnly
    State findByTitle(String title) {
        State.findByTitle(title)
    }

    @ReadOnly
    int countTypes() {
        State.count()
    }

    void delete(long id) {
        State state = State.get(id)
        if (!state) {
            throw new ValidationException(message: "objectNotFound", args: [id])
        }
        delete(state)
    }

    /**
     * There are three cases to consider:
     * 1. State with no listing associated with it. Just delete it
     * 2. State with active listing associated with it. Throw error, don't delete it
     * 3. State with soft deleted listing associated with it. Throw error, don't delete it
     **/
    void delete(@Nonnull State state) {
        checkNotNull(state, "state must not be null")

        if (ServiceItem.countByState(state) > 0) {
            throw new ValidationException(message: "delete.failure.serviceItem.exists", args: [state?.toString()])
        }

        try {
            state.delete(flush: true)
        } catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${state}. ${message}"

            // TODO: Is this still needed?
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [state?.toString(), message])
        }
    }

    void createRequired() {
        log.info "Loading states..."

        def statesInConfig = config.marketplace.metadata.states

        if (!statesInConfig) {
            log.error "States metadata info was not found in the loaded config files."
            return
        }

        statesInConfig.each { item ->
            if (!State.findByTitle(item.title as String)) {
                new State(title: item.title, description: item.description, isPublished: item.isPublished).save()
            }
        }
    }

}
