package marketplace.configuration

import grails.converters.JSON
import org.ozoneplatform.appconfig.client.controller.AbstractApplicationConfigurationController
import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration;
import ozone.marketplace.enums.*


class ApplicationConfigurationController extends AbstractApplicationConfigurationController {


    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    //TODO: Is this action (and its URL mapping) still needed?
    def validateGroup = {
        def groupName = params?.groupName.split("\\?")[0]

        def configs = marketplaceApplicationConfigurationService.getApplicationConfigurationsByGroupName(groupName)
        def invalidConfigs = []
        configs?.each { config ->
            marketplaceApplicationConfigurationService.validateApplicationConfiguration(config)
            if (config.errors.hasErrors()) {
                def errors = [:]
                errors["id"] = config.id
                errors["errors"] = config.errors.getFieldErrors().collect {
                    grailsApplication.getMainContext().getMessage(it.code, it.arguments, it.defaultMessage, Locale.getDefault())
                }
                invalidConfigs << errors
            }
        }

        doRender(invalidConfigs)
    }

    //TODO: Is this action still needed?
    // Get the application configuration as JSON for a particular ApplicationSetting
    // via GET to applicationConfiguration/getApplicationConfiguration/{ApplicationSetting}
    // Example: applicationConfiguration/getApplicationConfiguration/ABOUT_BOX_CONTENT
    def getApplicationConfiguration = {

        def setting = params.id as MarketplaceApplicationSetting

        def applicationConfiguration = [applicationConfiguration: applicationConfigurationService.getApplicationConfiguration(setting)]

        doRender(applicationConfiguration)
    }

    def getFranchiseFlag = {
        def franchiseFlag = [isFranchiseStore: marketplaceApplicationConfigurationService.isFranchiseStore()]

        doRender(franchiseFlag);
    }

    def getAgenciesAsJSON = {
        ApplicationConfiguration storesConfig = applicationConfigurationService.getApplicationConfiguration(MarketplaceApplicationSetting.STORE_THEMES)
        Map stores = storesConfig?.valueAsMap()
        def agencies = new ArrayList();
        for (Map.Entry entry in stores) {
            def agency = [id: entry.key, name: entry.key, url: entry.value]
            agencies.add(agency)
        }
        def results = [total: agencies.size(), data: agencies]
        doRender(results);
    }

    //This is only needed to keep grails dependencies out of the abstract super class.
	@Override
	void doRender(def model){
		render model as JSON
	}

    //There may be different rules to saving app config items so this is the OMP impl
    @Override
    void doSave(ApplicationConfiguration applicationConfiguration) {
        marketplaceApplicationConfigurationService.saveApplicationConfiguration(applicationConfiguration)
    }


    @Override
    void doValidate(ApplicationConfiguration applicationConfiguration) {
        marketplaceApplicationConfigurationService.validateApplicationConfiguration(applicationConfiguration);
    }

    @Override
    void doDecorate(configAsMap) {
        if (!(configAsMap instanceof Map))
            return

        def prefix = "application.configuration.${configAsMap.code}"
        configAsMap.description = messageSource.getMessage("${prefix}.description")
        configAsMap.title = messageSource.getMessage("${prefix}.label")
        configAsMap.help = messageSource.getMessage("${prefix}.help")
    }

}
