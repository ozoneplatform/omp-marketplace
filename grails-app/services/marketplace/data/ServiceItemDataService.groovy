package marketplace.data

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

import marketplace.ServiceItem


class ServiceItemDataService {

    @ReadOnly
    List<ServiceItem> findByOwnerId(long ownerId) {
        ServiceItem.where { owners { id == ownerId } }.list()
    }

    @ReadOnly
    int countHasTypeById(long typeId) {
        ServiceItem.where { types { id == typeId} }.count()
    }

    @Transactional
    void index(ServiceItem serviceItem) {
        serviceItem.index()
    }

    @Transactional
    void reindexAllByOwnerId(long ownerId) {
        def serviceItems = findByOwnerId(ownerId)

        // index() is used as it appears more reliable than reindex().
        serviceItems.each { index(it) }
    }

}
