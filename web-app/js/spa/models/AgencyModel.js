define([
    'backbone',
    'marketplace'
], function(Backbone, Marketplace){
    
    return Backbone.Model.extend({                    
    	urlRoot:  Marketplace.context + '/api/agency'
    }); 
    
});
