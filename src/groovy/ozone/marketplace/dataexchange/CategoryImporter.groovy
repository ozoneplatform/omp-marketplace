package ozone.marketplace.dataexchange

import marketplace.Category
/**
 * To change this template use File | Settings | File Templates.
 */
class CategoryImporter extends AbstractImporter{

    public CategoryImporter() {
        super("category")
    }

    def findExistingObject(def json) {
        return Category.findByUuid(json.uuid)
    }

    def createNewObject() {
        return new Category()
    }
}
