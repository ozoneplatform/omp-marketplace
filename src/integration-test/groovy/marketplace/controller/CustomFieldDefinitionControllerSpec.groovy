package marketplace.controller

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.Category
import marketplace.CheckBoxCustomFieldDefinition
import marketplace.Constants
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.CustomFieldDefinitionController
import marketplace.ImageURLCustomFieldDefinition
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.TextCustomField
import marketplace.TextCustomFieldDefinition
import marketplace.Types
import ozone.utils.Utils
import spock.lang.Specification

@Integration
@Rollback
class CustomFieldDefinitionControllerSpec extends Specification implements ControllerTestMixin<CustomFieldDefinitionController>{

    //def grailsApplication
    def customFieldDefinitionService
    def typesService
//    def controller
    def sessionFactory

    Profile owner

    void setup() {
        controller.typesService = typesService
        controller.sessionFactory = sessionFactory
        controller.customFieldDefinitionService = customFieldDefinitionService
        owner = new Profile(username: 'testUser')
    }

    void testTypeListinCreate(){
        when:
        int size = typesService.list([sort:'title',order:'asc']).size()
        for (i in 1..5){
            Types type = new Types(title:'Regular Type ' + i).save(flush:true)
        }
        def map = controller.create()
        then:
        size + 5 == map.typesList.size()
    }

    void testTypeListinEdit(){
        when:
        int size = typesService.list([sort:'title',order:'asc']).size()
        for (i in 1..5){
            new Types(title:'Regular Type ' + i).save(failOnError: true)
        }

        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(
            name: 'text custom field',
            label: 'text custom field label'
        )
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        def map = controller.edit()
        then:
        size + 5 == map.typesList.size()
    }

    void testList() {
        when:
        def customFieldDefinitionCount = CustomFieldDefinition.count()
        def model = controller.list()
        then:
        customFieldDefinitionCount == model.customFieldDefinitionInstanceList.size()
        customFieldDefinitionCount == model.customFieldDefinitionInstanceTotal
    }

    void testListDeletedItems() {
        when:
        def customFieldDefinitionCount = customFieldDefinitionService.list().size()

        for (i in 1..5){
            new TextCustomFieldDefinition(name:'Regular Type ' + i, label: "lbl$i").save(failOnError: true, flush: true)
        }
        def model = controller.list()
        then:
        customFieldDefinitionCount + 5 == model.customFieldDefinitionInstanceList.size()
        customFieldDefinitionCount + 5 == model.customFieldDefinitionInstanceTotal
    }

    void testShow() {
        when:
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(name: "Custom Field Definition A", label: 'lbl')
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.request.method = 'POST'
        def model = controller.show()
        then:
        "Custom Field Definition A" == model.customFieldDefinitionInstance.name
    }

    void testShowWithInvalidObject() {
        when:
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition()
        customFieldDefinition.save(flush:true)
        controller.params.id = 7878787878
        controller.request.method = 'POST'
        controller.show()
        then:
        controller.flash.message == "specificObjectNotFound"
    }

    void testDelete() {
        when:
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(name: 'field', label: 'lbl')
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.request.method = 'POST'
        controller.delete()
        then:
        "delete.success" == controller.flash.message
    }

    void testDeleteCustomFieldDefinitionAssociatedWithServiceItem() {
        when:
        Category cat = new Category(title: 'category').save(failOnError:true, flush: true)
        Types types = new Types(title: 'type').save(failOnError:true, flush: true)
        ServiceItem serviceItem = new ServiceItem(
            owners: [owner],
            uuid: Utils.generateUUID(),
            types: types,
            categories: [cat],
            title: 'listing',
            launchUrl: 'https:///'
        ).save(failOnError:true, flush: true)
        serviceItem.customFields = new ArrayList()
        CustomFieldDefinition cfd = new TextCustomFieldDefinition(id:100, types: [types], name: 'field', label: 'lbl')
        CustomField cf = new CustomField(customFieldDefinition: cfd, createdBy: owner.id, editedBy: owner.id)
        serviceItem.customFields << cf

        cfd.save(failOnError:true, flush: true)
        cf.save(failOnError:true, flush: true)
        serviceItem.save(failOnError:true, flush: true)

        controller.params.id = cfd.id
        controller.request.method = 'POST'
        controller.delete()
        then:
        "delete.success" == controller.flash.message
    }


