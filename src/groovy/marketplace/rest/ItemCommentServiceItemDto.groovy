package marketplace.rest

import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.ItemComment

/**
 * This class faciliates a JSON representation of ItemComments that includes information
 * about the associated ServiceItem
 */
class ItemCommentServiceItemDto {
    private ItemComment comment

    ItemCommentServiceItemDto(ItemComment comment) {
        this.comment = comment
    }

    JSONObject asJSON() {
        this.comment == null ?
            JSONObject.NULL :
            new JSONObject(this.comment.asJSON() + [
                serviceItem: this.comment.serviceItem.asJSONMinimum()
            ])
    }

    boolean equals(other) {
        this.comment.equals(other)
    }

    int hashCode() {
        this.comment.hashCode()
    }
}
