package marketplace

import grails.gorm.transactions.Transactional

import ozone.decorator.JSONDecoratorService


class ExtProfileService {

    AccountService accountService

    JSONDecoratorService JSONDecoratorService

    /**
     * The public-facing create method.
     *
     */
    @Transactional
    def create(def json, def username) {
        if (!accountService.isExtAdmin()) {
            throw new PermissionException('Insufficient authority to add profile')
        }
        JSONDecoratorService.preProcessJSON(json)
        def extProfile = new ExtProfile()
        extProfile.bindFromJSON(json.profile)

        save(extProfile, username, true)

        return extProfile
    }

    /**
     * The public-facing update method.
     *
     */
    @Transactional
    def update(def itemId, def json, def username) {
        if (!accountService.isExtAdmin()) {
            throw new PermissionException('Insufficient authority to update profile')
        }
        JSONDecoratorService.preProcessJSON(json)
        def profile = Profile.get(itemId)
        if (profile == null) {
            throw new ObjectNotFoundException("Unable to locate profile with id: ${itemId}")
        }
        log.debug("retrieved ${profile}")
        profile.bindFromJSON(json.profile)

        // We want to trigger an event to update the lastUpdated timestamp
        // for the ExtProfile.
        profile.version++

        save(profile, username, false)

        return profile
    }

    /*
     * Save the given ExtProfile.
     */

    private def save(ExtProfile extProfile, def username, def createFl) {
        def avatar = Avatar.findByIsDefault(true)
        if (avatar) {
            extProfile.avatar = avatar
        }

        extProfile.validate()
        println('EXTProfile Errors: ' + extProfile.errors)
        extProfile.save(failOnError: true, flush: true)

        log.debug "ExtProfile ${extProfile.id} saved"
    }

    @Transactional(readOnly = true)
    def get(def params) {
        return ExtProfile.get(params.id)
    }

    @Transactional(readOnly = true)
    def list(def params) {
        log.debug("list: params = ${params}")
        def returnValue

        if (params.username) {
            def criteria = ExtProfile.createCriteria()
            def results = criteria.list(params) {
                eq('username', params.username)
            }
            // total will always be 0 or 1 since username must be unique
            returnValue = [result: results, total: results.size()]
        } else if (params.systemUri && params.externalId) {
            def criteria = ExtProfile.createCriteria()
            def results = criteria.list(params) {
                and {
                    eq('systemUri', params.systemUri)
                    eq('externalId', params.externalId)
                }
            }
            // total will always be 0 or 1 since systemUri with externalId must be unique
            returnValue = [result: results, total: results.size()]
        } else {
            def results = ExtProfile.list(params)
            returnValue = [result: results, total: total()]
        }

        return returnValue
    }

    @Transactional(readOnly = true)
    def total() {
        return ExtProfile.count()
    }
}
