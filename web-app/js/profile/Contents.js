define([
    '../modal/Contents',
    './Header',
    './TabView',
    '../models/Profile',
    'handlebars'
], function(Contents, Header, TabView, Profile, Handlebars) {
    'use strict';

    var SuperClass = Contents,
        failureTemplate = Handlebars.compile(
            '<div class="failure-message">' +
                'Could not load information for profile {{id}}' +
            '</div>'
        );

    return SuperClass.extend({
        HeaderViewClass: Header,
        BodyViewClass: TabView,

        failureTemplate: failureTemplate,

        titleProperty: 'displayName',

        initialize: function(options) {
            this.model = new Profile({
                id: options.profileId
            });

            this.href = 'profile/' + options.profileId;

            SuperClass.prototype.initialize.apply(this, arguments);

            this.bodyView.activeTab = options.tab || 'profile';
        },

        route: function(params) {
            this.bodyView.setActiveTab(params.tab);
        }
    });
});
