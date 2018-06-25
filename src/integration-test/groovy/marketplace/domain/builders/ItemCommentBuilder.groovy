package marketplace.domain.builders

import marketplace.ItemComment
import marketplace.Profile
import marketplace.ServiceItem


class ItemCommentBuilder implements Builder<ItemComment> {

    String text
    Float rate

    Profile author
    ServiceItem serviceItem

    ItemComment build() {
        new ItemComment([text       : text,
                         rate       : rate,
                         author     : author,
                         serviceItem: serviceItem])
    }

}
