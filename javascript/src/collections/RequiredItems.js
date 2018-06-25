define([
    './BaseCollection',
    '../models/RequiredItem',
    'underscore',
    'jquery'
],
function(BaseCollection, RequiredItem, _, $) {
    'use strict';

    var SuperClass = BaseCollection;

    return SuperClass.extend({

        model: RequiredItem,

        isAffiliated: false,

        initialize: function (models, options) {
            SuperClass.prototype.initialize.call(this, models, options);
            this.context = options.context;
            this.id = options.id;

            if (options.isAffiliated) {
                this.isAffiliated = true;
            }
        },

        /**
         * For Affiliated, try to use both the normal URL, and the fallback URL in parallel.
         * Ideally we would try the new URL first and only use the fallback if it fails, But
         * unfortunately JSONP does not call any of the callbacks if there is a 404
         */
        sync: function(method, model, options) {
            //if either call succeeds, resolve the deferred
            function success() {
                //ensure that we only resolve once
                if (deferred.state() === 'pending') {
                    deferred.resolve.apply(deferred, arguments);
                }
            }

            //if both calls fail, reject the deferred
            function failure() {
                rejectedCount++;
                if (rejectedCount === 2) {
                    deferred.reject.apply(deferred, arguments);
                }
            }

            //for affiliated stores, try the old URL as well
            if (this.isAffiliated) {
                var deferred = $.Deferred(),
                    rejectedCount = 0;

                //sync with the normal URL
                SuperClass.prototype.sync.call(this, method, model, options)
                    .then(success, failure);

                //sync with the fallback URL
                SuperClass.prototype.sync.call(this, method, model, _.extend({
                    url: this.fallbackUrl()
                }, options)).then(success, failure);

                return deferred.promise();
            }

            //for non-affiliated, just do a normal sync
            else {
                return SuperClass.prototype.sync.apply(this, arguments);
            }
        },

        url: function() {
            return this.context + '/api/serviceItem/' + this.id + '/requiredServiceItems/';
        },

        fallbackUrl: function() {
            return this.context + '/public/serviceItem/getRequiredItems/' + this.id;
        },

        parse: function() {
            var me = this;

            //if this listing requires itself, it will appear in the results of the old call.
            //Filter it out.  Note that the new call never returns the listing itself
            return _.reject(SuperClass.prototype.parse.apply(this, arguments), function(it) {
                return it.id === me.id;
            });
        }
    });
});
