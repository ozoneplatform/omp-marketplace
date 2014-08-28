define([
    'views/BaseView',
    './FilterDropDown',
    'underscore',
    'jquery'
], function(BaseView, FilterDropDown, _, $) {
    'use strict';

    var SuperClass = BaseView;

    return SuperClass.extend({
        className: 'filter-menu bootstrap-active',

        //should be set via constructor
        filterConfig: null,

        dropDowns: null,

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.dropDowns = _.map(this.filterConfig, function(conf) {
                return new FilterDropDown(conf);
            });
        },

        render: function() {
            var me = this;

            _.each(this.dropDowns, function(dropDown) {
                me.$el.append(dropDown.render().$el);
            });

            this.fixIE();

            return this;
        },

        remove: function() {
            _.each(this.dropDowns, function(dropDown) {
                dropDown.remove();
            });

            return SuperClass.prototype.remove.apply(this, arguments);
        },

        fixIE: function() {
            var htmlEl = $('html');

            if (htmlEl.hasClass('ie7')) {
                //each successive filter drop down needs a lower z-index for the tooltips
                //to overlap the next column correctly
                _.each(this.dropDowns, function(dropDown) {
                    var $el = dropDown.$el;

                    $el.css('z-index', 10 - $el.index());
                });
            }
        }
    });
});
