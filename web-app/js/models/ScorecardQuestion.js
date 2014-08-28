define([
    'backbone',
    'jquery',
    'underscore'
],
function(Backbone, $, _) {

    var SuperClass = Backbone.Model;

    return SuperClass.extend({

        defaults: {
            question: '',
            description: '',
            image: '',
            showOnListing: true
        },

        url: function () {
            return Marketplace.context + '/scoreCardItem/' + ((this.id)? this.id : '');
        }

    });

});
