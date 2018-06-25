define([
    './PaginatedCollection',
    '../models/ServiceItem',
    'marketplace'
],
function(PaginatedCollection, ServiceItem, Marketplace) {

    return PaginatedCollection.extend({

        model: ServiceItem,

        url: Marketplace.context + '/public/serviceItem/getServiceItemsAsJSON'

    });

});