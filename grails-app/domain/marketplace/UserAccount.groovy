package marketplace


class UserAccount extends AuditStamped implements Serializable {

    String username
    Date lastLogin

    static constraints = {
        username(blank: false, nullable: false, unique: true, maxSize: 250)
        lastLogin(nullable: true)
    }

    static mapping = {}


    String display() {
        return username
    }

    String toString() {
        return display()
    }

    String prettyPrint() {
        toString()
    }
}
