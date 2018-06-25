package marketplace

import ozone.marketplace.domain.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import grails.converters.JSON

class CustomFieldDefinitionController extends MarketplaceAdminController {

    CustomFieldDefinitionService customFieldDefinitionService

    TypesService typesService

    protected retrieveDomain() { return CustomFieldDefinition.get(params.id) }

    protected String getDomainName() {
        return "customFieldDefinitionInstance"
    }

    protected String getObjectName() { return "custom field definition" }

    protected createDomain() {
        log.debug("createDomain: ${params}")
        def styleType = params.styleType ? Constants.CustomFieldDefinitionStyleType.valueOf(params.styleType) : null;
        if (!styleType) {
            log.warn "Could not find CustomFieldDefinition for styleType: '${params.styleType}'"
            return new CustomFieldDefinition(params)
        }
        return styleType.newFieldDefinition(params)
    }

    protected retrieveDomainCount() { return customFieldDefinitionService.countTypes() }


    protected retrieveDomainList() {
        try {
            if (params.sort == null) {
                params.sort = 'name'
            }
            log.debug("retrieveDomainList: ${params}")
            def domainList = customFieldDefinitionService.list(params)
            log.debug(domainList.toString())
            domainList.each { log.debug "${it.name} ${it.label} ${it.styleType} ${it.getClass()}" }
            return domainList
        }
        // The reason for this catch block is explained in
        // http://jira.codehaus.org/browse/GRAILS-2735
        catch (org.hibernate.exception.SQLGrammarException e) {
            params.sort = null
            flash['message'] = "Operation not supported by database"
            return customFieldDefinitionService.list(params)
        }
    }

    // TODO: is this still used?
    def getDropDownCFDFieldValues = {
        def result, json
        try {
            result = customFieldDefinitionService.getDropDownCFDFieldValues(params)

            json = [
                success: true,
                totalCount: result.totalCount,
                rows: result.each { ddfv ->
                    [
                        id: ddfv.id,
                        displayText: ddfv.displayText,
                        isEnabled: ddfv.isEnabled,
                        createdDate: AdminObjectFormatter.standardDateDisplay(ddfv.createdDate),
                        customFieldDefintionId: ddfv.customFieldDefinition?.id
                    ]
                }
            ]
        }
        catch (ValidationException ve) {
            json = [
                success: false,
                totalCount: 0,
                msg: ve.getMessage()
            ]
        }
        render (json as JSON)
    }

    def create = {
        def map = null
        if (session.modelMap != null) {
            session.modelMap[getDomainName()].errors = session.modelDomainErrors
            map = session.modelMap
            session.modelMap = null
            session.modelDomainErrors = null
        } else {
            map = super.createme()
            map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
        }
        return map
    }

    def edit = {
        def map = null
        if (session.modelMap != null) {
            session.modelMap[getDomainName()].errors = session.modelDomainErrors
            map = session.modelMap
            session.modelMap = null
            session.modelDomainErrors = null
        } else {
            map = super.editme()
            map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
        }
        return map
    }

    def update = {
        log.debug "update - params = ${params}"

        def domain = retrieveDomain()
        domain?.types?.clear()
        if (domain) {
            if (params.version) {
                def version = params.version.toLong()
                if (domain.version > version) {
                    domain.errors.rejectValue("version", "types.optimistic.locking.failure", messageSource.getMessage('objectStale', null, null))
                    def map = getDomainMap(domain)
                    map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
                    render(view: 'edit', model: map)
                    return
                }
            }
            domain.properties = params
            domain.scrubCR()
            try {
                // customFieldDefinitionService.
                if (!domain.hasErrors() && customFieldDefinitionService.update(domain, params)) {
                    flash.message = "update.success"
                    flash.args = [domain.toString()]
                    redirect(action: "show", id: domain.id)
                    return
                } else {
                    domain.errors.each { log.warn it }
                    def map = getDomainMap(domain)
                    map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
                    map.put('failureMessage', "update.failure")
                    map.put('failureArgs', ["custom field definition ${domain.toString()}", message])
                    session.modelMap = map
                    session.modelDomainErrors = domain.errors
                    redirect(action: "edit", id: domain.id)
                    return
                }
            }
            catch (Exception e) {
                String message = ExceptionUtils.getRootCauseMessage(e)
                if (e instanceof grails.validation.ValidationException) {
                    log.info("Error occurred trying to update ${domain}. ${message}")
                    message = ''

                } else if (e instanceof ozone.marketplace.domain.ValidationException) {
                    log.info("Error occurred trying to update ${domain}. ${message}")
                    // Get the message without a prefix of 'ValidationException:'
                    message = e.getMessage()
                } else {
                    log.warn("Error occurred trying to update ${domain}. ${message}", e)
                }

                // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
                def hibSession = sessionFactory.currentSession
                hibSession.setFlushMode(FlushMode.MANUAL)

                def map = getDomainMap(domain)
                map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
                map.put('failureMessage', "update.failure")
                map.put('failureArgs', ["custom field definition ${domain.toString()}", message])
                session.modelMap = map
                session.modelDomainErrors = domain.errors
                redirect(action: "edit", id: domain.id)
                return
            }
        } else {
            flash.message = "objectNotFound"
            flash.args = [params.id]
            redirect(action: "edit", id: params.id)
            return
        }
    }

    def delete() {
        try{
            response.reset()
            def cfd = retrieveDomain()
            customFieldDefinitionService.delete(cfd)
            flash.message = "delete.success"
            flash.args = [cfd.name]
            redirect(action:'list')
            return
        }
        catch (ValidationException ve) {
            flash.message = ve.message
            flash.args = ve.args
            redirect(action: "show", id: params.id)
            return
        }
    }

    def save = {
        log.debug 'save'
        def domain = createDomain()
        domain.scrubCR()
        try {
            log.debug "about to save - ${domain.hasErrors()}"
            if (!domain.hasErrors() && customFieldDefinitionService.save(domain)) {
                flash.message = "create.success"
                flash.args = [domain.toString()]
                redirect(action: "show", id: domain.id)
                return
            } else {
                String message = domain.errors.allErrors.join(" ")
                domain.errors.each { log.warn it }
                def map = getDomainMap(domain)
                map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
                map.put('failureMessage', "create.failure")
                map.put('failureArgs', ["custom field definition ${domain.toString()}", message])
                session.modelMap = map
                session.modelDomainErrors = domain.errors
                redirect(action: "create")
                return
            }
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            if (e instanceof grails.validation.ValidationException) {
                log.info("Error occurred trying to update ${domain}. ${message}")
                message = ''

            } else if (e instanceof ozone.marketplace.domain.ValidationException) {

                log.info("Error occurred trying to update ${domain}. ${message}")
                // Get the message without a prefix of 'ValidationException:'
                message = e.getMessage()
            } else {
                log.warn("Error occurred trying to update ${domain}. ${message}", e)
            }
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def hibSession = sessionFactory.currentSession
            hibSession.setFlushMode(org.hibernate.FlushMode.MANUAL)

            def map = getDomainMap(domain)
            map.put('typesList', typesService.list([sort: 'title', order: 'asc']))
            map.put('failureMessage', "create.failure")
            map.put('failureArgs', ["custom field definition ${domain.toString()}", message])
            session.modelMap = map
            session.modelDomainErrors = domain.errors
            redirect(action: "create")
            return
        }
    }

}
