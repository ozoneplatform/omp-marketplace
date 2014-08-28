package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import ozone.marketplace.domain.ValidationException;

@TestMixin(IntegrationTestMixin)
class CategoryServiceTests {

	def categoryService
	def serviceItemService

	void testBogusDelete() {
		boolean veThrown = false
		try{
			categoryService.delete(427358374529952)
		}
		catch(ValidationException ve){
			veThrown = true
			assert 'objectNotFound' == ve.message
		}
		assert true == veThrown
	}

    void testListByDate() {
        Category category = new Category(title: 'AAA').save()
        //assert null != Category.get(category.id)
        //assert null != category.createdDate
        def firstDate = category.createdDate

        try {
            Thread.currentThread().sleep(3000)
        } catch (Exception e) {}

        category = new Category(title: 'BBB').save()
        //assert null != Category.get(category.id)
        //assert null != category.createdDate
        def secondDate = category.createdDate

        try {
            Thread.currentThread().sleep(1000)
        } catch (Exception e) {}

        def params = ['editedSinceDate':firstDate]
        def r = categoryService.list(params)
        assert null != r
        assert 2 == r.size()

        params = ['editedSinceDate':secondDate]
        r = categoryService.list(params)
        assert null != r
        assert 1 == r.size()
    }


	void testDelete() {
		Category category = new Category(title: 'cat 1')
		category.save(flush:true, failOnError: true)
		assert null != Category.get(category.id)
		categoryService.delete(category.id)
		Category categoryAfterSoftDelete = Category.get(category.id)
		assert null == categoryAfterSoftDelete
	}

    void testAuditTrail() {
        def startTime = new Date()
        Thread.sleep(1000)

        Category obj = new Category(title: 'cat 1')
        obj.save(flush:true, failOnError: true)

        obj = Category.get(obj.id)
        assert null != obj
        def createdDate1 = obj.createdDate
        def editedDate1 = obj.editedDate

        assert obj.metaClass.hasProperty(obj, "createdBy")
        assert obj.metaClass.hasProperty(obj, "createdDate")
        assert obj.metaClass.hasProperty(obj, "editedBy")
        assert obj.metaClass.hasProperty(obj, "editedDate")

        assert obj.createdDate.after(startTime)
        assert obj.editedDate.after(startTime)

        obj.title = 'bozo5'
        Thread.sleep(1000)
        obj.save(flush:true)
        obj = Category.get(obj.id)
        assert obj.createdDate.equals(createdDate1)
        assert obj.editedDate.after(editedDate1)
    }

    //Check that getAllCategories returns a collection
    void testGetAllCategories(){
        assert true == categoryService.getAllCategories() instanceof Collection
    }
}
