package marketplace


class Avatar extends AuditStamped implements Serializable {

    byte[] pic
    String contentType
    boolean isDefault = false

    static constraints = {
        pic(nullable: true, maxSize: 10 * 1024 * 1024)
        contentType(nullable: true)
    }

    static mapping = {
        cache true
    }

}
