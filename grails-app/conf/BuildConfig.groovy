import grails.util.Environment

System.setProperty "ivy.checksums", ""
grails.project.plugins.dir = "${basedir}/plugins"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.dependency.resolver = "maven"

//grails.project.war.file = "target/${appName}-${appVersion}.war"

coverage {
    exclusions = [
        "**/org/apache/log4j/**",
        "**/marketplace/testing/**",
        "changelog*/**"
    ]
    xml = true
    enabledByDefault = false
}

codenarc.reports = {
    AmlXmlReport('xml') {
        outputFile = 'target/CodeNarcReport.xml'
        title = 'App Mall CodeNarc Report'
    }

    AmlHtmlReport('html') {
        outputFile = 'target/CodeNarcReport.html'
        title = 'App Mall CodeNarc Report'
    }
}

codenarc.ruleSetFiles = "file:grails-app/conf/CodeNarcRules.groovy"
codenarc.propertiesFile = "grails-app/conf/codenarc.properties"

def props = new Properties()
new File("application.properties").withInputStream { stream ->
    props.load(stream)
}
def config = new ConfigSlurper().parse(props)

def overlayConfigFile = new File("grails-app/conf/OverlayConfig.groovy")
if (overlayConfigFile.exists()) {
    def slurpedConfig = new ConfigSlurper().parse(overlayConfigFile.toURI().toURL())
    config.merge(slurpedConfig)
}

def warExcludes = [
    'aopalliance-1.0-sources.jar',
    'asm-3.0.jar',
    'asm-tree-3.0.jar',
    'cobertura-1.9.4.1.jar',
    'commons-codec-1.3-javadoc.jar',
    'commons-codec-1.3-sources.jar',
    'commons-lang-2.4-javadoc.jar',
    'commons-lang-2.4-sources.jar',
    'compass-2.2.1-src.jar',
    'core-3.1.1.jar',
    'jasper-compiler-5.5.15.jar',
    'jasper-compiler-jdt-5.5.15.jar',
    'jasper-runtime-5.5.15',
    'jsp-api-2.0-6.1.21.jar',
    //'jstl-1.1.2.jar',
    'standard-1.1.2.jar',
    'servlet-api-2.5-20081211.jar',
    "${config.mp.security.module}-project-${config.mp.security.rev}.zip",
    "rome-0.9.jar"
]

grails.war.resources = { stagingDir ->
    // this class is only used for development to simulate login so we don't need cas
    // remove it from the war so it doesn't get out
    delete(file: "${stagingDir}/WEB-INF/classes/AutoLoginAccountService.class")
    delete(file: "${stagingDir}/WEB-INF/classes/ehcache.xml")
    def libDir = "${stagingDir}/WEB-INF/lib"
    warExcludes.each { exclude ->
        delete(file: "${libDir}/${exclude}")
    }
    delete(dir: "${stagingDir}/js-test/doh")
    delete(dir: "${stagingDir}/js-test/dojo-release-1.6.0-src")
    delete(dir: "${stagingDir}/themes/accessibility-bow")
    delete(dir: "${stagingDir}/themes/accessibility-wob")
    delete(dir: "${stagingDir}/themes/marketplace")
    delete(dir: "${stagingDir}/themes/template")

    def overlayResources = config.overlay.war.resources
    overlayResources.each {
        it.tofile = "${stagingDir}/${it.tofile}"
        copy(it)
    }
}

grails.project.dependency.resolution = {

    // inherit Grails' default dependencies
    inherits("global") {
        excludes 'slf4j'
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
		mavenRepo 'http://maven.restlet.org'
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime('net.sf.ehcache:ehcache-jgroupsreplication:1.4', 'log4j:apache-log4j-extras:1.0')
        /*
        //Code-Coverage-1.2 (PLUGIN)
        runtime 'asm:asm-parent:3.0', 'asm:asm:3.0',
            'asm:asm-tree:3.0', 'net.sourceforge.cobertura:cobertura:1.9.4.1'
        */
        runtime('dom4j:dom4j:1.6.1') { excludes 'xml-apis' }
        runtime('org.hibernate:hibernate-ehcache:3.3.1.GA') { excludes 'ehcache', 'hibernate-core' }

        //runtime "hsqldb:hsqldb:1.8.0.10"

        //Rome-modules
        compile 'rome:modules:0.3.2'

        //only include these jdbc drivers for non production
        if (Environment.current != Environment.PRODUCTION) {
            runtime 'mysql:mysql-connector-java:5.1.6'
            runtime 'net.sourceforge.jtds:jtds:1.2.4'
            runtime 'postgresql:postgresql:9.1-901.jdbc3'
        }

        // HTTP Client
        compile('org.apache.httpcomponents:httpcore:4.3.2', 'org.apache.httpcomponents:httpclient:4.3.5')

        compile("org.codehaus.groovy.modules.http-builder:http-builder:0.7.1") {
            excludes "xml-apis", "asm", "groovy", "http-core", "httpclient"
        }

        //upgrade commons-validator to get correct validation of localhost URLs (OP-420)
        compile('commons-validator:commons-validator:1.4.0')

        //Fix for ClassNotFoundException: javax.ws.rs.ApplicationPath
        runtime('javax.ws.rs:jsr311-api:1.1.1')

        //phone number validation
        compile 'com.googlecode.libphonenumber:libphonenumber:4.3'

        compile("org.ozoneplatform:ozone-security:${config.mp.security.rev}") {
            excludes([group: 'org.springframework'])
        }
        compile 'org.grails:grails-web-databinding-spring:2.4.3'
    }

    plugins {
        compile ':database-migration:1.4.0'
        compile ':executor:0.3'
        compile ':feeds:1.6'
        compile ':ui-performance:1.2.2'
        compile ':quartz:1.0.1'
        compile ':pretty-time:2.1.3.Final-1.0.1'
        compile ':cache:1.1.5'
        compile 'org.ozoneplatform:ozone-appconfig:0.9'
        compile 'org.ozoneplatform:ozone-auditing:1.3.1'

        compile('org.ozoneplatform:ozone-messaging:1.19') {
            excludes([group: 'org.igniterealtime.smack'])
        }

        compile ':audit-trail:2.0.2'
        compile ':hibernate:3.6.10.12'

        test ':code-coverage:1.2.5'
        test ':build-test-data:2.2.3'
        test ':codenarc:0.20'
        test ':gmetrics:0.3.1'

        build ':tomcat:8.0.22'

        runtime ':cors:1.1.8' // OP-3932
        runtime ':jaxrs:0.11'

        compile ":elasticsearch:0.0.4.6", {
            excludes 'groovy-all'
        }
    }
}
