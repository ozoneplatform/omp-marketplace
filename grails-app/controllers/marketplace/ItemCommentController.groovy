package marketplace

import javax.servlet.http.HttpServletResponse

import org.hibernate.FlushMode
import org.hibernate.SessionFactory

import ozone.marketplace.domain.ValidationException


class ItemCommentController extends BaseMarketplaceRestController {

    static allowedMethods = [edit             : ['POST', 'PUT'],
                             saveItemComment  : 'POST',
                             deleteItemComment: ['POST', 'DELETE']]

    ServiceItemService serviceItemService

    ItemCommentService itemCommentService

    SearchableService searchableService

    ProfileService profileService

    SessionFactory sessionFactory

    def getUserComments() {
        String username = session.username
        boolean isAdmin = session.isAdmin ?: false
        String accessType = session.accessType

        params.author = params.id //Due to screwed up URL mapping

        def result = itemCommentService.getUserComments(params, username, isAdmin, accessType)

        def rows = result.collect { ItemComment comment -> toJsonBrief(it) }

        def response = [success   : true,
                        totalCount: getTotalCount(result),
                        rows      : rows]

        renderResult(response, -1, HttpServletResponse.SC_OK)
    }

    def commentsByServiceItem() {
        def result = itemCommentService.getServiceItemComments(params)

        def rows = result.collect { ItemComment comment -> toJson(comment) }

        def response = [success   : true,
                        totalCount: getTotalCount(result),
                        rows      : rows]

        renderResult(response, -1, HttpServletResponse.SC_OK)
    }

    def saveItemComment() {
        String username = params.useSystemUser ? 'System' : session.username
        boolean isAdmin = (params.useSystemUser ? true : session.isAdmin) ?: false

        if (!isFeedbackValid(params.commentTextInput as String,
                             params.int('newUserRating'),
                             params.int('currUserRating'))) {
            throw new ValidationException(message(code: "sic.validationException.saveItemComment.renderText") as String)
        }

        ItemComment comment = retryOnLockingFailure {
            itemCommentService.saveItemComment(params, username, isAdmin)
        }

        def response = [success: true,
                        data   : toJson(comment)]

        renderResult(response, -1, HttpServletResponse.SC_OK)
    }

    def edit() {
        forward(controller: "itemComment", action: "saveItemComment")
    }

    def deleteItemComment() {
        String username = session.username
        boolean isAdmin = session.isAdmin ?: false

        ServiceItem serviceItem = retryOnLockingFailure {
            itemCommentService.findAndDeleteItemComment(params, username, isAdmin)
        }

        def response = [success: true,
                        data   : [serviceItemRateStats: toJson(serviceItem)]]

        renderResult(response, -1, HttpServletResponse.SC_OK)
    }

    private static boolean isFeedbackValid(String commentTextInput, Integer newRating, Integer currRating) {
        return (commentTextInput != null && !commentTextInput.trim().isEmpty()) ||
                (newRating != null && isValidRating(newRating)) ||
                (currRating != null && isValidRating(currRating))
    }

    private static boolean isValidRating(int rating) {
        rating > 0 && rating <= 5
    }

    private static final Map<String, String> VALIDATION_MESSAGE_CODES =
            ["saveItemComment": "sic.validationException.saveItemComment.renderText"]

    private static final Map<String, String> EXCEPTION_MESSAGE_CODES =
            ["saveItemComment"  : "sic.exception.saveItemComment.renderText",
             "deleteItemComment": "sic.exception.deleteItemComment.renderText"]

    def handleException(Exception ex) {
        // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
        def session = sessionFactory.currentSession
        session.setFlushMode(FlushMode.MANUAL)

        String messageText = getExceptionMessage(ex)

        log.error(messageText, ex)

        def response = [success: false,
                        message: messageText]

        renderResult(response, -1, HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    }

    def handleAccessControlException(AccessControlException ex) {
        def response = [success: false,
                        message: ex.message]

        renderResult(response, -1, HttpServletResponse.SC_UNAUTHORIZED)
    }

    def handleObjectNotFoundException(ObjectNotFoundException ex) {
        def result = "${ex.message}"

        def response = [success: false,
                        message: result]

        renderResult(response, -1, HttpServletResponse.SC_NOT_FOUND)
    }

    def handleValidationException(ValidationException ex) {
        def response = [success   : false,
                        totalCount: 0,
                        msg       : getValidationErrorMessage(ex)]

        renderResult(response, -1, HttpServletResponse.SC_BAD_REQUEST)
    }

    private String getExceptionMessage(Exception ex) {
        String messageCode = EXCEPTION_MESSAGE_CODES.get(actionName)

        if (messageCode != null) {
            return message(code: messageCode, args: ["${ex.message}"])
        }

        ex.getMessage() ?: ex.class.name
    }

    private String getValidationErrorMessage(ValidationException ex) {
        if (ex.message) return ex.message

        def messageCode = VALIDATION_MESSAGE_CODES.get(actionName)

        messageCode != null ? message(code: messageCode, args: []) : "Validation exception"
    }

    private static Map toJson(ItemComment comment) {
        [id                  : comment.id,
         userId              : comment.author.id,
         username            : comment.author.username,
         displayName         : comment.author.display(),
         text                : comment.text,
         date                : comment.editedDate,
         userRate            : comment.rate,
         serviceItemRateStats: toJson(comment.serviceItem)]
    }

    private static Map toJsonBrief(ItemComment comment) {
        [id      : comment.id,
         itemId  : comment.serviceItem.id,
         date    : comment.createdDate,
         userRate: comment.rate,
         name    : comment.serviceItem.title,
         comment : comment.text]
    }

    private static Map toJson(ServiceItem serviceItem) {
        [avgRate   : serviceItem.avgRate,
         totalRate5: serviceItem.totalRate5,
         totalRate4: serviceItem.totalRate4,
         totalRate3: serviceItem.totalRate3,
         totalRate2: serviceItem.totalRate2,
         totalRate1: serviceItem.totalRate1,
         totalVotes: serviceItem.totalVotes]
    }


}
