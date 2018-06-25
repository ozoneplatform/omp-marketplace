define([
    '../views/tabbable/TabPaneView',
    './ThemeSwitcher',
    'views/ErrorPopover',
    'handlebars',
    'underscore',
    'jquery'
], function(TabPaneView, ThemeSwitcher, ErrorPopover, Handlebars, _, $) {
    'use strict';

    var SuperClass = TabPaneView,
        preferencesTemplate = Handlebars.compile('<div class="preferences">' +
            '<h4>Preferences</h4>' +
            '<div class="preference animations-preference">' +
                'Enable Animations' +
                '<input class="animations ios brand-success" type="checkbox" ' +
                    '{{#if animationsEnabled}}checked{{/if}} />' +
                '<div class="description">' +
                    'Allow animations in the store. (Note: This may impact performance.)' +
                '</div>' +
            '</div>' +
        '</div>'),
        preferencesSaveErrorMessage = 'There was an error saving your preferences.';

    return SuperClass.extend({
        title: 'Preferences',

        events: {
            'change .animations': 'setAnimationsPreference',
            'change .spa': 'setSPAPreference',
            'click .preferences .popover button': 'closePreferencesPopover'
        },

        render: function() {
            SuperClass.prototype.render.apply(this, arguments);

            this.$el.append(preferencesTemplate(this.model.attributes));
            this.$('input[type=checkbox]').svitch();

            this.themeSwitcher = this.makeThemeSwitcher();
            this.$el.append(this.themeSwitcher.$el);
            return this;
        },

        makeThemeSwitcher: function() {
            return new ThemeSwitcher().render();
        },

        setAnimationsPreference: function(e) {
            var $el = $(e.target),
                value = $el.is(':checked'),
                url = window.Marketplace.context + '/preferences/enableAnimations';

            $.post(url, { enableAnimations: value })
                .fail(_.bind(this.preferencesErrorPopover, this));
        },

        preferencesErrorPopover: function() {
            this.preferencesErrPopover = (new ErrorPopover({
                message: preferencesSaveErrorMessage,
                title: 'Preferences Save Error',
                placement: 'bottom',
                el: this.$('.animations-preference')[0]
            })).render();
        },

        closePreferencesPopover: function() {
            if (this.preferencesErrPopover) {
                this.preferencesErrPopover.remove();
            }
        },

        remove: function() {
            this.closePreferencesPopover();
            this.themeSwitcher.remove();

            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
