package marketplace


class ImagesController {

    def imagesService

    def get = {
        Images img = imagesService.get(params)
        if (!img || !img.bytes || !img.contentType) {
            response.sendError(404)
            return;
        }
        response.setContentType(img.contentType)
        response.setContentLength(img.bytes.length)
        response.outputStream << new ByteArrayInputStream(img.bytes)
    }


}

