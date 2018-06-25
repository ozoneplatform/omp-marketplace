package marketplace

import marketplace.rest.ServiceItemActivityInternalService

import ozone.marketplace.enums.RelationshipType
import grails.gorm.transactions.Transactional

class RelationshipService {

    ServiceItemService serviceItemService

    ServiceItemActivityService serviceItemActivityService

    ServiceItemActivityInternalService serviceItemActivityInternalService

    ChangeLogService changeLogService

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    @Transactional
    def addOrRemoveRequires(def parent_id, def requires_ids = []) {
        return addOrRemoveRequiresWithProfile(parent_id, null, requires_ids)
    }

    @Transactional
    def addOrRemoveRequiresWithProfile(def parent_id, def profile, def requires_ids = [],
                                       def checkAccess = true) {
        def changed = false
        requires_ids = requires_ids.unique() - null
        log.debug "About to add requires ${requires_ids} to Service Item ${parent_id}. profile = ${profile?.username}"
        def parent
        if (checkAccess) {
            parent = serviceItemService.getServiceItem(parent_id)
        } else {
            parent = ServiceItem.get(parent_id)
        }
        Relationship relationship = Relationship.findByOwningEntityAndRelationshipType(parent, RelationshipType.REQUIRE)
        if (!relationship) {
            relationship = new Relationship()
            relationship.relationshipType = RelationshipType.REQUIRE
            relationship.owningEntity = parent
            log.debug "No existing relationship. Creating a ${relationship?.relationshipType} relationship for ${relationship?.owningEntity?.title}"
        }
        def relatedItems = relationship.relatedItems?.findAll { it != null }
        if (relatedItems.collect { it.id } != requires_ids) {
            def oldRelatedItemsIds = relatedItems.collect { it.id }
            relationship.relatedItems = requires_ids.collect {
                if (checkAccess) {
                    serviceItemService.getServiceItem(it)
                } else {
                    ServiceItem.get(it)
                }
            }
            def newRelatedItemsIds = relationship.relatedItems.collect { it.id }
            saveRelationship(relationship, checkAccess)

            def itemsAdded = []
            def itemsRemoved = []
            newRelatedItemsIds.each {
                if (!oldRelatedItemsIds.contains(it)) {
                    //Using Get instead of serviceItemService.getServiceItem(it) because we want to bypass the security check. The change log should contain all of the old listings
                    // regardless of whether or not the last updater had permission to see all of them.
                    def si = ServiceItem.get(it)
                    itemsAdded << si
                }
            }
            oldRelatedItemsIds.each {
                if (!newRelatedItemsIds.contains(it)) {
                    def si = ServiceItem.get(it)
                    itemsRemoved << si
                }
            }
            serviceItemActivityInternalService.addRelationshipActivities(parent, itemsAdded, itemsRemoved)
        }
        else {
            log.debug("No requires changes")
        }
        return changed
    }

    @Transactional
    def saveRelationship(def relationship, def checkAccess = true) {
        log.debug "About to save Relationship of type ${relationship?.relationshipType} for Service Item ${relationship?.owningEntity?.title}"
        if (checkAccess) {
            serviceItemService.checkPermissionToEdit(relationship.owningEntity)
        }
        relationship.save(flush: true, failOnError: true)
    }

    @Transactional(readOnly = true)
    def showItem(def serviceItem, def isAdmin, def username) {
        boolean returnValue = true

        if (!serviceItem.statApproved() || serviceItem.isHidden) {
            boolean isOwner = serviceItem.isAuthor(username)
            if (!isAdmin && !isOwner) {
                returnValue = false
            }
        }

        return returnValue
    }

    @Transactional(readOnly = true)
    def getRequiresItems(def serviceItemId, def isAdmin, def username) {
        log.debug "getRequiresItems: serviceItemId = ${serviceItemId}, isAdmin = ${isAdmin}, username = ${username}"
        def items = []

        def parent = serviceItemService.getServiceItem(serviceItemId)
        Relationship rel = Relationship.findByOwningEntityAndRelationshipType(parent, RelationshipType.REQUIRE)
        if (rel) {
            rel.relatedItems.each {
                if (it != null && showItem(it, isAdmin, username)) {
                    items << it
                }
            }
        }
        return items
    }

