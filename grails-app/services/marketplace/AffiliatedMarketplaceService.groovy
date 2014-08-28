package marketplace

import ozone.marketplace.enums.ImageType;

import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest
import org.springframework.validation.FieldError
import ozone.utils.Utils
import org.apache.commons.lang.StringUtils
import org.springframework.transaction.annotation.Transactional
import ozone.marketplace.enums.MarketplaceApplicationSetting

class AffiliatedMarketplaceService {

    def imagesService

    def marketplaceApplicationConfigurationService

    /***
     *
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    def listAsJSON(def params) {
        log.debug "listAsJSON: params = ${params}"
        // Set up Default params if necessary
        if (params != null) {
            if (!params.start) params.start = "0"
            if (!params.limit) params.limit = "25"
            if (!params.sort) params.sort = "name"
            if (!params.dir) params.dir = "asc"
        }

        def ampList = AffiliatedMarketplace.createCriteria().list([max: params.limit, offset: params.start]) {
            if (params.sort) {
                order(params.sort, params.dir?.toLowerCase())
            }
			if(params.active){
				eq("active", 1)
			}
        }
        return [ampList: ampList, totalCount: ampList.size()]
    }

    /***
     *
     * @param params
     * @param request
     * @return
     */
    @Transactional
    def buildItemFromParams(def params, request) {
        def affiliatedMarketplace = new AffiliatedMarketplace(params)
        checkAndUpdateAmpServerIconUploadFromRequest(affiliatedMarketplace, request)
        return affiliatedMarketplace
    }

    /***
     *
     * @param affiliatedMarketplace
     * @param request
     */
    @Transactional
    public void checkAndUpdateAmpServerIconUploadFromRequest(affiliatedMarketplace, request) {
        if (request instanceof DefaultMultipartHttpServletRequest) {
            def ampMaxImageSize = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.AMP_IMAGE_MAX_SIZE)
            Long maxFileSize = Long.valueOf(ampMaxImageSize)
            BigDecimal fsMB = BigDecimal.valueOf(Utils.roundTwoDecimals((maxFileSize / imagesService.ONE_MB_SIZE))).setScale(2);
            def validationExceptionMsg = "'Affiliated Marketplace Server Image Icon' size must not exceed ${maxFileSize} bytes (${fsMB} MB)!"
            FieldError fe = new FieldError('affiliatedMarketplace', 'icon', null,
                false, (String[]) ["affiliatedMarketplace.icon.size.invalid"], (Object[]) ["${maxFileSize}"],
                validationExceptionMsg)
            def uploadResultMap = imagesService.performUpload('iconFile', maxFileSize, validationExceptionMsg,
                fe, request, AffiliatedMarketplace, "icon", ImageType.MARKETPLACE_APP, false)
            if (uploadResultMap.img != null) {
                if (affiliatedMarketplace.icon?.id) {
                    def currIconId = affiliatedMarketplace.icon.id
                    affiliatedMarketplace.icon = null
                    Images.get(currIconId)?.delete(flush: true)
                }
                affiliatedMarketplace.icon = uploadResultMap.img
                affiliatedMarketplace.save(flush: true)
            }
        }
    }

    @Transactional
    def save(affiliatedMarketplace, request) {
        checkAndUpdateAmpServerIconUploadFromRequest(affiliatedMarketplace, request)
        affiliatedMarketplace?.save(failOnError: true, flush: true)
    }

    @Transactional
    def delete(affiliatedMarketplace) {
        affiliatedMarketplace?.delete(failOnError: true, flush: true)
    }

    /***
     *
     * @param params
     * @param request
     * @return
     */
    @Transactional(readOnly = true)
    def getItemFromParams(def params, request) {
        def affiliatedMarketplace = AffiliatedMarketplace.get(params.id)
        def isNewVersion = false
        def paramAction = params.action
        if ((affiliatedMarketplace != null) && (StringUtils.isNotEmpty(paramAction)) && (paramAction.toLowerCase().equals("save"))) {
            if (params.version) {
                def currVersion = params.version.toLong()
                if (affiliatedMarketplace.version != currVersion) {
                    isNewVersion = true
                }
            }
        }
        return [affiliatedMarketplace: affiliatedMarketplace, isNewVersion: isNewVersion]
    }

    @Transactional(readOnly = true)
    def getMarketplaceIconImage(def params) {
        log.debug "getMarketplaceIconImage: params = ${params}"

        def contextPathForImages = "${params.contextPath}/images/get"
        def url = null
        def contentType = null
        def imageSize = null
        def imageId = null

        if ((!params.isDefault) && (params.iconId)) {
            def image = Images.get(params.iconId)
            if (image) {
                url = "${contextPathForImages}/${image.id}"
                contentType = image.contentType
                imageSize = image.imageSize
                imageId = image.id
            }
        }

        if ((params.isDefault) || (!url)) {
            url = "${params.contextPath}/images/partner_store.png"
        }

        return [
            url: url,
            contentType: contentType,
            imageSize: imageSize,
            imageId: imageId
        ]
    }
}
