package marketplace

import org.springframework.transaction.annotation.Transactional

class OwfWidgetTypesService extends MarketplaceService {

    @Transactional(readOnly = true)
    def getDefaultOwfWidgetType() {
        return 'standard'
    }

    @Transactional(readOnly = true)
    def countOwfWidgetTypes() {
        return OwfWidgetTypes.count()
    }

    @Transactional
    def getOwfWidgetType(def id) {
        return OwfWidgetTypes.get(id)
    }

    @Transactional(readOnly = true)
    def list(def params) {
        return OwfWidgetTypes.list(params)
    }

    @Transactional
    def createRequired() {
        log.info "Loading required OWF Widget Types"

        def widgetTypes = config.marketplace.metadata.owfWidgetTypes

        if (widgetTypes) {
            widgetTypes.each { item ->

                if (!OwfWidgetTypes.findByTitle(item.title)) {
                    new OwfWidgetTypes(title: item.title, description: item.description).save()
                }
            }
        } else {
            log.error "OWF Widget Type metadata info was not found in the loaded config files."
        }
    }
}
