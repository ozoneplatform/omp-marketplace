package marketplace.service

import grails.gorm.transactions.Rollback
import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import grails.testing.mixin.integration.Integration
import marketplace.OwfWidgetTypes
import spock.lang.Specification

@Integration
@Rollback
class OwfWidgetTypesServiceTests extends Specification{
    def owfWidgetTypesService

    void testDefaultTypeIsStandard()
    {
        when:
        def defaultValue = owfWidgetTypesService.defaultOwfWidgetType
        then:
        defaultValue == 'standard'
    }

    void testCreateRequiredLoadsItemsOnce() {
        expect:
        OwfWidgetTypes.all.size() > 0

        when:
        def initialWidgetTypes = owfWidgetTypesService.list()

        owfWidgetTypesService.createRequired()

        def newWidgetTypes = owfWidgetTypesService.list()

        then:
        initialWidgetTypes.size() == newWidgetTypes.size()
    }
}
