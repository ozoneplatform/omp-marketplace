package marketplace

import ozone.utils.Utils

class IntentDirection extends AuditStamped implements Serializable {

    //TODO: This class and its associated service appears to be unused

    String description
    String title
    String uuid

    static List<String> DIRECTION_TITLES = ["None", "Send", "Receive"]

    static searchable = {
        root false
        title index: 'analyzed'
        only = ['title']
    }

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID();
        }
    }

    static constraints = {
        title(blank: false, unique: true, inList: this.DIRECTION_TITLES)
        description(nullable: true, maxSize: 250)
        uuid(nullable: true, unique: true)
    }

}
