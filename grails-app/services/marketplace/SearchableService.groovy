package marketplace

import grails.converters.JSON
import grails.plugins.elasticsearch.ElasticSearchService

import marketplace.search.SearchCriteria
import org.elasticsearch.action.search.SearchPhaseExecutionException
import org.elasticsearch.common.ParsingException

//import org.elasticsearch.action.search.SearchPhaseExecutionException
//import org.elasticsearch.common.ParsingException
//import org.elasticsearch.search.SearchHits
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock


class SearchableService {

    static transactional = false

    ElasticSearchService elasticSearchService

    def searchListings(SearchCriteria searchCriteria) {
        def ops = [size: searchCriteria.max,
                   from: searchCriteria.offset,
                   types: searchCriteria.TYPES_TO_SEARCH]

        def retry = true

        while (retry) {
            try {
                return elasticSearchService.search(ops, searchCriteria.searchClause, searchCriteria.extraSearchSource)
            }
            catch (ParsingException pe) {
                log.warn "!Search Engine Parse Issue: QueryString \'${options.queryString}\' is not valid, and all results will FAIL to return listings."
                log.debug "!Search Engine Parse Exception Message: ${pe.getMessage()}"
                return null
            } catch (org.apache.lucene.search.BooleanQuery.TooManyClauses exp) {
                updateMaxClause(exp)
                retry = true
            }
            catch (SearchPhaseExecutionException spee) {
                System.err.println "SearchPhaseExecutionException: ${spee.getMessage()}"
                throw new IllegalArgumentException()
            }
            catch (Exception e) {
                log.error('searchListings error', e)
                throw e
            }
        }
    }

    /**
     * Increases max clauses count when too many clauses are built by lucene.
     */
    private void updateMaxClause(def exp) {
        log.error("Too many hits for query: ", exp);
        org.apache.lucene.search.BooleanQuery.setMaxClauseCount(org.apache.lucene.search.BooleanQuery.getMaxClauseCount() * 2);
    }

    void reindexServiceItems(def closure) {
        def criteria = ServiceItem.createCriteria()
        def serviceItems = criteria.list(closure)
        def total = serviceItems.size()
        log.info "Re-indexing ${total} listings!!!"
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            try {
                def i = 0
                serviceItems.each() {
                    if (it.types.ozoneAware && !it.owfProperties) {
                        log.debug "adding OwfProperties to serviceItem ${it.title}"
                        OwfProperties owfProperties = new OwfProperties()
                        owfProperties.save()
                        it.owfProperties = owfProperties
                        it.save()
                    } else if (!it.types.ozoneAware && it.owfProperties) {
                        log.debug "removing OwfProperties from serviceItem ${it.title}"
                        OwfProperties owfProperties = it.owfProperties
                        it.owfProperties = null
                        owfProperties.delete()
                        it.save()
                    }
                    it.index()
                    if ((i % 100) == 0) {
                        log.info "Indexed ${i} out of ${total}"
                    }
                    i++
                }
                log.info "Indexed ${i} out of ${total}"
            }
            catch (Exception ex) {
                log.error('reindex failed', ex)
            }
        } finally {
            lock.unlock();
        }
    }

    public void reindexAll() {
        elasticSearchService.index([:])
    }

    public void unindexAll() {
        elasticSearchService.unindex([:])
    }
}

