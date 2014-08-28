package marketplace.rest

import marketplace.CustomField
import marketplace.OwfProperties
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import org.codehaus.groovy.grails.commons.GrailsApplication

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
            ServiceItemActivity.class, serviceItemRestService, null,
            new Sorter<ServiceItemActivity>(Constants.SortDirection.DESC, 'activityDate'))
    }

    public ServiceItemActivityRestService() {}

    @Transactional(readOnly=true)
    public List<ServiceItemActivity> getAllByProfileId(Long profileId, Integer offset,
            Integer max) {
        ServiceItemActivity.createCriteria().list(max: max, offset: offset) {
            author {
                eq('id', profileId)
            }

            order(sorter.sortField, sorter.direction.name().toLowerCase())
        }
    }

    /**
     * Get ServiceItemActivities that belong to ServiceItems that have the given author
     */
    @Transactional(readOnly=true)
    public List<ServiceItemActivity> getAllByServiceItemOwnerId(Long profileId, Integer offset,
            Integer max) {
        ServiceItemActivity.createCriteria().list(max: max, offset: offset) {
            serviceItem {
                owners {
                    eq('id', profileId)
                }
            }

            order(sorter.sortField, sorter.direction.name().toLowerCase())
        }
    }
}
