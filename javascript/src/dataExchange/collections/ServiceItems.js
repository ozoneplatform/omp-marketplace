define(
    [
        'backbone',
        'jquery',
        'underscore'
    ],
    function(Backbone, $, _) {

        var SuperClass = Backbone.Collection;

        return SuperClass.extend({

//            model: ServiceItem,

            url: function() {
                return (Marketplace.context + '/listing/');
            },

            parse: function (resp) {
                return resp.data;
            }

        });

    });