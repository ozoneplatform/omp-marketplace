define([
    'marketplace',
    'backbone',
    'jquery',
    'underscore'
],
function(Marketplace, Backbone, $, _) {

    var SuperClass = Backbone.Model;

    return SuperClass.extend({

        url: function () {
            if(this.get('tag') !== undefined){
                return Marketplace.context + '/api/serviceItem/' + this.get('serviceItemId') + '/tag/'  + this.get('tag').id;
            } else{
                return Marketplace.context + '/api/serviceItem/' + this.get('serviceItemId') + '/tag/';
            }
        }

    });

});
