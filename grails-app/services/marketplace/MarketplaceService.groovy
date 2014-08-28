package marketplace

import org.apache.commons.lang.exception.ExceptionUtils

import ozone.marketplace.domain.ValidationException
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation

class MarketplaceService extends OzoneService {

    @Transactional
    def save(def domain) {
        try {
            if (!domain.save(flush: true)) {
                throw new ValidationException()
            }
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to create ${domain}. ${message}"
            throw new ValidationException(message: "create.failure", args: [domain.toString(), message])
        }
    }

	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW )
	def saveInNewTransaction(def domain){
        try {
            if (!domain.save(flush: true)) {
                throw new ValidationException()
            }
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to create ${domain}. ${message}"
            throw new ValidationException(message: "create.failure", args: [domain.toString(), message])
        }
	}
	
	@Transactional
	def delete(def domain){
		domain.delete()
	}
}
