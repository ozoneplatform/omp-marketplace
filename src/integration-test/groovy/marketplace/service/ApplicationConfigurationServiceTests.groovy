package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.*
//import static org.junit.Assert.assertThat
import static ozone.marketplace.enums.MarketplaceApplicationSetting.*

@Integration
@Rollback
class ApplicationConfigurationServiceTests extends Specification {

    @Autowired
    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService


    //TODO BVEST Finish this
    //This tests that config items are created.  this is called in bootstrap so when run as INTG is somewhat redundant
    void testCreateRequired(){
        marketplaceApplicationConfigurationService.createRequired()

        //Add codes to this as so that they can be verified that they are read in from the db
        def paramCodes = [
                FRANCHISE_STORE.code,
                INSIDE_OUTSIDE_BEHAVIOR.code
        ]

        //Get all the configs in a list so we can inspect each
        def applicationConfigurationList = ApplicationConfiguration.list()

        //Test that the provided list of codes is in the application configuration list
        paramCodes.each { code ->
            //assertThat(applicationConfigurationList.find({it.code == code}), is(notNullValue()))
        }
    }

    //Critical method here a lot of the application will use it to make decisions on what mode its running in
    void testIsFranchiseStore(){
        when:
        ApplicationConfiguration config = marketplaceApplicationConfigurationService.getApplicationConfiguration(FRANCHISE_STORE)

//        if(config.value == "true"){
//            assertThat(marketplaceApplicationConfigurationService.isFranchiseStore(), is(equalTo(true)))
//        } else{
//            assertThat(marketplaceApplicationConfigurationService.isFranchiseStore(), is(equalTo(false)))
//        }
        then:
        marketplaceApplicationConfigurationService.isFranchiseStore()
    }

    void testGetApplicationConfiguration(){
        when:
        ApplicationConfiguration config = marketplaceApplicationConfigurationService.getApplicationConfiguration(FRANCHISE_STORE)

        then:
        config.code == FRANCHISE_STORE.code
//        assertThat(config.code, is(equalTo(FRANCHISE_STORE.code)))
    }


    void testGetAllApplicationConfigurations(){
        when:
        def allApplicationConfigurations =  marketplaceApplicationConfigurationService.getAllApplicationConfigurations()
        then:
        allApplicationConfigurations.size() != 0
    }

    //Tests that when in franchise mode an error is registered if agency is null
    void testValidateApplicationConfigurationAgencyCanNotBeNullWhenFranchiseStore(){
        when:
        //Get a handle on the agency and set its value to null
        ApplicationConfiguration config =
            marketplaceApplicationConfigurationService.getApplicationConfiguration(STORE_NAME)
        config.value = null
        marketplaceApplicationConfigurationService.saveApplicationConfiguration(config)


        //Make sure that we are in franchise mode
        ApplicationConfiguration franchiseConfig =
            marketplaceApplicationConfigurationService.getApplicationConfiguration(FRANCHISE_STORE)
        franchiseConfig.value = "true"
        marketplaceApplicationConfigurationService.saveApplicationConfiguration(franchiseConfig)

        marketplaceApplicationConfigurationService.validateApplicationConfiguration(config)

        then:
        config.errors.getFieldError("value") != null
    }

    //Tests nothing happens if a null object is validated (objects should be checked to be non null in clients)
    void testValidateApplicationConfigurationNothingHappensWhenNull(){
        expect:
        marketplaceApplicationConfigurationService.validateApplicationConfiguration(null)
    }
}
