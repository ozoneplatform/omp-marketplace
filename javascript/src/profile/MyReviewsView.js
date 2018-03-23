define([
    '../views/ListView',
    './Comment',
    '../collections/ProfileItemComments',
    'handlebars',
    'underscore'
], function(ListView, Comment, ProfileItemComments, Handlebars, _) {
    'use strict';

    var SuperClass = ListView,
        template = Handlebars.compile('<h4>' +
            'Reviews' +
            '<span class="count">{{total}}</span>' +
        '</h4>' +
        '<h6>' +
            '{{#if empty}}' +
                '{{#if isSelf}}' +
                    'You have' +
                '{{else}}' +
                    'This user has' +
                '{{/if}}' +
                ' not reviewed any listings.' +
            '{{else}}' +
                '{{#if isSelf}}' +
                    'Your' +
                '{{else}}' +
                    "This user's" +
                '{{/if}}' +
                ' reviews of listings.' +
            '{{/if}}' +
        '</h6>' +
        '<div class="my-reviews-body"></div>');

    return SuperClass.extend({
        className: 'my-reviews',

        ItemView: Comment,

        isSelf: null,

        emptyText: '',

        initialize: function(options) {
            var ops = {
                    collection: new ProfileItemComments(null, {
                        profileUrl: _.result(options.model, 'url')
                    })
                };

            this.isSelf = options.model.isSelf();

            this.options = ops;
            SuperClass.prototype.initialize.call(this, ops);

            this.collection.fetch().done(_.bind(this.render, this));
        },

        render: function() {
            var total = this.collection.size();

            this.$el.append(template({
                total: total,
                isSelf: this.isSelf,
                empty: total === 0
            }));
            this.$body = this.$('.my-reviews-body');

            return SuperClass.prototype.render.apply(this, arguments);
        }
    });
});
