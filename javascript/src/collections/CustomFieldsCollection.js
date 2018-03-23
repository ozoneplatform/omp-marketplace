define([
    './BaseCollection'
],
function(BaseCollection) {

    return BaseCollection.extend({

        initialize: function (models, options) {
            BaseCollection.prototype.initialize.call(this, models, options);

            this.context = options.context;
        },

        url: function() {
            return (this.context + '/public/customFieldDefinition');
        }

    });

});
