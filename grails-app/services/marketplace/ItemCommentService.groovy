package marketplace

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

import org.hibernate.FlushMode
import org.hibernate.Session
import org.hibernate.SessionFactory

import marketplace.Constants.Action
import marketplace.rest.ServiceItemActivityInternalService
import org.apache.commons.lang.exception.ExceptionUtils

import ozone.marketplace.domain.ValidationException
import ozone.utils.Utils

import static ozone.utils.PagingOptions.pagingOptions
import static ozone.utils.SortOptions.sortOptions
import static ozone.utils.Utils.isValidRatingRange


class ItemCommentService {

    SessionFactory sessionFactory

    ProfileService profileService

    ServiceItemService serviceItemService

    ServiceItemActivityInternalService serviceItemActivityInternalService

    static final Map<String, Boolean> SERVICE_ITEM_RULES =
            [allNoRestrictions : false,
             allIfApproved     : true,
             userNoRestrictions: true,
             userIfApproved    : true]

    static final Map<String, Boolean> ITEM_COMMENT_RULES =
            [allNoRestrictions : false,
             allIfApproved     : false,
             userNoRestrictions: true,
             userIfApproved    : true]

    /**
     * Retrieves all of the comments for a user for all service items.
     * Includes a collection of rows with the creation date, title of the
     * ServiceItem being commented on and the actual comment itself.
     *
     * params:
     *
     *    limit - max number of records to return (for paging)
     *    start - 0 based offset into full result set (for paging)
     *    username - Profile.username field used to retreive user comments*/
    @ReadOnly
    List<ItemComment> getUserComments(Map params, String username, boolean isAdmin, String accessType) {
        def paging = pagingOptions(params)
        def sort = sortOptions(params, 'editedDate', [name: 'serviceItem'])

        String author = params.asString('author')

        try {
            return ItemComment.createCriteria().list(paging) {
                author {
                    eq('username', author)
                }
                serviceItem {
                    if (accessType != Constants.VIEW_ADMIN || !isAdmin) {
                        or {
                            ilike('approvalStatus', Constants.APPROVAL_STATUSES["APPROVED"])
                            owners {
                                eq('username', username)
                            }
                        }
                        eq('isHidden', 0)
                    }
                }
                order(sort.field, sort.order)
            } as List
        } catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred retreiving comments for user ${author}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            Session session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "comment.failure", args: [author, message])
        }
    }


    @ReadOnly
    List<ItemComment> getServiceItemComments(Map params) {
        def paging = pagingOptions(params)
        def sort = sortOptions(params, 'editedDate', [displayName: 'a.displayName'])

        String author = params.asString('author')
        Long serviceItemId = params.asLong('id')

        try {
            return ItemComment.createCriteria().list(paging) {
                createAlias("author", "a")
                if (author) {
                    eq('a.username', author)
                }
                eq('serviceItem.id', serviceItemId)
                order(sort.field, sort.order)
            } as List
        } catch (Exception e) {
            String message = ExceptionUtils.getRootCauseMessage(e)
            log.error "Error occurred retreiving comments for service item ${serviceItemId}. ${message}"
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            Session session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "comment.failure", args: [author, message])
        }
    }

    @ReadOnly
    ItemComment getAllowableComment(Long itemCommentId,
                                    String username,
                                    boolean isAdmin,
                                    Map<String, Boolean> rules) throws AccessControlException
    {
        ItemComment comment = ItemComment.get(itemCommentId)

        if (!comment) return null

        boolean isUser = username && username == comment?.author?.username
        boolean isAvailable = !comment.serviceItem?.isHidden ?: false
        boolean isApproved = comment.serviceItem?.statApproved() ?: false

        boolean matchesAnyRule = false
        if (rules?.allNoRestrictions && isAvailable) {
            matchesAnyRule = true
        }
        if (isAdmin) {
            matchesAnyRule = true
        }
        if (rules?.allIfApproved && isAvailable && isApproved) {
            matchesAnyRule = true
        }
        if (rules?.userNoRestrictions && isUser && isAvailable) {
            matchesAnyRule = true
        }
        if (rules?.userIfApproved && isUser && isAvailable && isApproved) {
            matchesAnyRule = true
        }

        if (!matchesAnyRule) {
            throw new AccessControlException('User is not authorized to access this comment')
        }

        return comment
    }

    @Transactional
    ItemComment saveItemComment(Map params, String username, boolean isAdmin) {
        Long itemCommentId = params.asLong('id')
        Long serviceItemId = params.asLong('serviceItemId')
        Integer rateVal = params.asInteger('newUserRating')

        String commentUsername = params.asString('username', username)
        String commentText = params.asString('text')
        String commentTextInput = params.asString('commentTextInput')

        ItemComment itemCommentToSave
        ChangeDetail changeDetail = null

        if (!itemCommentId) {
            itemCommentToSave = new ItemComment()
            Profile profile = profileService.findByUsername(commentUsername)
            if (profile) itemCommentToSave.author = profile
            itemCommentToSave.createdDate = new Date()
        }
        else {
            itemCommentToSave = getAllowableComment(itemCommentId, username, isAdmin, ITEM_COMMENT_RULES)
            if (!itemCommentToSave) {
                throw new ObjectNotFoundException("Cannot locate Allowable itemComment with id: ${itemCommentId}")
            }

            //Is this review being modified by someone who is not the reviewer?
            if (itemCommentToSave.author.username != username && itemCommentToSave.text != commentText) {
                String reviewOwner = itemCommentToSave.author.displayName
                changeDetail = new ChangeDetail(fieldName: "$reviewOwner's Review", oldValue: itemCommentToSave.text, newValue: params.text)
            }

            serviceItemId = itemCommentToSave.serviceItem.id
        }

        ServiceItem serviceItem = serviceItemService.getAllowableItem(serviceItemId, username, isAdmin, SERVICE_ITEM_RULES)
        if (!serviceItem) {
            throw new ObjectNotFoundException("Cannot locate Allowable serviceItem with id: ${serviceItemId}")
        }

        Lock lock = new ReentrantLock()
        lock.lock()
        try {
            itemCommentToSave.text = commentTextInput

            if (!itemCommentToSave.id) {
                itemCommentToSave.serviceItem = serviceItem
            }
            if (rateVal) {
                performRate(serviceItem, itemCommentToSave, rateVal)
            }
            itemCommentToSave.scrubCR()

            if (rateVal) {
                performRate(serviceItem, itemCommentToSave, rateVal)
            }

            if (itemCommentToSave.save()) {
                log.debug "Saving itemComment: ServiceID: ${serviceItem.id} / CommentId: ${itemCommentToSave.id}"
                if (!itemCommentId) {//Meaning new itemComment
                    if (!serviceItem.totalComments) {
                        serviceItem.totalComments = 0
                    }
                    serviceItem.totalComments += 1

                    serviceItem.itemComments.add(itemCommentToSave)
                }
            }
            else {
                log.error("Failure Saving itemComment: ServiceID: ${serviceItem.id} / CommentId: ${itemCommentToSave.id}")
                itemCommentToSave.errors.each { log.error it }
                throw new ValidationException(fieldErrors: itemCommentToSave.errors)
            }

            if (changeDetail) {
                Profile activityAuthor = profileService.findByUsername(username)
                ServiceItemActivity activity = new ServiceItemActivity(action: Action.REVIEW_EDITED, author: activityAuthor)
                activity.addToChangeDetails(changeDetail)
                serviceItemActivityInternalService.addServiceItemActivity(serviceItem, activity)
            }


            return itemCommentToSave
        } finally {
            lock.unlock()
        }
    }

    @Transactional
    ServiceItem findAndDeleteItemComment(Map params, String username, boolean isAdmin) {
        Long itemCommentId = params.asLong('itemCommentId')

        ItemComment itemCommentToDelete = getAllowableComment(itemCommentId, username, isAdmin, ITEM_COMMENT_RULES)
        if (!itemCommentToDelete) {
            throw new ObjectNotFoundException("Cannot locate Allowable itemComment with id: ${itemCommentId}")
        }

        Long serviceItemId = itemCommentToDelete.serviceItem.id
        ServiceItem serviceItem = serviceItemService.getAllowableItem(serviceItemId, username, isAdmin, SERVICE_ITEM_RULES)
        if (!serviceItem) {
            throw new ObjectNotFoundException("Cannot locate Allowable serviceItem with id: ${serviceItemId}")
        }

        deleteItemComment(itemCommentToDelete, serviceItem)

        String reviewOwner = itemCommentToDelete.author.displayName
        ChangeDetail changeDetail = new ChangeDetail(fieldName: "reviewOwner", newValue: reviewOwner)
        Profile activityAuthor = profileService.findByUsername(username)
        ServiceItemActivity activity = new ServiceItemActivity(action: Action.REVIEW_DELETED, author: activityAuthor)
        activity.addToChangeDetails(changeDetail)
        serviceItemActivityInternalService.addServiceItemActivity(serviceItem, activity)

        //return the serviceitem because the new rating stats will be useful in the response
        return serviceItem
    }

    @Transactional
    void deleteItemComment(ItemComment itemComment, ServiceItem serviceItem) {
        Lock lock = new ReentrantLock()
        lock.lock()
        try {
            Long serviceItemId = itemComment.serviceItem.id
            log.debug "Deleting itemComment: ServiceID: ${serviceItemId} / CommentId: ${itemComment.id}"

            Float userRate = itemComment.rate ?: null
            if (userRate) {
                userRate = Math.round(userRate)
                serviceItem.avgRate = Utils.removeRatingFromAverageRate(serviceItem.avgRate, serviceItem.totalVotes, userRate)
                if (serviceItem.totalVotes > 0) {
                    serviceItem.totalVotes -= 1
                }
                if (userRate && isValidRatingRange(userRate)) {
                    def intRate = userRate as Integer
                    if (serviceItem."totalRate${intRate}" && (serviceItem."totalRate${intRate}" > 0)) {
                        serviceItem."totalRate${intRate}"--
                    }
                }
            }
            serviceItem.totalComments -= 1
            serviceItem.removeFromItemComments(itemComment)
            itemComment.delete()
            serviceItem.save()
        } finally {
            lock.unlock()
        }
    }

    /*********************************************************************************************
     * RATING METHODS
     ********************************************************************************************* */
    @ReadOnly
    ItemComment retrieveItemCommentByAuthorId(Long authorId, Long serviceItemId) {
        ItemComment.createCriteria().get {
            author {
                eq("id", authorId)
            }
            serviceItem {
                eq("id", serviceItemId)
            }
        } as ItemComment
    }

    @ReadOnly
    List<ItemComment> retrieveItemCommentListByValue(Long serviceItemId, Integer rateValue) {
        ItemComment.createCriteria().list {
            and {
                eq("rate", Float.valueOf(rateValue))
                serviceItem {
                    eq("id", serviceItemId)
                }
            }
        } as List
    }

    @ReadOnly
    int yourRating(Long authorId, Long serviceItemId) {
        retrieveItemCommentByAuthorId(authorId, serviceItemId)?.rate ?: 0
    }

    private Float performRate(ServiceItem serviceItem, ItemComment itemComment, Integer rate) {

        if (!isValidRatingRange(rate)) {
            return 0
        }
        //if this profile already rated this ServiceItem, then update the rating, else create a new one
        int oldRate = 0
        if (itemComment.rate) {
            oldRate = Math.round(itemComment.rate)

            if (!isValidRatingRange(oldRate)) {
                return 0
            }


            log.debug("${itemComment.author.username} already rated ${serviceItem.title}, just update the rate to ${rate}")
            serviceItem.avgRate = Utils.removeRatingFromAverageRate(serviceItem.avgRate, serviceItem.totalVotes, oldRate)
            serviceItem.avgRate = Utils.addRatingToAverageRate(serviceItem.avgRate, (serviceItem.totalVotes - 1), rate)
        }
        else {
            log.debug("New rating by ${itemComment.author.username} for ${serviceItem.title}, set to ${rate}")
            serviceItem.avgRate = Utils.addRatingToAverageRate(serviceItem.avgRate, serviceItem.totalVotes, rate)
            serviceItem.totalVotes += 1
        }
        itemComment.rate = rate

        //Update total rate counts
        log.debug "Service Item: ${serviceItem.title}:${serviceItem.id} has new rate count: old (${oldRate}) -> new (${rate})"
        if (oldRate && isValidRatingRange(oldRate)) {
            if (serviceItem."totalRate${oldRate}" && (serviceItem."totalRate${oldRate}" > 0)) {
                serviceItem."totalRate${oldRate}"--
            }
        }

        if (isValidRatingRange(rate)) {
            if (!serviceItem."totalRate${rate}") {
                serviceItem."totalRate${rate}" = 1
            }
            else {
                serviceItem."totalRate${rate}"++
            }
            log.debug "Service Item: ${serviceItem.title}:${serviceItem.id} <> Star Counts [5 (${serviceItem.totalRate5}), 4 (${serviceItem.totalRate4}), 3 (${serviceItem.totalRate3}), 2 (${serviceItem.totalRate2}), 1 (${serviceItem.totalRate1})] "
        }

        if (serviceItem.save()) {
            log.debug "${serviceItem.title} was given a ${rate} star rating. Avergae Rating: ${serviceItem.avgRate}. Total Votes: ${serviceItem.totalVotes}"
        }
        else {
            log.error("Failure trying to add ${rate} star rating to ${serviceItem.title} Avergae Rating: ${serviceItem.avgRate}. Total Votes: ${serviceItem.totalVotes}")
            serviceItem.errors.each { log.error it }
            throw new ValidationException(message: "rating.add.failure")
        }
        return serviceItem.avgRate
    }

    @Transactional
    Float rate(ItemComment itemComment, Integer newRate) {
        if (!itemComment?.serviceItem || !itemComment?.author) return 0

        ServiceItem serviceItem = ServiceItem.get(itemComment.serviceItem.id)
        Lock lock = new ReentrantLock()
        lock.lock()
        try {
            return performRate(serviceItem, itemComment, newRate)
        } finally {
            lock.unlock()
        }
    }

}
