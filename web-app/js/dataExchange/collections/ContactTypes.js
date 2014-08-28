define([
    '../models/ContactType',
    './RestCollection'
], function(ContactType, RestCollection) {
    var SuperClass = RestCollection;

    return SuperClass.extend({
        model: ContactType,
        url: function() {
            return (Marketplace.context + '/api/contactType');
        }
    });
});
