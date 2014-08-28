define([
    'backbone',
    './BaseCollection'
],

function (Backbone, BaseCollection) {
    var SuperClass = BaseCollection;

    return SuperClass.extend({
        url: Marketplace.context + '/api/tag'
    });
});