    /**
     * The closure condition will be applied to each item to see if the item should be processed.
     * By default all items in the required tree of the specified parent will be returned.
     *
     * Note the specified parent listing will be returned even if it does not satisfy the
     * specified closure.
     * @param parentId service item to process
     * @param isAdmin
     * @param username
     * @param condition condition to which required items should satisfy
     * @param asJSON if true, returns JSON for each service item, otherwise returns service items themselves
     * @return the list of JSON object or domain objects for service item required by the given item.
     */
    @Transactional(readOnly = true)
    def getRequiredItems(def parentId, def isAdmin, def username, def condition = { true }, boolean asJSON = true) {
        log.debug "getRequiredItems: parentId = ${parentId}"
        def toProcess = new LinkedList()
        def processed = [] as Set
        def model = []

        //serviceItemService.getServiceItem will throw an exception if parentId is not found
        // or the user doesn't have permission to get the listing
        toProcess << serviceItemService.getServiceItem(parentId)

        while (toProcess) {
            def item = toProcess.remove(0)
            log.debug "processing ${item.title} with id ${item.id}"

            def requires = getRequiresItems(item.id, isAdmin, username)
            if (requires.retainAll(condition)) {
                log.debug "removed some requires items from ${item.title} with id ${item.id}"
            }
            requires.each {
                if (!processed.contains(it.id)) {
                    if (!toProcess.find { it2 -> it2.id == it.id }) {
                        log.debug "adding ${it.title} with id ${it.id} to toProcess queue"
                        toProcess << it
                    } else {
                        log.debug "${it.title} with id ${it.id} already in toProcess queue"
                    }
                } else {
                    log.debug "already processed ${it.title} with id ${it.id}"
                }
            }
            model.add(asJSON ? item.asJSON(requires) : item)
            processed << item.id
        }
        return model
    }

    @Transactional
    def deleteByServiceItem(ServiceItem serviceItem) {
        serviceItem.relationships.each {
            serviceItemActivityInternalService.addRelationshipActivities(serviceItem,
                [], it.relatedItems)
        }

        serviceItem.relationships.clear()
        serviceItem.save(flush: true)

        def criteria = Relationship.createCriteria()
        def relationships = criteria.list {
            relatedItems {
                eq('id', serviceItem.id)
            }
        }

        relationships.each {
            it.discard()
            def rel = Relationship.get(it.id)
            rel.removeFromRelatedItems(serviceItem)
            serviceItemActivityInternalService.addRelationshipActivities(rel.owningEntity, [], [serviceItem])
            rel.save()
        }
    }

    /**
     * Given a list of Service Item ids, determine all required Service Items
     * @param selectedIds the list of selected Service Item ids
     * @return the list of required item ids including the parents
     */
    List<Long> getAllRequiredBy(List<Long> selectedIds) {

        // Get all the relationships
        def criteria = Relationship.createCriteria()
        List<Relationship> relationships = criteria.list {
            owningEntity {
                'in'("id", selectedIds)
            }
            join('relatedItems')
        }

        // Get the required items based on the relationships
        List<Long> requiredItems = relationships.collect {
            rel -> rel.getRelatedItems().id
        }
        // Add back in the original ids
        requiredItems.addAll(selectedIds)
        requiredItems = requiredItems.flatten()
        requiredItems = requiredItems.unique()

        // returns the original items in addition to their required items
        return requiredItems
    }

    // Only check new ones, keep calling getAllRequiredBy
    List<Long> getAllRequiredChildren(List<Long> selectedIds) {
        List<Long> processed = getAllRequiredBy(selectedIds)
        while (processed.size() != selectedIds.size()) {
            selectedIds = processed
            processed = getAllRequiredBy(processed)
        }
        return processed
    }
}


