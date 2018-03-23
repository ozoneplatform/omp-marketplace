package marketplace

import org.hibernate.SessionFactory

import marketplace.rest.ServiceItemActivityInternalService

import ozone.marketplace.domain.ValidationException

import grails.gorm.transactions.Transactional

public class RejectionListingService {

    SessionFactory sessionFactory

    ServiceItemService serviceItemService

    ProfileService profileService

    ServiceItemActivityInternalService serviceItemActivityInternalService

    @Transactional(readOnly = true)
    def getLatestRejection(def serviceItemId) {
        def rej
        def rejs = RejectionListing.createCriteria().list(sort: 'createdDate', order: 'desc') {
            serviceItem {
                eq('id', serviceItemId)
            }
        }

        if (rejs) {
            rej = rejs[0]
        }
        return rej
    }

    @Transactional
    def reject(def params) {
        if (!params.username) {
            throw new ValidationException("username is required when rejecting a listing!")
        }
        def profile = profileService.findByUsername(params.username)
        if (!profile) {
            throw new ValidationException("username ${username} does not have a profile. You must specify a valid user to reject a listing!")
        }
        if (!params.containsKey('sid')) {
            throw new ValidationException("Service Item ID is required when rejecting a listing!")
        }
        def serviceItem = ServiceItem.get(params.sid.toLong())
        if (!serviceItem) {
            throw new ValidationException("Service Item ID ${sid} is not a valid service item. You must specify a valid service item to reject a listing!")
        }

        reject(profile, serviceItem, params.description, params.reasonId)
    }

    @Transactional
    def reject(def author, def serviceItem, def description, def reasonId = null) {
        description = description.size() <= 2000 ? description : description.substring(0, 2000)
        def rejectionListing = new RejectionListing(author: author, serviceItem: serviceItem, description: description)
        if (reasonId) {
            def justification = RejectionJustification.get(reasonId)
            rejectionListing.justification = justification
        }

        // Reject the serviceItem
        serviceItem.approvalStatus = Constants.APPROVAL_STATUSES["REJECTED"]
        rejectionListing.serviceItem = serviceItem

        rejectionListing.scrubCR()
        rejectionListing.validate()

        if (!rejectionListing.save()) {
            rejectionListing.errors.each { log.error it }
            throw new ValidationException(fieldErrors: rejectionListing.errors)
        }
        serviceItemActivityInternalService.addRejectionActivity(serviceItem, rejectionListing)

        serviceItem.addToRejectionListings(rejectionListing)
        if (!serviceItem.save()) {
            serviceItem.errors.each { log.error it }
            throw new ValidationException(fieldErrors: serviceItem.errors)
        }
    }
}
