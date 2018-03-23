define([
    './Reviews'
], function(Reviews) {
    'use strict';

    var SuperClass = Reviews;

    return SuperClass.extend({
        profileUrl: null,

        initialize: function(models, options) {
            this.profileUrl = options.profileUrl;

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        url: function() {
            return this.profileUrl + '/itemComment';
        }
    });
});
