package ozone.marketplace.dataexchange

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import marketplace.Category
import marketplace.ImportStatus
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

import java.text.SimpleDateFormat

@TestMixin(DomainClassUnitTestMixin)
class CategoryImporterTest {
    def categoryImporter
    def dateFormat

    void setUp() {
        FakeAuditTrailHelper.install()

        mockDomain(Category)
        categoryImporter = new CategoryImporter()
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    void testImportNewCategory() {
        def json = categoryToImport()
        ImportStatus stats = new ImportStatus()
        assertEquals 0, Category.count()
        categoryImporter.importFromJSON(json, stats.categories)
        assertEquals 0, stats.categories.failed
        assertEquals 1, stats.categories.created
        assertEquals 0, stats.categories.updated
        assertEquals 0, stats.categories.notUpdated

        assertEquals 1, Category.count()
        assertEquals json.title, Category.findByUuid(json.uuid).title
        assertEquals json.description, Category.findByUuid(json.uuid).description
    }

    void testImportUpdatedCategory() {
        existingCategory().save()
        def json = categoryToImport()
        json.editedDate = "2025-01-31T01:00:00"
        ImportStatus stats = new ImportStatus()
        assertEquals 1, Category.count()
        categoryImporter.importFromJSON(json, stats.categories)
        assertEquals 0, stats.categories.failed
        assertEquals 0, stats.categories.created
        assertEquals 1, stats.categories.updated
        assertEquals 0, stats.categories.notUpdated

        assertEquals 1, Category.count()
        assertEquals json.title, Category.findByUuid(json.uuid).title
        assertEquals json.description, Category.findByUuid(json.uuid).description
    }

    void testDoNotImportOldCategory() {
        Category oldCat = existingCategory()
        oldCat.editedDate = dateFormat.parse("2010-01-31")
        oldCat.save()
        def json = categoryToImport()
        ImportStatus stats = new ImportStatus()
        assertEquals 1, Category.count()
        categoryImporter.importFromJSON(json, stats.categories)
        assertEquals 0, stats.categories.failed
        assertEquals 0, stats.categories.created
        assertEquals 0, stats.categories.updated
        assertEquals 1, stats.categories.notUpdated

        assertEquals 1, Category.count()
        assertEquals existingCategory().title, Category.findByUuid(json.uuid).title
        assertEquals existingCategory().description, Category.findByUuid(json.uuid).description
    }

    private Category existingCategory() {
        return new Category(title: "Old", uuid: "1", editedDate: dateFormat.parse("2000-01-31"))
    }

    private JSONObject categoryToImport() {
        return new JSONObject([
                uuid: "1",
                title: "My Category",
                description: "Imported category",
                editedDate: "2005-01-31T01:00:00"
        ])
    }
}
