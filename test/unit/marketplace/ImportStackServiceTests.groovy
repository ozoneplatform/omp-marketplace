package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import grails.converters.JSON
import ozone.utils.User
import ozone.utils.Utils

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin([ControllerUnitTestMixin, DomainClassUnitTestMixin])
class ImportStackServiceTests {
    def importStackService
    User testAdmin1
    Profile testAdmin1Profile

    void setUp() {
        FakeAuditTrailHelper.install()

        mockDomain(Types)
        mockDomain(Profile)
        mockDomain(State)
        mockDomain(ServiceItem)
        mockDomain(OwfProperties)
        mockDomain(Intent)
        mockDomain(IntentAction)
        mockDomain(IntentDataType)

        testAdmin1 = new User()
        testAdmin1.username = "testAdmin1"

        testAdmin1Profile = new Profile()
        testAdmin1Profile.username = testAdmin1.username
        testAdmin1Profile.save()

        Types widgetType = new Types()
        widgetType.title = "App Component"
        widgetType.save()

        Types stackType = new Types()
        stackType.title = "OZONE Stack"
        stackType.hasLaunchUrl = false
        stackType.hasIcons = false
        stackType.save()

        importStackService = new ImportStackService()

        def relationshipServiceMock = mockFor(RelationshipService)
        relationshipServiceMock.demand.addOrRemoveRequiresWithProfile(1) { stackId, profile, widgetIds, checkPermissions -> }
        importStackService.relationshipService = relationshipServiceMock.createMock()

        def serviceItemServiceMock = mockFor(ServiceItemService, true)
        serviceItemServiceMock.demand.findByUniversalName(0..999) { return null }
        serviceItemServiceMock.demand.findByOwfProperty(0..999) { prop, value -> return null }
        serviceItemServiceMock.demand.populateDefaults(0..999) { si, owner ->
            si.with {
                owners = [testAdmin1Profile]
                techPocs = [owner.username]
                organization = owner.org
                state = State.findByTitle(Constants.APPROVAL_STATUSES.IN_PROGRESS)
                agency = new Agency(title: "store name",
                            iconUrl: "storeicon.png"
                )
            }


        }
        serviceItemServiceMock.demand.save(0..999) { si, username ->
            assertEquals testAdmin1.username, username
            si.uuid = Utils.generateUUID()
            si.save(failOnError: true)
        }
        importStackService.serviceItemService = serviceItemServiceMock.createMock()
    }

