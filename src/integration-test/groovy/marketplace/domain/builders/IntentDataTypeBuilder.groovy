package marketplace.domain.builders

import marketplace.IntentDataType


class IntentDataTypeBuilder implements Builder<IntentDataType> {

    String uuid
    String title
    String description

    IntentDataType build() {
        if (!uuid) uuid = UUID.randomUUID().toString()

        new IntentDataType([uuid       : uuid,
                            title      : title,
                            description: description])
    }

}
