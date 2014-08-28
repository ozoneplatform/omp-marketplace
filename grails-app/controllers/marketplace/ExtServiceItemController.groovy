package marketplace


import ozone.marketplace.domain.ValidationException
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import ozone.decorator.JSONDecoratorException
import ozone.marketplace.domain.ValidationException
import ozone.marketplace.enums.MarketplaceApplicationSetting

import grails.converters.JSON

class ExtServiceItemController extends BaseMarketplaceRestController {

    def extServiceItemService

    def extServiceItemQueryService

    def accountService

    def springcacheService

    def sessionFactory

    def marketplaceApplicationConfigurationService

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    private checkExtServiceItemEnabled(Closure closure) {
        if (marketplaceApplicationConfigurationService.is(MarketplaceApplicationSetting.EXTERNAL_SERVICE_ITEM_ENABLED)) {
            closure()
        } else {
            throw new PermissionException("System Does Not Allow External Calls")
        }
    }

    /*
     */
    def create = {
        try {
            checkExtServiceItemEnabled {
                def json = request.JSON
                log.debug "create: request=${json}"

                def extSvc = extServiceItemService.create(json)
                renderJSONResult (([id: extSvc.id] as JSON), response.SC_CREATED)
            }
        }
        catch (ConverterException ex) {
            handleExpectedException(ex, "create", response.SC_INTERNAL_SERVER_ERROR)
        }
        catch (ozone.marketplace.domain.ValidationException ex) {
            // handleException - logs the real error and sends a error reference to the caller
            handleException(ex, "create", response.SC_BAD_REQUEST)
        }
        catch (PermissionException ex) {
            handleExpectedException(ex, "create", response.SC_FORBIDDEN)
        }
        catch (JSONDecoratorException ex) {
            handleExpectedException(ex, "create", response.SC_FORBIDDEN)
        }
        catch (Exception ex) {
            ex.printStackTrace()
            handleException(ex, "create", response.SC_INTERNAL_SERVER_ERROR)
        }
    }

    /*
     */
    def update = {

        try {
            checkExtServiceItemEnabled {
                def json = request.JSON
                log.debug "update: request=${json}"
                log.debug "update: params=${params}"
                log.debug "update: session=${session}"

                def wasUpdated = extServiceItemService.update(params.id, json, session.username)

                if (wasUpdated) {
                    renderSimpleResult('Service Item successfully updated', response.SC_OK)
                } else {
                    renderSimpleResult('Service Item did not need to be updated', response.SC_OK)
                }
            }
        }
        catch (ConverterException ex) {
            handleExpectedException(ex, "update", response.SC_INTERNAL_SERVER_ERROR)
        }
        catch (ObjectNotFoundException noe) {
            handleExpectedException(noe, "update", response.SC_NOT_FOUND)
        }
        catch (ozone.marketplace.domain.ValidationException ve) {
            // handleException - logs the real error and sends a error reference to the caller
            handleException(ve, "update", response.SC_BAD_REQUEST)
        }
        catch (PermissionException ve) {
            handleExpectedException(ve, "update", response.SC_FORBIDDEN)
        }
        catch (JSONDecoratorException ex) {
            handleExpectedException(ex, "update", response.SC_FORBIDDEN)
        }
        catch (Exception e) {
            handleException(e, "update", response.SC_INTERNAL_SERVER_ERROR)
        }
    }

    /*
     * status codes:
     * 200 - success
     * 500 - internal server error
     */
    def list = {
        def model = []
        def total = 0
        try {
            def result = extServiceItemService.extServiceItems(params)

            total = result['listSize']
            result['extServiceItemList'].each{ model.add(it.asLegacyJSON()) }
            renderResult(model, total, response.SC_OK)
        }
        catch (Exception e) {
            handleException(e, "list")
        }
    }

    def getServiceItemsAsJSON = {
        log.debug "getServiceItemsAsJSON: request=${request}"
        if (!params.max) params.max = 10
        params.accessType = Constants.VIEW_USER
        def result
        def model = []
        def total
        try {
            checkExtServiceItemEnabled {
                def exemplar = new ExtServiceItem()
                result = extServiceItemQueryService.getItems(params, exemplar)
                result['serviceItemList']?.collect{ model.add(it.asLegacyJSON()) }
                total = result['listSize']
            }
        }
        catch (PermissionException ve) {
            handleExpectedException(ve, "getServiceItemsAsJSON", response.SC_FORBIDDEN)
        }
        catch (Exception e) {
            handleException(e, "getServiceItemsAsJSON")
        }
        renderResult(model, total, response.SC_OK)
    }

