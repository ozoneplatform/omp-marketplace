import grails.util.Environment

import marketplace.rest.AuditableDataBindingListener
import marketplace.rest.HashMapWriter
import marketplace.rest.ServiceItemDataBindingListener

import org.ozoneplatform.auditing.AuditLogListener
import org.springframework.security.web.FilterChainProxy

import marketplace.AccountService
import marketplace.AutoLoginAccountService
import marketplace.Constants
import ozone3.Configuration
import util.CustomPropertyEditorRegistrar

import ozone.banner.MP_BannerBean
import ozone.interceptor.MP_RESTInterceptor
import ozone.utils.ApplicationContextHolder
import org.ozoneplatform.auditing.AuditStampEventListener

// Place your Spring DSL code here
beans = {

    def DEFAULT_AGENCY = 'DEFAULT_STORE_NAME'

    xmlns context: 'http://www.springframework.org/schema/context'
    context.'component-scan'('base-package': 'marketplace.validator,marketplace.rest')

    auditLogListener(AuditLogListener) {
        sessionFactory = ref('sessionFactory')
        accountService = ref('accountService')
        grailsApplication = ref('grailsApplication')
        marketplaceApplicationConfigurationService = ref('marketplaceApplicationConfigurationService')
    }

    auditStampEventListener(AuditStampEventListener)

    applicationContextHolder(ApplicationContextHolder) { bean ->
        bean.factoryMethod = 'getInstance'
    }

    // wire up a different account service if -Duser=something and environment is development
    if (isDevelopment()) {
        //empty sprint security bean
        springSecurityFilterChain(FilterChainProxy, [])

        switch (System.properties.user) {
            case "testUser1":
                println("Using AutoLoginAccountService - you will be logged in as testUser1")
                accountService(AutoLoginAccountService) {
                    autoAccountUsername = "testUser1"
                    autoAccountName = "Test User 1"
                    autoAccountEmail = "testuser1@nowhere.com"
                    autoOrganization = DEFAULT_AGENCY
                    autoRoles = [Constants.USER]
                }
                break
            case "testAdmin1":
                println("Using AutoLoginAccountService - you will be logged in as testAdmin1")
                accountService(AutoLoginAccountService) {
                    autoAccountUsername = "testAdmin1"
                    autoAccountName = "Test Administrator 1"
                    autoAccountEmail = "testadmin1@nowhere.com"
                    autoOrganization = DEFAULT_AGENCY
                    autoRoles = [Constants.USER, Constants.ADMIN, Constants.EXTERNADMIN]
                }
                break
            default:
                if (Environment.current == Environment.DEVELOPMENT)// || Environment.current.startsWith('with_')) {
                {
                    println("Using AutoLoginAccountService - you will be logged in as testUser1")
                    accountService(AutoLoginAccountService) {
                        autoAccountUsername = "testUser1"
                        autoAccountName = "Test User 1"
                        autoAccountEmail = "testuser1@nowhere.com"
                        autoRoles = [Constants.USER]
                    }
                }
                else {
                    println("Not using AutoLoginAccountService - if you want to do so, set -Duser=[testUser1|testAdmin1|testAdmin2] in your environment")
                    accountService(AccountService)
                }
                break
        }
    }
    else if (isTest()) {
        springSecurityFilterChain(FilterChainProxy, [])

        accountService(AutoLoginAccountService) {
            autoAccountUsername = "testUser1"
            autoAccountName = "Test User 1"
            autoAccountEmail = "testuser1@nowhere.com"
            autoOrganization = DEFAULT_AGENCY
            autoRoles = [Constants.USER]
        }
    }
    else {
        importBeans('classpath:ozone/marketplace/application.xml')
        accountService(AccountService)
    }

    importBeans('classpath:ozone/marketplace/session-control.xml')

    customPropertyEditorRegistrar(CustomPropertyEditorRegistrar)

    mp_RESTInterceptorService(MP_RESTInterceptor) {}

    mp_BannerBeanService(MP_BannerBean) {
        enableBanner = false
        header = "<div class='north-banner-text'>Welcome to Marketplace</div>";
        footer = "<div class='south-banner-text'>Welcome to Marketplace</div>";
        css = """ body {
					padding-top: 100px!important;
					padding-bottom: 22px!important;
				}

				#header {
					top: 19px!important;
				}

				#footer {
					bottom: 19px!important;
				}

				#TypeMenuId, #DomainMenuId, #CategoryMenuId, #AgencyMenuId {
				  z-index: 18000 !important;
				  top: 155px !important;
				}

				#northBanner, #southBanner {
					display: block;
				}
				.bannerClass .north-banner-text {
					color: blue;
					text-align: center;
					font-size: 17px;
					background-color: orange;
					font-weight: bold;
				}
				.bannerClass .south-banner-text {
					color: pink;
					text-align: center;
					font-size: 17px;
					background-color: green;
					font-weight: bold;
				}""";
        js = """jQuery('html').addClass('bannerClass');""";
    }

    OzoneConfiguration(Configuration) {
        // log4j file watch interval in milliseconds
       // log4jWatchTime = 180000; // 3 minutes
        freeTextEntryWarningMessage = ""
    }

    /**
     * OP-5818: Use the Spring class instead of the default Grails subclass of it.*/
//    openSessionInViewInterceptor(OpenSessionInViewInterceptor) {
//        sessionFactory = ref('sessionFactory')
//    }

    hashMapWriter(HashMapWriter)

    auditableDataBindingListener(AuditableDataBindingListener)
    serviceItemDataBindingListener(ServiceItemDataBindingListener)
}

static boolean isProduction() {
    Environment.current == Environment.PRODUCTION
}

static boolean isDevelopment() {
    Environment.current == Environment.DEVELOPMENT
}

static boolean isTest() {
    Environment.current == Environment.TEST
}

