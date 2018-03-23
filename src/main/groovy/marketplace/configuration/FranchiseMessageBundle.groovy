package marketplace.configuration

import org.grails.spring.context.support.PluginAwareResourceBundleMessageSource

import org.springframework.core.io.Resource


class FranchiseMessageBundle extends PluginAwareResourceBundleMessageSource {

    /*
     * This method will set the order of base names for the property files based on the mode
     * the application is in.
     */

    public void setBaseNameOrder(isOverlayMode) {
        if (isOverlayMode) {
            //If we are in overlay mode then favor properties from messages_franchise
            super.setBasenames("classpath:messages_overlay", "WEB-INF/grails-app/i18n/messages_overlay", "classpath:messages", "WEB-INF/grails-app/i18n/messages")
        } else {
            //If we are not in overlay mode then favor properties from messages
            super.setBasenames("classpath:messages", "WEB-INF/grails-app/i18n/messages", "classpath:messages_overlay", "WEB-INF/grails-app/i18n/messages_overlay")
        }
    }

    //Helper method which defaults to the default local and no default message
    public String getMessage(String code) {
        return this.getMessage(code, null, Locale.getDefault())
    }

}
