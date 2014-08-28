package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import ozone.marketplace.domain.ValidationException;

@TestMixin(IntegrationTestMixin)
class CustomFieldDefinitionServiceTests {


	def customFieldDefinitionService
	def serviceItemService

    void log(def strIn){
        customFieldDefinitionService.logIt(strIn)
    }


    void testListByDate() {
        CustomFieldDefinition customFieldDefinition = new CustomFieldDefinition(
            name: 'AAA',
            label:'lbl',
            styleType: Constants.CustomFieldDefinitionStyleType.TEXT
        ).save(failOnError:true)
        assert null != CustomFieldDefinition.get(customFieldDefinition.id)
        assert null != customFieldDefinition.createdDate
        def firstDate = customFieldDefinition.createdDate

        try {
            Thread.currentThread().sleep(3000)
        } catch (Exception e) {}

        customFieldDefinition = new CustomFieldDefinition(
            name: 'BBB',
            label: 'lbl',
            styleType: Constants.CustomFieldDefinitionStyleType.TEXT
        ).save(failOnError: true)
        assert null != CustomFieldDefinition.get(customFieldDefinition.id)
        assert null != customFieldDefinition.createdDate
        def secondDate = customFieldDefinition.createdDate

        try {
            Thread.currentThread().sleep(1000)
        } catch (Exception e) {}

        def params = ['editedSinceDate':firstDate]
        def r = customFieldDefinitionService.list(params)
        assert null != r
        assert 2 == r.size()

        params = ['editedSinceDate':secondDate]
        r = customFieldDefinitionService.list(params)
        assert null != r
        assert 1 == r.size()
    }

	void testDelete() {
        CustomFieldDefinition cfd = new CustomFieldDefinition(
            name: 'BBB',
            label: 'lbl',
            styleType: Constants.CustomFieldDefinitionStyleType.TEXT
        ).save(failOnError: true)
		assert null != CustomFieldDefinition.get(cfd.id)
		customFieldDefinitionService.delete(cfd)
		CustomFieldDefinition cfdAfterSoftDelete = CustomFieldDefinition.get(cfd.id)
		assert null == cfdAfterSoftDelete
	}


    void testAuditTrail() {
        def startTime = new Date()
        Thread.sleep(1000)

        CustomFieldDefinition obj = new CustomFieldDefinition(
            name: 'BBB',
            label: 'lbl',
            styleType: Constants.CustomFieldDefinitionStyleType.TEXT
        ).save(failOnError: true)

        obj = CustomFieldDefinition.get(obj.id)
        assert null != obj
        def createdDate1 = obj.createdDate
        def editedDate1 = obj.editedDate

        assert obj.metaClass.hasProperty(obj, "createdBy")
        assert obj.metaClass.hasProperty(obj, "createdDate")
        assert obj.metaClass.hasProperty(obj, "editedBy")
        assert obj.metaClass.hasProperty(obj, "editedDate")

        assert obj.createdDate.after(startTime)
        assert obj.editedDate.after(startTime)

        obj.name = 'bozo5'
        Thread.sleep(1000)
        obj.save(flush:true)
        obj = CustomFieldDefinition.get(obj.id)
        assert obj.createdDate.equals(createdDate1)
        assert obj.editedDate.after(editedDate1)
    }

    // completely replace the fieldvalues for a DropDownCustomFieldDefinition
	void testDropDownUpdate1() {
        log('testDropDownUpdate1')
        Types types = new Types(title: 'type').save(failOnError: true)
		DropDownCustomFieldDefinition cfd = new DropDownCustomFieldDefinition(types: [types],
            name: 'color', label: 'what color', tooltip: 'tipsy', description: 'a dark and rainy night',
            isRequired: false)
        5.times{
            FieldValue fieldValue = new FieldValue()
            fieldValue.displayText = "value${it}"
            fieldValue.setCustomFieldDefinition(cfd);
            cfd.addToFieldValues(fieldValue)
        }
		cfd.save(failOnError:true, flush:true)
        def optionMap = createOptionMap(cfd)
        def oldFieldValues = cfd.getFieldValues().collect{ it.displayText }

        def newFieldValues = ["dog", "cat", "chicken"] as String[]

        def params = [ fieldOptions: newFieldValues, valueEnableFlags: '1,1,1']
        params.putAll(optionMap)
        customFieldDefinitionService.update(cfd, params)

        def newFieldValues2 = cfd.getFieldValues().collect{ it.displayText }

        println newFieldValues
        println newFieldValues2

        assert newFieldValues == newFieldValues2
	}

