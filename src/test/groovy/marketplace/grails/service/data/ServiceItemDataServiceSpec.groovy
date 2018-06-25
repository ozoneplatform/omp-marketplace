package marketplace.grails.service.data

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemService
import marketplace.Types
import marketplace.data.ServiceItemDataService

import static marketplace.grails.service.DomainBuilders.createProfile
import static marketplace.grails.service.DomainBuilders.createType
import static marketplace.grails.service.DomainBuilders.stubServiceItem


class ServiceItemDataServiceSpec
        extends Specification
        implements ServiceUnitTest<ServiceItemDataService>, DataTest
{

    void setupSpec() {
        mockDomain ServiceItem
        mockDomain Profile
        mockDomain Types
    }

    def 'findByOwnerId'() {
        given:
        def profile1 = createProfile(username: 'user1')
        def profile2 = createProfile(username: 'user2')
        def item1 = stubServiceItem(owners: [profile1])
        def item2 = stubServiceItem(owners: [profile1, profile2])
        stubServiceItem(owners: [profile2])

        when:
        def items = service.findByOwnerId(profile1.id)

        then:
        items as Set == [item1, item2] as Set
    }

    def 'countHasTypeById'() {
        given:
        def type1 = createType(title: "type1")
        def type2 = createType(title: "type2")
        stubServiceItem(types: type1)
        stubServiceItem(types: type1)
        stubServiceItem(types: type2)

        expect:
        service.countHasTypeById(type1.id) == 2
    }

}
