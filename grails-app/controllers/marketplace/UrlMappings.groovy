package marketplace

class UrlMappings {
    static mappings = {
        '/login' view: '/login'
        '/logout' view: '/logout'

        "/i18n/$bundle"(controller: "i18n",action: "index")
//        "/$controller/$action?/$id?"{
//            constraints {
//                // apply constraints here
//            }
//        }
//        "/$controller/rating/$id/" {
//            action = [GET: "rating"]
//        }
//        "/$controller/lookup/$id/" {
//            action = [GET: "lookup"]
//        }
//
//        "/$controller/$id/comments" {
//            action = [GET: "comments"]
//        }
//        // "500"(view:'/error')

        /**
         * Marketplace Version REST API
         * @see marketplace.rest.ApiController
         */
        "/api"(namespace: 'api', controller: 'api', action: 'version', method: 'GET')

        /**
         * Tag REST API
         * @see marketplace.rest.TagController
         */
        "/api/tag"(namespace: 'api', resources: 'tag', excludes: ['create', 'edit'])
        "/api/tag/search"(namespace: 'api', controller: 'tag', action: 'findByTitle', method: 'GET')

        /**
         * Agency REST API
         * @see marketplace.rest.AgencyController
         */
        "/api/agency"(namespace: 'api', resources: 'agency', excludes: ['create', 'edit'])

        /**
         * Contact Type REST API
         * @see marketplace.rest.ContactTypeRestController
         */
        "/api/contactType"(namespace: 'api', resources: 'contactTypeRest', excludes: ['create', 'edit'])

        /**
         * Search REST API
         * @see marketplace.rest.SearchRestController
         */
        "/api/search"(namespace: 'api', controller: 'searchRest', action: 'search', method: 'GET')
        "/api/search/affiliated"(namespace: 'api', controller: 'searchRest', action: 'affiliatedSearch', method: 'GET')

        /**
         * Profile REST API
         * @see marketplace.rest.ProfileRestController
         **/
        "/api/profile"(namespace: 'api', resources: 'profileRest', excludes: ['create', 'edit', 'show'])
        "/api/profile/$id"(namespace: 'api', controller: 'profileRest', action: 'show', method: 'GET') {
            constraints { id(matches: /self|\d+/) }
        }
        "/api/profile/$id/serviceItem"(namespace: 'api', controller: 'profileRest', action: 'serviceItems', method: 'GET') {
            constraints { id(matches: /self|\d+/) }
        }
        "/api/profile/$id/itemComment"(namespace: 'api', controller: 'profileRest', action: 'itemComments', method: 'GET') {
            constraints { id(matches: /self|\d+/) }
        }
        "/api/profile/$id/tag"(namespace: 'api', controller: 'profileRest', action: 'tags', method: 'GET') {
            constraints { id(matches: /self|\d+/) }
        }
        "/api/profile/$id/activity"(namespace: 'api', controller: 'profileRest', action: 'activities', method: 'GET') {
            constraints { id(matches: /self|\d+/) }
        }
        "/api/profile/$id/serviceItem/activity"(namespace: 'api', controller: 'profileRest', action: 'serviceItemActivities', method: 'GET') {
            constraints { id(matches: /self|\d+/) }
        }

        /**
         * ServiceItem REST API
         * @see marketplace.rest.ServiceItemRestController
         **/
        "/api/serviceItem"(namespace: 'api', resources: 'serviceItemRest', excludes: ['create', 'edit'])
        "/api/serviceItem/activity"(namespace: 'api', controller: 'serviceItemRest', action: 'activities', method: 'GET')
        "/api/serviceItem/$id/activity"(namespace: 'api', controller: 'serviceItemRest', action: 'serviceItemActivities', method: 'GET')
        "/api/serviceItem/$serviceItemId/rejectionListing"(namespace: 'api', controller: 'serviceItemRest', action: 'latestRejectionListing', method: 'GET')
        "/api/serviceItem/$serviceItemId/rejectionListing"(namespace: 'api', controller: 'serviceItemRest', action: 'saveRejectionListing', method: 'POST')
        "/api/serviceItem/$id/requiredServiceItems"(namespace: 'api', controller: 'serviceItemRest', action: 'requiredServiceItems', method: 'GET')
        "/api/serviceItem/$id/requiringServiceItems"(namespace: 'api', controller: 'serviceItemRest', action: 'requiringServiceItems', method: 'GET')
        "/api/serviceItem/$serviceItemId/itemComment"(namespace: 'api', controller: 'serviceItemRest', action: 'itemComments', method: 'GET')
        "/api/serviceItem/$serviceItemId/itemComment"(namespace: 'api', controller: 'serviceItemRest', action: 'saveItemComment', method: 'POST')
        "/api/serviceItem/$serviceItemId/itemComment/$itemCommentId"(namespace: 'api', controller: 'serviceItemRest', action: 'updateItemComment', method: 'PUT')
        "/api/serviceItem/$serviceItemId/itemComment/$itemCommentId"(namespace: 'api', controller: 'serviceItemRest', action: 'deleteItemComment', method: 'DELETE')
        "/api/serviceItem/$id/tag"(namespace: 'api', controller: 'serviceItemRest', action: 'tags', method: 'GET')
        "/api/serviceItem/$id/tag"(namespace: 'api', controller: 'serviceItemRest', action: 'saveTags', method: 'POST')
        "/api/serviceItem/$serviceItemId/tag/$tagId"(namespace: 'api', controller: 'serviceItemRest', action: 'deleteTag', method: 'DELETE')

        "/admin" {
            controller = "administration"
        }
        "/admin/partner-stores" {
            controller="administration"
            action="partnerStores"
        }
        /**
         * START - FOR THEME CONTROLLER
         */
        "/images/$img_name**" {
            controller = "theme"
            action = "getImageURL"
        }
        "/themes" {
            controller = "theme"
            action = "getAvailableThemes"
        }
        /***
         * END - FOR THEME CONTROLLER
         */
        "/images/get/$id"(
            controller: 'images',
            action: 'get'
        )

        "/images/types/$id"(controller: "types", action: "image")

        "/public/images/get/$id"(
            controller: 'images',
            action: 'get'
        )
        "/types/imageDelete/$id"(
            controller:'types',
            action:'imageDelete'
        )
		"/type" {
                controller="types"
                action = [GET:"getTypesList"]
        }
        "/state" {
                controller="state"
                action = [GET:"performDoList"]
        }

        "/public/serviceItem/getServiceItemsAsJSON"(controller:"serviceItem", action:"getServiceItemsAsJSON")
		"/public/serviceItem/getOwfCompatibleItems"(controller:"serviceItem", action:"getOwfCompatibleItems")
        "/public/serviceItem/getOWFRequiredItems/$id"(controller:"relationship", action:"getOWFRequiredItems")
        "/public/serviceItem/getStats"(controller:"extServiceItem", action:"getStats")
        "/public/serviceItem/getHibernateStats"(controller:"extServiceItem", action:"getHibernateStats")
        "/public/serviceItem"(controller:"serviceItem", action:"getServiceItemsAsJSON")
		"/public/serviceItem/$id?"(controller:"serviceItem", action:"getDetailListingForServiceItemAsJSON")
        "/public/category/search"(controller:"category", action:"search")
        "/public/$controller"(action:"getListAsJSON")
        "/public/$controller/$id?"(action:"getItemAsJSON")


        "/public/extServiceItem/disable/$id"(
            controller: 'extServiceItem',
            action: 'disable'
        )
        "/public/extServiceItem/enable/$id"(
            controller: 'extServiceItem',
            action: 'enable'
        )
        "/public/extServiceItem" {
            controller = "extServiceItem"
            action = [GET: "getServiceItemsAsJSON", POST: "create"]
        }
        "/public/extServiceItem/$id" {
            controller = "extServiceItem"
            action = [GET: "getItemAsJSON", PUT: "update"]
        }
        "/public/extProfile" {
            controller = "extProfile"
            action = [GET: "getListAsJSON", POST: "create"]
        }
        "/public/extProfile/$id" {
            controller = "extProfile"
            action = [GET: "getItemAsJSON", PUT: "update"]
        }

        /**
         * For franchise store affiliated marketplace search
         */
        "/public/outsideSearch"(
            controller: "externalAccess",
            action: "search"
        )

        /**
         * For affiliated marketplace search
         */
        "/public/search"(
            controller: "search",
            action: "getListAsJSON"
        )

        "/public/openSearch"(
            controller: "search",
            action: "openSearch"
        )

        "/config.js" {
            controller="config"
            action="config"
        }

        // "500"(controller: "error", action: "serverError")

        "/public/openSearchDescriptor" {
            controller = "search"
            action = "openSearchDescriptor"
        }


        "/public/exportAll"(
            controller: "export",
            action: "exportAll"
        )

        "/public/exportData"(
            controller: "export",
            action: "exportData"
        )

        "/public/storeDescriptor" {
            controller = "descriptor"
            action = [GET: "storeDescriptor"]
        }


        "/public/store/attributes"(controller: "franchiseReporting", action: "getStoreAttributes")

        //Restful API here
        "/applicationConfiguration/configs/$id?" {
            controller = "applicationConfiguration"
            action = [GET: "list", PUT: "update"]
        }
        "/applicationConfiguration/configs/validate/$id?" {
            controller = "applicationConfiguration"
            action = [GET: "validateGroup"]
        }

        "/scoreCardItemResponse/save" (parseRequest: true) {
            controller = "scoreCardItemResponse"
            action = "save"
        }
        
        "/scoreCardItem" (parseRequest: true) {
            controller = 'scoreCardItem'
            action = [POST: 'save', GET: 'getScoreCardData']
        }
        
        "/scoreCardItem/$id" (parseRequest: true) {
            controller = 'scoreCardItem'
            action = [DELETE: 'delete', PUT: 'update', GET: 'show']
            constraints {
                id(matches:/\d+/)
            }
        }

        /**
         *  REST API for CRUD actions on a listing
         *  Initially only implementing POST  OP-1505
         * @param windowname True if client is making a dojo window.name request. Allows
         *  cross domain requests from a client
         *  GET = retrieve representation of resource
         *  POST = create new resource
         *  PUT = edit resource (update)
         *  DELETE = remove resource
         *
         */

        "/listing"(parseRequest: true) {
            controller = "listing"
            action = [GET: "list", POST: "save"]
        }

        "/listing/$id"(parseRequest: true) {
            controller = "listing"
            action = [GET: "show", PUT: "update", DELETE: "delete"]
            constraints {
                id(matches: /\d+/)
            }
        }

        "/rejectionListing/reject/${id}" (parseRequest: true) {
            controller = "rejectionListing"
            action = [PUT: 'reject']
        }
    }
}
