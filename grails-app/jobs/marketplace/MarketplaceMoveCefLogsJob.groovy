package marketplace

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*
import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.ozoneplatform.auditing.quartz.jobs.AbstractFileMovementJob


class MarketplaceMoveCefLogsJob extends  AbstractFileMovementJob{
	
	MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService
	
	def group = "cefLogSweepingGroup"
	
	
	static triggers = { 
		long fiveMinutes = 1000 * 60 * 5
		long oneHour = fiveMinutes * 12
		simple name: 'MarketplaceMoveCefLogsTrigger', startDelay: fiveMinutes, repeatInterval: oneHour
	}
	
	@Override
	public String getFromLocation(){
		marketplaceApplicationConfigurationService.valueOf(CEF_LOG_LOCATION)
	}
	
	@Override
	public String getToLocation(){
		marketplaceApplicationConfigurationService.valueOf(CEF_LOG_SWEEP_LOCATION)
	}
	
	@Override
	public boolean isJobEnabled(){
		marketplaceApplicationConfigurationService.is(CEF_LOG_SWEEP_ENABLED)
	}
	
}
