define([
    '../views/BaseView',
    'underscore'
], function(BaseView, _) {
    'use strict';

    var SuperClass = BaseView;

    return SuperClass.extend({
        // the header view
        headerView: null,

        //A backbone view constructor for the header of
        //the window
        HeaderViewClass: null,

        //arguments to pass into the header view
        headerViewArgs: null,

        // the body view
        bodyView: null,

        //A backbone view constructor for the body of the window
        BodyViewClass: null,

        //Arguments to pass into the body view
        bodyViewArgs: null,

        //a Handlebars template to render if the model cannot be fetched. This template
        //should accept the model attributes as its data
        failureTemplate: null,

        //the model property name from which to pull this view's title
        titleProperty: 'title',

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.headerView = new this.HeaderViewClass(_.extend({
                model: this.model,
                href: this.href
            }, this.headerViewArgs));

            this.bodyView = new this.BodyViewClass(_.extend({
                model: this.model,
                href: this.href
            }, this.bodyViewArgs));

            this.model.fetch()
                .done(_.bind(this.render, this))
                .fail(_.bind(this.renderFailureTemplate, this));
        },

        render: function() {
            this.$el.empty()
                .append(this.headerView.render().$el)
                .append(this.bodyView.render().$el);

            return this;
        },

        renderFailureTemplate: function() {
            this.$el.empty().append(this.failureTemplate(this.model.attributes));
        },

        remove: function() {
            this.headerView.remove();
            this.bodyView.remove();

            SuperClass.prototype.remove.apply(this, arguments);
        },

        getTitle: function() {
            return this.model.get(this.titleProperty);
        }
    });
});
