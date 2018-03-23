package org.grails.plugins.jaxrs

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
 * Main application class. Note that this only gets run if the plugin
 * is started directly or through running the test suite.
 */
@Configuration
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
