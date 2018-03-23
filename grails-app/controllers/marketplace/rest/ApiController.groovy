package marketplace.rest

import java.text.ParseException

import grails.converters.JSON

import marketplace.configuration.MarketplaceApplicationConfigurationService

import ozone.marketplace.enums.MarketplaceApplicationSetting


class ApiController implements RestExceptionHandlers {

    private static Properties GRAILS_BUILD_INFO
    private static Properties GIT_INFO

    static namespace = 'api'

    static responseFormats = ['json']

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    def version() {
        Properties grailsBuildInfo = getGrailsBuildInfo()
        Properties gitInfo = getGitInfo()

        render([name        : storeName ?: "unknown",
                version     : grailsBuildInfo['info.app.version'] ?: "unknown",
                buildDate   : parseDate(grailsBuildInfo['build.time'] as String),
                buildNumber : gitInfo['git.commit.id.abbrev'] ?: "unknown",
                commitId    : gitInfo['git.commit.id'] ?: "unknown",
                commitDate  : parseDate(gitInfo['git.commit.time'] as String),
                commitBranch: gitInfo['git.branch'] ?: "unknown",
                javaVersion : grailsBuildInfo['build.java.version'] ?: "unknown"] as JSON)
    }

    private String getStoreName() {
        marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.STORE_NAME)
    }

    private static Properties getGrailsBuildInfo() {
        if (!GRAILS_BUILD_INFO) {
            GRAILS_BUILD_INFO = readResourcePropertiesFile('META-INF/grails.build.info')
        }
        GRAILS_BUILD_INFO
    }

    private static Properties getGitInfo() {
        if (!GIT_INFO) {
            GIT_INFO = readResourcePropertiesFile('git.properties')
        }
        GIT_INFO
    }

    private static Properties readResourcePropertiesFile(final String filename) {
        Properties props = new Properties()

        try {
            InputStream resourceStream = Thread.currentThread().contextClassLoader.getResourceAsStream(filename)
            resourceStream.withStream { stream -> props.load(stream) }
        } catch (Exception ignored) {
        }

        return props
    }

    private static String parseDate(String date) {
        try {
            return new Date().parse("yyyy-MM-dd'T'HH:mm:ssX", date, TimeZone.getTimeZone("GMT"))
        } catch (ParseException ignored) {
            return "unknown"
        }
    }

}