    // creates a map for existing fieldValues. The entries look like _fv_hidden_id_cat:10 where 10 is the
    // database id for fieldValue 'cat'. Entries like this are included in the params returned by the
    // cfd admin pages.
    def createOptionMap(DropDownCustomFieldDefinition cfd){
        def optionMap = [:]
        cfd.fieldValues.each{
            def tmpString = "_fv_hidden_id_"
            def tmpGString = "${it.displayText}"
            tmpString = tmpString + tmpGString
            optionMap.put(tmpString, it.id)
        }
        optionMap.each{key, value -> log("key = ${key} ${key.getClass()} value = ${value} ${value.getClass()}") }

        return optionMap
    }

    // rearrange fieldvalues for a DropDownCustomFieldDefinition
	void testDropDownUpdate2() {
        log('testDropDownUpdate2')
        Types types = new Types(title: 'type').save(failOnError:true)
		DropDownCustomFieldDefinition cfd = new DropDownCustomFieldDefinition(types: [types],
            name: 'color', label: 'what color', tooltip: 'tipsy', description: 'a dark and rainy night',
            isRequired: false)
        def fieldValues = ["dog", "cat", "chicken", "goat", "pig"]

        fieldValues.each{
            FieldValue fieldValue = new FieldValue()
            fieldValue.displayText = it
            fieldValue.setCustomFieldDefinition(cfd);
            cfd.addToFieldValues(fieldValue)
        }
		cfd.save(failOnError:true, flush:true)
        def optionMap = createOptionMap(cfd)

        fieldValues.eachPermutation{
            def params = [ fieldOptions: it as String[], valueEnableFlags: '1,1,0,1,1']
            params.putAll(optionMap)

            log("updating to ${it}")
            customFieldDefinitionService.update(cfd, params)

            def newFieldValues2 = cfd.getFieldValues().collect{ it.displayText }

            log it
            log newFieldValues2

            assert it == newFieldValues2
        }
	}

    // delete, rearrange and add fieldvalues for a DropDownCustomFieldDefinition
	void testDropDownUpdate3() {
        log('testDropDownUpdate3')
        Types types = new Types(title: 'type').save(failOnError:true)
		DropDownCustomFieldDefinition cfd = new DropDownCustomFieldDefinition(types: [types],
            name: 'color', label: 'what color', tooltip: 'tipsy', description: 'a dark and rainy night',
            isRequired: false)
        ["dog", "cat", "chicken", "goat", "pig"].each{
            FieldValue fieldValue = new FieldValue()
            fieldValue.displayText = it
            fieldValue.setCustomFieldDefinition(cfd)
            cfd.addToFieldValues(fieldValue)
        }
		cfd.save(failOnError:true, flush:true)
        def optionMap = createOptionMap(cfd)
        def oldFieldValues = cfd.getFieldValues().collect{ it.displayText }

        def newFieldValues = ["chicken", "cat", "dog", "gato", "perro", "mariposa"  ] as String[]

        //String[] y = x.toArray(new String[0]);

        def params = [ fieldOptions: newFieldValues, valueEnableFlags: '1,1,0,1,1,0']
        params.putAll(optionMap)
        customFieldDefinitionService.update(cfd, params)

        def newFieldValues2 = cfd.getFieldValues().collect{ it.displayText }

        println newFieldValues
        println newFieldValues2

        assert newFieldValues == newFieldValues2
	}

    //
	void testDropDownSave1() {
        Types types = new Types(title: 'type').save(failOnError:true)
		def fieldValues1 = ["Red", "Green", "Blue"]
		DropDownCustomFieldDefinition cfd = new DropDownCustomFieldDefinition(types: [types],
            name: 'color', label: 'what color', tooltip: 'tipsy', description: 'a dark and rainy night',
            isRequired: false, fieldValues: fieldValues1.collect{new FieldValue(displayText: it)})

		customFieldDefinitionService.save(cfd)
		DropDownCustomFieldDefinition cfd2 = CustomFieldDefinition.get(cfd.id)
        def fieldValues2 = cfd2.getFieldValues().collect{ it.displayText }

        assert fieldValues1 == fieldValues2
	}
}
