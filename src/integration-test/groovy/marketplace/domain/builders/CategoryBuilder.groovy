package marketplace.domain.builders

import marketplace.Category


class CategoryBuilder implements Builder<Category> {

    String title
    String description
    String uuid

    Category build() {
        if (!uuid) uuid = UUID.randomUUID().toString()

        new Category([uuid       : uuid,
                      title      : title,
                      description: description])
    }

}