    void testUpdate(){
        when:
        //log('testUpdate:')
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(name: 'field', label: 'lbl')
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.params.name = "Custom Field Definition B"
        controller.request.method = 'POST'
        controller.update()
        then:
        "update.success" == controller.flash.message
        when:
        CustomFieldDefinition updatedCustomFieldDefinition = CustomFieldDefinition.get(customFieldDefinition.id)
        then:
        updatedCustomFieldDefinition.name == customFieldDefinition.name
    }

    void testUpdateWithTooLongOfName(){
        //log('testUpdateWithTooLongOfName:')
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition()
        customFieldDefinition.save(flush:true)
        controller.params.name = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
        controller.params.id = customFieldDefinition.id
        controller.request.method = 'POST'
        controller.update()
// TODO: is this test still valid?
//		assert "/customFieldDefinition/edit" == controller.modelAndView.viewName
    }

    void testUpdateWithInvalidObject() {
        when:
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition()
        customFieldDefinition.save(flush:true)
        controller.params.id = 7878787878
        controller.params.name = "gghfgh"
        controller.request.method = 'POST'
        controller.update()
        then:
        controller.flash.message == "objectNotFound"
    }

    void testSave(){
        when:
        controller.params.name = "Custom Field Definition A"
        controller.params.label = "My Field"
        controller.params.description = "Description of Custom Field Definition A"
        controller.params.tooltip = ""
		controller.params.styleType = Constants.CustomFieldDefinitionStyleType.TEXT.name()
        controller.request.method = 'POST'
        controller.save()
        then:
        "create.success" == controller.flash.message
    }

    void testSaveWithTooLongOfName(){
        controller.params.label = "My Field"
        controller.params.name = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
        controller.params.description = "Description of Custom Field Definition A"
		controller.params.styleType = Constants.CustomFieldDefinitionStyleType.TEXT.name()
        controller.request.method = 'POST'
		controller.save()
// TODO: is this test still valid?
//		assert "/customFieldDefinition/create" == controller.modelAndView.viewName
    }

    void testUpdatedWithServiceItem() {
        given:
        Category cat = new Category(title: 'category').save(failOnError: true)
        Types widget = new Types(title:"Widget", description: "Used for OWF").save(failOnError: true)
        Types desktop = new Types(title:"Desktop All", description: "Not Used for OWF").save(failOnError: true)

        ServiceItem serviceItem = new ServiceItem(
            owners: [owner],
            uuid: Utils.generateUUID(),
            types: widget,
            categories: [cat],
            title: 'test service item',
            launchUrl: 'https:///'
        )
        serviceItem.customFields = new ArrayList()
        CustomFieldDefinition cfd = new TextCustomFieldDefinition(id:100, name:'luminosity', types: [widget, desktop], label: 'lbl').save(failOnError: true)
        TextCustomField cf = new TextCustomField(customFieldDefinition: cfd, createdBy: owner.id, editedBy: owner.id, value: "test")

        serviceItem.customFields << cf
        serviceItem.save(failOnError: true)

        when:
        // update customFieldDefinition to not have type desktop
        controller.params.id = cfd.id
        controller.params.types = [desktop]
        controller.request.method = 'POST'
        controller.update()

        then:
        //Changed for grails 2.
        //For some reason in grails 1 this was 200.  But the code does do a redirect so 302 seems right
        controller.response.status == 302
        controller.flash.message == "update.success"
    }

