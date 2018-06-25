package marketplace.grails.service

import spock.lang.Specification

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.Text
import marketplace.TextService

import static marketplace.TextService.DEPRECATED_TEXTS
import static marketplace.TextService.REQUIRED_TEXTS
import static marketplace.grails.service.DomainBuilders.createText


class TextServiceSpec
        extends Specification
        implements ServiceUnitTest<TextService>, DataTest
{

    List<Text> texts

    void setupSpec() {
        mockDomain Text
    }

    void exampleTexts() {
        texts = [createText(name: 'text1'),
                 createText(name: 'text2'),
                 createText(name: 'text3', readOnly: true)]
    }

    def 'list'() {
        given:
        exampleTexts()

        expect:
        service.list() as Set == [texts[0], texts[1]] as Set
    }

    def 'get'() {
        given:
        exampleTexts()

        expect:
        service.get([id: texts[0].id]) == texts[0]
    }

    def 'get for readOnly Text'() {
        given:
        exampleTexts()

        expect:
        service.get([id: texts[2].id]) == null
    }

    def 'countTypes'() {
        given:
        exampleTexts()

        expect:
        service.countTypes() == 2
    }

    def 'manageRequiredTexts adds required'() {
        given:
        exampleTexts()

        when:
        service.manageRequiredTexts()

        then:
        REQUIRED_TEXTS.each { text ->
            Text.findByName(text.name as String) != null
        }
    }

    def 'manageRequiredTexts removes deprecated'() {
        given:
        DEPRECATED_TEXTS.each { name ->
            createText(name: name)
        }

        assert Text.count() == DEPRECATED_TEXTS.size()

        when:
        service.manageRequiredTexts()

        then:
        Text.count() == REQUIRED_TEXTS.size()
    }

}
