package marketplace.service

import spock.lang.Specification

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.test.mixin.integration.Integration

import marketplace.ImportStackService
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.Types
import marketplace.domain.builders.DomainBuilderMixin

import ozone.utils.User


@Integration
@Rollback
class ImportStackServiceSpec extends Specification implements DomainBuilderMixin {

    ImportStackService importStackService

    User testAdmin1
    Profile testAdmin1Profile

    Types widgetType
    Types stackType

    void setupData() {
        testAdmin1 = new User()
        testAdmin1.username = "testAdmin1"

        testAdmin1Profile = Profile.findByUsername("testAdmin1")

        widgetType = $type { title = "App Component" }
        stackType = $type { title = "OZONE Stack"; hasLaunchUrl = false }
    }

    void testSimpleImport() {
        given:
        setupData()

        def simpleStack = buildSimpleStackMap()
        def htmlDescriptor = createHtmlDescriptor(simpleStack)

        when:
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)

        then:
        importedStack != null
        importedStack.owfProperties != null
        simpleStack.name == importedStack.title
        simpleStack.description == importedStack.description
        simpleStack.stackContext == importedStack.owfProperties.stackContext
    }

    void testEmptyImport() {
        given:
        setupData()

        when:
        importStackService.importStackDescriptor("", testAdmin1)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "App to import does not match the expected format"
    }

    void testExtraProperties() {
        given:
        setupData()

        def stackWithExtras = buildSimpleStackMap()
        stackWithExtras.weirdData = 'important'
        def htmlDescriptor = createHtmlDescriptor(stackWithExtras)

        when:
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)

        then: 'full descriptor is retained (not just the stuff that Marketplace needs)'
        stackWithExtras == JSON.parse(importedStack.owfProperties.stackDescriptor)
    }

    void testNullDescription() {
        given: 'create a stack with a null description (this is valid, and OWF can export this)'
        setupData()

        def stackWithNoDescription = buildSimpleStackMap()
        stackWithNoDescription.description = null
        def htmlDescriptor = createHtmlDescriptor(stackWithNoDescription)

        when:
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)

        then: 'Make sure that the corresponding listing has a null'
        importedStack.description == null
    }

    void testDescriptionWithSemicolon() {
        given:
        setupData()

        def stackWithSemicolons = buildSimpleStackMap()
        stackWithSemicolons.description = "This description has a semicolon; make sure it doesn't mess up parsing."
        def htmlDescriptor = createHtmlDescriptor(stackWithSemicolons)

        when:
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)
        // Make sure that the corresponding listing has a the correct description

        then:
        stackWithSemicolons.description == importedStack.description
    }

    void testImportWidget() {
        given:
        setupData()

        def simpleWidget = buildSimpleWidgetValidIntentsMap()

        when:
        def widget = importStackService.importAndSaveWidget(mapToParsedJSON(simpleWidget), testAdmin1)

        then:
        simpleWidget.displayName == widget.title
        simpleWidget.description == widget.description
        simpleWidget.widgetUrl == widget.launchUrl
        simpleWidget.imageUrlSmall == widget.imageSmallUrl
        simpleWidget.imageUrlLarge == widget.imageLargeUrl
        simpleWidget.widgetVersion == widget.versionName
        simpleWidget.universalName == widget.owfProperties.universalName
        simpleWidget.visible == widget.owfProperties.visibleInLaunch
        simpleWidget.singleton == widget.owfProperties.singleton
        simpleWidget.background == widget.owfProperties.background
        simpleWidget.mobileReady == widget.owfProperties.mobileReady
        simpleWidget.descriptorUrl == widget.owfProperties.descriptorUrl
        simpleWidget.width == widget.owfProperties.width
        simpleWidget.height == widget.owfProperties.height
        // TODO Intents
    }

    void testImportWidgetWithNulls() {
        given:
        setupData()

        def simpleWidget = buildSimpleWidgetMap()
        simpleWidget.with {
            description = null
            universalName = null
            widgetVersion = null
        }

        when:
        def widget = importStackService.importAndSaveWidget(mapToParsedJSON(simpleWidget), testAdmin1)

        then:
        widget.description == null
        widget.owfProperties.universalName == null
        widget.versionName == null
    }

    void testImportComponent() {
        given:
        setupData()

        ServiceItem stack = buildSimpleStack()

        when:
        def simpleWidget = buildSimpleWidgetMap()
        def components = importStackService.importComponents(stack, [simpleWidget], testAdmin1)

        then:
        components.size() == 1
        simpleWidget.displayName == components[0].title
        Types.findByTitle("App Component") == components[0].types
    }

    void testStackWithTwoWidgets() {
        given:
        setupData()

        ServiceItem stack = buildSimpleStack()

        when:
        def widgetsJson = buildTwoWidgets()
        def listings = importStackService.importComponents(stack, widgetsJson, testAdmin1)
        then:
        listings.size() == 2
        widgetsJson[0].universalName == listings[0].owfProperties.universalName
        widgetsJson[0].displayName == listings[0].title
        widgetsJson[0].description == listings[0].description
        widgetsJson[1].universalName == listings[1].owfProperties.universalName
        widgetsJson[1].displayName == listings[1].title
        widgetsJson[1].description == listings[1].description
    }

    void testStackWidgetRelationships() {
        given:
        setupData()

        ServiceItem stack = buildSimpleStack()
        def widgetsJson = buildTwoWidgets()

        when:
        importStackService.importComponents(stack, widgetsJson, testAdmin1)

        def result = ServiceItem.get(stack.id)

        then:
        result.validate()
    }

    private def buildSimpleStackMap() {
        [name        : 'name',
         description : 'description',
         stackContext: 'context']
    }

    private ServiceItem buildSimpleStack() {
        def owfProps = $owfProperties {
            stackContext = 'context'
        }

        $serviceItem {
            title = 'name'
            description = 'description'
            owner = testAdmin1Profile
            type = widgetType
            owfProperties = owfProps
        }
    }

    private def buildSimpleWidgetMap() {
        [displayName  : "Title",
         description  : "Widget 1",
         universalName: "universalName",
         visible      : true,
         singleton    : false,
         background   : false,
         mobileReady  : false,
         widgetTypes  : ["standard"],
         descriptorUrl: "http://foo.org/descriptor.html",
         imageUrlSmall: "http://foo.org/small.png",
         imageUrlLarge: "http://foo.org/large.png",
         widgetUrl    : "http://foo.org/widget",
         width        : 400,
         height       : 600,
         widgetVersion: "1.0",
         intents      : [send   : [[action   : "Graph",
                                    dataTypes: ["application/vnd.owf.sample.price"]],
                                   [action   : "View",
                                    dataTypes: ["text/html"]]],
                         receive: []]]
    }

    private def buildSimpleWidgetValidIntentsMap() {
        [displayName  : "Title",
         description  : "Widget 1",
         universalName: "universalName",
         visible      : true,
         singleton    : false,
         background   : false,
         mobileReady  : false,
         widgetTypes  : ["standard"],
         descriptorUrl: "http://foo.org/descriptor.html",
         imageUrlSmall: "http://foo.org/small.png",
         imageUrlLarge: "http://foo.org/large.png",
         widgetUrl    : "http://foo.org/widget",
         width        : 400,
         height       : 600,
         widgetVersion: "1.0",
         intents      : [send   : [[action   : "share",
                                    dataTypes: ["audio"]],
                                   [action   : "subscribe",
                                    dataTypes: ["text"]],
                                   [action   : "share",
                                    dataTypes: ["json", "text"]],
                                   [action   : "plot",
                                    dataTypes: ["text"]]],
                         receive: [[action   : "share",
                                    dataTypes: ["audio"]],
                                   [action   : "plot",
                                    dataTypes: ["json", "text"]]]]]
    }

    private def buildTwoWidgets() {
        def simpleWidget1 = buildSimpleWidgetMap()
        simpleWidget1.universalName = "different"
        simpleWidget1.displayName = "Other Title"
        def simpleWidget2 = buildSimpleWidgetMap()
        return [simpleWidget1, simpleWidget2]
    }

    private String createHtmlDescriptor(def stackProperties) {
        return wrapJSON(stackProperties as JSON)
    }

    private String wrapJSON(JSON json) {
        // This is copied from OWF's empty_descriptor.html template that it uses to create the stack descriptor.
        def emptyDescriptor = new File('./src/integration-test/resources/empty_stack_descriptor.html').text
        return emptyDescriptor.
                replaceFirst("var data;", java.util.regex.Matcher.quoteReplacement("var data = ${json.toString()};"))
    }

    private def mapToParsedJSON(def data) {
        return JSON.parse((data as JSON).toString())
    }

}
