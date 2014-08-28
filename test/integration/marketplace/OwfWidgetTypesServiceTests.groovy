package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*

@TestMixin(IntegrationTestMixin)
class OwfWidgetTypesServiceTests {
    def owfWidgetTypesService

    void testDefaultTypeIsStandard()
    {
        def defaultValue = owfWidgetTypesService.defaultOwfWidgetType
        assert defaultValue == 'standard'
    }

    void testCreateRequiredLoadsItemsOnce() {
        assert OwfWidgetTypes.all.size() > 0

        def initialWidgetTypes = owfWidgetTypesService.list()

        owfWidgetTypesService.createRequired()

        def newWidgetTypes = owfWidgetTypesService.list()

        assert initialWidgetTypes.size() == newWidgetTypes.size()
    }
}
