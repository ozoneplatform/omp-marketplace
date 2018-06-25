package marketplace.domain

import grails.gorm.transactions.Rollback
//import grails.test.mixin.integration.Integration
import grails.testing.mixin.integration.Integration

import marketplace.OwfProperties
import marketplace.ServiceItem

import spock.lang.Specification

import marketplace.domain.builders.DomainBuilderMixin


@Integration
@Rollback
class OwfPropertiesSpec extends Specification implements DomainBuilderMixin {

    OwfProperties defaultProperties
    ServiceItem serviceItem1

    void setupData() {
        def itemOwner = $userProfile { username = 'testOwner' }

        $default $type { title = 'Test Type' }

        serviceItem1 = $serviceItem {
            title = 'Service Item'
            owner = itemOwner
        }

        defaultProperties = $owfProperties {
            serviceItem = serviceItem1
            universalName = 'org.ozone.widget'
        }
    }

    void testThatDefaultPropertiesPassesValidation() {
        given:
        setupData()

        expect:
        defaultProperties.validate()
    }

    void testMinimumStackContextLength() {
        setup:
        setupData()
        defaultProperties.stackContext = "s"

        expect:
        validateAndPrintErrors(defaultProperties)
    }

    void testMinimumStackDescriptorLength() {
        setup:
        setupData()
        defaultProperties.stackDescriptor = "s"

        expect:
        validateAndPrintErrors(defaultProperties)
    }

    void testVeryLargeStackDescriptorLength() {
        setup:
        setupData()
        defaultProperties.stackDescriptor = "s"*1000000

        expect:
        validateAndPrintErrors(defaultProperties)
    }

    void testMinimumUniversalNameLength() {
        setup:
        setupData()
        defaultProperties.universalName = "s"

        expect:
        validateAndPrintErrors(defaultProperties)
    }

    // test that OwfProperties objects for different ServiceItem
    // are not allowed to have same universalName.
    void testUniversalNameIsUnique() {
        setup:
        setupData()

        when:
        def newOwner = $userProfile { username = 'testUniqueName' }
        def newItem = $serviceItem {
            owner = newOwner
            title = 'Service Item 2'
        }

        def newProperties = $owfProperties(save: false) {
            serviceItem = newItem
            universalName = defaultProperties.universalName
        }

        then:
        !newProperties.validate(['universalName'])
        newProperties.save(flush: true) == null

        OwfProperties.count() == old(OwfProperties.count())
    }

    // test that OwfProperties objects for same ServiceItem
    // are allowed to have same universalName.
    // test exercises special case code in universalName validator.
    void testUniversalNameIsUnique2() {
        setup:
        setupData()

        when:
        def newProperties = $owfProperties(save: false) {
            universalName = defaultProperties.universalName
        }
        newProperties.serviceItem = serviceItem1
        save(newProperties)

        then:
        OwfProperties.count() == old(OwfProperties.count()) + 1
    }

    // test that OwfProperties objects for same ServiceItem
    // are allowed to change universalName.
    void testUniversalNameIsUnique3() {
        setup:
        setupData()

        when:
        def newProperties = $owfProperties {
            serviceItem = serviceItem1
            universalName = defaultProperties.universalName + '.foo'
        }

        then:
        OwfProperties.count() == old(OwfProperties.count()) + 1

        newProperties.id != defaultProperties.id
        newProperties.id == serviceItem1.owfProperties.id
    }

    void testBlankUniversalNameSavedAsNull() {
        setup:
        setupData()

        when:
        defaultProperties.universalName = ""
        save(defaultProperties)

        then:
        OwfProperties.findById(defaultProperties.id).universalName == null
    }

    void testWhiteSpaceUniversalNameSavedAsNull() {
        setup:
        setupData()

        when:
        defaultProperties.universalName = "    "
        save(defaultProperties)

        then:
        OwfProperties.findById(defaultProperties.id).universalName == null
    }

    void testLeadingWhitespaceAreTrimmedFromUniversalName() {
        setup:
        setupData()

        when:
        defaultProperties.universalName = "    leading"
        save(defaultProperties)

        then:
        OwfProperties.findById(defaultProperties.id).universalName == "leading"
    }

    void testTrailingWhitespaceIsTrimmedFromUniversalName() {
        setup:
        setupData()

        when:
        defaultProperties.universalName = "trailing    "
        save(defaultProperties)

        then:
        OwfProperties.findById(defaultProperties.id).universalName == "trailing"
    }

    void testIntents() {
        setup:
        setupData()

        def intent = $intent {
            dataType = $intentDataType { title = "Test Data Type" }
            action = $intentAction { title = "Test Action" }
            send = true
            receive = false
        }

        when:
        save(defaultProperties.addToIntents(intent))

        def result = OwfProperties.findById(defaultProperties.id)

        then:
        result.intents.size() == 1
    }

    void testMinimumDescriptorIdLength() {
        given:
        setupData()
        defaultProperties.descriptorUrl = "s"

        expect:
        validateAndPrintErrors(defaultProperties)
    }

}
