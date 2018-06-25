package marketplace

import javax.servlet.http.HttpSession

import grails.config.Config

import org.springframework.web.context.request.RequestContextHolder
import grails.util.Holders
import grails.orm.HibernateCriteriaBuilder
import groovy.time.TimeCategory

abstract class OzoneService {

    Config config = Holders.config

    /**
     * getSession() - get the session from the Spring framework RequestContextHolder
     */
    HttpSession getSession() {
        try {
            return RequestContextHolder.currentRequestAttributes().getSession()
        }
        catch (Exception ignored) {}
        return null
    }

    def parseEditedSinceDate(def params) {
        def retDate
        if (params?.editedSinceDate) {
            retDate = params.editedSinceDate
            if (retDate instanceof String) {
                try {
                    retDate = Helper.parseExternalDate(retDate)
                } catch (Exception e) {
//                    log.error "Unable to parse input date: ${params.editedSinceDate}: ${e.message}"
                    retDate = null
                }
            } else if (!(retDate instanceof Date)) {
                // This would be abnormal but don't let the app choke
//                log.warn "Request received with editedSinceDate of type: ${retDate?.class?.name}; ignoring"
                retDate = null
            }
        }
        return retDate
    }

    /**
     * Returns the list of domain objects by performing a "editedDate" based search.
     * @param criteria
     * @param params
     * @param editedDate
     * @return
     */
    protected List onOrAfterEditedDate(HibernateCriteriaBuilder criteria, params, editedDate) {
        // Introduce a delta in date comparison since exact comparison of dates may not work
        use(TimeCategory) {
            editedDate = editedDate - 100.millisecond
        }
        criteria.list(params) {
            ge('editedDate', editedDate)
        }
    }
}
