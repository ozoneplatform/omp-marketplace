define(
[
    '../views/tabbable/TabPaneView',
    'backbone',
    'bxslider',
    'handlebars',
    'jquery',
    'underscore',
    'jquery.magnific-popup'
],
function(TabPaneView, Backbone, BxSlider, Handlebars, $, _) {

    var SuperClass = TabPaneView;

    return SuperClass.extend({

        // the tab's title
        title: 'Overview',

        // this.el's CSS class
        className: 'quickview-overview',

        // the overview Handlebars template
        overviewTpl: Handlebars.compile(
            '{{#if screenshots}}' +
                '<div class="screenshots fixed-pane">' +
                    '<ul class="bxslider">' +
                        '{{#each screenshots}}' +
                            '<li class="bx-li">'+
                                '<img class="bx-img" src="{{smallImageUrl}}" />'+
                            '</li>' +
                        '{{/each}}' +
                    '</ul>' +
                    '<p class="enlarge">Click to enlarge</p>' +
                '</div>' +
            '{{else}}' +
                '<div class="screenshots no-screenshots fixed-pane">' +
                    'No screenshots provided.' +
                '</div>' +
            '{{/if}}' +
            '<div class="description {{#if noDescription}}no-description{{/if}}">' +
                '<p>{{{description}}}</p>' +
            '</div>'
        ),

        // the service item
        // which this QuickView displays
        serviceItemModel: null,

        // the screenshots
        $screenshots: null,

        carouselIndex: 0,

        events: {
            'click .bx-li': 'showScreenshots',
            'click .enlarge': 'showScreenshots'
        },

        /**
         * Renders the tab.
         * @returns {Overview} this for chaining purposes
         */
        render: function() {
            var overviewData = this.getOverviewData();
            this.$el.append(this.overviewTpl(overviewData));

            return this;
        },

        /**
         * Get the data needed to render the overview Handlebars template.
         * @returns {Object} the data needed to render the overview Handlebars template
         */
        getOverviewData: function() {
            var overviewData = this.serviceItemModel.toJSON();

            // description?
            if (
                overviewData.description &&
                !overviewData.description.match(/^\s*$/) // Apps pushed to the Store with no description end up having the following descirption: ' '
            ) {
                // escape HTML and
                // replace newlines with <br />
                var escapedDescription = Handlebars.Utils.escapeExpression(
                    overviewData.description
                );
                overviewData.description = escapedDescription.replace(/\n/g, '\n\n');
                overviewData.description = escapedDescription.replace(/\n/g, '<br />');
                overviewData.noDescription = false;
            }
            // no description?
            else {
                overviewData.description = 'No description provided.';
                overviewData.noDescription = true;
            }

            return overviewData;
        },

        /**
         * Gets the tab's href.
         * @returns {String} the tab's href
         */
        href: function () {
            return 'quickview-' + this.title.toLowerCase();
        },

        /**
         * Destroys the tab. Calls the super class.
         * @returns {Overview} this for chaining purposes
         */
        remove: function() {
            // screenshots?
            if (this.$screenshots && this.$screenshots.length > 0) {
                this.$screenshots.destroyShow();
            }

            return SuperClass.prototype.remove.call(this);
        },

        onActivateOnce: function() {
            this.$screenshots = this.$el.find('.bxslider');

            // grab whether to show the pagination dots
            var showPager = !_.isEmpty(this.serviceItemModel.get('screenshots'));

            if (this.$screenshots.length > 0) {

                this.$screenshots.bxSlider({
                    controls: true,
                    pager: showPager,
                    buildPager: function(slideIndex) {
                        // return an empty link
                        // so just the background image for the page will show
                        // but it will be properly styled
                        return '<a href=""></a>';
                    },
                    prevText: '',
                    nextText: '',
                    onAfterSlide: _.bind(function (i) {
                        this.carouselIndex = i;
                    }, this)
                });

                this.$el.find('.bx-prev').addClass('icon-chevron-left');
                this.$el.find('.bx-next').addClass('icon-chevron-right');
            }

            return this;
        },

        showScreenshots: function (evt) {
            var screenshots = this.serviceItemModel.get('screenshots'),
                largeScreenshots = _.map(screenshots, function (screenshot) {
                    return {src: screenshot.largeImageUrl || screenshot.smallImageUrl};
                });

            evt.preventDefault();

            $.magnificPopup.open({
                type: 'image',
                gallery: {
                    enabled: true
                },
                items:largeScreenshots
            }, this.carouselIndex);
        }

    });
});
