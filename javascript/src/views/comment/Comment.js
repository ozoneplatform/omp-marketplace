define([
    '../BaseView',
    'handlebars',
    'moment',
    'underscore',
    'raty'
], function(BaseView, Handlebars, moment, _) {
    'use strict';

    var SuperClass = BaseView,
        template = Handlebars.compile(
            '<span class="item-user-rating"></span>' +
            '<span class="item-review-title">{{title}}</span>' +
            '<span class="item-review-date">{{date}}</span>' +
            '<p class="review-text">{{text}}</p>'
        );

    /**
     * This class represents a superclass of a quickview-like comment view.  This superclass
     * is designed to encapsulate commonality between the review views on the quickview and the
     * Profile window
     */
    return SuperClass.extend({
        className: ' comment',

        //a property or function to return the title to be displayed for this review
        title: null,

        $rating: null,

        render: function() {
            var me = this,
                attrs = _.extend({
                    title: _.result(this, 'title'),
                    date: moment(this.model.get('editedDate')).fromNow()
                }, this.model.attributes);

            this.$el.append(template(attrs));

            this.$rating = this.$el.children('.item-user-rating');

            //activate raty asynchronously as it only works after the element is attached
            //to the main DOM tree
            setTimeout(function() {
                me.$rating.raty({
                    hints: ['', '', '', '', ''],
                    readOnly: true,
                    score: me.model.get('rate')
                });
            }, 0);

            return this;
        },

        remove: function() {
            this.$rating.raty('destroy');

            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
