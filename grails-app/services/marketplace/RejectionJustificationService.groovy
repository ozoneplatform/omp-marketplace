package marketplace

import org.hibernate.SessionFactory

import ozone.marketplace.domain.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import grails.gorm.transactions.Transactional

class RejectionJustificationService extends MarketplaceService {

    SessionFactory sessionFactory

    @Transactional(readOnly = true)
    def list(def params) {
        return RejectionJustification.list(params)
    }

    @Transactional(readOnly = true)
    def get(def params) {
        return RejectionJustification.get(params.id)
    }

    @Transactional(readOnly = true)
    def countJustifications() {
        return RejectionJustification.count()
    }

    @Transactional
    def delete(def id) {
        RejectionJustification rj
        try {
            rj = RejectionJustification.get(id)
            if (!rj) {
                throw new ValidationException(message: "objectNotFound", args: [id])
            }

            def criteria = RejectionListing.createCriteria()
            def cnt = criteria.get {
                projections {
                    count('id')
                }
                justification {
                    eq('id', new Long(id))
                }
            }
            if (cnt > 0) {
                throw new ValidationException(message: "delete.failure.rejectionListing.exists", args: [rj?.toString()])
            }
            rj.delete(flush: true)
        }
        catch (ValidationException ve) {
            throw ve
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${rj}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [rj?.toString(), message])
        }
    }


    @Transactional
    def createRequired() {
        log.info "Loading rejectionJustification..."

        def justificationsInConfig = config.marketplace.metadata.rejectionJustifications

        if (justificationsInConfig) {
            justificationsInConfig.each { item ->
                if (!RejectionJustification.findByTitle(item.title)) {
                    new RejectionJustification(title: item.title, description: item.description).save()
                }
            }
        } else {
            log.error "Rejection justification metadata info was not found in the loaded config files."
        }
    }
}
