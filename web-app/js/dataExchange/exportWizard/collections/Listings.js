define(
    [
        'exportWizard/models/Listing',
        'backbone',
        'jquery',
        'underscore'
    ],
    function(Listing, Backbone, $, _) {

        var SuperClass = Backbone.Collection;

        return SuperClass.extend({

            model: Listing,

            initialize: function (options) {
                this.id = options.id;
            },

            url: function() {
                return (Marketplace.context + '/public/ServiceItem/' + this.id);
            },

            parse: function (resp) {
                return resp.rows;
            }

        });

    });