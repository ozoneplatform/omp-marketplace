define([
    'views/ListView',
    './ThemeView',
    'collections/ThemeCollection',
    'handlebars'
], function(ListView, ThemeView, ThemeCollection, Handlebars) {
    'use strict';

    var SuperClass = ListView,
        template = Handlebars.compile(
            '<h4>Theme</h4>' +
            '<p>The Alternative Discovery Page has one theme. Additional themes will be available in future releases.</p>' +
            '<ul></ul>');

    return SuperClass.extend({
        ItemView: ThemeView,
        className: 'themes',

        initialize: function() {
            this.collection = new ThemeCollection();
            this.collection.fetch();

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        render: function() {
            this.$el.html(template());
            this.$body = this.$('ul');

            return SuperClass.prototype.render.apply(this, arguments);
        }
    });
});
