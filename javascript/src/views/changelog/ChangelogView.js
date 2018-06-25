define([
    '../BaseView',
    '../ListView',
    '../LoadMoreListView',
    './ChangelogItemView',
    'handlebars',
    'bootstrap'
], function(BaseView, ListView, LoadMoreListView, ChangelogItemView, Handlebars) {
    'use strict';

    var ChangelogListView = ListView.extend({
        tagName: 'ul',

        className: 'unstyled list-group',

        ItemView: ChangelogItemView
    });

    var ChangelogLoadMoreView = LoadMoreListView.extend({
        ListView: ChangelogListView
    });

    var headerTemplate = Handlebars.compile('<h4>{{title}}</h4>');

    return BaseView.extend({

        className: 'changelog-pane',

        //overridable via initialize options
        title: 'Changelog',

        // ServiceItem model
        model: null,

        initialize: function (options) {
            BaseView.prototype.initialize.call(this, options);

            this.collection = this.model.changelogs();
            this.collection.fetch();
            this.changelogs = new ChangelogLoadMoreView({
                collection: this.collection
            });
        },

        render: function () {
            this.$el
                .append(headerTemplate({ title: this.title }))
                .append(this.changelogs.render().el);

            return this;
        },

        remove: function () {
            this.changelogs.remove();
            BaseView.prototype.remove.call(this);
            return this;
        }

    });
});
