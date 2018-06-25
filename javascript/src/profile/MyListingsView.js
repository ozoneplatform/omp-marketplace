define([
    '../views/BaseView',
    '../views/CategorizedServiceItemList',
    '../collections/ProfileServiceItems',
    'backbone',
    'handlebars',
    'underscore',
    'jquery'
], function(BaseView, CategorizedServiceItemList, ProfileServiceItems, Backbone, Handlebars,
        _, $) {
    'use strict';

    var SuperClass = BaseView,
        titleTemplate = Handlebars.compile(
            '<h4>{{title}}<span class="count">{{total}}</span></h4>' +
            '<h6>' +
                '{{#if empty}}' +
                    '{{#if ownListings}}' +
                        'You do' +
                    '{{else}}' +
                        'This user does' +
                    '{{/if}}' +
                    ' not own any listings.' +
                '{{else}}' +
                    '{{#if ownListings}}' +
                        'Listings you own.' +
                    '{{else}}' +
                        'Listings owned by this user.' +
                    '{{/if}}' +
                '{{/if}}' +
            '</h6>'
        ),
        groupHeaderTemplate = Handlebars.compile(
            '<h5>{{groupName}}<span class="count">{{groupSize}}</span></h5>'),
        statusOrder = ['In Progress', 'Pending', 'Rejected', 'Approved'],

        /**
         * Primary Sort: Approval Status as dictated by statusOrder
         * Secondard Sort: type
         * Teritary Sort: title
         */
        comparator = function(a, b) {
            var aStatus = a.get('approvalStatus'),
                bStatus = b.get('approvalStatus');

            return (statusOrder.indexOf(aStatus) - statusOrder.indexOf(bStatus)) ||
                    a.get('types').title.localeCompare(b.get('types').title) ||
                    a.get('title').localeCompare(b.get('title'));
        };

    return SuperClass.extend({
        className: 'my-listings',

        childViews: [],

        //private variable to handle duplicate render calls
        initialFetchComplete: false,

        initialize: function(options) {
            var me = this,
                superClassOptions = _.clone(this.options);

            this.profileModel = options.model;

            //do not pass model to super class initialize
            superClassOptions.model = undefined;

            superClassOptions.collection = new ProfileServiceItems([], {
                profileUrl: _.result(this.profileModel, 'url'),
                comparator: comparator
            });

            this.options = superClassOptions;
            SuperClass.prototype.initialize.call(this, superClassOptions);

            this.collection.fetch().done(function() {
                me.initialFetchComplete = true;
            }).done(_.bind(this.render, this));
        },

        render: function() {
            //if the initial fetch is not complete, ignore render calls
            if (!this.initialFetchComplete) {
                return this;
            }

            var $el = this.$el,

                //the total ServiceItem count
                total = this.collection.size(),

                //the ServiceItems grouped by approvalStatus
                statusGroups = this.collection.groupBy(function(si) {
                    return si.get('approvalStatus');
                }),

                //pairs containing the name of the group, and the view
                groupViews = _.map(statusGroups, function(items, groupName) {
                    return [groupName, new CategorizedServiceItemList({
                        collection: new Backbone.Collection(items)
                    })];
                }),

                //dom elements containing the subviews for each group
                groupEls = _.map(groupViews, function(tuple) {
                    var listView = tuple[1],
                        groupName = tuple[0];

                    return $('<div class="status-group">')
                        .append(groupHeaderTemplate({
                            groupName: groupName,
                            groupSize: listView.collection.size()
                        }))
                        .append(listView.render().$el);
                });

            //clean up previous renders
            _.each(this.childViews, function(view) {
                if (view.isRendered()) {
                    view.remove();
                }
            });
            $el.empty();

            //add the title
            $el.append(titleTemplate({
                title: 'Listings',
                ownListings: this.profileModel.isSelf(),
                empty: total === 0,
                total: total
            }));

            //add the ServiceItem lists
            _.each(groupEls, function(el) {
                $el.append(el);
            });

            //remember the childViews so that we can call remove on them when we are removed
            this.childViews = _.map(groupViews, function(tuple) { return tuple[1]; });

            return this;
        },

        remove: function() {
            _.each(this.childViews, function(view) { view.remove(); });
            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
