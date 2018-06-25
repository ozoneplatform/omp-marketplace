package org.ozoneplatform.auditing

import javax.servlet.http.HttpServletRequest

import marketplace.AuditLoggingService
import org.ozoneplatform.auditing.filter.AbstractAuditingFilters
import org.springframework.web.context.request.RequestContextHolder as RCH

class MarketplaceAuditingFilters extends AbstractAuditingFilters{

    AuditLoggingService auditLoggingService

	public String getApplicationVersion(){
		auditLoggingService.applicationVersion
	}


	@Override
	public boolean doCefLogging() {
        auditLoggingService.doCefLogging()
	}


	@Override
	public String getUserName() {
		auditLoggingService.userName
	}


	@Override
	public String getHostClassification() {
        auditLoggingService.hostClassification
	}

	@Override
	public String getDeviceProduct() {
        auditLoggingService.deviceProduct
	}

	@Override
	public String getDeviceVendor() {
		auditLoggingService.deviceVendor
	}

	@Override
	public String getDeviceVersion() {
        auditLoggingService.deviceVersion
	}

	@Override
	public int getCEFVersion() {
		auditLoggingService.getCEFVersion()
	}

	public HttpServletRequest getRequest()  {
        return RCH?.getRequestAttributes()?.getRequest()
    }

	@Override
	public Map<String, String> getUserInfo(){
        auditLoggingService.userInfo
	}
}
