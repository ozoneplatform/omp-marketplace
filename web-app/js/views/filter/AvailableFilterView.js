define([
    './FilterView',
    'handlebars',
    'underscore',
    'bootstrap' //for tooltip
], function(FilterView, Handlebars, _) {
    'use strict';

    var SuperClass = FilterView,
        template = Handlebars.compile('<a href="{{href}}">' +
            '<span class="filter-value">{{title}}</span>' +
            '<span class="filter-count">({{count}})</span>' +
        '</a>');

    return SuperClass.extend({
        render: function() {
            var attrs = _.extend({
                href: this.getFilterBaseUrl() +
                    this.filterKind + '/' + this.model.get('id')
            }, this.model.attributes);

            this.$el.html(template(attrs));
            return SuperClass.prototype.render.apply(this, arguments);
        }
    });
});
