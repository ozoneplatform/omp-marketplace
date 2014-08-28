package marketplace

import ozone.marketplace.domain.ValidationException
import ozone.marketplace.enums.ImageType;

import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import org.springframework.validation.FieldError

import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest
import org.apache.commons.lang.StringUtils
import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.utils.Utils
import grails.plugin.cache.Cacheable
import org.springframework.transaction.annotation.Transactional

class TypesService extends MarketplaceService {

    def sessionFactory
    def imagesService
    def marketplaceApplicationConfigurationService
    def grailsApplication

    @Cacheable("typeIconImageCache")
    @Transactional(readOnly = true)
    def getTypeIconImage(def params, usePublic = false) {

        def contextPathForImages = "${params.contextPath}/images/get"
        if (usePublic) {
            contextPathForImages = "${params.contextPath}/public/images/get"
        }
        def types = Types.findById(params.id)
        def imageMap = [:]
        def image = types?.image

        if (image) {
            imageMap.with {
                url = "${contextPathForImages}/${image.id}"
                contentType = image.contentType
                imageSize = image.imageSize
                id = image.id
            }
        } else if (types) {
            def defaults = Constants.DEFAULT_TYPE_IMAGE_FILES
            def filename = defaults."${types.title}" ?: defaults.default
            // Defaults for filenames already include the complete path except context
            imageMap.url = "${params.contextPath}/${filename}"
        }

        return imageMap
    }

    @Transactional(readOnly = true)
    Images getDefaultTypesImage(params) {
        String typesTitle = params.title ?: "default"
        String typesImageFilePath = Constants.DEFAULT_TYPE_IMAGE_FILES[typesTitle] ?: Constants.DEFAULT_TYPE_IMAGE_FILES["default"]

        imagesService.loadImageFromFile(ImageType.TYPES, typesImageFilePath, true)
    }

    @Transactional
    def deleteTypeImage(def type) {
        def currDomainImage = type.image
        type.image = null
        type.save()
        currDomainImage.delete()
    }

    @Transactional
    def getItemFromParams(def params, request) {
        def types = Types.get(params.id)
        def isNewVersion = false
        def paramAction = params._action_Update
        if ((types != null) && (StringUtils.isNotEmpty(paramAction)) && (paramAction.toLowerCase().equals("save"))) {
            checkAndUpdateTypeImageUploadFromRequest(types, request)
            if (params.version) {
                def currVersion = params.version.toLong()
                if (types.version != currVersion) {
                    isNewVersion = true
                }
            }
        }
        return [domain: types, isNewVersion: isNewVersion]
    }

    @Transactional
    def buildItemFromParams(def params, request) {
        def types = new Types(params)
        checkAndUpdateTypeImageUploadFromRequest(types, request)
        return types
    }

    private void checkAndUpdateTypeImageUploadFromRequest(types, request) {
        if (request instanceof DefaultMultipartHttpServletRequest) {
            def typeMaxImageSize = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.TYPE_IMAGE_MAX_SIZE)
            Long maxFileSize = Long.valueOf(typeMaxImageSize)
            BigDecimal fsMB = BigDecimal.valueOf(Utils.roundTwoDecimals((maxFileSize / imagesService.ONE_MB_SIZE))).setScale(2);
            def validationExceptionMsg = "'Type Image Icon' size must not exceed ${maxFileSize} bytes (${fsMB} MB)!"
            FieldError fe = new FieldError('types', 'image', null,
                false, (String[]) ["types.image.size.invalid"], (Object[]) ["${maxFileSize}"],
                validationExceptionMsg)
            def uploadResultMap = imagesService.performUpload('typeImage', maxFileSize, validationExceptionMsg, fe,
                request, Types, "image", ImageType.TYPES, false)
            if (uploadResultMap.img != null) {
                if (types.image?.id) {
                    def currImageId = types.image.id
                    types.image = null
                    Images.get(currImageId)?.delete(flush: true)
                }
                types.image = uploadResultMap.img
                types.save(flush: true)
            }
        }
    }

    @Transactional(readOnly = true)
    def list(def params) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = Types.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = Types.list(params)
        }
        return results
    }

    @Transactional(readOnly = true)
    def get(def params) {
        return Types.get(params.id)
    }

    @Transactional(readOnly = true)
    def countTypes() {
        return Types.count()
    }

    /**
     *
     * There are five cases to consider:
     * 1. Type with no listing associated with it. Just delete it
     * 2. Type with active listing associated with it. Throw error, don't delete it
     * 3. Type with soft deleted listing associated with it. Throw error, don't delete it
     * 4. Type with active Custom Field associated with it. Throw error, don't delete it
     * 5. Type that is immutable. Throw error, don't delete it
     */
    @Transactional
    def delete(def id) {
        Types type
        try {
            type = Types.get(id)
            if (!type) {
                throw new ValidationException(message: "objectNotFound", args: [id])
            }
            if (type.isPermanent) {
                throw new ValidationException(message: "delete.failure.isImmutable", args: [type.title])
            }
            def criteria = ServiceItem.createCriteria()
            def cnt = criteria.get {
                projections {
                    count('id')
                }
                types {
                    eq('id', new Long(id))
                }
            }
            if (cnt > 0) {
                throw new ValidationException(message: "delete.failure.serviceItem.exists", args: [type.title])
            }
            def cfdCriteria = CustomFieldDefinition.createCriteria()
            def cfdCnt = cfdCriteria.get {
                projections {
                    count('id')
                }
                types {
                    eq('id', new Long(id))
                }
            }
            if (cfdCnt > 0) {
                throw new ValidationException(message: "delete.failure.customFieldDefinition.exists", args: [type.toString()])
            }
            type.delete(flush: true)
        }
        catch (ValidationException ve) {
            throw ve
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred trying to delete ${type}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [type?.toString(), message])
        }
    }

    @Transactional(readOnly = true)
    def getAllTypes() {
        Types.list(sort: 'title', order: 'asc')
    }

    @Transactional
    def createRequired() {

        log.info "Loading types..."

        def typesInConfig = grailsApplication.config.marketplace.metadata.types

        if (typesInConfig) {
            typesInConfig.each { Map typeInfo ->
                String title = typeInfo.title
                if (!Types.findByTitle(title)) {
                    new Types(title: title, description: typeInfo.description,
                        ozoneAware: typeInfo.ozoneAware, hasLaunchUrl: typeInfo.hasLaunchUrl, hasIcons: typeInfo.hasIcons, isPermanent: typeInfo.isPermanent).save()
                }
            }

            log.info "Types loaded."
        } else {
            log.error "Types metadata info was not found in the loaded config files."
        }
    }
}
