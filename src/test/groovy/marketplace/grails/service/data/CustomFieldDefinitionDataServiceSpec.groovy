package marketplace.grails.service.data

import spock.lang.Specification

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.CustomFieldDefinition
import marketplace.Types
import marketplace.data.CustomFieldDefinitionDataService

import static marketplace.grails.service.DomainBuilders.createType
import static marketplace.grails.service.DomainBuilders.stubFieldDefinition


class CustomFieldDefinitionDataServiceSpec
        extends Specification
        implements ServiceUnitTest<CustomFieldDefinitionDataService>, DataTest
{

    void setupSpec() {
        mockDomain Types
        mockDomain CustomFieldDefinition
    }

    def 'countHasTypeById'() {
        given:
        def type1 = createType(title: "type1")
        def type2 = createType(title: "type2")
        stubFieldDefinition(types: [type1])
        stubFieldDefinition(types: [type1, type2])
        stubFieldDefinition(types: [type2])

        expect:
        service.countHasTypeById(type1.id) == 2
    }

}
