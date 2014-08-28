package marketplace

import grails.test.mixin.TestFor

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(OwfProperties)
class OwfPropertiesTests {

    OwfProperties defaultProperties

    void setUp() {
        FakeAuditTrailHelper.install()

        def serviceItem = new ServiceItem()

        defaultProperties = new OwfProperties(
            owfWidgetType: "My Widget Type",
            singleton: false,
            visibleInLaunch: true,
            background: false,
            mobileReady: false,
            stackContext: null,
            stackDescriptor: null,
            universalName: null,
            descriptorUrl: null,
            serviceItem: serviceItem
        )

        mockForConstraintsTests(OwfProperties)
        mockDomain(OwfProperties, [defaultProperties])
        mockDomain(ServiceItem, [serviceItem])
        mockDomain(Intent)
    }

    void testThatDefaultPropertiesPassesValidation() {
        assertTrue defaultProperties.validate()
    }

    void testMinimumStackContextLength() {
        defaultProperties.stackContext = "s"
        assertTrue defaultProperties.validate()
    }

    void testMaximumAllowedStackContextLength() {
        defaultProperties.stackContext = "s"*200
        assertTrue defaultProperties.validate()
    }

    void testStackContextCannotExceedMaxLength() {
        defaultProperties.stackContext = "s"*201
        assertFalse defaultProperties.validate()
    }

    void testMinimumStackDescriptorLength() {
        defaultProperties.stackDescriptor = "s"
        assertTrue defaultProperties.validate()
    }

    void testVeryLargeStackDescriptorLength() {
        defaultProperties.stackDescriptor = "s"*1000000
        assertTrue defaultProperties.validate()
    }

    void testMinimumUniversalNameLength() {
        defaultProperties.universalName = "s"
        assertTrue defaultProperties.validate()
    }

    void testMaxAllowedUniversalName() {
        defaultProperties.universalName = "s"*255
        assertTrue defaultProperties.validate()
    }

    void testUniversalNameCannotExceedMaxLength() {
        defaultProperties.universalName = "s"*256
        assertFalse defaultProperties.validate()
    }

    void testUniversalNameIsUnique() {
        defaultProperties.universalName = "My Widget"
        assertTrue defaultProperties.validate()
        defaultProperties.save()

        OwfProperties newProperty = new OwfProperties(owfWidgetType: "My Widget Type",
                                                      singleton: false,
                                                      visibleInLaunch: true,
                                                      background: false,
                                                      mobileReady: false,
                                                      stackContext: null,
                                                      stackDescriptor: null,
                                                      universalName: "My Widget")
        assertFalse newProperty.validate()
    }

    void testAllWhiteSpaceUniversalNameSavedAsNull() {
        defaultProperties.universalName = ""
        defaultProperties.save()
        assertNull defaultProperties.universalName

        defaultProperties.universalName = "    "
        defaultProperties.save()
        assertNull defaultProperties.universalName
    }

    void testLeadingAndTrailingWhitespaceIsTrimmedFromUniversalName() {
        defaultProperties.universalName = "    leading"
        defaultProperties.save()
        assertEquals defaultProperties.universalName, "leading"

        defaultProperties.universalName = "trailing    "
        defaultProperties.save()
        assertEquals defaultProperties.universalName, "trailing"
    }

    void testMinimumDescriptorIdLength() {
        defaultProperties.descriptorUrl = "s"
        assertTrue defaultProperties.validate()
    }

    void testMaxAllowedDescriptorId() {
        defaultProperties.descriptorUrl = "s"*2083
        assertTrue defaultProperties.validate()
    }

    void testDescriptorIdCannotExceedMaxLength() {
        defaultProperties.descriptorUrl = "s"*2084
        assertFalse defaultProperties.validate()
    }

    void testIntents() {
        assert defaultProperties.intents == new HashSet()

        def dataType = new IntentDataType(title: "unique title")
        def action = new IntentAction(title: "unique action")
        def intent = new Intent(dataType: dataType, action: action, send: true, receive: false)
        defaultProperties.addToIntents(intent).save()

        assertEquals 1, defaultProperties.intents.size()
    }
}
