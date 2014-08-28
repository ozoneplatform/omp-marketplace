package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import grails.test.ControllerUnitTestCase;
import ozone.utils.Utils

@TestMixin(IntegrationTestMixin)
class CustomFieldDefinitionControllerTests {

    def grailsApplication
    def customFieldDefinitionService
    def typesService
    def controller
    def sessionFactory

    def owner

    void setUp() {
        controller = new CustomFieldDefinitionController()
        controller.customFieldDefinitionService = customFieldDefinitionService
        controller.typesService = typesService
        controller.sessionFactory = sessionFactory

        owner = new Profile(username: 'testOwner')
    }

    private def log(def strIn){
        customFieldDefinitionService.logIt(strIn)
    }

    void testTypeListinCreate(){
        int size = typesService.list([sort:'title',order:'asc']).size()
        for (i in 1..5){
            Types type = new Types(title:'Regular Type ' + i).save(flush:true)
        }
        def map = controller.create()
        assert size + 5 == map.typesList.size()
    }

    void testTypeListinEdit(){
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
        assert size + 5 == map.typesList.size()
    }

    void testList() {
        def customFieldDefinitionCount = CustomFieldDefinition.count()
        def model = controller.list();
        assert customFieldDefinitionCount == model.customFieldDefinitionInstanceList.size()
        assert customFieldDefinitionCount == model.customFieldDefinitionInstanceTotal
    }

    void testListDeletedItems() {
        def customFieldDefinitionCount = customFieldDefinitionService.list().size()

        for (i in 1..5){
            new TextCustomFieldDefinition(name:'Regular Type ' + i, label: "lbl$i").save(failOnError: true)
        }
        def model = controller.list();
        assert customFieldDefinitionCount + 5 == model.customFieldDefinitionInstanceList.size()
        assert customFieldDefinitionCount + 5 == model.customFieldDefinitionInstanceTotal
    }

    void testShow() {
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(name: "Custom Field Definition A", label: 'lbl')
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        def model = controller.show()
        assert "Custom Field Definition A" == model.customFieldDefinitionInstance.name
    }

    void testShowWithInvalidObject() {
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition()
        customFieldDefinition.save(flush:true)
        controller.params.id = 7878787878
        controller.show()
        assert controller.flash.message == "specificObjectNotFound"
    }

    void testDelete() {
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(name: 'field', label: 'lbl')
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.delete()
        assert "delete.success" == controller.flash.message
    }

    void testDeleteCustomFieldDefinitionAssociatedWithServiceItem() {
        Category cat = new Category(title: 'category').save(failOnError:true)
        Types types = new Types(title: 'type').save(failOnError:true)
        ServiceItem serviceItem = new ServiceItem(
            owners: [owner],
            uuid: Utils.generateUUID(),
            types: types,
            categories: [cat],
            title: 'listing',
            launchUrl: 'https:///'
        ).save(failOnError:true)
        serviceItem.customFields = new ArrayList()
        CustomFieldDefinition cfd = new TextCustomFieldDefinition(id:100, types: [types], name: 'field', label: 'lbl')
        CustomField cf = new CustomField(customFieldDefinition: cfd)
        serviceItem.customFields << cf

        cfd.save(failOnError:true)
        serviceItem.save(failOnError:true)

        controller.params.id = cfd.id
        controller.delete()
        assert "delete.success" == controller.flash.message
    }


    void testUpdate(){
        log('testUpdate:')
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition(name: 'field', label: 'lbl')
        customFieldDefinition.save(flush:true, failOnError: true)
        controller.params.id = customFieldDefinition.id
        controller.params.name = "Custom Field Definition B"
        controller.update()
        assert "update.success" == controller.flash.message
        CustomFieldDefinition updatedCustomFieldDefinition = CustomFieldDefinition.get(customFieldDefinition.id)
        assert updatedCustomFieldDefinition.name == customFieldDefinition.name
    }

    void testUpdateWithTooLongOfName(){
        log('testUpdateWithTooLongOfName:')
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition()
        customFieldDefinition.save(flush:true)
        controller.params.name = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
        controller.params.id = customFieldDefinition.id
        controller.update()
// TODO: is this test still valid?
//		assert "/customFieldDefinition/edit" == controller.modelAndView.viewName
    }

