package marketplace

import marketplace.rest.ServiceItemActivityInternalService
import org.springframework.beans.factory.annotation.Autowired
import ozone.marketplace.domain.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils
import org.hibernate.FlushMode
import org.springframework.transaction.annotation.Transactional
import ozone.utils.Utils
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder as RCH
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import marketplace.Constants.Action

public class ItemCommentService {

    def sessionFactory
    def profileService
    def serviceItemService

    @Autowired
    ServiceItemActivityInternalService serviceItemActivityInternalService

    def static final serviceItemRules = ['allNoRestrictions':false,
        'allIfApproved':true,
        'userNoRestrictions':true,
        'userIfApproved':true]

    def static final itemCommentRules = ['allNoRestrictions':false,
         'allIfApproved':false,
        'userNoRestrictions':true,
        'userIfApproved':true]

    /**
     * Retrieves all of the comments for a user for all service items.
     * Includes a collection of rows with the creation date, title of the
     * ServiceItem being commented on and the actual comment itself.
     *
     * params:
     *
     *    limit - max number of records to return (for paging)
     *    start - 0 based offset into full result set (for paging)
     *    username - Profile.username field used to retreive user comments
     */
    @Transactional(readOnly = true)
    def getUserComments(def params, def username, def isAdmin, def accessType) {
        try {
            def sortField
            switch (params.sort) {
                case 'name':
                    sortField = 'serviceItem'
                    break
                case 'date':
                    sortField = 'editedDate'
                    break
                default:
                    sortField = "editedDate"
            }

            def items = ItemComment.createCriteria().list(max: params.limit, offset: params.start) {
                author {
                    eq('username', params.author)
                }
                serviceItem {
                    if ((!accessType.equals(Constants.VIEW_ADMIN)) || !isAdmin) {
                        or {
                            ilike('approvalStatus', Constants.APPROVAL_STATUSES["APPROVED"])
                            owners {
                                eq('username', username)
                            }
                        }
                        eq('isHidden', 0)
                    }
                }
                order(sortField, params?.dir?.toLowerCase())
            }
            return items
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred retreiving comments for user ${params.author}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "comment.failure", args: [params.author?.toString(), message])
        }
    }

    @Transactional(readOnly = true)
    def getServiceItemComments(def params) {
        try {
            log.debug "getServiceItemComments: parms = ${params}"
            def sortField
            switch (params.sort) {
                case 'displayName':
                    sortField = 'a.displayName'
                    break
                case 'date':
                    sortField = 'editedDate'
                    break
                default:
                    sortField = "editedDate"
            }
            def sortDir = params.dir ? params.dir.toLowerCase() : "desc"
            def items = ItemComment.createCriteria().list(max: params.limit, offset: params.start) {
                createAlias("author", "a")
                if (params.author) {
                    eq('a.username', params.author)
                }
                eq('serviceItem.id', Long.parseLong(params.id))
                order(sortField, sortDir)
            }
            return items
        }
        catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred retreiving comments for service item ${params.id}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "comment.failure", args: [params.author?.toString(), message])
        }
    }

    @Transactional(readOnly = true)
    def getAllowableComment(def id, def sessionParams, def rules) throws AccessControlException {
        def comment = ItemComment.get(id)
        def isUser = false
        def isAvailable = false
        def isApproved = false
        def matchesRule = false

        if (comment) {
            isUser = (sessionParams?.username == comment?.author?.username)
            isAvailable = !comment?.serviceItem?.isHidden
            isApproved = comment?.serviceItem.statApproved()

            if (rules?.allNoRestrictions && isAvailable) {
                matchesRule = true
            }
            if (sessionParams?.isAdmin) {
                matchesRule = true
            }
            if (rules?.allIfApproved && isAvailable && isApproved) {
                matchesRule = true
            }
            if (rules?.userNoRestrictions && isUser && isAvailable) {
                matchesRule = true
            }
            if (rules?.userIfApproved && isUser && isAvailable && isApproved) {
                matchesRule = true
            }

            if (matchesRule) {
                return comment
            } else {
                throw new AccessControlException('User is not authorized to access this comment');
            }
        }
        return null;
    }

    public HttpSession retrieveHttpSession() {
        return RCH.currentRequestAttributes().getSession()
    }

    @Transactional
    def saveItemComment(def params) {

        def session = params.useSystemUser ? null : retrieveHttpSession()

        def sessionParams = ['isAdmin': params.useSystemUser ? true : session.isAdmin,
            'username': params.useSystemUser ? "System" : session.username]

        def itemCommentToSave
        def si_id = params.serviceItemId?.toLong()
        def rateVal = params.newUserRating

        ChangeDetail changeDetail = null

        if(!params.id){
            itemCommentToSave = new ItemComment()
            def profile = profileService.findByUsername(params.username ? params.username : session.username)
            if (profile) itemCommentToSave.author = profile
            itemCommentToSave.createdDate = new Date()
        } else {
            itemCommentToSave = getAllowableComment(params.id,
                                                    sessionParams,
                                                    itemCommentRules)
            if(!itemCommentToSave){
                throw new ObjectNotFoundException("Cannot locate Allowable itemComment with id: ${params.id}")
            }

            //Is this review being modified by someone who is not the reviewer?
            if(itemCommentToSave.author.username != sessionParams.username && itemCommentToSave.text != params.text) {
                String reviewOwner = itemCommentToSave.author.displayName
                changeDetail = new ChangeDetail(fieldName: "$reviewOwner's Review", oldValue: itemCommentToSave.text, newValue: params.text)
            }

            si_id = itemCommentToSave.serviceItem.id
        }

        def si = serviceItemService.getAllowableItem(si_id,
                                                sessionParams,
                                                serviceItemRules)

        if(!si){
            throw new ObjectNotFoundException("Cannot locate Allowable serviceItem with id: ${si_id}")
        }

        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            itemCommentToSave.text = params.commentTextInput

            if (!itemCommentToSave.id) {
                itemCommentToSave.serviceItem = si
            }

            if (rateVal) {
                performRate(si, itemCommentToSave, rateVal)
            }

            itemCommentToSave.scrubCR()

            if (itemCommentToSave.save()) {
                log.debug "Saving itemComment: ServiceID: ${si.id} / CommentId: ${itemCommentToSave.id}"
                if (!params.id) {//Meaning new itemComment
                    if (!si.totalComments) {
                        si.totalComments = 0
                    }
                    si.totalComments += 1

                    if (!si.itemComments) {
                        si.itemComments = new LinkedHashSet<ItemComment>()
                    }
                    si.itemComments.add(itemCommentToSave)
                }
            } else {
                log.error("Failure Saving itemComment: ServiceID: ${si.id} / CommentId: ${itemCommentToSave.id}")
                itemCommentToSave.errors.each { log.error it }
                throw new ValidationException(fieldErrors: itemCommentToSave.errors)
            }

            if(changeDetail) {
                Profile activityAuthor = profileService.findByUsername(sessionParams.username)
                ServiceItemActivity activity = new ServiceItemActivity(action: Action.REVIEW_EDITED, author: activityAuthor)
                activity.addToChangeDetails(changeDetail)
                serviceItemActivityInternalService.addServiceItemActivity(si, activity)
            }

            return itemCommentToSave
        }finally {
          lock.unlock();
        }
    }

    @Transactional
    ServiceItem findAndDeleteItemComment(def params) {

        def session = retrieveHttpSession()

        def sessionParams = ['isAdmin':session.isAdmin,
                             'username':session.username]

        def itemCommentToDelete = getAllowableComment(params.itemCommentId,
                                                    sessionParams,
                                                    itemCommentRules)
        if(!itemCommentToDelete){
            throw new ObjectNotFoundException("Cannot locate Allowable itemComment with id: ${params.itemCommentId}")
        }

        def si_id = itemCommentToDelete.serviceItem.id
        def si = serviceItemService.getAllowableItem(si_id,
                                                sessionParams,
                                                serviceItemRules)

        if(!si){
            throw new ObjectNotFoundException("Cannot locate Allowable serviceItem with id: ${si_id}")
        }

        deleteItemComment(itemCommentToDelete, si)

        String reviewOwner = itemCommentToDelete.author.displayName
        ChangeDetail changeDetail = new ChangeDetail(fieldName: "reviewOwner", newValue: reviewOwner)
        Profile activityAuthor = profileService.findByUsername(sessionParams.username)
        ServiceItemActivity activity = new ServiceItemActivity(action: Action.REVIEW_DELETED, author: activityAuthor)
        activity.addToChangeDetails(changeDetail)
        serviceItemActivityInternalService.addServiceItemActivity(si, activity)

        //return the serviceitem because the new rating stats will be useful in the response
        return si;
    }

    @Transactional
    def deleteItemComment(itemCommentToDelete, si) {
        def si_id = itemCommentToDelete.serviceItem.id

        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            log.debug "Deleting itemComment: ServiceID: ${si_id} / CommentId: ${itemCommentToDelete.id}"

            def userRate = itemCommentToDelete.rate ?: null
            if (userRate) {
                userRate = Math.round(userRate)
                si.avgRate = Utils.removeRatingFromAverageRate(si.avgRate, si.totalVotes, userRate)
                if (si.totalVotes > 0) {
                    si.totalVotes -= 1
                }
                if (userRate && Utils.rangeCheck(userRate, itemCommentToDelete.author.username) == 1) {
                    if (si."totalRate${userRate}" && (si."totalRate${userRate}" > 0)) {
                        si."totalRate${userRate}"--
                    }
                }
            }
            si.totalComments -= 1
            si.removeFromItemComments(itemCommentToDelete)
            itemCommentToDelete.delete()
            si.save()
        } finally {
            lock.unlock();
        }
    }

    /*********************************************************************************************
     * RATING METHODS
     ********************************************************************************************* */
    @Transactional(readOnly = true)
    def retrieveItemCommentByAuthorId(Long author_id, Long si_id) {

        def crit = ItemComment.createCriteria()
        def itemComment = crit.get {
            author {
                eq("id", author_id)
            }
            serviceItem {
                eq("id", si_id)
            }
        }
        return itemComment
    }

    @Transactional(readOnly = true)
    def retrieveItemCommentListByValue(Long si_id, Integer rateValue) {
        def crit = ItemComment.createCriteria()
        def itemComment = crit.list {
            and {
                eq("rate", Float.valueOf(rateValue))
                serviceItem {
                    eq("id", si_id)
                }
            }
        }
        return itemComment
    }

    @Transactional(readOnly = true)
    int yourRating(def author_id, def si_id) {
        def itemComment = retrieveItemCommentByAuthorId(author_id, si_id)
        return itemComment?.rate ?: 0
    }

    private Float performRate(def si, def itemComment, def newRate) {
        int rate = newRate.toInteger()

        if (Utils.rangeCheck(rate, itemComment.author.username) == 0) {
            return 0;
        }

        //if this profile already rated this ServiceItem, then update the rating, else create a new one
        def ratMessage
        int old_rate = 0
        if (itemComment.rate) {
            old_rate = Math.round(itemComment.rate)

            if (Utils.rangeCheck(old_rate, itemComment.author.username) == 0) {
                return 0;
            }

            log.debug("${itemComment.author.username} already rated ${si.title}, just update the rate to ${rate}")
            ratMessage = "${itemComment.author.username} already rated ${si.title}, just update the rate to ${rate}"
            si.avgRate = Utils.removeRatingFromAverageRate(si.avgRate, si.totalVotes, old_rate)
            si.avgRate = Utils.addRatingToAverageRate(si.avgRate, (si.totalVotes - 1), rate)
        }
        else {
            log.debug("New rating by ${itemComment.author.username} for ${si.title}, set to ${rate}")
            ratMessage = "New rating by ${itemComment.author.username} for ${si.title}, set to ${rate}"
            si.avgRate = Utils.addRatingToAverageRate(si.avgRate, si.totalVotes, rate)
            si.totalVotes += 1
        }
        itemComment.rate = rate

        //Update total rate counts
        log.debug "Service Item: ${si.title}:${si.id} has new rate count: old (${old_rate}) -> new (${rate})"
        if (old_rate && Utils.rangeCheck(old_rate, itemComment.author.username) == 1) {
            if (si."totalRate${old_rate}" && (si."totalRate${old_rate}" > 0)) {
                si."totalRate${old_rate}"--
            }
        }

        if (Utils.rangeCheck(rate, itemComment.author.username) == 1) {
            if (!si."totalRate${rate}") {
                si."totalRate${rate}" = 1
            } else {
                si."totalRate${rate}"++
            }
            log.debug "Service Item: ${si.title}:${si.id} <> Star Counts [5 (${si.totalRate5}), 4 (${si.totalRate4}), 3 (${si.totalRate3}), 2 (${si.totalRate2}), 1 (${si.totalRate1})] "
        }

        if (si.save()) {
            log.debug "${si.title} was given a ${rate} star rating. Avergae Rating: ${si.avgRate}. Total Votes: ${si.totalVotes}"
        } else {
            log.error("Failure trying to add ${rate} star rating to ${si.title} Avergae Rating: ${si.avgRate}. Total Votes: ${si.totalVotes}")
            si.errors.each { log.error it }
            throw new ValidationException(message: "rating.add.failure")
        }
        return si.avgRate
    }

    @Transactional
    def rate(def itemComment, def newRate) {
        if (!itemComment?.serviceItem || !itemComment?.author) return 0

        def si = ServiceItem.get(itemComment.serviceItem.id)
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            return performRate(si, itemComment, newRate)
        } finally {
            lock.unlock();
        }
    }
}
