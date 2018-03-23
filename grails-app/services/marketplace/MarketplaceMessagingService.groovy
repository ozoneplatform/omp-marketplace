package marketplace

import grails.core.GrailsApplication
import grails.gorm.services.Service

import marketplace.configuration.MarketplaceApplicationConfigurationService

import org.ozoneplatform.messaging.payload.OzoneMessage
import org.ozoneplatform.messaging.service.api.MessageService

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

@Service
class MarketplaceMessagingService {

    static transactional = false

    MessageService messageService

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    ProfileService profileService

    GrailsApplication grailsApplication

    void sendNotificationOfChange(ServiceItem serviceItem, ServiceItemActivity activity){
        if (!(grailsApplication.config.notifications.enabled == true)) return

        OzoneMessage message = createMessage(serviceItem)

        Profile editedBy = serviceItem.findEditedByProfile()
        if(activity.action == Constants.Action.INSIDE || activity.action == Constants.Action.OUTSIDE){
            message.body = "${serviceItem.title} set to ${activity.action.description} by ${editedBy}"
        } else{
            message.body = "${serviceItem.title} ${activity.action.description} by ${editedBy}"
        }

        try{
            runAsync{
                messageService.sendGroupMessage(message)
            }
        } catch (Exception e){
            log.error "Unable to send message: ${e.message}"
        }
    }

    OzoneMessage createMessage(ServiceItem serviceItem) {
        OzoneMessage message = new OzoneMessage()
        message.subject = "Recent activity for ${serviceItem.title}"
        message.classification = this.marketplaceApplicationConfigurationService.valueOf(SECURITY_LEVEL)

        //URL Fields
        String baseUrl = this.marketplaceApplicationConfigurationService.valueOf(URL_PUBLIC)

        if(!baseUrl?.endsWith("/"))
            baseUrl = baseUrl + "/"

        message.href = "${baseUrl}serviceItem/search?queryString=uuid=${serviceItem.uuid}"
        message.sourceURL = baseUrl

        def distroList = profileService.getProfilesWithAdminRole().collect { person -> person.username}
        message.recipients << distroList

        return message
    }
}
