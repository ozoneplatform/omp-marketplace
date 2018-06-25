define([
    'backbone'
], function(Backbone, ContactType) {

    var SuperClass = Backbone.Collection;

     return SuperClass.extend({
        //the total count in the paged collection
        totalCount: null,

        parse: function (response) {
            this.totalCount = response.totalCount;
            return response.data;
        }
    });
});

