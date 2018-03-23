package marketplace

import grails.core.GrailsApplication

import marketplace.configuration.MarketplaceApplicationConfigurationService
import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.context.ApplicationContext
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest

import org.apache.logging.log4j.core.util.Loader

import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.marketplace.enums.ImageType;
import ozone.utils.Utils
import grails.gorm.transactions.Transactional


public class ImagesService {

    public static final Long ONE_MB_SIZE = 1024 * 1024

    def commonImagesLoc = '/themes/common/images'

    GrailsApplication grailsApplication
    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    @Transactional(readOnly = true)
    def get(def params) {
        try {
            if (params.id) {
                def img = Images.get(Long.parseLong(params.id))
                if (img) {
                    return img
                }
            }
        } catch (NumberFormatException nfe) {
            String message = ExceptionUtils.getRootCauseMessage(nfe)
            log.error("Error occurred trying to retrieve image. ${message}", nfe)
        }
        return null
    }


    @Transactional
    def performUpload(def fileInputName, def maxFileSize, validationExceptionMsg, fieldError, request, beanTarget, def beanTargetName, imageType, isDefault) {
        def allowFileUpload = this.marketplaceApplicationConfigurationService.is(MarketplaceApplicationSetting.ALLOW_IMAGE_UPLOAD)
        if (allowFileUpload) {
            def img = null
            def f = null
            if (request instanceof DefaultMultipartHttpServletRequest) {
                f = request.getFile(fileInputName)
                if (f && !f.empty) {
                    def bytes = f.getBytes()
                    Long imageSize = f.getSize()
                    if (imageSize > maxFileSize) {
                        BeanPropertyBindingResult bpr = new BeanPropertyBindingResult(beanTarget, beanTargetName)
                        bpr.addError((FieldError) fieldError)
                        log.error "Error occurred trying to upload new Image for beanTarget: ${beanTarget}.${beanTargetName} with error: ${bpr}"
                        def validationException = new grails.validation.ValidationException(validationExceptionMsg, bpr)
                        BigDecimal fsMB = BigDecimal.valueOf(Utils.roundTwoDecimals((imageSize / ONE_MB_SIZE))).setScale(2);
                        validationException.setFullMessage("${validationExceptionMsg} Current image file size is ${imageSize} bytes (${fsMB} MB).");
                        throw validationException
                    }
                    if (bytes.length > 0) {
                        img = new Images(contentType: f.getContentType(), bytes: bytes, imageSize: imageSize.doubleValue(), isDefault: isDefault, type: imageType)
                        img.save(flush: true)
                    }
                }
            }
            return [status: "Success", img: img, file: f]
        } else {
            return [status: "Failure", msg: "Administrator has disabled image file uploads."]
        }
    }


    @Transactional(readOnly = true)
    def getImageByCriteria(imageType, isDefault) {
        def images = Images.withCriteria
                {
                    and {
                        eq('type', imageType)
                        eq('isDefault', isDefault)
                    }
                }
        return images
    }

    /**
     *  Creates (but does not save) a new Images object from a file
     *
     */
    Images loadImageFromFile(ImageType imageType, String imageFilePath, boolean isDefault = false) {
        Images image = null

//        File imageFile = grailsApplication.mainContext.getResource(imageFilePath).file
        File imageFile = grailsApplication.mainContext.getResource('classpath:' +imageFilePath).file
        if (imageFile && imageFile.exists()) {
            def validContentTypes = ['image/png', 'image/jpeg', 'image/gif']
            if (!validContentTypes.contains(Utils.getMimeType(imageFilePath))) {
                log.info "Image file '${imageFilePath}' content type must be one of: ${validContentTypes}"
            } else {
                image = new Images(bytes: imageFile.getBytes(),
                        imageSize: imageFile.length(),
                        contentType: Utils.getMimeType(imageFile.getName()),
                        isDefault: isDefault,
                        type: imageType)
            }
        } else {
            log.info "Unable to load image ${imageFilePath}"
        }

        return image
    }

    def loadImageFileFromSystem(imageFileName) {
        File f
        URL url = Loader.getResource("${imageFileName}")
        if (url) {
            String fileName = url.toString()
            if (fileName.startsWith('file:/')) {

                try {
                    f = new File(url.toURI());
                } catch (URISyntaxException e) {
                    f = new File(url.getPath());
                }
            }
        } else {
            f = new File("${imageFileName}")
        }
        return f
    }

}
