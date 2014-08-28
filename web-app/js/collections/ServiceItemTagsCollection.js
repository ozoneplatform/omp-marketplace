define([
    './BaseCollection',
    '../models/ServiceItemTagModel'
],
function(BaseCollection, ServiceItemTagModel) {

    return BaseCollection.extend({

        model: ServiceItemTagModel,

        initialize: function (models, options) {
            BaseCollection.prototype.initialize.call(this, models, options);
            this.id = options.id;
            this.context = options.context;
        },

        url: function() {
            return (this.context + '/api/serviceItem/' + this.id + '/tag');
        }
    });

});
