define([
    './BaseCollection',
    'marketplace'
],
function(BaseCollection) {

    return BaseCollection.extend({

        url: function() {
            return (Marketplace.context + '/rejectionJustification/getListAsExt');
        }

    });

});