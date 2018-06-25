define([
    '../views/comment/Comment',
    'handlebars',
    'jquery'
], function(Comment, Handlebars, $) {
    'use strict';

    var SuperClass = Comment,
        linkTemplate = Handlebars.compile(
            '<a href="#quickview/{{serviceItemId}}/reviews"></a>'
        );

    return SuperClass.extend({
        title: function() {
            return this.model.get('serviceItem').title;
        },

        render: function() {
            var titleEl;

            SuperClass.prototype.render.apply(this, arguments);
            titleEl = this.$('.item-review-title');

            //create the link
            $(linkTemplate({ serviceItemId: this.model.get('serviceItem').id }))

                //insert the link tag before the title element
                .insertBefore(titleEl)

                //move the title element into the link tag
                .append(titleEl);
        }
    });
});
