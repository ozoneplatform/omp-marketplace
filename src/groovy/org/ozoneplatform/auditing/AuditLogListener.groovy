package org.ozoneplatform.auditing

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

import javax.servlet.http.HttpServletRequest

import marketplace.AccountService
import marketplace.AdminObjectFormatter
import marketplace.ChangeDetail
import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.collection.PersistentCollection
import org.hibernate.collection.PersistentSet
import org.hibernate.event.*
import org.hibernate.type.*
import org.ozoneplatform.auditing.hibernate.AbstractAuditLogListener
import org.springframework.web.context.request.RequestContextHolder as RCH
import org.ozoneplatform.auditing.format.cef.Extension

import ozone.marketplace.util.event.*


public class AuditLogListener extends AbstractAuditLogListener implements PostCollectionUpdateEventListener{

  	GrailsApplication grailsApplication

  	AccountService accountService

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

  	boolean transactional = false


	@Override
    public void onPostUpdateCollection(PostCollectionUpdateEvent event){
    }

    @Override
    public void onPostInsert(final PostInsertEvent event) {
	   if(!(event.getEntity() instanceof ChangeDetail)){
			super.onPostInsert(event)
	   }
    }

    @Override
    public void onPostUpdate(final PostUpdateEvent event){
		if(!(event.getEntity() instanceof ChangeDetail)){
		  super.onPostUpdate(event)
		}
    }


	@Override
	public HttpServletRequest getRequest(){
		RCH?.getRequestAttributes()?.getRequest()
	}

	@Override
    protected boolean doCefLogging() {
       marketplaceApplicationConfigurationService.is(CEF_LOGGING_ENABLED)
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

    @Override
	protected boolean doCefObjectAccessLogging(){
        marketplaceApplicationConfigurationService.is(CEF_OBJECT_ACCESS_LOGGING_ENABLED)
	}

	@Override
	public String getApplicationVersion() {
		grailsApplication.metadata['app.version']
	}

	@Override
	public String getHostClassification() {
		marketplaceApplicationConfigurationService.valueOf(SECURITY_LEVEL) ?: Extension.UNKOWN_VALUE
	}

	@Override
	public String getUserName() {
		accountService.getLoggedInUsername()
	}

    @Override
    protected boolean isAccessAuditable(String entityClassName) {
        //TODO: Should this be a database configuration?
        entityClassName in ["marketplace.ServiceItem"]
    }
}
