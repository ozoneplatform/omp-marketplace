define([
    '../models/Agency',
    './RestCollection'
], function(Agency, RestCollection) {
    var SuperClass = RestCollection;

    return SuperClass.extend({
        model: Agency,
        url: function() {
            return (Marketplace.context + '/api/agency');
        }
    });
});
