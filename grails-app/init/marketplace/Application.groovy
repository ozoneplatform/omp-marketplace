package marketplace

import org.springframework.boot.autoconfigure.EnableAutoConfiguration

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity


@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
    
}
