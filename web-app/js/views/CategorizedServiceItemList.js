define([
    './ListView',
    './BaseView',
    'handlebars',
    'underscore',
    'jquery'
], function(ListView, BaseView, Handlebars, _, $) {
    'use strict';

    var SuperClass = ListView,
        subSectionTemplate = Handlebars.compile(
            '<div class="service-item-subsection">' +
                '<h6 class="subsection-title collapsible">' +
                    '<span class="arrow"></span>{{title}}' +
                '</h6>' +
                '<ul></ul>'
        ),
        itemTemplate = Handlebars.compile(
            '{{#if isAffiliated}}' +
                '<a href="#quickview-affiliated/{{affiliatedUrl}}/{{id}}">' +
            '{{else}}' +
                '<a href="#quickview/{{id}}">' +
            '{{/if}}' +
                '<img class="service-item-icon" src="{{imageSmallUrl}}" data-id="{{id}}" />' +
                '<span title="{{title}}">{{title}}</span>' +
            '</a>'
        ),
        ItemView = BaseView.extend({
            tagName: 'li',

            usingDefaultIcon: false,

            isAffiliated: false,

            render: function() {
                var attrs;

                if (this.model.get('imageSmallUrl')) {
                    attrs = this.model.attributes;
                }
                else {
                    attrs = _.extend({}, this.model.attributes, {
                        imageSmallUrl: this.model.getDefaultIconUrl()
                    });

                    this.usingDefaultIcon = true;
                }

                _.extend(attrs, {
                    isAffiliated: this.isAffiliated,
                    affiliatedUrl: encodeURIComponent(this.affiliatedUrl)
                });

                this.$el.append(itemTemplate(attrs));

                //error doesn't bubble so we can't use the events object
                this.$('.service-item-icon').error(_.bind(this.setDefaultIcon, this));

                if (this.model.get('isEnabled') === false) {
                    this.$el.addClass('disabled');
                }
                return this;
            },

            setDefaultIcon: function() {
                if (!this.usingDefaultIcon) {
                    this.$('.service-item-icon').attr('src', this.model.getDefaultIconUrl());
                    this.usingDefaultIcon = true;
                }
            },

            remove: function() {
                this.$('.service-item-icon').off('error');

                BaseView.prototype.remove.apply(this, arguments);
            }
        });

    return SuperClass.extend({
        //elements for each subsection
        subSections: null,

        className: 'categorized-service-item-list ' + (SuperClass.prototype.className || ''),

        ItemView: ItemView,

        events: {
            'click .subsection-title': 'toggleCollapse'
        },

        initialize: function(options) {
            //a list of objects with the following properties:
            // title: the subsection title
            // $el: the element for the subsection
            this.subSections = [];

            this.itemViewOptions = {
                affiliatedUrl: options.affiliatedUrl,
                isAffiliated: options.isAffiliated
            };

            SuperClass.prototype.initialize.apply(this, arguments);

            //disable the superclass' preRender system, since it needs to work a bit differently
            //with the subsections in this class
            this.preRenderFragment = null;
        },

        render: function() {
            var me = this;

            SuperClass.prototype.render.apply(this, arguments);

            //add pre-existing subsections to the element
            _.each(this.subSections, function(subsection) {
                me.$el.append(subsection.$el);
            });

            return this;
        },

        addOne: function(model) {
            var subSection = this.getSubSection(model) || this.createSubSection(model),
                oldBody = this.$body;

            //replace the $body used by the superclass with the correct wrapper element
            this.$body = subSection.children('ul');

            SuperClass.prototype.addOne.apply(this, arguments);

            this.$body = oldBody;
        },

        getSubSection: function(model) {
            var title = this.getSubSectionProperty(model),
                subsection  = _.findWhere(this.subSections, { title: title });

            return subsection && subsection.$el;
        },

        createSubSection: function(model) {
            var title = this.getSubSectionProperty(model),
                $el = $(subSectionTemplate({ title: title }));

            if (this.isRendered()) {
                $el.appendTo(this.$el);
            }

            this.subSections.push({ title: title, $el: $el });

            return $el;
        },

        getSubSectionProperty: function(model) {
            return model.get('types').title;
        },

        toggleCollapse: function(e) {
            var $el = $(e.target),
                subsection = $el.closest('.service-item-subsection');

            subsection.children('ul').collapse('toggle');
            subsection.toggleClass('collapsed');
        }
    });
});
