package marketplace

import org.codehaus.groovy.grails.commons.ConfigurationHolder as configHolder

class CustomFieldUtility {
    /**
     * Read custom field definitions from the configuration file and apply them to the system
     */
    def static createPreConfiguredCustomFields() {
        //if (configHolder.config.customFields) {
            //CustomFieldBuilder builder = new CustomFieldBuilder()
            //Closure customFieldsClosure = configHolder.config.customFields
            //customFieldsClosure.resolveStrategy = Closure.DELEGATE_ONLY
            //customFieldsClosure.setDelegate(builder)
            //customFieldsClosure.call()
        //}
    }
}
