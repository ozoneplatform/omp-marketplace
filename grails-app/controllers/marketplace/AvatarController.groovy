package marketplace

class AvatarController {

    def index = {}

    def create = {}

    def pic = {
        def avatar = Avatar.get(params.id)
        if (!avatar || !avatar.pic || !avatar.contentType) {
            response.sendError(404)
            return;
        }
        response.setContentType(avatar.contentType)
        response.setContentLength(avatar.pic.length)
        response.outputStream << new ByteArrayInputStream(avatar.pic)
    }

    def upload = {

        def avatar

        if (!params.id) {
            avatar = new Avatar()
        } else {
            avatar = Avatar.get(params.id)
        }

        // Get the avatar file from the multi-part request
        def f = request.getFile('avatar')

        // List of OK mime-types
        def okcontents = ['image/png', 'image/jpeg', 'image/gif']
        if (!okcontents.contains(f.getContentType())) {
            flash.message = "Avatar must be one of: ${okcontents}"
            render(view: 'create', model: [avatar: avatar])
            return;
        }

        // Save the image and mime type
        avatar.pic = f.getBytes()
        avatar.contentType = f.getContentType()
        log.info("File uploaded: " + avatar.contentType)

        // Validation works, will check if the image is too big
        if (!avatar.save()) {
            render(view: 'create', model: [avatar: avatar])
            return;
        }
        flash.message = "Avatar (${avatar.contentType}, ${avatar.pic.size()} bytes) uploaded."
        redirect(action: 'pic', id: avatar.id)
        return
    }

}