    void testUpdateWithInvalidObject() {
        CustomFieldDefinition customFieldDefinition = new TextCustomFieldDefinition()
        customFieldDefinition.save(flush:true)
        controller.params.id = 7878787878
        controller.params.name = "gghfgh"
        controller.update()
        assert controller.flash.message == "objectNotFound"
    }

    void testSave(){
        controller.params.name = "Custom Field Definition A"
        controller.params.label = "My Field"
        controller.params.description = "Description of Custom Field Definition A"
        controller.params.tooltip = ""
		controller.params.styleType = Constants.CustomFieldDefinitionStyleType.TEXT.name()
        controller.save()
        assert "create.success" == controller.flash.message
    }

    void testSaveWithTooLongOfName(){
        controller.params.label = "My Field"
        controller.params.name = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
        controller.params.description = "Description of Custom Field Definition A"
		controller.params.styleType = Constants.CustomFieldDefinitionStyleType.TEXT.name()
		controller.save()
// TODO: is this test still valid?
//		assert "/customFieldDefinition/create" == controller.modelAndView.viewName
    }

    @Override
    protected void reset() {
        // workaround from http://jira.codehaus.org/browse/GRAILS-6483
        controller.response.committed = false
        controller.response.reset()
    }

    void testUpdatedWithServiceItem() {
        Category cat = new Category(title: 'category').save(failOnError: true)
        Types widget = new Types(title:"Widget", description: "Used for OWF")
        Types desktop = new Types(title:"Desktop All", description: "Not Used for OWF")
        widget.save(failOnError: true)
        desktop.save(failOnError: true)

        ServiceItem serviceItem = new ServiceItem(
            owners: [owner],
            uuid: Utils.generateUUID(),
            types: widget,
            categories: [cat],
            title: 'test service item',
            launchUrl: 'https:///'
        )
        serviceItem.customFields = new ArrayList()
        CustomFieldDefinition cfd = new TextCustomFieldDefinition(id:100, name:'luminosity', types: [widget, desktop], label: 'lbl')
        TextCustomField cf = new TextCustomField(customFieldDefinition: cfd)
        assert true == cf.isEmpty()
        cf.value = "test"
        assert false == cf.isEmpty()
        serviceItem.customFields << cf
        cfd.save(failOnError: true)
        serviceItem.save(failOnError: true)

        // update customFieldDefinition to not have type desktop
        controller.params.id = cfd.id
        controller.params.types = [desktop]
        controller.update()

        //Changed for grails 2.
        //For some reason in grails 1 this was 200.  But the
        //code does do a redirect so 302 seems right
        assert 302 == controller.response.status
        assert "update.success" == controller.flash.message

        // We should not be able to delete the customFieldDefinition now because a serviceItem
        // has the "luminosity" custom field...
        reset()
        controller.params.id = cfd.id
        controller.delete()
        assert 302 == controller.response.status
        assert "delete.success" == controller.flash.message
    }

    // CheckBox tests
    void testShowCheckBox() {
        showTest(new CheckBoxCustomFieldDefinition())
    }
    void testSaveCheckBox() {
        saveTest(new CheckBoxCustomFieldDefinition())
    }
    void testDeleteCheckBox() {
        deleteTest(new CheckBoxCustomFieldDefinition())
    }
    void testUpdateCheckBox(){
        updateTest(new CheckBoxCustomFieldDefinition())
    }

    // ImageURL tests
    void testShowImageURL() {
        showTest(new ImageURLCustomFieldDefinition())
    }
    void testSaveImageURL() {
        saveTest(new ImageURLCustomFieldDefinition())
    }
    void testDeleteImageURL() {
        deleteTest(new ImageURLCustomFieldDefinition())
    }
    void testUpdateImageURL(){
        updateTest(new ImageURLCustomFieldDefinition())
    }

    private void showTest(def exemplar) {
        Class classIn = exemplar.class
        CustomFieldDefinition customFieldDefinition = classIn.newInstance()
        customFieldDefinition.name =  "Custom Field Definition A"
        customFieldDefinition.label =  "Custom Field Definition A"
        customFieldDefinition.save(failOnError:true)
        controller.params.id = customFieldDefinition.id
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
        controller.update()
        assert "update.success" == controller.flash.message
        CustomFieldDefinition updatedCustomFieldDefinition = CustomFieldDefinition.get(customFieldDefinition.id)
        assert updatedCustomFieldDefinition.name == customFieldDefinition.name
    }
}
