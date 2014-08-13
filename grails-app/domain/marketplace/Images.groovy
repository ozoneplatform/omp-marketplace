package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject
import gorm.AuditStamp
import ozone.marketplace.enums.ImageType

@AuditStamp
class Images implements Serializable {

    static bindableProperties = ['contentType', 'type', 'isDefault', 'bytes', 'imageSize']
    static modifiableReferenceProperties = []

    String contentType
    ImageType type
    boolean isDefault = false
    byte[] bytes
    Double imageSize

    static constraints = {
        bytes(nullable: false, maxSize: 10 * 1024 * 1024)
        imageSize(nullable: true)
        contentType(nullable: true)
        type(nullable: false)
    }

    static mapping = {
        cache true
    }

    def asJSON() {
        return new JSONObject(
            id: id,
            isDefault: isDefault,
            imageSize: imageSize,
            contentType: contentType,
            type: type
        )
    }
}
