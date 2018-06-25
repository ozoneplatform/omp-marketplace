package marketplace.grails.service

import marketplace.CustomFieldDefinition
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.State
import marketplace.Text
import marketplace.Types
import marketplace.UserDomainInstance


class DomainBuilders {

    static State createState(Map params) {
        new State(params).save(flush: true)
    }

    static ServiceItem stubServiceItem(Map params) {
        new ServiceItem(params).save(flush: true, validate: false)
    }

    static CustomFieldDefinition stubFieldDefinition(Map params) {
        new CustomFieldDefinition(params).save(flush: true, validate: false)
    }

    static Text createText(Map params) {
        new Text(params).save(flush: true)
    }

    static Profile createProfile(Map params) {
        new Profile(params).save(flush: true)
    }

    static Types createType(Map params) {
        new Types(params).save(flush: true)
    }

    static UserDomainInstance createUserDomainInstance(String username) {
        new UserDomainInstance(username: username).save(flush: true)
    }

}
