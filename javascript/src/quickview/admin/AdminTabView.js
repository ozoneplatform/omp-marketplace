define([
    '../../views/tabbable/TabPaneView',
    './SettingsView',
    '../../views/changelog/ChangelogView',
    'handlebars',
    'jquery',
    'underscore',
    'bootstrap'
],
function(TabPaneView, SettingsView, ChangelogView, Handlebars, $, _) {

    return TabPaneView.extend({

        className: 'row quickview-admin',

        title: 'Admin',

        href: function () {
            return 'quickview-' + this.title.toLowerCase();
        },

        // ServicieItem model
        model: null,

        layout: '<div class="span4"></div>' +
                '<div class="span8"></div>',

        initialize: function (options) {
            TabPaneView.prototype.initialize.call(this, options);

            this.settingsView = new SettingsView(this.options);
            this.changelogView = new ChangelogView(this.options);
        },

        render: function () {
            this.$el.append(this.layout);
            this.$('.span4').append(this.settingsView.render().el);
            this.$('.span8').append(this.changelogView.render().el);
            return this;
        },

        remove: function () {
            this.settingsView.remove();
            this.changelogView.remove();
            TabPaneView.prototype.remove.call(this);
            return this;
        }

    });

});
