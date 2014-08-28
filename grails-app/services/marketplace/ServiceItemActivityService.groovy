package marketplace

import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import org.springframework.transaction.annotation.Transactional

class ServiceItemActivityService extends OzoneService {

    def sessionFactory
    def changeLogService
    def profileService
    def accountService

    //TODO: this is used by the my listings page (via the ServiceItem/myListingView controller/action) to populate the recent activities. It can be removed when that feature is migrated to the REST API
    @Transactional(readOnly = true)
    def getLatestActivity(def params, def username, def accessType) {
        log.debug 'getLatestActivity:'
        try {
            def activities = ServiceItemActivity.createCriteria().list(max: params.limit, offset: params.start) {
                serviceItem {
                    if (accessType.equals(Constants.VIEW_USER)) {
                        owners {
                            eq('username', username)
                        }
                        //eq('isHidden', 0)
                    }
                }
                order('id', 'desc')
            }
            return activities
        }

        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred retreiving service item activiies ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new Exception(message, e)
        }
    }
}
