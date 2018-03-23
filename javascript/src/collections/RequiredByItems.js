define([
    './RequiredItems'
], function(RequiredItems) {
    'use strict';

    var SuperClass = RequiredItems;

    //same structures as the required items collection, but use the URL that gets the
    //items that this service item is required by.
    return SuperClass.extend({
        url: function() {
            return this.context + '/api/serviceItem/' + this.id + '/requiringServiceItems/';
        },

        fallbackUrl: function() {
            return this.context + '/relationship/getRequiredByItems/' + this.id;
        }
    });
});
