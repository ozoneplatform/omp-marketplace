package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import ozone.marketplace.enums.MarketplaceApplicationSetting

class CommonTagLibTests extends GroovyPagesTestCase {

    def formatGrid
    def marketplaceApplicationConfigurationService

    void setUp() {
        formatGrid = '<g:each in="${grid}" var="row">' +
                '<g:each in="${row}" var="item">${item}:</g:each>' +
            '/</g:each>'
    }

    void testTruncateTextSimpleTruncation() {
        def template = '<common:truncateText text="1234567890" truncateAt="4" var="text">${text}</common:truncateText'
        assertOutputEquals('1234...', template)
    }

    void testTruncateTextShortEnoughStringIsNotTruncated() {
        def template = '<common:truncateText text="1234567890" truncateAt="10" var="text">${text}</common:truncateText'
        assertOutputEquals('1234567890', template)
    }

    void testTruncateTooManyLines() {
        def template = '<common:truncateText text="1234567890\\n1234567890\\n1234567890" truncateAt="1000" lineCount="2" var="text">${text}</common:truncateText'
        assertOutputEquals('1234567890\n1234567890...', template)
    }

    void testTruncateBodySubstitution() {
        def template = '<common:truncateText text="1234567890" truncateAt="1000" var="text">First ${text} Second ${text}</common:truncateText'
        assertOutputEquals('First 1234567890 Second 1234567890', template)
    }

    void testConvertListToGridEmptyList() {
        def template = '<common:convertListToGrid listSize="3" elementList="${list}" var="grid"> ' + formatGrid +'</common:convertListToGrid>'
        assertOutputEquals(" ", template, [list:[]])
    }

    void testConvertListToGridEvenlyDivisible() {
        def template = '<common:convertListToGrid listSize="3" elementList="${list}" var="grid"> ' + formatGrid +'</common:convertListToGrid>'
        assertOutputEquals(" 1:2:3:/4:5:6:/", template, [list:1..6])
    }

    void testConvertListToGridWithRemainder() {
        def template = '<common:convertListToGrid listSize="4" elementList="${list}" var="grid"> ' + formatGrid +'</common:convertListToGrid>'
        assertOutputEquals(" 1:2:3:4:/5:/", template, [list:1..5])
    }

    void testConvertListToGridWithOneRow() {
        def template = '<common:convertListToGrid listSize="7" elementList="${list}" var="grid"> ' + formatGrid +'</common:convertListToGrid>'
        assertOutputEquals(" 1:2:3:4:5:/", template, [list:1..5])
    }

    void testFreeTextWarningWhenPresent() {
        def theWarning = "It's a trap!"
        def conf = marketplaceApplicationConfigurationService.getApplicationConfiguration(MarketplaceApplicationSetting.FREE_WARNING_CONTENT)
        conf.value = theWarning
        marketplaceApplicationConfigurationService.saveApplicationConfiguration(conf)


        def template = '<common:freeTextWarning/>'
        assertOutputEquals("<div class='warning'>${theWarning}</div>", template)
    }

    void testFreeTextWarningWhenEmpty() {
        def conf = marketplaceApplicationConfigurationService.getApplicationConfiguration(MarketplaceApplicationSetting.FREE_WARNING_CONTENT)
        conf.value = ""
        marketplaceApplicationConfigurationService.saveApplicationConfiguration(conf)

        def template = '<common:freeTextWarning/>'
        assertOutputEquals("", template)
    }
}
