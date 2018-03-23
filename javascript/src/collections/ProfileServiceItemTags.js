define([
    '../models/ProfileServiceItemWithTags',
    'backbone',
    'underscore',
    'marketplace'
], function(ProfileServiceItemWithTags, Backbone, _, Marketplace) {
    'use strict';

    var SuperClass = Backbone.Collection;

    return SuperClass.extend({
        model: ProfileServiceItemWithTags,

        //the model of the profile whose tags we are fetching
        profile: null,

        initialize: function(models, options) {
            this.profile = options.profile;

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        //sort the ServiceItems by title. The tags are sorted server-side
        comparator: 'title',

        /**
         * The data comes in pivoted around ServiceItemTag, like this:
         * {
         *  id: 1,
         *  serviceItem: {
         *      id: 1,
         *      title: 'test listing'
         *  },
         *  tag: {
         *      id: 1,
         *      title: 'tag'
         *  }
         * }
         *
         * We want it to be pivoted around ServiceItem, so each element
         * is structured like this:
         * {
         *  title: 'test listing',
         *  id: 1,
         *  tags: [{
         *      id: 1
         *      title: 'tag'
         *  }]
         * }
         */
        parse: function(data) {
            //map from serviceItem id to service item title
            var titles = _.object(_.map(data, function(serviceItemTag) {
                    var si = serviceItemTag.serviceItem;

                    return [si.id, si.title];
                })),

                //a list of serviceItemId -> tag mappings.
                //serviceItemIds can appear multiple times here
                tagPairs = _.map(data, function(serviceItemTag) {
                    var si = serviceItemTag.serviceItem,
                        tag = serviceItemTag.tag;

                    return [si.id, tag];
                }),
                groupedTagPairs = _.groupBy(tagPairs, function(pair) { return pair[0]; }),

                //map object from serviceitem id to list of tags
                tagMap = _.object(_.map(groupedTagPairs, function(tagPairs, siId) {
                    var tags = _.flatten(_.map(tagPairs, function(pair) {
                        return pair[1];
                    }));

                    return [siId, tags];
                }));

            return _.map(titles, function(title, id) {
                return {
                    id: id,
                    title: title,
                    tags: tagMap[id]
                };
            });
        },

        url: function() {
            return Marketplace.context + '/api/profile/' + this.profile.id + '/tag';
        }
    });
});
