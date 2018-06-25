package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.Constants
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomFieldDefinition
import marketplace.FieldValue
import marketplace.Types
import marketplace.domain.builders.DomainBuilderMixin


@Integration
@Rollback
class CustomFieldDefinitionServiceTests extends Specification implements DomainBuilderMixin {

    def customFieldDefinitionService
    def serviceItemService

    void testListByDate() {
        given:
        def fieldDef1 = $fieldDefinition { name = 'AAA' }

        sleep(2000)

        def fieldDef2 = $fieldDefinition { name = 'BBB' }

        when:
        def results1 = customFieldDefinitionService.list([editedSinceDate: fieldDef1.editedDate])
        def results2 = customFieldDefinitionService.list([editedSinceDate: fieldDef2.editedDate])

        then:
        results1?.size() == 2
        results2?.size() == 1
    }

    void testDelete() {
        given:
        def cfd = $fieldDefinition { name = 'BBB' }
        assert CustomFieldDefinition.get(cfd.id) != null

        when:
        customFieldDefinitionService.delete(cfd)

        def result = CustomFieldDefinition.get(cfd.id)

        then:
        result == null
    }

    // completely replace the fieldvalues for a DropDownCustomFieldDefinition
    void testDropDownUpdate1() {
        when:
        Types types = new Types(title: 'type').save(failOnError: true)
        DropDownCustomFieldDefinition cfd = new DropDownCustomFieldDefinition(types: [types],
                                                                              name: 'color', label: 'what color', tooltip: 'tipsy', description: 'a dark and rainy night',
                                                                              isRequired: false)
        for (i in 1..5) {
            FieldValue fieldValue = new FieldValue()
            fieldValue.displayText = "value${i}"
            fieldValue.setCustomFieldDefinition(cfd);
            cfd.addToFieldValues(fieldValue)
        }

        cfd.save(failOnError: true, flush: true)
        def optionMap = createOptionMap(cfd)

        def fieldsValues1 = ["dog", "cat", "chicken"]

        def params = [fieldOptions: fieldsValues1 as String[], valueEnableFlags: '1,1,1']
        params.putAll(optionMap)
        customFieldDefinitionService.update(cfd, params)

        def fieldsValues2 = cfd.getFieldValues().collect { it.displayText }

        then:
        fieldsValues1 == fieldsValues2
    }

    // creates a map for existing fieldValues. The entries look like _fv_hidden_id_cat:10 where 10 is the
    // database id for fieldValue 'cat'. Entries like this are included in the params returned by the
    // cfd admin pages.
    private Map createOptionMap(DropDownCustomFieldDefinition cfd) {
        def optionMap = [:]
        cfd.fieldValues.each {
            def tmpString = "_fv_hidden_id_"
            def tmpGString = "${it.displayText}"
            tmpString = tmpString + tmpGString
            optionMap.put(tmpString, it.id)
        }
        optionMap
    }

    // rearrange fieldvalues for a DropDownCustomFieldDefinition
    void testDropDownUpdate2() {
        setup:
        Types types = new Types(title: 'type').save(failOnError: true)
        def cfd = new DropDownCustomFieldDefinition([types      : [types],
                                                     name       : 'color',
                                                     label      : 'what color',
                                                     tooltip    : 'tipsy',
                                                     description: 'a dark and rainy night',
                                                     isRequired : false])
        def fieldValues = ["dog", "cat", "chicken", "goat", "pig"]

        fieldValues.each {
            FieldValue fieldValue = new FieldValue()
            fieldValue.displayText = it
            fieldValue.setCustomFieldDefinition(cfd)
            cfd.addToFieldValues(fieldValue)
        }
        cfd.save(failOnError: true, flush: true)
        def optionMap = createOptionMap(cfd)

        fieldValues.eachPermutation {
            def params = [fieldOptions: it as String[], valueEnableFlags: '1,1,0,1,1']
            params.putAll(optionMap)

            customFieldDefinitionService.update(cfd, params)

            def newFieldValues2 = cfd.getFieldValues().collect { it.displayText }
            assert it == newFieldValues2
        }
    }

    // delete, rearrange and add fieldvalues for a DropDownCustomFieldDefinition
    void testDropDownUpdate3() {
        when:
        Types types = new Types(title: 'type').save(failOnError: true)
        def cfd = new DropDownCustomFieldDefinition([types      : [types],
                                                     name       : 'color',
                                                     label      : 'what color',
                                                     tooltip    : 'tipsy',
                                                     description: 'a dark and rainy night',
                                                     isRequired : false])
        ["dog", "cat", "chicken", "goat", "pig"].each {
            FieldValue fieldValue = new FieldValue()
            fieldValue.displayText = it
            fieldValue.setCustomFieldDefinition(cfd)
            cfd.addToFieldValues(fieldValue)
        }
        cfd.save(failOnError: true, flush: true)
        def optionMap = createOptionMap(cfd)

        def newFieldValues = ["chicken", "cat", "dog", "gato", "perro", "mariposa"]

        def params = [fieldOptions: newFieldValues as String[], valueEnableFlags: '1,1,0,1,1,0']
        params.putAll(optionMap)
        customFieldDefinitionService.update(cfd, params)

        def newFieldValues2 = cfd.getFieldValues().collect { it.displayText }

        then:
        newFieldValues == newFieldValues2
    }

    //
    void testDropDownSave1() {
        when:
        Types types = new Types(title: 'type').save(failOnError: true)

        def fieldsText1 = ["Red", "Green", "Blue"]
        def fieldValues1 = fieldsText1.collect { new FieldValue(displayText: it) }

        def cfd1 = new DropDownCustomFieldDefinition([types      : [types],
                                                     name       : 'color',
                                                     label      : 'what color',
                                                     tooltip    : 'tipsy',
                                                     description: 'a dark and rainy night',
                                                     isRequired : false,
                                                     fieldValues: fieldValues1])

        customFieldDefinitionService.save(cfd1)

        def cfd2 = DropDownCustomFieldDefinition.get(cfd1.id)

        then:
        fieldsText1 == cfd2.getFieldValues().collect { it.displayText }
    }
}
