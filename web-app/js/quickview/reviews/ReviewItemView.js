/*
 * Copyright 2013 Next Century Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

define(
[
    '../../views/BaseView',
    '../../models/Review',
    'backbone',
    'jquery',
    'underscore',
    'handlebars',
    'moment',
    'marketplace',
    'bootstrap-editable',
    'raty'
],
function(BaseView, ReviewModel, Backbone, $, _, Handlebars, moment, Marketplace) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'item-review',

        tpl:    Handlebars.compile(
                    '<span class="item-user-rating"></span>' +
                    '<h6 class="item-review-author">' +
                        '{{#if authorLink}}' +
                            '<a href="#profile/{{id}}">{{displayName}}</a>' +
                        '{{else}}' +
                            '{{displayName}}' +
                        '{{/if}}' +
                    '</h6>' +
                    '<span class="item-review-date"></span><br>' +
                    '<p class="review-text">{{text}}</p>' +
                    '<div class="delete-review">' +
                        '<button class="btn btn-small btn-primary delete-review-btn">' +
                            '<i class="icon-minus"></i>' +
                        '</button>' +
                    '</div>'
                ),

        isAffiliated: false,

        data: function () {
            var attributes = _.extend({}, this.model.attributes);

            //we want to provide placeholder for empty text, but not if the field is going to be
            //editable. In that case we let the editable plugin handle it.
            if($.trim(attributes.text) === '' && !this.reviewsAreEditable) {
                attributes.text = 'No comment provided.';
            }

            //do not make the author name a link if this is an affiliated quickview
            attributes.authorLink = !this.isAffiliated;

            //the older (non-REST) server call that is currently used for some reason
            //returns an id and a userId, and the id is wrong.  To future-proof for
            //a more reasonable server call though, allow the use of id if userId doesn't
            //exist
            attributes.id = attributes.userId === undefined ? attributes.id : attributes.userId;

            return attributes;
        },

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);

            this.listenTo(this.model, 'change', this.update);
            this.listenTo(this.model, 'destroy', this.remove);
            this.reviewsAreEditable = Marketplace.user.isAdmin && !this.isAffiliated;
        },

        events: function () {
            var eventMap = {};

            if(this.reviewsAreEditable) {
                _.extend(eventMap, {
                    'mouseenter' : 'cancelConfirmations'
                });
            }

            return eventMap;
        },

        render: function () {
            var me = this;

            this.$el.html( this.tpl(this.data()) );

            this.$rating = this.$el.children('.item-user-rating').raty({
                hints: ['', '', '', '', ''],
                readOnly: true,
                score: this.model.get('userRate')
            });

            var $reviewDate = this.$el.children('.item-review-date');
            $reviewDate.html(moment(this.model.get('date')).fromNow());

            if(this.reviewsAreEditable) {
                this.$el.addClass('reviews-editable');

                this.$('.review-text').editable({
                    type: 'textarea',
                    mode: 'inline',
                    inputclass: 'item-review-editable',
                    showbuttons: 'bottom',
                    rows: 3,
                    emptyclass: 'item-review-no-comment',
                    emptytext: 'No comment provided.',
                    url: function (params) {
                        return me.model.save({ text: params.value });
                    },
                    validate: function (value) {
                        if($.trim(value) === '' && !me.model.get('userRate')) {
                            return 'You cannot submit an empty comment if there is no rating.';
                        }
                    }
                });

                this.$('.delete-review').confirm({
                    placement: 'top',
                    title: 'Remove Comment',
                    content: 'Clicking "OK" will remove this comment and rating from this listing.',
                    ok: function() {
                        me.model.destroy();
                    }
                });
            }

            return this;
        },

        update: function () {
            this.$rating.raty('destroy');
            this.render();
        },

        cancelConfirmations: function () {
            //Cancel any pending confirmation if we're moving onto another review
            this.$('.delete-review').confirm('cancelOthers');
        },

        remove: function () {
            this.$rating.raty('destroy');
            return SuperClass.prototype.remove.call(this);
        }
    });

});
