package marketplace.dataexchange

import grails.testing.gorm.DataTest
import marketplace.Category
import marketplace.ImportStatus
import org.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.CategoryImporter
import spock.lang.Specification

import java.text.SimpleDateFormat

class CategoryImporterTest extends Specification implements DataTest{
    def categoryImporter
    def dateFormat

    void setup() {
//        FakeAuditTrailHelper.install()

        mockDomain(Category)
        categoryImporter = new CategoryImporter()
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    void testImportNewCategory() {
        when:
        def json = categoryToImport()
        ImportStatus stats = new ImportStatus()
        then:
        0 == Category.count()

        when:
        categoryImporter.importFromJSON(json, stats.categories)
        then:
        0 == stats.categories.failed
        1 == stats.categories.created
        0 == stats.categories.updated
        0 == stats.categories.notUpdated

        1 == Category.count()
        json.title == Category.findByUuid(json.uuid).title
        json.description == Category.findByUuid(json.uuid).description
    }

    void testImportUpdatedCategory() {
        when:
        existingCategory().save()
        def json = categoryToImport()
        json.editedDate = "2025-01-31T01:00:00"
        ImportStatus stats = new ImportStatus()
        then:
        1 == Category.count()

        when:
        categoryImporter.importFromJSON(json, stats.categories)
        then:
        0 == stats.categories.failed
        0 == stats.categories.created
        1 == stats.categories.updated
        0 == stats.categories.notUpdated
        1 == Category.count()
        json.title == Category.findByUuid(json.uuid).title
        json.description == Category.findByUuid(json.uuid).description
    }

    void testDoNotImportOldCategory() {
        when:
        Category oldCat = existingCategory()
        oldCat.editedDate = dateFormat.parse("2010-01-31")
        oldCat.save()
        def json = categoryToImport()
        ImportStatus stats = new ImportStatus()
        then:
        1 == Category.count()

        when:
        categoryImporter.importFromJSON(json, stats.categories)
        then:
        0 == stats.categories.failed
        0 == stats.categories.created
        0 == stats.categories.updated
        1 == stats.categories.notUpdated

        1 == Category.count()
        existingCategory().title == Category.findByUuid(json.uuid).title
        existingCategory().description == Category.findByUuid(json.uuid).description
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
