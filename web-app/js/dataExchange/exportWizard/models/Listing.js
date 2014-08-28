define(
    [
        'backbone',
        'jquery',
        'underscore'
    ],
    function(Backbone, $, _) {

        var SuperClass = Backbone.Model;

        return SuperClass.extend({

            url: function() {
                return (Marketplace.context + '/public/ServiceItem/' + this.id);
            }

        });

    });