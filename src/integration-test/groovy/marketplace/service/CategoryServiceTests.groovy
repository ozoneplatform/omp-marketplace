package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.Category
import marketplace.CategoryService
import marketplace.domain.builders.DomainBuilderMixin

import ozone.marketplace.domain.ValidationException


@Integration
@Rollback
class CategoryServiceTests extends Specification implements DomainBuilderMixin {

    CategoryService categoryService

    void testListByDate() {
        given:
        def category1 = $category { title = 'AAA' }

        sleep(2000)

        def category2 = $category { title = 'BBB' }

        when:
        def results1 = categoryService.list([editedSinceDate: category1.editedDate])
        def results2 = categoryService.list([editedSinceDate: category2.editedDate])

        then:
        results1?.size() == 2
        results2?.size() == 1
    }


    void testDelete() {
        given:
        def category = $category { title = 'Category 1' }

        when:
        categoryService.delete(category.id)

        then:
        Category.get(category.id) == null
    }

    void testDeleteNotFound() {
        when:
        categoryService.delete(427358374529952)

        then:
        def ex = thrown(ValidationException)
        ex.message == 'objectNotFound'
    }

}
