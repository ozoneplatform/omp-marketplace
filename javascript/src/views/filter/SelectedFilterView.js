define([
    './FilterView',
    'handlebars',
    'underscore',
    'bootstrap' //for tooltip
], function(FilterView, Handlebars, _) {
    'use strict';

    var SuperClass = FilterView,
        template = Handlebars.compile(
            '<span class="filter-value">{{title}}</span>' +
            '<a class="remove icon-remove" href="{{href}}"></a>'
        );

    return SuperClass.extend({
        render: function() {
            var attrs = _.extend({
                href: this.getFilterBaseUrl() +
                    'removeFilter?field=' +  this.filterRemoveField  +
                    '&values=' + this.model.get('id')
            }, this.model.attributes);
            this.$el.html(template(attrs));

            return SuperClass.prototype.render.apply(this, arguments);
        }
    });
});

