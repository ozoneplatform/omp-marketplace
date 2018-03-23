package marketplace.grails.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.ServiceItem
import marketplace.State
import marketplace.StateService

import ozone.marketplace.domain.ValidationException

import static marketplace.grails.service.DomainBuilders.createState
import static marketplace.grails.service.DomainBuilders.stubServiceItem


class StateServiceSpec
        extends OwfUnitSpecification
        implements ServiceUnitTest<StateService>, DataTest
{

    void setupSpec() {
        mockDomain State
        mockDomain ServiceItem
    }

    void setup() {
        service.sessionFactory = mockSessionFactory()
    }

    def 'get'() {
        given:
        def state = createState(title: 'state1')

        expect:
        service.get([id: state.id]) == state
    }

    def 'findByTitle'() {
        given:
        def state = createState(title: 'state1')

        expect:
        service.findByTitle('state1') == state
    }

    def 'getAllStates'() {
        given:
        createState(title: 'c')
        createState(title: 'b')
        createState(title: 'a')

        expect:
        service.getAllStates()*.title == ['a', 'b', 'c']
    }

    def 'delete by entity'() {
        given:
        def state = createState(title: 'state1')

        when:
        service.delete(state)

        then:
        State.count() == 0
    }

    def 'delete by id'() {
        given:
        def state = createState(title: 'state1')

        when:
        service.delete(state.id)

        then:
        State.count() == 0
    }

    def 'delete by id when not found'() {
        when:
        service.delete(1L)

        then:
        def ex = thrown(ValidationException)
        ex.message == 'objectNotFound'
    }

    def 'delete with related service item'() {
        given:
        def state = createState(title: 'state1')
        stubServiceItem(state: state)

        when:
        service.delete(state)

        then:
        def ex = thrown(ValidationException)
        ex.message == 'delete.failure.serviceItem.exists'
    }

    def 'createRequired adds States from config'() {
        given:
        service.config = mockConfig(
                [marketplace: [metadata: [states: DefaultMetadata.STATES]]])

        when:
        service.createRequired()

        then:
        State.count() == DefaultMetadata.STATES.size()
    }

}