    /*
     */
    def retrieve = {
        def model = []
        def total = 0
        try {
            def newParams = [id: params.id]
            def result = extServiceItemService.extServiceItems(newParams)

            total = result['listSize']
            if (!total) {
                throw new ObjectNotFoundException("Cannot locate object with id: ${params.id}")
            }
	            result['extServiceItemList'].each{ model.add(it.asLegacyJSON()) }
            renderResult(model, total, response.SC_OK)

        }
        catch (ObjectNotFoundException noe) {
            handleExpectedException(noe, "retrieve", response.SC_NOT_FOUND)
        }
        catch (Exception e) {
            handleException(e, "retrieve")
        }
    }

    def disable = {
        try {
            checkExtServiceItemEnabled {
                log.debug "disable: params=${params}"
                log.debug "disable: session=${session}"

                extServiceItemService.disable(params.id, session.username)
                render(status: response.SC_OK)
            }
        }
        catch (ObjectNotFoundException noe) {
            handleExpectedException(noe, "disable", response.SC_NOT_FOUND)
        }
        catch (PermissionException ve) {
            handleExpectedException(ve, "disable", response.SC_FORBIDDEN)
        }
        catch (JSONDecoratorException ex) {
            handleExpectedException(ex, "create", response.SC_FORBIDDEN)
        }
        catch (ValidationException ve) {
            handleExpectedException(ve, "disable", response.SC_BAD_REQUEST)
        }
        catch (Exception e) {
            handleException(e, "disable", response.SC_INTERNAL_SERVER_ERROR)
        }
    }

    def enable = {
        try {
            checkExtServiceItemEnabled {
                log.debug "enable: params=${params}"
                log.debug "enable: session=${session}"

                extServiceItemService.enable(params.id, session.username)
                render(status: response.SC_OK)
            }
        }
        catch (ObjectNotFoundException noe) {
            handleExpectedException(noe, "enable", response.SC_NOT_FOUND)
        }
        catch (PermissionException ve) {
            handleExpectedException(ve, "enable", response.SC_FORBIDDEN)
        }
        catch (JSONDecoratorException ex) {
            handleExpectedException(ex, "create", response.SC_FORBIDDEN)
        }
        catch (ValidationException ve) {
            handleExpectedException(ve, "enable", response.SC_BAD_REQUEST)
        }
        catch (Exception e) {
            handleException(e, "enable", response.SC_INTERNAL_SERVER_ERROR)
        }
    }

    /*
      * Retrieve the detail-listing of a service item as a JSON object
      * TODO: this code is copied from ServiceItemController.groovy. We need to refactor...
      */
    def getItemAsJSON = {
        def model
        try {
            checkExtServiceItemEnabled {
                model = extServiceItemService.getServiceItemListing(params)
                if (!model) {
                    handleException(new Exception("ExtServiceItem with id '" + params.id + "' does not exist"), "getItemAsJSON", response.SC_FORBIDDEN)
                    return
                }
            }
        }
        catch (AccessControlException ae) {
            handleException(ae, "getDetailListingForServiceItemAsJSON", response.SC_FORBIDDEN)
        }
        catch (PermissionException ve) {
            handleExpectedException(ve, "getItemAsJSON", response.SC_FORBIDDEN)
        }
        catch (Exception e) {
            handleException(e, "getItemAsJSON")
        }
        renderResult(model.asLegacyJSON(), 1, response.SC_OK)
    }

    def getCacheConfig(def cacheIn) {
        def config = cacheIn.getCacheConfiguration()
        return [cache: cacheIn.getName(),
            maxElementsInMemory: config.getMaxElementsInMemory(),
            eternal: config.isEternal(),
            timeToIdleSeconds: config.getTimeToIdleSeconds(),
            timeToLiveSeconds: config.getTimeToLiveSeconds(),
            overflowToDisk: config.isOverflowToDisk(),
            diskPersistent: config.isDiskPersistent(),
            diskExpiryThreadIntervalSeconds: config.getDiskExpiryThreadIntervalSeconds(),
            maxElementsOnDisk: config.getMaxElementsOnDisk()]
    }

