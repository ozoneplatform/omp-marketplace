define([
    '../../collections/BaseCollection',
    '../models/AgencyModel',
    'marketplace'
], function(BaseCollection, AgencyModel, Marketplace){

    return BaseCollection.extend({

        model: AgencyModel,

        url:  Marketplace.context + '/api/agency'

    });

});
