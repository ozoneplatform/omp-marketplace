package marketplace.controller

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONObject

import org.springframework.http.HttpStatus

import marketplace.OwfWidgetTypes
import marketplace.OwfWidgetTypesController
import spock.lang.Specification

@Integration
@Rollback
class OwfWidgetTypesControllerISpec
        extends Specification
        implements ControllerTestMixin<OwfWidgetTypesController> {

    OwfWidgetTypes type1
    OwfWidgetTypes type2
    OwfWidgetTypes type3

    List<OwfWidgetTypes> types

    void setupData() {
        type1 = new OwfWidgetTypes([title: "title 1", description: "test"]).save(failOnError: true, flush: true)
        type2 = new OwfWidgetTypes([title: "title 2", description: "test"]).save(failOnError: true, flush: true)
        type3 = new OwfWidgetTypes([title: "title 3", description: "test"]).save(failOnError: true, flush: true)

        types = [type1, type2, type3]
    }

    void testCanRetrieveOneTypeAsJson() {
        given:
        setupData()

        when:
        params([id: type1.id])

        controller.getItemAsJSON()

        then:
        responseStatus == HttpStatus.OK

        and:
        def json = responseJson as JSONObject

        json.getInt('total') == 1

        def data = json.getJSONObject('data')
        data.getString('title') == type1.title
    }


    void testCannotRetrieveTypeWithNonexistentId() {
        when:
        params([id: -1])

        controller.getItemAsJSON()

        then:
        responseStatus == HttpStatus.NOT_FOUND

        def json = responseJson as JSONObject

        // Because why not return a total and an error message as the data?
        json.getInt('total') == 1
        json.getString('data') == "Error occurred during getItemAsJSON. Exception: OWF Widget Type with id -1 does not exist"
    }

    void testCanRetrieveAllTypesAsJson() {
        given:
        int initialCount = OwfWidgetTypes.count()

        setupData()

        when:
        controller.getListAsJSON()

        then:
        responseStatus == HttpStatus.OK

        and:
        def json = responseJson as JSONObject

        json.getInt('total') == initialCount + 3

        def data = json.getJSONArray('data') as List<JSONObject>
        data.size() == initialCount + 3
        data[initialCount..initialCount+2]*.getString('title') == types*.title
    }

}
