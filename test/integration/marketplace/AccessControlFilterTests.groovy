package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.util.GrailsWebUtil

@TestMixin(IntegrationTestMixin)
class AccessControlFilterTests {

    def filterInterceptor
    def grailsApplication
    def grailsWebRequest

    def request(Map params, controllerName, actionName) {
        grailsWebRequest = GrailsWebUtil.bindMockWebRequest(grailsApplication.mainContext)
        if (params != null) grailsWebRequest.params.putAll(params)
        grailsWebRequest.controllerName = controllerName
        grailsWebRequest.actionName = actionName
        filterInterceptor.preHandle(grailsWebRequest.request, grailsWebRequest.response, null)
    }

    def getResponse() {
        grailsWebRequest.currentResponse
    }

    void doTest(Map params, controllerName, actionName) {
        def result = request(params, controllerName, actionName)
        println result
        if (grailsWebRequest.session.isAdmin) {
            assert true == result
        } else {
            assert false == result
        }
    }

    // Category Access Control Tests
    void testCategoryCreate() { doTest(null, "category", "create") }
    void testCategoryCreateMe() { doTest(null, "category", "createme") }
    void testCategoryDelete() { doTest(null, "category", "delete") }
    void testCategoryEdit() { doTest(null, "category", "edit") }
    void testCategoryEditMe() { doTest(null, "category", "editme") }
    void testCategoryIndex() { doTest(null, "category", "index") }
    void testCategoryList() { doTest(null, "category", "list") }
    void testCategorySave() { doTest(null, "category", "save") }
    void testCategorySaveMe() { doTest(null, "category", "saveme") }
    void testCategoryShow() { doTest(null, "category", "show") }
    void testCategorySoftDelete() { doTest(null, "category", "softDelete") }
    void testCategoryUpdate() { doTest(null, "category", "update") }
    void testCategoryUpdateMe() { doTest(null, "category", "updateme") }

    // Custom Field Definition Access Control Tests
    void testCustomFieldDefinitionCreate() { doTest(null, "customFieldDefinition", "create") }
    void testCustomFieldDefinitionCreateMe() { doTest(null, "customFieldDefinition", "createme") }
    void testCustomFieldDefinitionDelete() { doTest(null, "customFieldDefinition", "delete") }
    void testCustomFieldDefinitionEdit() { doTest(null, "customFieldDefinition", "edit") }
    void testCustomFieldDefinitionEditMe() { doTest(null, "customFieldDefinition", "editme") }
    void testCustomFieldDefinitionIndex() { doTest(null, "customFieldDefinition", "index") }
    void testCustomFieldDefinitionList() { doTest(null, "customFieldDefinition", "list") }
    void testCustomFieldDefinitionSave() { doTest(null, "customFieldDefinition", "save") }
    void testCustomFieldDefinitionSaveMe() { doTest(null, "customFieldDefinition", "saveme") }
    void testCustomFieldDefinitionShow() { doTest(null, "customFieldDefinition", "show") }
    void testCustomFieldDefinitionSoftDelete() { doTest(null, "customFieldDefinition", "softDelete") }
    void testCustomFieldDefinitionUpdate() { doTest(null, "customFieldDefinition", "update") }
    void testCustomFieldDefinitionUpdateMe() { doTest(null, "customFieldDefinition", "updateme") }

    // Rejection Justification Access Control Tests
    void testRejectionJustificationCreate() { doTest(null, "rejectionJustification", "create") }
    void testRejectionJustificationCreateMe() { doTest(null, "rejectionJustification", "createme") }
    void testRejectionJustificationDelete() { doTest(null, "rejectionJustification", "delete") }
    void testRejectionJustificationEdit() { doTest(null, "rejectionJustification", "edit") }
    void testRejectionJustificationEditMe() { doTest(null, "rejectionJustification", "editme") }
    void testRejectionJustificationIndex() { doTest(null, "rejectionJustification", "index") }
    void testRejectionJustificationList() { doTest(null, "rejectionJustification", "list") }
    void testRejectionJustificationSave() { doTest(null, "rejectionJustification", "save") }
    void testRejectionJustificationSaveMe() { doTest(null, "rejectionJustification", "saveme") }
    void testRejectionJustificationShow() { doTest(null, "rejectionJustification", "show") }
    void testRejectionJustificationSoftDelete() { doTest(null, "rejectionJustification", "softDelete") }
    void testRejectionJustificationUpdate() { doTest(null, "rejectionJustification", "update") }
    void testRejectionJustificationUpdateMe() { doTest(null, "rejectionJustification", "updateme") }

    // Service Item Access Control Tests
    void testServiceItemAdminList() { doTest(null, "serviceItem", "adminList") }
    void testServiceItemAdminView() { doTest(null, "serviceItem", "adminView") }

    // Snoop Access Control Tests
    void testSnoopIndex() { doTest(null, "snoop", "index") }

    // State Access Control Tests
    void testStateCreate() { doTest(null, "state", "create") }
    void testStateCreateMe() { doTest(null, "state", "createme") }
    void testStateDelete() { doTest(null, "state", "delete") }
    void testStateEdit() { doTest(null, "state", "edit") }
    void testStateEditMe() { doTest(null, "state", "editme") }
    void testStateIndex() { doTest(null, "state", "index") }
    void testStateList() { doTest(null, "state", "list") }
    void testStateSave() { doTest(null, "state", "save") }
    void testStateSaveMe() { doTest(null, "state", "saveme") }
    void testStateShow() { doTest(null, "state", "show") }
    void testStateSoftDelete() { doTest(null, "state", "softDelete") }
    void testStateUpdate() { doTest(null, "state", "update") }
    void testStateUpdateMe() { doTest(null, "state", "updateme") }

    // Text Access Control Tests
    void testTextCreate() { doTest(null, "text", "create") }
    void testTextCreateMe() { doTest(null, "text", "createme") }
    void testTextDelete() { doTest(null, "text", "delete") }
    void testTextEdit() { doTest(null, "text", "edit") }
    void testTextEditMe() { doTest(null, "text", "editme") }
    void testTextIndex() { doTest(null, "text", "index") }
    void testTextList() { doTest(null, "text", "list") }
    void testTextSave() { doTest(null, "text", "save") }
    void testTextSaveMe() { doTest(null, "text", "saveme") }
    void testTextShow() { doTest(null, "text", "show") }
    void testTextSoftDelete() { doTest(null, "text", "softDelete") }
    void testTextUpdate() { doTest(null, "text", "update") }
    void testTextUpdateMe() { doTest(null, "text", "updateme") }

    // Types Access Control Tests
    void testTypesCreate() { doTest(null, "types", "create") }
    void testTypesCreateMe() { doTest(null, "types", "createme") }
    void testTypesDelete() { doTest(null, "types", "delete") }
    void testTypesEdit() { doTest(null, "types", "edit") }
    void testTypesEditMe() { doTest(null, "types", "editme") }
    void testTypesIndex() { doTest(null, "types", "index") }
    void testTypesList() { doTest(null, "types", "list") }
    void testTypesSave() { doTest(null, "types", "save") }
    void testTypesSaveMe() { doTest(null, "types", "saveme") }
    void testTypesShow() { doTest(null, "types", "show") }
    void testTypesSoftDelete() { doTest(null, "types", "softDelete") }
    void testTypesUpdate() { doTest(null, "types", "update") }
    void testTypesUpdateMe() { doTest(null, "types", "updateme") }
}
