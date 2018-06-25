package marketplace

import ozone.marketplace.enums.DefinedDefaultTypes;
import ozone.utils.Utils


class DefaultImages extends AuditStamped implements Serializable {

    Images image
    DefinedDefaultTypes definedDefaultType

    static belongsTo = Images

    static mapping = {
        cache true
    }

    static constraints = {
        definedDefaultType(unique: true, nullable: false)
        image(nullable: false)
    }

    DefaultImages() {}

    DefaultImages(definedDefaultType, imageFile, imageType) {
        log.info "Adding Default image for '${definedDefaultType}'"
        this.definedDefaultType = definedDefaultType
        addNewImage(imageFile, imageType)
    }

    private void addNewImage(imageFile, imageType) {
        this.image = new Images()
        this.image.bytes = imageFile.getBytes()
        this.image.imageSize = imageFile.length()
        this.image.contentType = Utils.getMimeType(imageFile.getName())
        this.image.isDefault = true
        this.image.type = imageType
        this.image.save()
    }
}
