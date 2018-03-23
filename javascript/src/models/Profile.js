define([
    '../collections/ProfileChangelogCollection',
    '../collections/ProfileServiceItemTags',
    'backbone',
    'marketplace'
], function(ProfileChangelogCollection, ProfileServiceItemTags, Backbone, Marketplace) {
    'use strict';

    var SuperClass = Backbone.Model;

    return SuperClass.extend({
        urlRoot: Marketplace.context + '/api/profile/',

        changelogCollection: null,
        serviceItemTagsCollection: null,

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.changelogCollection = new ProfileChangelogCollection([], {
                id: this.id,
                context: Marketplace.context
            });

            this.serviceItemTagsCollection = new ProfileServiceItemTags([], {
                profile: this
            });
        },

        changelogs: function() {
            return this.changelogCollection;
        },

        serviceItemTags: function() {
            return this.serviceItemTagsCollection;
        },

        //return true if the user represented by this model is the currently logged
        //in user
        isSelf: function() {
            return parseInt(this.id, 10) === window.Marketplace.user.id ||
                this.id === 'self';
        },

        isAdmin: function() {
            return Marketplace.user.isAdmin;
        }
    });
});
