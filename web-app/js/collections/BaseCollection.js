define([
    'backbone',
    'underscore',
    'jquery'
],
function(Backbone, _, $) {

    var SuperClass = Backbone.Collection;

    return SuperClass.extend({

        initialize: function (models, options) {
            this.jsonp = !!(options || {}).jsonp;

            // merge all options onto collection instance
            _.extend(this, this.options);
        },

        parse: function (resp) {
            return (resp.rows || resp.data || resp);
        },

        sync: function(method, model, options) {
            if (this.jsonp) {
                options.dataType = 'jsonp';
            }
            return SuperClass.prototype.sync.call(this, method, model, options);
        },

        fetchIfEmpty: function (options) {
            return this.length === 0 ? this.fetch(options) : $.Deferred().resolve().promise();
        }

    });

});
