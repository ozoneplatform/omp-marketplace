define([
    'collections/BaseCollection',
    'marketplace'
], function(BaseCollection, Marketplace) {
    'use strict';

    var SuperClass = BaseCollection;

    return SuperClass.extend({
        url: Marketplace.context + '/themes/'
    });
});
