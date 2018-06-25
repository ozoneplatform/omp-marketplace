package marketplace.domain.builders

import marketplace.Intent
import marketplace.IntentAction
import marketplace.IntentDataType


class IntentBuilder implements Builder<Intent> {

    IntentAction action
    IntentDataType dataType
    Boolean send
    Boolean receive

    Intent build() {
        new Intent([action  : action,
                    dataType: dataType,
                    send    : send,
                    receive : receive])
    }

}
