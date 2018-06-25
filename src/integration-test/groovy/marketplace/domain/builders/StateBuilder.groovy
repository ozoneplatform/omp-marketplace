package marketplace.domain.builders

import marketplace.State


class StateBuilder implements Builder<State> {

    String uuid
    String title
    String description
    Boolean isPublished

    State build() {
        if (!uuid) uuid = UUID.randomUUID().toString()
        if (isPublished == null) isPublished = false

        new State([uuid       : uuid,
                   title      : title,
                   description: description,
                   isPublished: isPublished])
    }

}
