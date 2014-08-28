define([
    'views/BaseView',
    'views/LoadMask',
    'handlebars',
    'underscore',
    'jquery',
    'marketplace',
    'jquery.magnific-popup'
], function(BaseView, LoadMask, Handlebars, _, $, Marketplace) {
    'use strict';

    var SuperClass = BaseView,
        template = Handlebars.compile(
            '<h5>{{display_name}}</h5>' +
            '<a class="screenshots">' +
                '{{#each screenshots}}' +
                    '<img src="{{this.url}}" ' +
                        '{{#if this.description}}' +
                            'data-title="{{this.description}}"' +
                        '{{/if}}' +
                        '/>' +
                '{{/each}}' +
            '</a>' +
            '<fieldset>' +
                '<button class="btn btn-primary apply-theme-btn">Apply</button>' +
                '<button disabled="disabled" class="btn active-theme-btn">' +
                    'Active' +
                '</button>' +
            '</fieldset>'
        ),
        activeThemeClass = 'active-theme';

    return SuperClass.extend({
        tagName: 'li',

        events: {
            'click .apply-theme-btn': 'applyTheme',
            'click .screenshots': 'showImageDetail'
        },

        render: function() {
            var attrs = _.extend({}, this.model.attributes, {
                screenshots: this.getScreenshots()
            });

            this.$el.html(template(attrs));

            this.createImageTooltips();

            //determine if this is the current theme
            if (this.model.get('name') === Marketplace.theme.name) {
                this.$el.addClass(activeThemeClass);
            }

            return this;
        },

        createImageTooltips: function() {
            this.$('.screenshots > img[data-title]').tooltip({
                delay: 750
            });
        },

        applyTheme: function() {
            LoadMask.show();
            $.ajax({
                url: Marketplace.context + '/theme/selectTheme',
                method: 'post',
                data: {theme: this.model.get('name')}
            })
            .done(function () {
                window.location.reload();
            })
            .fail(function () {
                Marketplace.error('Error applying theme');
                LoadMask.hide();
            });
        },

        showImageDetail: function(e) {
            var index = $(e.target).index(),
                screenshots = _.map(this.getScreenshots(), function(screenshot) {
                    return { src: screenshot.url };
                });

            $.magnificPopup.open({
                type: 'image',
                gallery: {
                    enabled: true
                },
                items: screenshots
            }, index);
        },

        /**
         * Creates a list of screenshot objects with normalized
         * URLs and descriptions.  If the "thumb" is a separate url, it
         * is also included, but it is ignored if it is a duplicate
         * of one of the "screenshots"
         */
        getScreenshots: function() {
            var thumb = this.model.get('thumb'),
                screenshots = this.model.get('screenshots'),
                thumbIsDupe = !!(_.find(screenshots, function(screenshot) {
                    return thumb == null || screenshot.url === thumb;
                })),
                screenshotObjs = thumbIsDupe ? screenshots : [{
                    url: thumb
                }].concat(screenshots);

            return _.map(screenshotObjs, function(screenshot) {
                return _.extend({}, screenshot, {
                    url: Marketplace.context + '/' + screenshot.url
                });
            });
        },

        remove: function() {
            this.$('.screenshots > img[data-title]').tooltip('destroy');
            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
