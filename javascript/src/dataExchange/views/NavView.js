define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    './WizardBaseView',
    './NavItemView'
], function($, _, Backbone, Handlebars, BaseView, NavItemView){

    var SuperClass = BaseView;

    var navView = SuperClass.extend({

        tagName: 'ul',
        className: 'nav nav-list',
        template: Handlebars.compile('<div class="test">{{title}}</div>'),

        initialize: function() {

        },

        render: function() {
            var me = this;
            var items = me.model.get('cards');
            me.subViews = [];

            _.each(items, function(value, key, list) {

                var itemView = new NavItemView({model: list.at(key)}) ;
                me.$el.append(itemView.render().el);
                me.subViews.push(itemView); // save it so we can destroy it when done

            });
            return me;
        },

        remove: function () {

            _.each(this.subViews, function(view) {
                view.remove();
            })
            return SuperClass.prototype.remove.call(this);
        }

    });

    return navView;
});