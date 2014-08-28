package marketplace

import grails.test.mixin.TestFor
import ozone.utils.TestUtil
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(Category)
class CategoryTests {
    def category

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(Category)
        category = new Category()
    }

    void testBlankConstraints(){
        TestUtil.assertPropertyBlank('title',category)
    }

    void testDescriptionRequired(){
        TestUtil.assertPropertyRequired('title',category)
    }

    void testSizeContraints(){
        TestUtil.checkSizeConstraintProperty('title',category, 50)
    	TestUtil.checkSizeConstraintProperty('description',category, 250)
    }

    void testFindDuplicates(){
        def testCategory = new Category(title: "category1", uuid: "1234")
        mockDomain(Category, [testCategory])

        def duplicateUuidCategory = new JSONObject(title: "category2", uuid: "1234")
        assertEquals testCategory.uuid, duplicateUuidCategory.uuid
        assertTrue Category.findDuplicates(duplicateUuidCategory)

        def duplicateTitleCategory = new JSONObject(title: "category1", uuid: "4321")
        assertEquals testCategory.title, duplicateTitleCategory.title
        assertTrue Category.findDuplicates(duplicateTitleCategory)

        def uniqueCategory = new JSONObject(title: "category3", uuid: "4321")
        assertFalse(testCategory.uuid == uniqueCategory.uuid)
        assertFalse(testCategory.title == uniqueCategory.title)
        assertFalse Category.findDuplicates(uniqueCategory)
    }
}