    void testSimpleImport() {
        def simpleStack = buildSimpleStackMap()
        def htmlDescriptor = createHtmlDescriptor(simpleStack)
        ServiceItem importedStack =  importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)
        assertNotNull(importedStack)
        assertNotNull(importedStack.owfProperties)
        assertEquals(simpleStack.name, importedStack.title)
        assertEquals(simpleStack.description, importedStack.description)
        assertEquals(simpleStack.stackContext, importedStack.owfProperties.stackContext)
    }

    void testEmptyImport() {
        shouldFail {
            importStackService.importStackDescriptor("", testAdmin1)
        }
    }

    void testExtraProperties() {
        def stackWithExtras = buildSimpleStackMap()
        stackWithExtras.weirdData = 'important'
        def htmlDescriptor = createHtmlDescriptor(stackWithExtras)
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)
        // Make sure the full descriptor is being kept around (not just the stuff that Marketplace needs
        assertEquals(stackWithExtras, JSON.parse(importedStack.owfProperties.stackDescriptor))
    }

    void testNullDescription() {
        // Why this test is needed: a JSON null doesn't automatically become a Groovy null
        //def parsed = JSON.parse('{"description": null}')
        //assertNotNull parsed.description // description is not null
        //assertTrue parsed.description.equals(null) // yes it is
        //assertFalse parsed.description == null // no it's not
        //String isThisNull = parsed.description
        //assertEquals "null", isThisNull // that's not what I meant by null

        // So create a stack with a null description (this is valid, and OWF can export this)
        def stackWithNoDescription = buildSimpleStackMap()
        stackWithNoDescription.description = null
        def htmlDescriptor = createHtmlDescriptor(stackWithNoDescription)
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)
        // Make sure that the corresponding listing has a null
        assertNull importedStack.description
    }

    void testDescriptionWithSemicolon() {
        def stackWithSemicolons = buildSimpleStackMap()
        stackWithSemicolons.description = "This description has a semicolon; make sure it doesn't mess up parsing."
        def htmlDescriptor = createHtmlDescriptor(stackWithSemicolons)
        ServiceItem importedStack = importStackService.importStackDescriptor(htmlDescriptor, testAdmin1)
        // Make sure that the corresponding listing has a the correct description
        assertEquals stackWithSemicolons.description, importedStack.description
    }

    void testImportWidget() {
        def simpleWidget = buildSimpleWidgetValidIntentsMap()
        def widget = importStackService.importAndSaveWidget(mapToParsedJSON(simpleWidget), testAdmin1)
        assertEquals simpleWidget.displayName, widget.title
        assertEquals simpleWidget.description, widget.description
        assertEquals simpleWidget.widgetUrl, widget.launchUrl
        assertEquals simpleWidget.imageUrlSmall, widget.imageSmallUrl
        assertEquals simpleWidget.imageUrlLarge, widget.imageLargeUrl
        assertEquals simpleWidget.widgetVersion, widget.versionName
        assertEquals simpleWidget.universalName, widget.owfProperties.universalName
        assertEquals simpleWidget.visible, widget.owfProperties.visibleInLaunch
        assertEquals simpleWidget.singleton, widget.owfProperties.singleton
        assertEquals simpleWidget.background, widget.owfProperties.background
        assertEquals simpleWidget.mobileReady, widget.owfProperties.mobileReady
        assertEquals simpleWidget.descriptorUrl, widget.owfProperties.descriptorUrl
        assertEquals simpleWidget.width, widget.owfProperties.width
        assertEquals simpleWidget.height, widget.owfProperties.height
        //assertEquals simpleWidget.intents.size(), widget.owfProperties.intents.size()
        // TODO Intents
    }

    void testImportWidgetWithNulls() {
        def simpleWidget = buildSimpleWidgetMap()
        simpleWidget.with {
            description = null
            universalName = null
            widgetVersion = null
        }
        def widget = importStackService.importAndSaveWidget(mapToParsedJSON(simpleWidget), testAdmin1)
        assertNull widget.description
        assertNull widget.owfProperties.universalName
        assertNull widget.versionName
    }

    void testImportComponent() {
        ServiceItem stack = buildSimpleStack()
        stack.save()
        def simpleWidget = buildSimpleWidgetMap()

        def components = importStackService.importComponents(stack, [ simpleWidget ], testAdmin1)
        assertEquals 1, components.size()
        assertEquals simpleWidget.displayName, components[0].title
        assertEquals Types.findByTitle("App Component"), components[0].types
    }

    void testStackWithTwoWidgets() {
        ServiceItem stack = buildSimpleStack()
        stack.save()
        def widgetsJson = buildTwoWidgets()

        def listings = importStackService.importComponents(stack, widgetsJson, testAdmin1)
        assertEquals 2, listings.size()
        assertEquals widgetsJson[0].universalName, listings[0].owfProperties.universalName
        assertEquals widgetsJson[0].displayName, listings[0].title
        assertEquals widgetsJson[0].description, listings[0].description
        assertEquals widgetsJson[1].universalName, listings[1].owfProperties.universalName
        assertEquals widgetsJson[1].displayName, listings[1].title
        assertEquals widgetsJson[1].description, listings[1].description
    }

    void testStackWidgetRelationships() {
        ServiceItem stack = buildSimpleStack()
        stack.save(failOnError: true)
        def widgetsJson = buildTwoWidgets()
        def relationshipServiceMock = mockFor(RelationshipService)
        relationshipServiceMock.demand.addOrRemoveRequiresWithProfile(1) { stackId, profile, widgetIds, checkPermissions ->
            def stackArg = ServiceItem.get(stackId)
            def widgets = ServiceItem.getAll(widgetIds)
            assertNotNull stackArg
            assertEquals stack.title, stackArg.title
            assertEquals 2, widgets.size()
            assertNotNull widgets[0]
            assertEquals widgetsJson[0].displayName, widgets[0].title
            assertNotNull widgets[1]
            assertEquals widgetsJson[1].displayName, widgets[1].title
        }
        importStackService.relationshipService = relationshipServiceMock.createMock()

        importStackService.importComponents(stack, widgetsJson, testAdmin1)
        relationshipServiceMock.verify()
    }

    private def buildSimpleStackMap() {
        [
                name : 'name',
                description: 'description',
                stackContext: 'context'
        ]
    }

    private ServiceItem buildSimpleStack() {
        ServiceItem stack = new ServiceItem()
        stack.with {
            title = 'name'
            description = 'description'
            owners = [testAdmin1Profile]
            types = Types.findByTitle("OZONE Stack")
            uuid = Utils.generateUUID()
            owfProperties = new OwfProperties()
            owfProperties.stackContext = 'context'
        }
        return stack
    }

    private def buildSimpleWidgetMap() {
        [
                displayName: "Title",
                description: "Widget 1",
                universalName: "universalName",
                visible: true,
                singleton: false,
                background: false,
                mobileReady: false,
                widgetTypes: ["standard"],
                descriptorUrl: "http://foo.org/descriptor.html",
                imageUrlSmall: "http://foo.org/small.png",
                imageUrlLarge: "http://foo.org/large.png",
                widgetUrl: "http://foo.org/widget",
                width: 400,
                height: 600,
                widgetVersion: "1.0",
                intents: [
                        send: [
                                [
                                        action: "Graph",
                                        dataTypes: ["application/vnd.owf.sample.price"]
                                ],
                                [
                                        action: "View",
                                        dataTypes: ["text/html"]
                                ]
                        ],
                        receive: []
                ]
        ]
    }

    private def buildSimpleWidgetValidIntentsMap() {
        [
                displayName: "Title",
                description: "Widget 1",
                universalName: "universalName",
                visible: true,
                singleton: false,
                background: false,
                mobileReady: false,
                widgetTypes: ["standard"],
                descriptorUrl: "http://foo.org/descriptor.html",
                imageUrlSmall: "http://foo.org/small.png",
                imageUrlLarge: "http://foo.org/large.png",
                widgetUrl: "http://foo.org/widget",
                width: 400,
                height: 600,
                widgetVersion: "1.0",
                intents: [
                        send: [
                                [
                                        action: "share",
                                        dataTypes: ["audio"]
                                ],
                                [
                                        action: "subscribe",
                                        dataTypes: ["text"]
                                ],
                                [
                                        action: "share",
                                        dataTypes: ["json", "text"]
                                ],
                                [
                                        action: "plot",
                                        dataTypes: ["text"]
                                ]
                        ],
                        receive: [
                                    [
                                        action: "share",
                                        dataTypes: ["audio"]
                                    ],
                                    [
                                        action: "plot",
                                        dataTypes: ["json", "text"]
                                    ]
                        ]
                ]
        ]
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
        def emptyDescriptor = new File('./test/integration/resources/empty_stack_descriptor.html').text
        return emptyDescriptor.replaceFirst("var data;", java.util.regex.Matcher.quoteReplacement("var data = ${json.toString()};"))
    }

    private def mapToParsedJSON(def data) {
        return JSON.parse((data as JSON).toString())
    }
}
