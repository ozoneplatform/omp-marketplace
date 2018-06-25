package marketplace

import grails.gorm.transactions.Transactional
import org.hibernate.SessionFactory

@Transactional
class ChangeLogService extends MarketplaceService {

    SessionFactory sessionFactory

    ProfileService profileService

    //TODO: this is being used by ContactService and ExtServiceItemService, can be removed when those functions are moved to the REST API
    def saveChangeDetail(Object objectIn, String fieldName, String oldValue, String newValue, Long versionIn = -1) {
        //MARKETPLACE-2854 If this is a new object then don't create a change detail
        if (!objectIn.version) return
        def newVersion = objectIn.version
        if (versionIn != -1) {
            newVersion = versionIn
        }
        def changeDetail = new ChangeDetail(
            objectClassName: objectIn.getClass().getName(),
            objectId: objectIn.id,
            // TODO: do this a better way!!!!
            objectVersion: newVersion,
            fieldName: fieldName,
            oldValue: oldValue ?: '',
            newValue: newValue ?: ''
        )

//        log.debug "saveChangeDetail: saving ${changeDetail}"
        // do we want flush:true?
        if (!changeDetail.save()) {
            changeDetail.errors.each {
//                log.error it
            }
        }
    }
}
