package marketplace

import ozone.marketplace.domain.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import org.springframework.transaction.annotation.Transactional

class CategoryService extends MarketplaceService {

    def sessionFactory

    @Transactional(readOnly = true)
    def search(params) {

        def c = Category.createCriteria()
        if (params.containsKey('query') && !params.query) params.remove('query')
        def results = c.list(params) {
            if (params.query) {
                ilike("title", "%${params.query}%")
            }
        }

        return [
            data: results,
            total: results.totalCount
        ]
    }

    @Transactional(readOnly = true)
    def list(def params) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = Category.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = Category.list(params)
        }
        return results
    }

    @Transactional(readOnly = true)
    def get(def params) {
        return Category.get(params.id)
    }

    @Transactional(readOnly = true)
    def countTypes() {
        return Category.count()
    }

    /**
     * There are two cases to consider:
     * 1. Category with no listing associated with it. Delete it
     * 2. Category with active listing associated with it. Throw error, don't delete it
     */
    @Transactional
    def delete(def id) {
        Category category
        try {
            category = Category.get(id)
            if (!category) {
                throw new ValidationException(message: "objectNotFound", args: [id])
            }
            def criteria = ServiceItem.createCriteria()
            def cnt = criteria.get {
                projections {
                    count('id')
                }
                categories {
                    eq('id', new Long(id))
                }
            }
            if (cnt > 0) {
                throw new ValidationException(message: "delete.failure.serviceItem.exists", args: [category?.toString()])
            }
            category.delete(flush: true)
        }
        catch (ValidationException ve) {
            throw ve
        }
        catch (Exception e) {
            e.printStackTrace()
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${category}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [category?.toString(), message])
        }
    }

    @Transactional(readOnly = true)
    def getAllCategories() {
        Category.list(sort: 'title', order: 'asc')
    }
}