    void testUpdatedWithServiceItem_delete() {
        given:
        Category cat = new Category(title: 'category').save(failOnError: true)
        Types widget = new Types(title:"Widget", description: "Used for OWF").save(failOnError: true)
        Types desktop = new Types(title:"Desktop All", description: "Not Used for OWF").save(failOnError: true)

        ServiceItem serviceItem = new ServiceItem(
                owners: [owner],
                uuid: Utils.generateUUID(),
                types: widget,
                categories: [cat],
                title: 'test service item',
                launchUrl: 'https:///'
        )
        serviceItem.customFields = new ArrayList()
        CustomFieldDefinition cfd = new TextCustomFieldDefinition(id:100, name:'luminosity', types: [widget, desktop], label: 'lbl').save(failOnError: true)
        TextCustomField cf = new TextCustomField(customFieldDefinition: cfd, createdBy: owner.id, editedBy: owner.id, value: "test")

        serviceItem.customFields << cf
        serviceItem.save(failOnError: true)

        when:
        // We should not be able to delete the customFieldDefinition now because a serviceItem
        // has the "luminosity" custom field...
        controller.params.id = cfd.id
        controller.request.method = 'POST'
        controller.delete()

        then:
        controller.response.status == 302
        controller.flash.message == "delete.success"
    }

    // CheckBox tests
    void testShowCheckBox() {
        expect:
        showTest(new CheckBoxCustomFieldDefinition())
    }
    void testSaveCheckBox() {
        expect:
        saveTest(new CheckBoxCustomFieldDefinition())
    }
    void testDeleteCheckBox() {
        expect:
        deleteTest(new CheckBoxCustomFieldDefinition())
    }
    void testUpdateCheckBox(){
        expect:
        updateTest(new CheckBoxCustomFieldDefinition())
    }

    // ImageURL tests
    void testShowImageURL() {
        expect:
        showTest(new ImageURLCustomFieldDefinition())
    }
    void testSaveImageURL() {
        expect:
        saveTest(new ImageURLCustomFieldDefinition())
    }
    void testDeleteImageURL() {
        expect:
        deleteTest(new ImageURLCustomFieldDefinition())
    }
    void testUpdateImageURL(){
        expect:
        updateTest(new ImageURLCustomFieldDefinition())
    }

    private void showTest(def exemplar) {
        Class classIn = exemplar.class
        CustomFieldDefinition customFieldDefinition = classIn.newInstance()
        customFieldDefinition.name =  "Custom Field Definition A"
        customFieldDefinition.label =  "Custom Field Definition A"
        customFieldDefinition.save(failOnError:true)
        controller.params.id = customFieldDefinition.id
        controller.request.method = 'POST'
        def model = controller.show()
        assert "Custom Field Definition A" == model.customFieldDefinitionInstance.name
    }

    private void saveTest(def exemplar){
        Constants.CustomFieldDefinitionStyleType styleType = exemplar.styleType
        controller.params.name = "Custom Field Definition A"
        controller.params.label = "My Field"
        controller.params.description = "Description of Custom Field Definition A"
        controller.params.tooltip = ""
		controller.params.styleType = styleType.name()
        controller.request.method = 'POST'
        controller.save()
        assert "create.success" == controller.flash.message
    }

    private void deleteTest(def exemplar) {
        Class classIn = exemplar.class
        CustomFieldDefinition customFieldDefinition = classIn.newInstance()
        customFieldDefinition.name = 'custom field'
        customFieldDefinition.label = 'custom field'
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.request.method = 'POST'
        controller.delete()
        assert "delete.success" == controller.flash.message
    }

    private void updateTest(def exemplar){
        Class classIn = exemplar.class
        CustomFieldDefinition customFieldDefinition = classIn.newInstance()
        customFieldDefinition.name = 'custom field'
        customFieldDefinition.label = 'custom field'
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.params.name = "Custom Field Definition B"
        controller.request.method = 'POST'
        controller.update()
        assert "update.success" == controller.flash.message
        CustomFieldDefinition updatedCustomFieldDefinition = CustomFieldDefinition.get(customFieldDefinition.id)
        assert updatedCustomFieldDefinition.name == customFieldDefinition.name
    }
}
