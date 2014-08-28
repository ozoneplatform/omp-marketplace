package marketplace

import org.apache.commons.lang.exception.ExceptionUtils
import grails.plugin.cache.CacheEvict

import grails.converters.JSON

class TypesController extends MarketplaceAdminWithDeleteController {

    def typesService
    def imagesService
    def searchableService

    protected retrieveDomain() {
        def resultMap = typesService.getItemFromParams(params, request)
        if (resultMap.isNewVersion) {
            params.version = "${resultMap.domain.version}"
        }
        return resultMap.domain
    }

    protected String getDomainName() { return "typesInstance" }

    protected String getObjectName() { return "types" }

    protected createDomain() {
        def returnValue = typesService.buildItemFromParams(params, request)
        return returnValue
    }

    protected createEmptyDomain() {
        def returnValue = new Types(params)
        return returnValue
    }

    @Override
    protected deleteDomain() {
        typesService.delete(params.id)
    }

    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        return typesService.list(params)
    }

    protected retrieveDomainCount() { return typesService.countTypes() }

    void postUpdateDomain() {
        def id = params.id
        searchableService.reindexServiceItems({ types { eq('id', new Long(id)) } })
        flash.message = "reindex.update"
    }

    def getTypeImageIconUrl = {
        def defUrl = typesService.getTypeIconImage(params)?.url
        render ([
            success: true,
            url: defUrl
        ] as JSON)
    }

    def defaultTypesIconImage = {
        def currTypeIconImageMap = typesService.getTypeIconImage([contextPath: request.contextPath])
        forward(controller: "images", action: "get", id: currTypeIconImageMap?.id)
    }

    def editTypeImage = {
        def domain = retrieveDomain()
        def template = params.template
        log.info "renderTemplate: ${template}"
        render(template: "/types/${template}", model: [typesInstance: domain])
    }

    def imageDelete = {
        def domain = retrieveDomain()
        def jsonResult
        if (domain) {
            if (domain.image) {
                try {
                    typesService.deleteTypeImage(domain)
                    response.status = 200
                    jsonResult = ["${params.domainInstanceName}": domain]
                } catch (Exception e) {
                    response.status = 500
                    String message = ExceptionUtils.getRootCauseMessage(e)
                    String errorMsg = "Error occurred trying to delete Types Image. ${message}"
                    log.error(errorMsg, e)
                    jsonResult = ["${params.domainInstanceName}": domain, msg: "${errorMsg}"]
                }
            } else {
                flash.message = "Types Image Object Not Found"
                flash.args = [params.id]
                response.status = 500
                jsonResult = ["${params.domainInstanceName}": domain, msg: flash.message]
            }
        } else {
            flash.message = "Types Object Not Found"
            flash.args = [params.id]
            response.status = 500
            jsonResult = ["${params.domainInstanceName}": domain, msg: flash.message]
        }
        render jsonResult as JSON
    }

    def performDoList = {
        def typesList = typesService.getAllTypes()
        render ([
            success: typesList == null,
            totalCount: (typesList == null) ? 0 : typesList.size(),
            records: typesList.collect { type ->
                [
                    id: type?.id,
                    title: type?.title,
                    description: type?.description
                ]
            }
        ]) as JSON
    }

    def getTypesList = {
        def typesList = typesService.getAllTypes()

        render typesList as JSON
    }

    @CacheEvict(["typeIconImageCache", "serviceItemBadgeCache", "serviceItemIconCache", "serviceItemIconImageCache"])
    def update(){
        Types type = Types.get(params.id)
        if (!type) {
            flash.message = "objectNotFound"
            flash.args = [params.id]
            redirect(action: 'edit', id: params.id)
        } else if (!type.isPermanent) {
            log.debug "TypesController.update: params = ${params}"
            super.update()
        } else {
            flash.message = "update.failure.isImmutable"
            flash.args = [type.title]
            redirect(action: 'edit', id: type.id)
        }
    }

    def image = {
        Types types = Types.findById(params.id)
        if (!types) {
            response.sendError(404)
            return;
        }

        Images typesImage = types.image ?: typesService.getDefaultTypesImage([title: types.title])
        if (!typesImage || !typesImage.bytes || !typesImage.contentType) {
            response.sendError(404)
            return;
        }

        response.setContentType(typesImage.contentType)
        response.setContentLength(typesImage.bytes.length)
        response.outputStream << new ByteArrayInputStream(typesImage.bytes)
    }
}
