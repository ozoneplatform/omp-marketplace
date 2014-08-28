define([
    '../../views/BaseView',
    '../../models/Review',
    'backbone',
    'jquery',
    'underscore',
    'handlebars',
    'raty'
],
function (BaseView, ReviewModel, Backbone, $, _, Handlebars, Raty) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'current-user-review',

        tpl: Handlebars.compile(
            '<h4>Review this listing:</h4>' +
            '<span class="current-user-rating"></span>' +
            '<textarea class="current-user-comment" placeholder="{{freeTextWarning}}">{{text}}</textarea>' +
            '<div class="user-review-actions invisible btn-toolbar">' +
                '<button class="btn btn-primary">Publish</button>' +
                '<button class="btn cancel">Cancel</button>' +
            '</div>' +
            '{{#if freeTextWarning}}' +
                '<p class="free-text-warning">{{freeTextWarning}}</p>' +
            '{{/if}}'
        ),

        serviceItemModel: null,

        events: {
            'keyup .current-user-comment': 'onCommentChange',
            'click .btn-primary': 'publish',
            'click .cancel': 'render'
        },

        reviewCharacterLimit: 250,

        affiliatedReviewText: 'You cannot review listings from Affiliated Stores.',

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);
            if(this.model) {
                this.bindModelEvents();
            }
        },

        bindModelEvents: function () {
            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'destroy', function () {
                delete this.model;
                this.render();
            });
        },

        render: function () {
            var freeTextWarning = _.findWhere(Marketplace.appconfigs, { code: 'free.warning.content' });

            var markup = this.tpl({
                text: this.model ? this.model.get('text') : '',
                freeTextWarning: this.serviceItemModel.get('isAffiliated') ? '' : freeTextWarning.value,
                placeholder: this.serviceItemModel.get('isAffiliated') ? '' : 'Write a comment (optional)'
            });
            this.$el.html(markup);

            this.serviceItemModel.get('isAffiliated') && this.renderMask();

            // jQuery stuff
            this.$comment = this.$el.children('textarea');
            this.$rating = this.$el.children('.current-user-rating').raty({
                hints: ['', '', '', '', ''],
                score: (this.model && this.model.get('userRate')) || 0,
                click: _.bind(this.enableActions, this)
            });
            this.$actions = this.$el.children('.user-review-actions');

            return this;
        },

        renderMask: function () {
            this.$el.append($('<div></div>', {
                'class': 'mask'
            }));
            this.$el.append($('<div></div>', { 'class': 'mask-text'}).html(this.affiliatedReviewText));
        },

        onCommentChange: function (evt) {
            var value = this.$comment.val();

            if(value.length > this.reviewCharacterLimit) {
                this.$comment.val(value.substr(0, this.reviewCharacterLimit));
            }

            this.enableActions();
        },

        enableActions: function () {
            this.$actions.removeClass('invisible');
        },

        hideActions: function () {
            this.$actions.addClass('invisible');
        },

        publish: function () {
            if(this._saving) { return; }

            var me = this,
                comment = me.$comment.val(),
                rating = me.$rating.raty('getScore'),
                review, data, options, xhr;

            // trim, handles copy and paste case
            if(comment.length > this.reviewCharacterLimit) {
                comment = comment.substr(0, this.reviewCharacterLimit);
                this.$comment.val(comment);
            }

            review = this.model || new ReviewModel({
                serviceItemId: this.serviceItemModel.get('id')
            });

            data = {
                text: comment,
                userRate: rating
            };

            options = {
                success: function (model) {
                    if(!me.model) {
                        me.model = model;
                        me.collection.unshift(model);
                        me.bindModelEvents();
                    }
                }
            };

            this._saving = true;
            xhr = review.save(data, options);
            if(xhr) {
                xhr.always(function () {
                    me._saving = false;
                });
            } else {
                this._saving = false;
            }
        }
    });
});
