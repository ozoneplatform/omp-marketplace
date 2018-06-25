package marketplace

import grails.core.DefaultGrailsApplication
import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import grails.util.GrailsWebUtil
import grails.web.servlet.context.GrailsWebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import grails.gorm.transactions.Rollback
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Integration
@Rollback
class AccessControlFilterTests extends Specification {

    def filterInterceptor = new HandlerInterceptor() {
        @Override
        boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            def user = request.getAttribute('username')
            if(user.toString().contains('Admin'))
                return true
            return false
        }

        @Override
        void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        }

        @Override
        void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        }
    }
    def grailsApplication
    def grailsWebRequest

    def request(Map params, controllerName, actionName) {
        //grailsApplication = new DefaultGrailsApplication()
//        def x = grailsApplication.mainContext
        grailsWebRequest = GrailsWebMockUtil.bindMockWebRequest(grailsApplication.mainContext)
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
        if (grailsWebRequest.session.isAdmin) {
            assert true == result
        } else {
            assert false == result
        }
    }

    // Category Access Control Tests
    void testCategoryCreate() { expect: doTest(null, "category", "create") }
    void testCategoryCreateMe() { expect: doTest(null, "category", "createme") }
    void testCategoryDelete() { expect: doTest(null, "category", "delete") }
    void testCategoryEdit() { expect: doTest(null, "category", "edit") }
    void testCategoryEditMe() { expect: doTest(null, "category", "editme") }
    void testCategoryIndex() { expect: doTest(null, "category", "index") }
    void testCategoryList() { expect: doTest(null, "category", "list") }
    void testCategorySave() { expect: doTest(null, "category", "save") }
    void testCategorySaveMe() { expect: doTest(null, "category", "saveme") }
    void testCategoryShow() { expect: doTest(null, "category", "show") }
    void testCategorySoftDelete() { expect: doTest(null, "category", "softDelete") }
    void testCategoryUpdate() { expect: doTest(null, "category", "update") }
    void testCategoryUpdateMe() { expect: doTest(null, "category", "updateme") }

    // Custom Field Definition Access Control Tests
    void testCustomFieldDefinitionCreate() { expect: doTest(null, "customFieldDefinition", "create") }
    void testCustomFieldDefinitionCreateMe() { expect: doTest(null, "customFieldDefinition", "createme") }
    void testCustomFieldDefinitionDelete() { expect: doTest(null, "customFieldDefinition", "delete") }
    void testCustomFieldDefinitionEdit() { expect: doTest(null, "customFieldDefinition", "edit") }
    void testCustomFieldDefinitionEditMe() { expect: doTest(null, "customFieldDefinition", "editme") }
    void testCustomFieldDefinitionIndex() { expect: doTest(null, "customFieldDefinition", "index") }
    void testCustomFieldDefinitionList() { expect: doTest(null, "customFieldDefinition", "list") }
    void testCustomFieldDefinitionSave() { expect: doTest(null, "customFieldDefinition", "save") }
    void testCustomFieldDefinitionSaveMe() { expect: doTest(null, "customFieldDefinition", "saveme") }
    void testCustomFieldDefinitionShow() { expect: doTest(null, "customFieldDefinition", "show") }
    void testCustomFieldDefinitionSoftDelete() { expect: doTest(null, "customFieldDefinition", "softDelete") }
    void testCustomFieldDefinitionUpdate() { expect: doTest(null, "customFieldDefinition", "update") }
    void testCustomFieldDefinitionUpdateMe() { expect: doTest(null, "customFieldDefinition", "updateme") }

    // Rejection Justification Access Control Tests
    void testRejectionJustificationCreate() { expect: doTest(null, "rejectionJustification", "create") }
    void testRejectionJustificationCreateMe() { expect: doTest(null, "rejectionJustification", "createme") }
    void testRejectionJustificationDelete() { expect: doTest(null, "rejectionJustification", "delete") }
    void testRejectionJustificationEdit() { expect: doTest(null, "rejectionJustification", "edit") }
    void testRejectionJustificationEditMe() { expect: doTest(null, "rejectionJustification", "editme") }
    void testRejectionJustificationIndex() { expect: doTest(null, "rejectionJustification", "index") }
    void testRejectionJustificationList() { expect: doTest(null, "rejectionJustification", "list") }
    void testRejectionJustificationSave() { expect: doTest(null, "rejectionJustification", "save") }
    void testRejectionJustificationSaveMe() { expect: doTest(null, "rejectionJustification", "saveme") }
    void testRejectionJustificationShow() { expect: doTest(null, "rejectionJustification", "show") }
    void testRejectionJustificationSoftDelete() { expect: doTest(null, "rejectionJustification", "softDelete") }
    void testRejectionJustificationUpdate() { expect: doTest(null, "rejectionJustification", "update") }
    void testRejectionJustificationUpdateMe() { expect: doTest(null, "rejectionJustification", "updateme") }

    // Service Item Access Control Tests
    void testServiceItemAdminList() { expect: doTest(null, "serviceItem", "adminList") }
    void testServiceItemAdminView() { expect: doTest(null, "serviceItem", "adminView") }

    // Snoop Access Control Tests
    void testSnoopIndex() { expect: doTest(null, "snoop", "index") }

    // State Access Control Tests
    void testStateCreate() { expect: doTest(null, "state", "create") }
    void testStateCreateMe() { expect: doTest(null, "state", "createme") }
    void testStateDelete() { expect: doTest(null, "state", "delete") }
    void testStateEdit() { expect: doTest(null, "state", "edit") }
    void testStateEditMe() { expect: doTest(null, "state", "editme") }
    void testStateIndex() { expect: doTest(null, "state", "index") }
    void testStateList() { expect: doTest(null, "state", "list") }
    void testStateSave() { expect: doTest(null, "state", "save") }
    void testStateSaveMe() { expect: doTest(null, "state", "saveme") }
    void testStateShow() { expect: doTest(null, "state", "show") }
    void testStateSoftDelete() { expect: doTest(null, "state", "softDelete") }
    void testStateUpdate() { expect: doTest(null, "state", "update") }
    void testStateUpdateMe() { expect: doTest(null, "state", "updateme") }

    // Text Access Control Tests
    void testTextCreate() { expect: doTest(null, "text", "create") }
    void testTextCreateMe() { expect: doTest(null, "text", "createme") }
    void testTextDelete() { expect: doTest(null, "text", "delete") }
    void testTextEdit() { expect: doTest(null, "text", "edit") }
    void testTextEditMe() { expect: doTest(null, "text", "editme") }
    void testTextIndex() { expect: doTest(null, "text", "index") }
    void testTextList() { expect: doTest(null, "text", "list") }
    void testTextSave() { expect: doTest(null, "text", "save") }
    void testTextSaveMe() { expect: doTest(null, "text", "saveme") }
    void testTextShow() { expect: doTest(null, "text", "show") }
    void testTextSoftDelete() { expect: doTest(null, "text", "softDelete") }
    void testTextUpdate() { expect: doTest(null, "text", "update") }
    void testTextUpdateMe() { expect: doTest(null, "text", "updateme") }

    // Types Access Control Tests
    void testTypesCreate() { expect: doTest(null, "types", "create") }
    void testTypesCreateMe() { expect: doTest(null, "types", "createme") }
    void testTypesDelete() { expect: doTest(null, "types", "delete") }
    void testTypesEdit() { expect: doTest(null, "types", "edit") }
    void testTypesEditMe() { expect: doTest(null, "types", "editme") }
    void testTypesIndex() { expect: doTest(null, "types", "index") }
    void testTypesList() { expect: doTest(null, "types", "list") }
    void testTypesSave() { expect: doTest(null, "types", "save") }
    void testTypesSaveMe() { expect: doTest(null, "types", "saveme") }
    void testTypesShow() { expect: doTest(null, "types", "show") }
    void testTypesSoftDelete() { expect: doTest(null, "types", "softDelete") }
    void testTypesUpdate() { expect: doTest(null, "types", "update") }
    void testTypesUpdateMe() { expect: doTest(null, "types", "updateme") }
}
