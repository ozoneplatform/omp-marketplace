define([
    'backbone',
    'underscore'
], function(Backbone, _) {

    return Backbone.Model.extend({

        // function or an object
        // {
        //     'create': 'create url',
        //     'read': 'read url',
        //     'update': 'update url',
        //     'delete': 'delete url'
        // }
        api: null,


        sync: function(method, model, options) {
            var api = _.result(this, 'api');

            if (this.jsonp) {
                options.dataType = 'jsonp';
            }

            if(api && !options.url && api[method]) {
                options.url = api[method];
            }

            return Backbone.Model.prototype.sync.call(this, method, model, options);
        }

    });

});
