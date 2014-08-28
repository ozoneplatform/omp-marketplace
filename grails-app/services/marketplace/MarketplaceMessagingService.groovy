package marketplace

import org.ozoneplatform.messaging.payload.OzoneMessage
import static ozone.marketplace.enums.MarketplaceApplicationSetting.*


class MarketplaceMessagingService {

    static transactional = false

    def messageService

    def marketplaceApplicationConfigurationService

    def profileService

    def grailsApplication

    public void sendNotificationOfChange(ServiceItem serviceItem, ServiceItemActivity activity){
        if(grailsApplication.config.notifications.enabled == true){
            OzoneMessage message = createMessage(serviceItem)
            
            if(activity.action == Constants.Action.INSIDE || activity.action == Constants.Action.OUTSIDE){
                message.body = "${serviceItem.title} set to ${activity.action.description} by ${serviceItem.editedBy}"
            } else{
                message.body = "${serviceItem.title} ${activity.action.description} by ${serviceItem.editedBy}"
            }

            try{
                runAsync{
                    messageService.sendGroupMessage(message)
                }
            } catch (Exception e){
                log.error "Unable to send message: ${e.message}"
            }
        }
    }

    public OzoneMessage createMessage(ServiceItem serviceItem) {
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