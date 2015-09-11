package marketplace

import grails.util.Holders
import org.hibernate.FlushMode
import org.hibernate.StaleObjectStateException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import ozone.marketplace.domain.ValidationException
import org.codehaus.groovy.grails.web.json.JSONObject
import javax.servlet.http.HttpServletResponse

class ItemCommentController extends BaseMarketplaceRestController {

    def config = Holders.config
    def serviceItemService
    def itemCommentService
    def searchableService
    def profileService
    def sessionFactory

    static allowedMethods = [
        edit: ['POST', 'PUT'],
        saveItemComment: 'POST',
        deleteItemComment: ['POST', 'DELETE']
    ]

    /**
     * Returns a JSON structure containing the user's comments for all ServiceItems.
     * If an exception occurs returns JSON with status success false
     */
    def getUserComments = {
        def result
        def json
        params.author = params.id //Due to screwed up URL mapping
        try {
            result = itemCommentService.getUserComments(params, session.username, session.isAdmin, session.accessType)

            def rows = []
            result.each { ic ->
                rows << [
                    id: ic.id,
                    itemId: ic.serviceItem.id,
                    date: ic.createdDate,
                    userRate: ic.rate,
                    name: ic.serviceItem.title,
                    comment: ic.text
                ]
            }

            json = [
                success: true,
                totalCount: result.totalCount,
                rows: rows
            ]
        }
        catch (ValidationException ve) {
            log.warn('getUserComments', ve)
            json = [
                success: false,
                totalCount: 0,
                msg: ve.getMessage()
            ]
        }

        renderResult(json, -1, HttpServletResponse.SC_OK)
    }

