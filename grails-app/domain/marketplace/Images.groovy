package marketplace

import org.grails.web.json.JSONObject

import ozone.marketplace.enums.ImageType


class Images extends AuditStamped implements Serializable, ToJSON {

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

    @Override
    JSONObject asJSON() {
        new JSONObject([id         : id,
                        isDefault  : isDefault,
                        imageSize  : imageSize,
                        contentType: contentType,
                        type       : type])
    }

}
