package marketplace

class TextService extends MarketplaceService {

    boolean transactional = false

    def list(def params) {
        return Text.findAllByReadOnly(false, params)
    }

    def get(def params) {
        def text = Text.createCriteria().get {
            eq("id", new Long(params.id))
            eq("readOnly", false)
        }

        return text
    }

    def countTypes() {
        return Text.countByReadOnly(false)
    }

    def manageRequiredTexts() {
        log.info "Loading text"
        Text textRecord;

        def alphaVersionText = /The Apps Mall allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content./

        if (Text.findByName(Constants.TEXT_NAME_ABOUT) == null) {
            log.info "Adding Text '${Constants.TEXT_NAME_ABOUT}'..."
            new Text(name: Constants.TEXT_NAME_ABOUT, value: "${alphaVersionText}", readOnly: false).save()
        }
        if (Text.findByName(Constants.TEXT_NAME_VERSION) == null) {
            log.info "Adding Text '${Constants.TEXT_NAME_VERSION}'..."
            new Text(name: Constants.TEXT_NAME_VERSION, value: "2.0", readOnly: true).save()
        }

        // remove unused text records.
        if ((textRecord = Text.findByName(Constants.TEXT_NAME_MP_DESCRIPTION)) != null) {
            log.info "Removing Text '${Constants.TEXT_NAME_MP_DESCRIPTION}'..."
            textRecord.delete()
        }
        if ((textRecord = Text.findByName(Constants.TEXT_NAME_MP_ADVANCED_SEARCH_DESCRIPTION)) != null) {
            log.info "Removing Text '${Constants.TEXT_NAME_MP_ADVANCED_SEARCH_DESCRIPTION}'..."
            textRecord?.delete()
        }
        if ((textRecord = Text.findByName(Constants.TEXT_NAME_ANALYST)) != null) {
            log.info "Removing Text '${Constants.TEXT_NAME_ANALYST}'..."
            textRecord.delete()
        }
        if ((textRecord = Text.findByName(Constants.TEXT_NAME_DEVELOPER)) != null) {
            log.info "Removing Text '${Constants.TEXT_NAME_DEVELOPER}'..."
            textRecord.delete()
        }
        if ((textRecord = Text.findByName(Constants.TEXT_NAME_ADMINISTRATOR)) != null) {
            log.info "Removing Text '${Constants.TEXT_NAME_ADMINISTRATOR}'..."
            textRecord.delete()
        }
    }
}
