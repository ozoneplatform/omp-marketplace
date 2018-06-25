package marketplace.grails.controller

import spock.lang.Specification

import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.web.json.JSONObject

import marketplace.OwfWidgetTypes
import marketplace.OwfWidgetTypesController
import marketplace.OwfWidgetTypesService


class OwfWidgetTypesControllerSpec
        extends Specification
        implements ControllerUnitTest<OwfWidgetTypesController>
{

    OwfWidgetTypesService owfWidgetTypesService = Mock()

    OwfWidgetTypes type1
    OwfWidgetTypes type2
    OwfWidgetTypes type3

    List<OwfWidgetTypes> types

    void setupSpec() {
        JSON.registerObjectMarshaller(OwfWidgetTypes, { OwfWidgetTypes it -> it.asJSON() })
    }

    void setup() {
        controller.owfWidgetTypesService = owfWidgetTypesService

        type1 = new OwfWidgetTypes([title: "title 1", description: "test"])
        type2 = new OwfWidgetTypes([title: "title 2", description: "test"])
        type3 = new OwfWidgetTypes([title: "title 3", description: "test"])

        types = [type1, type2, type3]
    }

    void testCanRetrieveOneTypeAsJson() {
        given:
        owfWidgetTypesService.getOwfWidgetType(type1.id) >> type1

        when:
        params.id = type1.id
        controller.getItemAsJSON()

        then:
        response.status == 200

        with(response.json, JSONObject) {
            total == 1
            data?.title == this.type1.title
        }
    }

    void testCannotRetrieveTypeWithNonexistentId() {
        when:
        params.id = -1
        controller.getItemAsJSON()

        then:
        response.status == 404
        response.contentAsString == /"Error occurred during getItemAsJSON. Exception: OWF Widget Type with id -1 does not exist"/
    }

    void testCanRetrieveAllTypesAsJson() {
        given:
        owfWidgetTypesService.list(_) >> types
        owfWidgetTypesService.countOwfWidgetTypes() >> 3

        when:
        controller.getListAsJSON()

        then:
        response.status == 200

        with(response.json, JSONObject) {
            total == 3
            data*.title == this.types*.title
        }
    }

}
