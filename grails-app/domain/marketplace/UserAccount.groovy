package marketplace

@gorm.AuditStamp
class UserAccount implements Serializable {

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
