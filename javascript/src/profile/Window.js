define([
    '../views/ModalView',
    './Contents'
], function(ModalView, Contents) {
    'use strict';

    var SuperClass = ModalView;

    return SuperClass.extend({
        className: SuperClass.prototype.className + ' profile-window',

        contents: null,

        footer: false,
        animate: false,

        initialize: function(options) {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.contents = new Contents(options);
        },

        render: function() {
            SuperClass.prototype.render.apply(this, arguments);

            this.$body.append(this.contents.$el);

            return this;
        },

        route: function() {
            this.contents.route.apply(this.contents, arguments);
        },

        remove: function() {
            this.contents.remove();
            SuperClass.prototype.remove.apply(this, arguments);
        },

        getTitle: function() {
            return this.contents.getTitle();
        }
    });
});
