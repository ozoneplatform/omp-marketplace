package marketplace.listing

import marketplace.AccountService
import marketplace.BaseMarketplaceRestController
import grails.converters.JSON

import marketplace.ImportStackService
import marketplace.ServiceItem
import marketplace.ServiceItemService


class ListingController extends BaseMarketplaceRestController {

    static allowedMethods = [
            list: 'GET',
//            show: 'GET',
//            update: 'PUT',
            save: 'POST',
//            delete: 'DELETE'
    ]

    ServiceItemService serviceItemService

    ImportStackService importStackService

    AccountService accountService

    def index() {
        redirect(action: "list", params: params)
        return
    }


    def list = {
        // For now, only provide the list to administrators. If we need to provide the list for
        // regular users in the future, we will have to filter it to only list the listings they
        // are allowed to see.
        if (accountService.isAdmin()) {
            def listings = ServiceItem.list()
            renderResult(listings, listings.size(), response.SC_OK)
        } else {
            renderResult([], 0, response.SC_OK)
        }
    }

    def show = {
        render "Retrieve one\n"
    }


    def save = {

        /**
         * Create a listing from JSON
         * Handles REST POST
         */

        try {
            log.debug "listing post rest call: params = ${params}"

            importStackService.validateJson(params.data)
            // Result is map where key item is the serviceItem and key isNew indicates whether or not the
            // service item was created new. False indicates that it already existed in the store. Key Msg is
            // text message returned.
            def results = serviceItemService.createListing(params.data)

            if (results?.success) {
                def count = 1

                // Use renderResult since it automatically handles request with window-name=true
                renderResult([id: results.item.id, isNew: results.isNew, msg: results.msg], count, response.SC_OK)

            } else {

                if (results) {
                    renderResult([msg: results.message], 0, response.SC_BAD_REQUEST)

                } else {
                    renderResult([msg: "Request did not contain valid OZONE App JSON"], 0, response.SC_BAD_REQUEST)
                }


            }


        } catch (IllegalArgumentException e) {

            handleExpectedException(e, 'Store Create Listing', response.SC_BAD_REQUEST)


        } catch (Exception e) {
            log.error message(code: "sic.log.error.exceptionOccurred", args: ["${e.getMessage()}"])
            log.error('createListing:', e)
            handleException(e, 'createListing')
        }
    }


    def update = {

        render "Update\n"
    }

    def delete = {

        render "Delete\n"
    }
}
