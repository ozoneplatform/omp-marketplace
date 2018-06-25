package marketplace

import grails.gorm.transactions.Transactional

class IntentDirectionService {

    boolean transactional = true

    def serviceMethod() {

    }

    @Transactional
    def createRequired() {
        log.info "Loading intent directions..."

        IntentDirection.DIRECTION_TITLES.each { title ->

            if (!IntentDirection.findByTitle(title)) {
                new IntentDirection(title: title).save()
            }
        }
    }

}
