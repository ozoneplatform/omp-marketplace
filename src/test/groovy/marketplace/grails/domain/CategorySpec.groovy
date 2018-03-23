package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import ozone.utils.TestUtil
import org.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(Category)
class CategorySpec extends Specification  implements DomainConstraintsUnitTest<Category> {
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true }}}
    List<Class> getDomainClasses() {[Category]}

    def catMock
    void setup() {
        //FakeAuditTrailHelper.install()

       // mockForConstraintsTests(Category)
        catMock = Mock(Category)
    }

    void testBlankConstraints(){
        expect:
        propertyValueIsInvalid('title', "", ConstrainedProperty.BLANK_CONSTRAINT)
        propertyValueIsValid("title", "Test Category")
       // TestUtil.assertPropertyBlank('title',category)
    }

    void testDescriptionNotRequired(){
        expect:
        propertyValueIsValid("description", "")
        //TestUtil.assertPropertyRequired('title',category)
    }

    void testSizeContraints(){
        expect:
        propertyHasMaxSize("title", 50)
        propertyHasMaxSize("description", 250)

//        TestUtil.checkSizeConstraintProperty('title',category, 50)
//    	TestUtil.checkSizeConstraintProperty('description',category, 250)
    }

    void testFindDuplicates(){
        when:
        def testCategory = new Category(title: "category1", uuid: "1234")
        //mockDomain(Category, [testCategory])

        then:
        testCategory.validate()
        and:
        testCategory.save()

        and:
        Category.count() == old(Category.count()) + 1

        when:
        def duplicateUuidCategory = new Category(title: "category2", uuid: "1234")

        then:
        !duplicateUuidCategory.validate(['uuid'])
        and:
        !duplicateUuidCategory.save()
        Category.count() == old(Category.count())

        when:
        def duplicateTitleCategory = new JSONObject(title: "category1", uuid: "4321")
        then:
        testCategory.title == duplicateTitleCategory.title
        Category.findDuplicates(duplicateTitleCategory) == true

        when:
        def uniqueCategory = new Category(title: "category3", uuid: "4321")

        then:
        uniqueCategory.validate(['uuid','title'])
        and:
        uniqueCategory.save()
        Category.count() == old(Category.count()) + 1

    }
}
