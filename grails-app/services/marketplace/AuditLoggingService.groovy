package marketplace

import grails.core.GrailsApplication
import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.springframework.beans.BeansException

import org.apache.log4j.Logger

import org.ozoneplatform.auditing.format.cef.CEF
import org.ozoneplatform.auditing.format.cef.Extension
import org.ozoneplatform.auditing.format.cef.factory.CEFFactory
import org.ozoneplatform.auditing.format.cef.factory.ExtensionFactory
import org.ozoneplatform.auditing.enums.EventTypes
import org.ozoneplatform.auditing.enums.PayloadType

import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration
import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

import ozone.utils.User

/**
 * TODO Flesh out this class more and migrate the existing copy-pasted audit logging
 * code to use it
 */
class AuditLoggingService {
    private static final Logger cefLog = Logger.getLogger(AuditLoggingService.class)


    GrailsApplication grailsApplication
    AccountService accountService
    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    public String getApplicationVersion(){
        return grailsApplication.metadata['app.version']
    }

    public boolean doCefLogging() {
        try{
            ApplicationConfiguration doCefLogging =
                marketplaceApplicationConfigurationService.getApplicationConfiguration(
                    CEF_LOGGING_ENABLED)

            if(doCefLogging) return Boolean.valueOf(doCefLogging.value)
        } catch (Throwable t){
            return true
        }
        return true
    }

    public String getUserName() {
        return accountService.getLoggedInUsername()
    }

    public String getHostClassification() {
        String hostCls
        try{
            ApplicationConfiguration securityLevel =
                marketplaceApplicationConfigurationService.getApplicationConfiguration(
                    SECURITY_LEVEL)

                hostCls = securityLevel?.value ?: Extension.UNKOWN_VALUE
        } catch (BeansException ex){
            hostCls = Extension.UNKOWN_VALUE
        }

        hostCls
    }

    public String getDeviceProduct() {
        grailsApplication.config.cef.device.product
    }

    public String getDeviceVendor() {
        grailsApplication.config.cef.device.vendor
    }

    public String getDeviceVersion() {
        grailsApplication.config.cef.device.version
    }

    public int getCEFVersion() {
        grailsApplication.config.cef.version
    }

    public Map<String, String> getUserInfo(){
        User currentUser = accountService.getLoggedInUser()

        def map = [:]
        map['USERNAME'] = currentUser.username
        map['NAME'] 	= currentUser.name
        map['ORG'] 		= currentUser.org
        map['EMAIL'] 	= currentUser.email
        map['ROLES']	= accountService.getLoggedInUserRoles().collect{
            it instanceof String ? it : it.authority
        }

        map
    }

    public void logImport(ImportTask task) {
        CEF cef = CEFFactory.buildBaseCEF(getCEFVersion(), deviceVendor, deviceProduct,
            deviceVersion, EventTypes.IMPORT.description, "An Import was triggered.", 7)

        def fields = ExtensionFactory.getBaseExtensionFields(null, userName,
            applicationVersion, hostClassification)

        fields[Extension.EVENT_TYPE] = EventTypes.IMPORT.getDescription()
        fields[Extension.PAYLOAD_CLS] = hostClassification
        fields[Extension.PAYLOAD] = task.name
        fields[Extension.PAYLOAD_ID] = task.url
        fields[Extension.PAYLOAD_TYPE] = PayloadType.OBJECT.getDescription()
        fields[Extension.MEDIA_TYPE] = Extension.UNKOWN_VALUE

        cef.extension = new Extension(fields)

        cefLog.info cef.toString()
    }
}
