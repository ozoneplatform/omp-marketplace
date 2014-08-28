package org.ozoneplatform.auditing

import java.util.Map;

import javax.servlet.http.HttpServletRequest

import marketplace.AccountService
import marketplace.configuration.MarketplaceApplicationConfigurationService
import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration
import org.ozoneplatform.auditing.filter.AbstractAuditingFilters
import org.ozoneplatform.auditing.format.cef.Extension
import org.springframework.beans.BeansException
import org.springframework.web.context.request.RequestContextHolder as RCH
import ozone.utils.User

class MarketplaceAuditingFilters extends AbstractAuditingFilters{

	GrailsApplication grailsApplication

	AccountService accountService

	MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

	public String getApplicationVersion(){
		return grailsApplication.metadata['app.version']
	}


	@Override
	public boolean doCefLogging() {
		try{
			ApplicationConfiguration doCefLogging = marketplaceApplicationConfigurationService.getApplicationConfiguration(CEF_LOGGING_ENABLED)
			if(doCefLogging)
				return Boolean.valueOf(doCefLogging.value)
		} catch (Throwable t){
			return true
		}
		return true
	}


	@Override
	public String getUserName() {
		return accountService.getLoggedInUsername()
	}


	@Override
	public String getHostClassification() {
        String hostCls
		try{
		    ApplicationConfiguration securityLevel = marketplaceApplicationConfigurationService.getApplicationConfiguration(SECURITY_LEVEL)
            hostCls = securityLevel?.value ?: Extension.UNKOWN_VALUE
		} catch (BeansException ex){
			hostCls = Extension.UNKOWN_VALUE
		}

        hostCls
	}

	@Override
	public String getDeviceProduct() {
		grailsApplication.config.cef.device.product
	}

	@Override
	public String getDeviceVendor() {
		grailsApplication.config.cef.device.vendor
	}

	@Override
	public String getDeviceVersion() {
	    	grailsApplication.config.cef.device.version
	}

	@Override
	public int getCEFVersion() {
		grailsApplication.config.cef.version
	}


	public HttpServletRequest getRequest()  {
        return RCH?.getRequestAttributes()?.getRequest()
    }


	@Override
	public Map<String, String> getUserInfo(){
		User currentUser = accountService.getLoggedInUser()
		def map = [:]
		map['USERNAME'] = currentUser.username
		map['NAME'] 	= currentUser.name
		map['ORG'] 		= currentUser.org
		map['EMAIL'] 	= currentUser.email
		map['ROLES']	= accountService.getLoggedInUserRoles().collect{it instanceof String ? it : it.authority}
		map
	}

}
