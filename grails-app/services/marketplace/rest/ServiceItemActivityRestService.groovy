package marketplace.rest

import marketplace.CustomField
import marketplace.OwfProperties
import grails.gorm.transactions.Transactional
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import grails.core.GrailsApplication

import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ModifyRelationshipActivity
import marketplace.ServiceItemSnapshot
import marketplace.Constants
import marketplace.RejectionListing
import marketplace.RejectionActivity
import marketplace.Profile

import marketplace.Sorter

@Service
class ServiceItemActivityRestService
        extends ChildObjectRestService<ServiceItem, ServiceItemActivity> {

    @Autowired
    ServiceItemActivityRestService(GrailsApplication grailsApplication,
            ServiceItemRestService serviceItemRestService) {

        super(ServiceItem.class, 'serviceItem', 'serviceItemActivities', grailsApplication,
            ServiceItemActivity.class, serviceItemRestService, null,new Sorter<ServiceItemActivity>(Constants.SortDirection.DESC, 'activityTimestamp'))
//            new Sorter<ServiceItemActivity>(Constants.SortDirection.DESC, 'activityTimestamp'))
    }

    ServiceItemActivityRestService() {}

    @Transactional(readOnly=true)
    List<ServiceItemActivity> getAllByProfile(Profile profile, Integer offset, Integer max) {
        ServiceItemActivity.findAllByAuthor(profile, [max: max, offset: offset, sort: sortField, order: sortOrder])
    }

    /**
     * Get ServiceItemActivities that belong to ServiceItems that have the given author
     */
    @Transactional(readOnly=true)
    List<ServiceItemActivity> getAllByServiceItemOwner(Profile profile, Integer offset, Integer max) {
        ServiceItemActivity.createCriteria().list(max: max, offset: offset) {
            serviceItem {
                owners {
                    eq('id', profile.id)
                }
            }
            order(sortField, sortOrder)
        } as List
    }

    private String getSortField() { sorter.sortField }

    private String getSortOrder() { sorter.direction.name().toLowerCase() }

}
