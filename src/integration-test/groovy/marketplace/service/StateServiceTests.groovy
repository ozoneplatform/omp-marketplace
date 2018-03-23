package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.State
import marketplace.StateService
import marketplace.domain.builders.DomainBuilderMixin

import ozone.marketplace.domain.ValidationException


@Integration
@Rollback
class StateServiceTests extends Specification implements DomainBuilderMixin {

    StateService stateService

    void testListByDate() {
        given:
        def state1 = $state { title = 'AAA' }

        sleep(2000)

        def state2 = $state { title = 'BBB' }

        when:
        def results1 = stateService.list([editedSinceDate: state1.editedDate])
        def results2 = stateService.list([editedSinceDate: state2.editedDate])

        then:
        results1?.size() == 2
        results2?.size() == 1
    }

    void testDelete() {
        given:
        def state = $state { title = 'Test State' }

        when:
        stateService.delete(state.id)

        then:
        State.get(state.id) == null
    }

    void testSoftDelete() {
        when:
        stateService.delete(427358374529952)

        then:
        def ve = thrown(ValidationException)
        ve.message == 'objectNotFound'
    }

    void testParseEditedDate_valid(def date) {
        given:
        def params = ['editedSinceDate': date]

        when:
        def result = stateService.parseEditedSinceDate(params)

        then:
        result != null
        result instanceof Date

        where:
        date << ['2012-03-02T12:58:38Z',
                 '2012-03-02T12:58:38UTC',
                 '2012-03-02T12:58:38EST',
                 new Date()]
    }

    void testParseEditedDate_invalid(def date) {
        given:
        def params = date ? ['editedSinceDate': date] : [:]

        when:
        def result = stateService.parseEditedSinceDate(params)

        then:
        result == null

        where:
        date << [null,
                 'unparseable string',
                 new Integer(23)]
    }


}
