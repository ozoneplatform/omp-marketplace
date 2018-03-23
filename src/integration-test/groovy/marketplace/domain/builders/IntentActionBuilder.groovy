package marketplace.domain.builders

import marketplace.IntentAction


class IntentActionBuilder implements Builder<IntentAction> {

    String uuid
    String title
    String description

    IntentAction build() {
        if (!uuid) uuid = UUID.randomUUID().toString()

        new IntentAction([uuid       : uuid,
                          title      : title,
                          description: description])
    }

}
