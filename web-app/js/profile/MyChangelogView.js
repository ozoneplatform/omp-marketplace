define([
    '../views/changelog/ChangelogView',
    'handlebars',
    'underscore'
], function(ChangelogView, Handlebars, _) {
    'use strict';

    var SuperClass = ChangelogView,
        subTitleTemplate = Handlebars.compile('<h6>' +
            'Recent changes to ' +
            "{{#if isSelf}}your{{else}}this user's{{/if}}" +
            ' listings.' +
        '</h6>'),
        emptyText = '';

    // A ChangelogView with an extra subtitle
    return SuperClass.extend({
        title: 'Recent Activity',

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);

            //change some options on the listview nested within the superclass
            this.changelogs.listView.emptyText = emptyText;
            this.changelogs.listView.ItemView = this.changelogs.listView.ItemView.extend({
                globalActivity: true
            });
        },

        render: function() {
            SuperClass.prototype.render.apply(this, arguments);
            this.$('> h4').after(subTitleTemplate({ isSelf: this.model.isSelf() }));

            return this;
        }
    });
});
