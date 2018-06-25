package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


class Intent extends AuditStamped implements Serializable, ToJSON {
    static searchable = {
        root false
        action component: true
        dataType component: true
        send index: 'not_analyzed'
        receive index: 'not_analyzed'
        only = ['send', 'receive', 'dataType', 'action']
    }

    static bindableProperties = ['send', 'receive', 'action', 'dataType']
    static modifiableReferenceProperties = []

    static belongsTo = OwfProperties

    IntentAction action
    IntentDataType dataType
    Boolean send
    Boolean receive

    static constraints = {
        action nullable: false
        dataType nullable: false
    }

    static mapping = {
        cache true
        batchSize 50
        owfProperties joinTable: [
            name: 'owf_properties_intent',
            key: 'intent_id'
        ]
    }

    String toString() {
        if (send && receive) {
            "Sends/Receives: $action -> $dataType"
        } else if (send) {
            "Sends: $action -> $dataType"
        } else {
            "Receives: $action -> $dataType"
        }
    }

    String prettyPrint() {
        toString()
    }

    @Override
    JSONObject asJSON() {
        new JSONObject([id      : id,
                        action  : action.asJSONRef(),
                        dataType: dataType.asJSONRef(),
                        send    : send,
                        receive : receive])
    }

    def bindFromJSON(JSONObject json) {
        [
            "id",
            "action",
            "dataType",
            "send",
            "receive"
        ].each(JS.optStr.curry(json, this))

        [
            "editedDate"
        ].each(JS.optDate.curry(json, this))
    }

    @Override
    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(action?.title)
                .append(dataType?.title)
            .append(send)
            .append(receive)
        def code = builder.toHashCode()
        return code;
    }

    @Override
    boolean equals(Object obj) {

        // Since intents are typically in a lazy loaded collection, the instances could be
        // hibernate proxies, so use the GORM 'instanceOf' method
        Boolean sameType
        try {
            sameType = obj.instanceOf(Intent)
        } catch(MissingMethodException mme) {
            sameType = false
        }

        if (sameType) {
            Intent other = (Intent) obj
            EqualsBuilder builder = new EqualsBuilder()

            builder.append(send, other.send)
                    .append(receive, other.receive)

            if (this.action.title != null && other.action.title != null) {
                builder.append(this.action.title, other.action.title)
            }
            else {
                builder.append(this.action.id, other.action.id)
            }

            if (this.dataType.title != null && other.dataType.title != null) {
                builder.append(this.dataType.title, other.dataType.title)
            }
            else {
                builder.append(this.dataType.id, other.dataType.id)
            }

            return builder.isEquals();
        }
        return false;
    }

}
