define([
    '../views/ListView',
    '../tag/views/TagView',
    'handlebars',
    'underscore'
], function(ListView, TagView, Handlebars, _) {
    'use strict';

    var SuperClass = ListView,
        MyTagView = TagView.extend({
            allowDelete: function() {
                return TagView.prototype.allowDelete.call(this) ||
                    this.model.get('createdBy').id === 'self';
            }
        }),

        //a view for each row
        MyTagsRow = SuperClass.extend({
            tagName: 'tr',

            ItemView: MyTagView,

            serviceItemModel: null,

            initialize: function(options) {
                var ops = {
                    collection: options.model.tags()
                };

                this.serviceItemModel = options.model;

                //pass ops up the chain, not options
                this.options = ops;
                SuperClass.prototype.initialize.call(this, ops);
            },

            render: function() {
                this.$el.append(rowTemplate(this.serviceItemModel.attributes));
                this.$body = this.$('.tags');

                return SuperClass.prototype.render.apply(this, arguments);
            },

            removeOne: function() {
                //remove rows for listings where the last tag was deleted
                if (this.collection.size() === 0) {
                    this.remove();
                }
            }
        }),
        template = Handlebars.compile(
            '<caption>' +
                '<h4>My Tags<span class="count"></span></h4>' +
                '<h6>' +
                    '{{#if empty}}' +
                        '{{#if isSelf}}' +
                            'You have' +
                        '{{else}}' +
                            'This user has' +
                        '{{/if}}' +
                        ' not added any tags.' +
                    '{{else}}' +
                        '{{#if isSelf}}' +
                            'Tags you added.' +
                        '{{else}}' +
                            'Tags added by this user.' +
                        '{{/if}}' +
                    '{{/if}}' +
                '</h6>' +
            '</caption>' +
            '<thead><tr><th>Listing</th><th class="tags">Tags</th></tr></thead>' +
            '<tbody></tbody>'
        ),
        rowTemplate = Handlebars.compile('<td class="service-item">' +
            '<a href="#quickview/{{id}}">{{title}}</a>' +
        '</td>' +
        '<td class="tags"></td>');

    return SuperClass.extend({
        className: 'my-tags',

        tagName: 'table',

        ItemView: MyTagsRow,

        isSelf: null,

        emptyText: '',

        initialize: function(options) {
            var ops = {
                collection: options.model.serviceItemTags()
            };

            this.isSelf = options.model.isSelf();

            this.options = ops;
            SuperClass.prototype.initialize.call(this, ops);

            this.collection.fetch().done(_.bind(this.render, this));
        },

        render: function() {
            this.$el.append(template({
                isSelf: this.isSelf,
                empty: this.collection.size() === 0
            }));

            this.updateCount();

            this.$body = this.$('tbody');

            return SuperClass.prototype.render.apply(this, arguments);
        },

        showEmptyText: function() {
            this.$('caption').append('<div class="empty-text">' + this.emptyText + '</div>');
            this.$('thead').css('display', 'none');
        },

        hideEmptyText: function() {
            this.$('caption .empty-text').remove();
            this.$('thead').css('display', '');
        },

        removeOne: function() {
            this.updateCount();
            SuperClass.prototype.removeOne.apply(this, arguments);
        },

        updateCount: function() {
            //counts of tags on each listing
            var tagCounts = this.collection.map(function(siTag) {
                    return siTag.tags().size();
                }),
                total = _.reduce(tagCounts, function(a,b) { return a + b; }) || 0;

            this.$('.count').text(total);
        }
    });
});