    /**
     * saveComment
     */
    def saveItemComment = {
        log.debug "Saving itemComment using params: ${params}"

        def ic, json, responseCode = HttpServletResponse.SC_OK

        try {
            if (!isFeedbackValid(params)) {
                def errorMsg = message(code: "sic.validationException.saveItemComment.renderText")
                log.error(errorMsg)
                throw new ValidationException(message: errorMsg)
            }

            try {
                ic = itemCommentService.saveItemComment(params)
            } catch (e) {
                if ([StaleObjectStateException, HibernateOptimisticLockingFailureException].any { e in it }) {
                    // Retry the operation
                    log.error("Exception thrown: ${e.class.name}, retrying save itemComment for serviceItem")
                    itemCommentService.saveItemComment(params)
                    log.error("Successfully saved itemComment for serviceItem")
                } else {
                    log.error("Exception thrown: ${e.class.name}, not retrying save")
                    throw e
                }
            }

            json = [
                success: true,
                data: [
                    id: ic.id,
                    userId: ic.author.id,
                    username: ic.author.username,
                    displayName: ic.author.display(),
                    text: ic.text,
                    date: ic.editedDate,
                    userRate: ic.rate,
                    serviceItemRateStats: [
                        avgRate: ic.serviceItem.avgRate,
                        totalRate5: ic.serviceItem.totalRate5,
                        totalRate4: ic.serviceItem.totalRate4,
                        totalRate3: ic.serviceItem.totalRate3,
                        totalRate2: ic.serviceItem.totalRate2,
						totalRate1: ic.serviceItem.totalRate1,
						totalVotes: ic.serviceItem.totalVotes
                    ]
                ]
            ]
        }
        catch (AccessControlException e) {
            log.info e?.message
            json = [
                success: false,
                message: e?.message
            ]
            responseCode = HttpServletResponse.SC_UNAUTHORIZED
        }
        catch (ObjectNotFoundException noe) {
            def result = "${noe.message}"
            log.warn result

            json = [
                success: false,
                message: result
            ]
            responseCode = HttpServletResponse.SC_NOT_FOUND
        }
        catch (ValidationException e) {
            json = [
                success: false,
                message: e.message ?: message(code: "sic.validationException.saveItemComment.renderText", args: [])
            ]
            responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
        catch (Exception e) {
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            def result = message(code: "sic.exception.saveItemComment.renderText", args: ["${e.message}"])
            log.error result

            json = [
                success: false,
                message: result
            ]
            responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
        renderResult(json, -1, responseCode)
    }

    private boolean isFeedbackValid(params) {
        return params.commentTextInput?.trim() as boolean ||
            (params.newUserRating && (params.newUserRating as int) > 0) ||
            (params.currUserRating && (params.currUserRating as int) > 0)
    }

    /**
     * edit - EDIT/UPDATE ITEM COMMENT
     */
    def edit = {
        forward(controller: "itemComment", action: "saveItemComment", params: params)
    }

    /**
     * deleteComment -
     */
    def deleteItemComment = {
        def json, responseCode = HttpServletResponse.SC_OK

        ServiceItem serviceItem

        log.debug "In deleteItemComment : CommentId: ${params.itemCommentId}"
        try {
            try {
                serviceItem = itemCommentService.findAndDeleteItemComment(params)
            } catch (e) {
                if ([StaleObjectStateException, HibernateOptimisticLockingFailureException].any { e in it }) {
                    // Retry the operation
                    log.error("Exception thrown: ${e.class.name}, retrying delete itemComment ${params.itemCommentId} for serviceItem")
                    itemCommentService.findAndDeleteItemComment(params)
                    log.error("Successfully deleted itemComment ${params.itemCommentId} for serviceItem")
                } else {
                    log.error("Exception thrown: ${e.class.name}, not retrying delete itemComment ${params.itemCommentId} for serviceItem")
                    throw e
                }
            }

            json = [
                success: true,
                data: [
                    serviceItemRateStats: new JSONObject(
                        avgRate: serviceItem.avgRate,
                        totalRate5: serviceItem.totalRate5,
                        totalRate4: serviceItem.totalRate4,
                        totalRate3: serviceItem.totalRate3,
                        totalRate2: serviceItem.totalRate2,
                        totalRate1: serviceItem.totalRate1,
                        totalVotes: serviceItem.totalVotes
                    )
                ]
            ]
        }
        catch (AccessControlException e) {
            log.info e?.message
            json = [
                success: false,
                message: e?.message
            ]
            responseCode = HttpServletResponse.SC_UNAUTHORIZED
        }
        catch (ObjectNotFoundException noe) {
            def result = "${noe.message}"
            log.warn result

            json = [
                success: false,
                message: result
            ]
            responseCode = HttpServletResponse.SC_NOT_FOUND
        }
        catch (Exception e) {
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            def result = message(code: "sic.exception.deleteItemComment.renderText", args: ["${e.message}"])
            log.error result

            json = [
                success: false,
                message: result
            ]
            responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }

        renderResult(json, -1, responseCode)
    }

    def commentsByServiceItem = {
        def result, json, responseCode
        try {
            result = itemCommentService.getServiceItemComments(params)
            responseCode = HttpServletResponse.SC_OK
            json = [
                success: true,
                totalCount: result.totalCount,
                rows: result.collect { ic ->
                    [
                        id: ic.id,
                        userId: ic.author.id,
                        username: ic.author.username,
                        displayName: ic.author.display(),
                        text: ic.text,
                    date: ic.editedDate,
                        userRate: ic.rate,
                        serviceItemRateStats: [
                            avgRate: ic.serviceItem.avgRate,
                            totalRate5: ic.serviceItem.totalRate5,
                            totalRate4: ic.serviceItem.totalRate4,
                            totalRate3: ic.serviceItem.totalRate3,
                            totalRate2: ic.serviceItem.totalRate2,
	                        totalRate1: ic.serviceItem.totalRate1,
	                        totalVotes: ic.serviceItem.totalVotes
                        ]
                    ]
                }
            ]
        }
        catch (ValidationException ve) {
            responseCode = HttpServletResponse.SC_BAD_REQUEST
            json = [
                success: false,
                totalCount: 0,
                msg: ve.getMessage()
            ]
        }
        renderResult(json, -1, responseCode)
    }

}
