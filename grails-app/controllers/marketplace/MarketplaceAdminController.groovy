package marketplace

import org.hibernate.SessionFactory

import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode

abstract class MarketplaceAdminController extends BaseMarketplaceRestController {

    SessionFactory sessionFactory

    def messageSource

    abstract protected String getDomainName()

    abstract protected retrieveDomain()

    abstract protected retrieveDomainList()

    abstract protected retrieveDomainCount()

    abstract protected createDomain()

    abstract protected getObjectName()

    protected createEmptyDomain() {
        return null
    }

    protected void postUpdateDomain() {

    }

    def index = {
        redirect(action: 'list', params: params)
        return
    }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete: 'POST', save: 'POST', saveme: 'POST', update: 'POST', updateme: 'POST']

    protected getDomainMap(domain) {
        def map = [:]
        map.put(getDomainName(), domain)
        return map
    }

    def list = {
        if (!params.max) params.max = 10
        def map = [:]
        map.put(getDomainName() + "List", retrieveDomainList())
        map.put(getDomainName() + "Total", retrieveDomainCount())
        return map;
    }

    def show = {
        def domain = retrieveDomain()
        if (!domain) {
            flash.message = "specificObjectNotFound"
            flash.args = [getObjectName(), params.id]
            redirect(action: 'list')
            return
        } else {
            return getDomainMap(domain)
        }
    }

    def editme = {
        def domain = retrieveDomain()
        if (!domain) {
            flash.message = "objectNotFound"
            flash.args = [params.id]
            redirect(action: 'list')
            return
        } else {
            return getDomainMap(domain)
        }
    }

    def edit = {
        editme()
    }

    def handleUpdateExceptionMsg = { e, params, domain ->
        def ref = Helper.generateLogReference()
        log.warn "Error saving item. ${ref}"
        flash.message = "update.failure"
        if (e instanceof grails.validation.ValidationException) {
            log.warn e.getMessage()
            flash.args = [": Error saving object", "Reference = ${ref}"]
        } else {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error("Error occurred trying to update ${domain}. ${message}", e)
            flash.args = [domain?.prettyPrint(), "Reference = ${ref}"]
        }
        // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
        domain?.clearErrors()
        def session = sessionFactory.currentSession
        session.setFlushMode(FlushMode.MANUAL)
        redirect(action: 'edit', id: params.id)
        return
    }

    def update = {
        def domain
        try {
            log.debug "update: params = ${params}"
            domain = retrieveDomain()
            if (domain) {
                domain.properties = params
                domain.scrubCR()
                log.debug 'about to save'
                domain.save(failOnError: true, flush: true)
                postUpdateDomain()
                if (!flash.message) flash.message = "update.success"
                flash.args = [domain.prettyPrint()]
                redirect(action: 'show', id: domain.id)
                return
            } else {
                flash.message = "objectNotFound"
                flash.args = [params.id]
                redirect(action: 'edit', id: params.id)
                return
            }
        }
        catch (grails.validation.ValidationException ve) {
            handleUpdateExceptionMsg(ve, params, domain)
        } catch (Exception e) {
e.printStackTrace()
            handleUpdateExceptionMsg(e, params, domain)
        }
    }

    def createme = {
        log.debug 'createme'
        def domain = createDomain()
        return getDomainMap(domain)
    }

    def create = {
        createme()
    }

    def save = {
        log.debug "save: params = ${params}"
        def domain

        try {
            domain = createDomain()
            domain.scrubCR()
            log.debug 'about to save'
            domain.withTransaction { domain.save(failOnError: true, flush: true) }
            flash.message = "create.success"
            flash.args = [domain.prettyPrint()]
            redirect(action: 'show', id: domain.id)
            return
        }
        catch (grails.validation.ValidationException ve) {
            def ref = Helper.generateLogReference()
            log.warn "Error saving item. ${ref}"
            log.warn ve.getMessage()
            flash.message = "create.failure"
            domain.clearErrors()
            render(view: 'create', model: getDomainMap(domain))
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            def ref = Helper.generateLogReference()
            log.warn "Error saving item. ${ref}"
            log.error("Error occurred trying to create ${domain}. ${message}", e)
            flash.message = "create.failure"
            flash.args = [domain.prettyPrint(), "Reference = ${ref}"]
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            render(view: 'create', model: getDomainMap(domain))
        }
    }

    def getListAsJSON() {
        if (!params.max) params.max = 100
        def model
        def total
        try {
            model = retrieveDomainList()
            total = retrieveDomainCount()
        }
        catch (Exception e) {
            handleException(e, "getListAsJSON")
            return
        }
        renderResult(model, total, 200)
    }

    def getItemAsJSON() {
        def model
        try {
            model = retrieveDomain()
            if (!model) {
                handleException(new Exception(getObjectName() + " with id " + params.id + " does not exist"), "getItemAsJSON", 404)
                return
            }
        } catch (Exception e) {
            handleException(e, "getItemAsJSON")
            return
        }
        renderResult(model, 1, 200)
    }
}
