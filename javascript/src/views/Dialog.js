define([
    './BaseView',
    'marketplace',
    'handlebars'
], function(BaseView, Marketplace, Handlebars) {
    'use strict';

    var SuperClass = BaseView,
        template = Handlebars.compile(
            '<h3>' +
                '<span class="title">{{title}}</span>' +
                '<span class="close close-dialog">&times;</span>' +
            '</h3>' +
            '<p>{{text}}</p>' +
            '<div class="dialog-footer">' +
                '<button class="close-dialog btn btn-primary btn-small">Close</button>' +
            '</div>'
        );

    var Dialog = SuperClass.extend({
        className: 'bootstrap-active dialog',

        events: {
            'click .close-dialog': 'remove'
        },

        //these properties should be set by
        //passing arguments to the constructor
        title: null,
        text: null,

        render: function() {
            this.$el.html(template({
                title: this.title,
                text: this.text
            }));

            return this;
        },

        show: function() {
            this.render().$el.appendTo(document.body);
        }
    });

    //static and globally accessible convenience methods for making dialogs
    Marketplace.showDialog = Dialog.show = function(title, text) {
        var dialog = new Dialog({title: title, text: text});
        dialog.show();

        return dialog;
    };

    return Dialog;
});
