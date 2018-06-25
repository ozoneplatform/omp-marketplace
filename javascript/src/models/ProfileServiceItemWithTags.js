define([
    '../collections/ServiceItemTagsCollection',
    'backbone',
    'marketplace',
    'underscore'
], function(ServiceItemTagsCollection, Backbone, Marketplace, _) {
    'use strict';

    var SuperClass = Backbone.Model;

    return SuperClass.extend({
        tagsCollection: null,

        profile: null,

        /**
         * @param options.profile A Profile model for the person who associated
         * this service item with this tag
         */
        initialize: function(attributes, options) {
            var me = this,
                createdBy = options.collection.profile.attributes,
                tagModels = _.map(this.get('tags'), function(tag) {
                    return {
                        serviceItemId: me.id,
                        tag: tag,
                        id: tag.id,
                        createdBy: createdBy
                    };
                });

            this.tagsCollection = new ServiceItemTagsCollection(tagModels, {
                id: this.id,
                context: Marketplace.context
            });

            this.listenTo(this.tagsCollection, 'remove', _.bind(this.checkEmptyTags, this));

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        tags: function() {
            return this.tagsCollection;
        },

        checkEmptyTags: function() {
            if (this.tagsCollection.size() === 0) {
                this.collection.remove(this);
            }
        }
    });
});
