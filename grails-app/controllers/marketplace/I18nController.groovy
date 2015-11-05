package marketplace

import grails.util.Holders

class I18nController {

    def index = {
        def messagesrc = Holders.getGrailsApplication().getMainContext().getBean("messageSource")


        def locale = 'en'
        def accLang = request.getHeader('Accept-Language')
        if (accLang) {
            accLang = accLang.split(',')
            locale = accLang[0]
        }

        locale = new Locale(locale)

        def properties = messagesrc.getMergedProperties(locale).getProperties()
        def sw = new ByteArrayOutputStream()
        properties.store(sw, "")
        render sw.toString()
    }
}
