define([
    './ChangelogCollection'
], function(ChangelogCollection) {
    'use strict';

    var SuperClass = ChangelogCollection;

    return SuperClass.extend({
        url: function() {
            return (this.context + '/api/profile/' + this.id + '/serviceItem/activity/');
        }
    });
});
