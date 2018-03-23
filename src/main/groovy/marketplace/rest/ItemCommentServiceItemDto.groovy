package marketplace.rest

import org.grails.web.json.JSONObject

import marketplace.ItemComment
import marketplace.ToJSON

/**
 * This class faciliates a JSON representation of ItemComments that includes information
 * about the associated ServiceItem*/
class ItemCommentServiceItemDto implements ToJSON {

    private ItemComment comment

    ItemCommentServiceItemDto(ItemComment comment) {
        this.comment = comment
    }

    @Override
    JSONObject asJSON() {
        if (comment == null) return null

        new JSONObject(comment.asJSON() + [serviceItem: comment.serviceItem.asJSONMinimum()])
    }

    boolean equals(other) {
        this.comment.equals(other)
    }

    int hashCode() {
        this.comment.hashCode()
    }
}
