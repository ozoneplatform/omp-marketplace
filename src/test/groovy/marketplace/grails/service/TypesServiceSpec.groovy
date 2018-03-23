package marketplace.grails.service

import grails.core.GrailsApplication
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.CustomFieldDefinition
import marketplace.ServiceItem
import marketplace.Types
import marketplace.TypesService
import marketplace.data.CustomFieldDefinitionDataService
import marketplace.data.ServiceItemDataService

import ozone.marketplace.domain.ValidationException

import static marketplace.grails.service.DomainBuilders.createType


class TypesServiceSpec
        extends OwfUnitSpecification
        implements ServiceUnitTest<TypesService>, DataTest {

    void setupSpec() {
        mockDomain Types
        mockDomain ServiceItem
        mockDomain CustomFieldDefinition
    }

    GrailsApplication grailsApp = Mock()
    ServiceItemDataService serviceItemDataService = Mock()
    CustomFieldDefinitionDataService customFieldDefinitionDataService = Mock()

    void setup() {
        service.with {
            grailsApplication = this.grailsApp
            sessionFactory = mockSessionFactory()
            serviceItemDataService = this.serviceItemDataService
            customFieldDefinitionDataService = this.customFieldDefinitionDataService
        }
    }


    void "getAllTypes: sorts by title ascending"() {
        given:
        def type3 = createType(title: "Type 3")
        def type2 = createType(title: "Type 2")
        def type1 = createType(title: "Type 1")

        expect:
        service.getAllTypes() == [type1, type2, type3]
    }

    void "countTypes"() {
        given:
        createType(title: "Type 1")
        createType(title: "Type 2")
        createType(title: "Type 3")

        expect:
        service.countTypes() == 3
    }

    void "delete"() {
        given:
        def type1 = createType(title: "Type 1")
        assert Types.count() == 1

        and:
        serviceItemDataService.countHasTypeById(_) >> 0
        customFieldDefinitionDataService.countHasTypeById(_) >> 0

        when:
        service.delete(type1.id)

        then:
        Types.count() == 0
    }

    void "delete: when id not found, should throw ValidationException"() {
        when:
        service.delete(-1L)

        then:
        def ex = thrown(ValidationException)
        ex.message == TypesService.ERROR_NOT_FOUND
    }

    void "delete: when is immutable, should throw ValidationException"() {
        given:
        def type1 = createType(title: "Type 1", isPermanent: true)
        assert Types.count() == 1

        when:
        service.delete(type1.id)

        then:
        def ex = thrown(ValidationException)
        ex.message == TypesService.ERROR_IS_IMMUTABLE

        Types.count() == 1
    }

    void "delete: when has associated ServiceItem, should throw ValidationException"() {
        given:
        def type1 = createType(title: "Type 1")
        assert Types.count() == 1

        and:
        serviceItemDataService.countHasTypeById(_) >> 1
        customFieldDefinitionDataService.countHasTypeById(_) >> 0

        when:
        service.delete(type1.id)

        then:
        def ex = thrown(ValidationException)
        ex.message == TypesService.ERROR_HAS_ASSOCIATED_SERVICE_ITEM

        Types.count() == 1
    }

    void "delete: when has associated CustomFieldDefinition, should throw ValidationException"() {
        given:
        def type1 = createType(title: "Type 1")
        assert Types.count() == 1

        and:
        serviceItemDataService.countHasTypeById(_) >> 0
        customFieldDefinitionDataService.countHasTypeById(_) >> 1

        when:
        service.delete(type1.id)

        then:
        def ex = thrown(ValidationException)
        ex.message == TypesService.ERROR_HAS_ASSOCIATED_FIELD

        Types.count() == 1
    }

    void "createRequired: adds Types from application config"() {
        given:
        assert Types.count() == 0

        grailsApp.getConfig() >> mockConfig(
                [marketplace: [metadata: [types: DefaultMetadata.TYPES]]])

        when:
        service.createRequired()

        then:
        Types.count() == DefaultMetadata.TYPES.size()
    }

}
