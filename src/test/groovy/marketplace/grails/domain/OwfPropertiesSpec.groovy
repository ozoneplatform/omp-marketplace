package marketplace.grails.domain

import marketplace.OwfProperties
import marketplace.ServiceItem
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class OwfPropertiesSpec extends Specification implements DomainConstraintsUnitTest<OwfProperties>{

    @Autowired
    OwfProperties defaultProperties

    @Autowired
    ServiceItem serviceItem
    void setup() {
        serviceItem = new ServiceItem()

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
    }
    void testMaximumAllowedStackContextLength() {
        expect:
        propertyHasMaxSize('stackContext', 200)
//        defaultProperties.stackContext = "s"*200
//        assertTrue defaultProperties.validate()
    }

    void testMaxAllowedUniversalName() {
        expect:
        propertyHasMaxSize('universalName', 255)
//        defaultProperties.universalName = "s"*255
//        assertTrue defaultProperties.validate()
    }

    void testMaxAllowedDescriptorId() {
        expect:
        propertyHasMaxSize('descriptorUrl', 2083)
//        given:
//        setupData()
//        defaultProperties.descriptorUrl = "s"*2083
//        assertTrue defaultProperties.validate()
    }
}
