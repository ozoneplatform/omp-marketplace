define([
    './BaseCollection',
    '../models/Review'
],
function(BaseCollection, Review) {

    return BaseCollection.extend({

        model: Review,

        initialize: function (models, options) {
            BaseCollection.prototype.initialize.call(this, models, options);
            
            this.id = options.id;
            this.jsonp = !!options.jsonp;
            this.context = options.context;
        },

        url: function() {
            return (this.context + '/itemComment/commentsByServiceItem/' + this.id);
        }

    });

});
