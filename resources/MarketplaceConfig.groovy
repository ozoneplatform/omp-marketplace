import grails.util.Holders
import grails.util.*

println "Loading custom Marketplace configurations."

// Session timeout in minutes
//httpsession.timeout=60

//enables dynamic changes to gsp files in a running webapp
//grails.gsp.enable.reload = true

// Scheduled Import timeout value, in milliseconds. Applied to the connection to the remote
//   repository when reading listings. Default is 30 minutes.
//marketplace.importTimeout = 1800000

//Set the Default Theme
//Uncomment this to replace with your custom theme if you need to make it the default theme for the application
//marketplace.defaultTheme = "cobalt"


environments {

    production {

        dataSource {
            dbCreate = "none"
            username = "sa"
            password = ""
            driverClassName = "org.h2.Driver"
            url = "jdbc:h2:file:mktplProdDb"
            pooled = true
            properties {
                minEvictableIdleTimeMillis = 180000
                timeBetweenEvictionRunsMillis = 180000
                testOnBorrow = true
                testWhileIdle = false
                testOnReturn = false
                validationQuery = "SELECT 1"
                validationInterval = 30000
                maxActive = 100
                initialSize = 10
                maxIdle = 50
                minIdle = 10
                removeAbandoned = false
                jdbcInterceptors = null
            }
        }

        notifications {
            xmpp {
                username = ''
                password = ''
                host = ''
                room = ''
                port = 5222
            }
            enabled = false
        }

        log4j = {
            appenders {
                rollingFile name: 'stacktrace',
                        maxFileSize: "10000KB",
                        maxBackupIndex: 10,
                        file: "logs/stacktrace.log",
                        layout: pattern(conversionPattern: '%d{dd MMM yyyy HH:mm:ss,SSS z} %m%n')
            }
            error stacktrace: "StackTrace"
        }

		elasticSearch {
			client.mode = 'local'  //In a clustered environment this should be 'dataNode'
			discovery.zen.ping.multicast.enabled=false
			discovery.zen.ping.unicast.hosts = 'localhost:9300'  //Change this to a list of valid nodes, comma delimited
			replicas = 1

            //Example of how to specify a file system path for the embedded elasticSearch instance
            /*path.data = new File(
                "${userHome}/.grails/projects/${appName}/searchable-index/${GrailsUtil.environment}"
            ).absolutePath*/
		}

    }

}

// Variables used for CEF logging. These fields are combined to form the CEF prefix.
// For more information about CEF logging, check ArcSight documentation
cef {
    device {
        // The device vendor or organization
        vendor = "OZONE"
        // The device product name
        product = "OWF"
        // The device version
        version = "500-27_L2::1.3"
    }
    // The CEF version
    version = 0
}


/**
 * Example scheduledImport configuration
 * marketplace.scheduledImports = [[
 *     name: 'Test Import',
 *     enabled: true,
 *     url: 'https://localhost:8443/marketplace/public/exportAll',
 *     keyStore: [
 *         file: null, //set null to use javax.net.ssl.keyStore system var
 *         password: null //set null to use javax.net.ssl.keyStorePassword system var
 *     ],
 *     trustStore: [
 *         file: null //set null to use javax.net.ssl.trustStore system var
 *     ],
 *     partial: true,
 *     frequency: [
 *         count: 1,
 *         unit: 'minutes' //one of 'minutes', 'hours', 'days'
 *     ]
 * ]]
 */
marketplace.scheduledImports = []

println "MarketplaceConfig.groovy completed successfully."
