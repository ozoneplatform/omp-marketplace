package marketplace

import ozone.marketplace.domain.ValidationException

class CategoryControllerTests extends MarketplaceAdminControllerTests  {

    def categoryServiceMock

    CategoryControllerTests() {
        super(CategoryController.class)
    }

    @Override
    void setUp() {
        super.setUp()

        categoryServiceMock = mockFor(CategoryService)
        controller.categoryService =  categoryServiceMock.createMock()
        categoryServiceMock.demand.with {
            list(0..1) { params -> Category.list() }
            countTypes(0..1) { -> Category.count() }
        }
        controller.metaClass.postUpdateDomain = { -> }
    }

    @Override
    void testDelete() {
        categoryServiceMock.demand.delete(0..1) { id -> }
        super.testDelete()
    }

    @Override
    void testDeleteFailure() {
        categoryServiceMock.demand.delete(0..1) { id -> throw new ValidationException(message: validationExceptionMessage) }
        super.testDeleteFailure()
    }

}
