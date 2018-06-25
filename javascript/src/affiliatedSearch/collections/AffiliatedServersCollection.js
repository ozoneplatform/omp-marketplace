define([
    '../../collections/BaseCollection',
    '../models/AffiliatedServersModel',
    'marketplace'
], function (BaseCollection, AffiliatedServersModel, Marketplace) {

    return BaseCollection.extend({

        model: AffiliatedServersModel,

        url:  Marketplace.context + '/affiliatedMarketplace/listAsJSON',

        parse: function (response) {
            return response.data;
        }

    });

});
