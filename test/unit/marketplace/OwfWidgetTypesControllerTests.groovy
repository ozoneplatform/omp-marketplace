package marketplace

import grails.converters.JSON
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.web.ControllerUnitTestMixin


import org.hibernate.SessionFactory
import org.hibernate.classic.Session
import ozone.decorator.JSONDecoratorService

@TestMixin([ControllerUnitTestMixin, DomainClassUnitTestMixin])
class OwfWidgetTypesControllerTests {
    def domainInstance1, domainInstance2, domainInstance3
    def sessionMock, sessionFactoryMock
    def serviceMock

    List instancesList;

    //override these if domain fields/constraints are different
    def typeParams1 = [title: "title 1"]
    def typeParams2 = [title: "title 2"]
    def typeParams3 = [title: "title 2"]

    OwfWidgetTypesControllerTests() {
    }

    void setUp() {
        mockDomain(OwfWidgetTypes)

        sessionMock = mockFor(Session)
        sessionFactoryMock = mockFor(SessionFactory)

        domainInstance1 = new OwfWidgetTypes(typeParams1)
        domainInstance2 = new OwfWidgetTypes(typeParams2)
        domainInstance3 = new OwfWidgetTypes(typeParams3)

        instancesList = [domainInstance1, domainInstance2, domainInstance3]
        controller.metaClass.JSONDecoratorService.postProcessJSON

        mockService()
    }

    def mockService() {
        serviceMock = mockFor(OwfWidgetTypesService)
        controller.owfWidgetTypesService = serviceMock.createMock()
        serviceMock.demand.with {
            getOwfWidgetType(0..1) { id -> instancesList[id-1] }
            list(0..1) { params -> instancesList }
            countOwfWidgetTypes(0..1) { -> instancesList.size()}
        }

        def decoratorServiceMock = mockFor(JSONDecoratorService)
        controller.JSONDecoratorService = decoratorServiceMock.createMock()
        decoratorServiceMock.demand.with {
            postProcessJSON(0..1) { json -> json }
        }
    }

    void testCanRetrieveOneTypeAsJson() {
        controller.params.id = 1
        controller.getItemAsJSON()

        assert controller.response.status == 200

        def respStr=controller.response.contentAsString
        def jsonResp = JSON.parse(respStr)
        println "jsonResp = ${jsonResp}"

   		assert jsonResp?.total == 1
        assert jsonResp?.data?.title == domainInstance1.title

    }

    void testCanRetrieveAllTypesAsJson() {
        controller.getListAsJSON()

        assert controller.response.status == 200

        def respStr=controller.response.contentAsString
        def jsonResp = JSON.parse(respStr)
        println "jsonResp = ${jsonResp}"

        assert jsonResp?.total == 3
        assert jsonResp?.data[0].title == domainInstance1.title
        assert jsonResp?.data[1].title == domainInstance2.title
        assert jsonResp?.data[2].title == domainInstance3.title
    }

//    void testCannotRetrieveTypeWithNonexistentId() {
//        controller.params.id = 9000
//        controller.getItemAsJSON()
//
//        assertEquals 404, controller.response.status
//
//        def respStr=controller.response.contentAsString
//        def jsonResp = JSON.parse(respStr)
//        println "jsonResp = ${jsonResp}"
//    }
}
