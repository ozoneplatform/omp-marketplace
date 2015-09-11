package marketplace

import org.apache.commons.lang.exception.ExceptionUtils
import grails.util.Holders
import org.hibernate.FlushMode
import ozone.marketplace.domain.ValidationException

import grails.converters.JSON

import javax.servlet.http.Cookie

class ProfileController extends BaseMarketplaceRestController {

    def accountService
    def profileService
    def sessionFactory
    def messageSource
    def auditLogListener
    def config = Holders.config
    def themeService

    static allowedMethods = [edit: 'POST', editBio: 'POST', update: 'POST', updateSessionAccessAlertShown: 'POST', onBeforeLogout: 'POST']

    def index = { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 10
        if (params.sort == null) {
            params.sort = 'username'
        }
        [profiles: profileService.list(params), total: profileService.total()]
    }

    def showAccessAlert = {
        def sessVal = session
        if (Boolean.valueOf(session.allowShowAccessAlert)) {//This makes sure action is not directly accessed
            session.redirectToShowAlertViewInitiated = false
            session.allowShowAccessAlert = false
            render(template: '/showAccessAlertView', model: [showAccessAlert: session.showAccessAlert,
                accessAlertMsg: "${session.accessAlertMsg}", initialUrl: "${session.initalRequestURL}"])
        } else {
            redirect(controller: "serviceItem", action: "index")
        }
    }

    def updateSessionAccessAlertShown = {
        session.accessAlertShown = Boolean.valueOf(params.accessAlertShown)
        if (session.initalRequestURL?.equals(params.redirectUrl)) {
            session.initalRequestURL = null
        }
        if (params.actionStatus != null) {
            response.status = Integer.valueOf(params.actionStatus)
            render "Response returned with status ${params.actionStatus}"
        }
    }

    def onBeforeLogout = {
        def profile = profileService.get([id: session.profileID])
        Cookie cookie = new Cookie("${request.contextPath?.replaceAll('/', '')}_theme_css", null)
        cookie.setPath(request.getContextPath())
        cookie.setMaxAge(-1)
        cookie.setValue("${request.contextPath}/" + themeService.getCurrentTheme().css.toString())
        response.addCookie(cookie)
        response.status = 200
        render "onBeforeLogout for '<b>${profile.username}</b>' complete"
    }

    def detail = {
        def profile = profileService.get(params)
        if (!profile) {
            flash.message = "specificObjectNotFound"
            flash.args = ['profile', params.id]
            redirect(action: list)
        } else {
            [profile: profile]
        }
    }

    def edit = {
        def profile = profileService.get(params)
        if (!profile) {
            flash.message = "specificObjectNotFound"
            flash.args = ['profile', params.id]
            redirect(action: list)
        } else {
            [profile: profile]
        }
    }

    def update = {
        log.info "update for ${params}"
        try {
            def sessionParams = ['isAdmin': session.isAdmin, 'username': session.username]
            def profile = profileService.getAllowableUser(params.id, sessionParams)

            if (profile) {
                if (params.version) {
                    def version = params.version.toLong()
                    if (profile.version > version) {
                        profile.errors.rejectValue("version", "types.optimistic.locking.failure", messageSource.getMessage('objectStale', null, null))
                        render(view: 'edit', model: [profile: profile])
                        return
                    }
                }
                profile.displayName = params.displayName
                profile.email = params.email
                profile.bio = params.bio
                profile.scrubCR()

                if (!profile.hasErrors() && profile.save(flush: true)) {
                    // If the currently logged in user changes their displayName, then we
                    // need to update the fullname in the session.
                    if (session.username.equals(profile.username)) {
                        def newFullname = profile.displayName ?: profile.username
                        if (!newFullname.equals(session.fullname)) {
                            log.debug "updating fullname in session from ${session.fullname} to ${newFullname}"
                            session.fullname = newFullname
                        }
                    }
                    profileService.reindexServiceItemsByUser(profile.id)

                    flash.message = "update.success"
                    flash.args = [profile.prettyPrint()]
                    redirect(action: detail, id: profile.id)
                } else {
                    render(view: 'edit', model: [profile: profile])
                }


            } else {
                flash.message = "specificObjectNotFound"
                flash.args = ['profile', params.id]
                redirect(action: edit, id: params.id)
            }

        } catch (AccessControlException e) {
            log.info e?.message
            render e?.message

        } catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            def sessionParams = ['isAdmin': session.isAdmin, 'username': session.username]
            def profile = profileService.getAllowableUser(params.id, sessionParams)
            log.error("Error occurred trying to update ${profile}. ${message}", e)
            flash.message = "update.failure"
            flash.args = [profile.prettyPrint(), message]
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            render(view: 'edit', model: [profile: profile])
        }
    }

    def search = {
        def model
        def data = []
        def total = 0
        if (!params.max) params.max = 20
        try {
            model = profileService.search(params)
            data = model['data']
            total = model['total']
            response.status = 200

            render ([
                success: ((data == null) ? false : true),
                totalCount: total,
                records: data.collect { profile ->
                    [
                        id: profile?.id,
                        display: profile?.display(),
                        username: profile?.username,
                        fmtdisplay: params.query ? Helper.highlight(profile?.display()?.encodeAsHTML(), params.query) : profile?.display(),
                        fmtusername: params.query ? Helper.highlight(profile?.username?.encodeAsHTML(), params.query) : profile?.username
                    ]
                }
            ] as JSON)

        } catch (Exception e) {
            handleException(e, "search")
            return
        }
    }
}