    def getAccuracy(statsIn) {
        def returnValue = 'unknown'
        switch (statsIn.getStatisticsAccuracy()) {
            case net.sf.ehcache.Statistics.STATISTICS_ACCURACY_NONE:
                returnValue = 'STATISTICS_ACCURACY_NONE'
            case net.sf.ehcache.Statistics.STATISTICS_ACCURACY_BEST_EFFORT:
                returnValue = 'STATISTICS_ACCURACY_BEST_EFFORT'
            case net.sf.ehcache.Statistics.STATISTICS_ACCURACY_GUARANTEED:
                returnValue = 'STATISTICS_ACCURACY_GUARANTEED'
        }

        return returnValue
    }

    protected getCacheStats(def cacheIn) {
        def stats = cacheIn.getStatistics()
        return [cache: cacheIn.getName(),
            cacheHits: stats.getCacheHits(),
            cacheMisses: stats.getCacheMisses(),
            objectCount: stats.getObjectCount(),
            onDiskHits: stats.getOnDiskHits(),
            inMemoryHits: stats.getInMemoryHits(),
            averageGetTime: stats.getAverageGetTime(),
            evictionCount: stats.getEvictionCount(),
            accuracy: getAccuracy(stats)
        ]
    }

    def getHibernateEntityStats(def entityName, def stats) {
        return [entity: entityName,
            loadCount: stats.getLoadCount(),
            updateCount: stats.getUpdateCount(),
            insertCount: stats.getInsertCount(),
            deleteCount: stats.getDeleteCount(),
            fetchCount: stats.getFetchCount(),
            optimisticLockFailureCount: stats.getOptimisticFailureCount()
        ]
    }

    def getHibernateSecondLevelCacheStats(def entityName, def stats) {
        return [entity: entityName,
            hitCount: stats.getHitCount(),
            missCount: stats.getMissCount(),
            putCount: stats.getPutCount(),
            elementCountInMemory: stats.getElementCountInMemory(),
            elementCountOnDisk: stats.getElementCountOnDisk(),
            sizeInMemory: stats.getSizeInMemory()
        ]
    }

    private logStringArray(String labelIn, String[] arrayIn) {
        log.info(labelIn)
        for (int i = 0; i < arrayIn.length; i++) {
            log.info(arrayIn[i]);
        }
    }

    def getHibernateStats = {
        try {
            log.debug "getHibernateStats: params=${params}"

            def statistics = sessionFactory.statistics

            def entityNames1 = statistics.getEntityNames()
            log.debug("entityNames = ${entityNames1}")
            def queries = statistics.getQueries()
            log.debug("queries = ${queries}")
            def regions = statistics.getSecondLevelCacheRegionNames()
            log.debug("regions = ${regions}")

            def entityNames = ['marketplace.ServiceItem', 'marketplace.ServiceItemActivity']

            def jsonResult = ([
                secondLevelCacheStats: entityNames.collect { getHibernateSecondLevelCacheStats(it, statistics.getSecondLevelCacheStatistics(it)) },
                entityStats: entityNames.collect { getHibernateEntityStats(it, statistics.getEntityStatistics(it)) },
                summary: statistics.toString()
            ] as JSON)

            if (params.resetStats) {
                statistics.clear()
            }

            renderJSONResult jsonResult, response.SC_OK
        }
        catch (Exception ex) {
            handleException(ex, "getHibernateStats", response.SC_INTERNAL_SERVER_ERROR)
        }
    }

    def getStats = {
        try {
            log.debug "getStats: params=${params}"

            def cacheNames = ['serviceItemBadgeCache',
                'serviceItemIconCache',
                'serviceItemIconImageCache',
                'serviceItemDescriptionCache',
                'typeIconImageCache',
                'nuggetCache',
                'bannerBeanNorth',
                'bannerBeanSouth',
                'bannerBeanCSS',
                'bannerBeanJS'
            ]
            def caches = cacheNames.collect() { springcacheService.getOrCreateCache(it) }

            def jsonResult = ([
                stats: caches.collect { getCacheStats(it) },
                configurations: caches.collect { getCacheConfig(it) }
            ] as JSON)

            if (params.resetStats) {
                caches.each {
                    it.clearStatistics()
                }
            }

            renderJSONResult jsonResult, response.SC_OK
        }
        catch (Exception ex) {
            handleException(ex, "getStats", response.SC_INTERNAL_SERVER_ERROR)
        }
    }
}
