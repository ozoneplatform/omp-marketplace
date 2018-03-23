define([
    '../views/tabbable/TabPaneView',
    'views/ErrorPopover',
    'handlebars',
    'moment',
    'bootstrap-editable'
], function(TabPaneView, ErrorPopover, Handlebars, moment) {
    'use strict';

    var SuperClass = TabPaneView,
        infoTemplate = Handlebars.compile('<div class="info">' +
            '<h4>{{title}}</h4>' +
            '<dl>' +
                '<dt>Name</dt><dd>{{displayName}}</dd>' +
                '<dt>Username</dt><dd>{{username}}</dd>' +
                '<dt>Email</dt><dd><a href="mailto:{{email}}">{{email}}</a></dd>' +
                '<dt>Member Since</dt><dd>{{since}}</dd>' +
                '<dt>Bio</dt>' +
                '<dd class="bio">' +
                    '<div class="content">{{bio}}</div>' +
                '</dd>' +
            '</dl>' +
        '</div>'),
        bioMaxLength = 1000,
        bioSelfEmptyText = 'Click to enter a little bit about yourself',
        bioOtherEmptyText = 'No bio',
        bioEmptyClass = 'empty',
        bioSaveErrorMessage = 'There was an error saving your bio changes.';

    return SuperClass.extend({
        title: 'Profile',

        events: {
            'click .preferences .popover button': 'closePreferencesPopover',
            'click .bio + .popover button': 'closeBioPopover'
        },

        render: function() {
            var me = this,
                $content,
                tmplArgs = _.extend({
                    title: this.title
                }, this.model.attributes);

            this.$el
                .append(infoTemplate(_.extend({
                    since: moment(this.model.get('createdDate')).format('MM/DD/YYYY')
                }, tmplArgs)));

            $content = this.$('.bio > .content');

            if (this.model.isSelf() || this.model.isAdmin()) {
                $content.editable({
                    type: 'textarea',
                    tpl: '<textarea maxlength="' + bioMaxLength + '"></textarea>',
                    mode: 'inline',
                    showbuttons: 'bottom',
                    rows: 6,
                    emptyclass: bioEmptyClass,
                    emptytext: this.model.isSelf() ? bioSelfEmptyText : bioOtherEmptyText,
                    url: function(params) {
                        me.model.save({ bio: params.value })
                            .fail(_.bind(me.bioErrorPopover, me));
                    },
                    validate: function(value) {
                        if (value.length > bioMaxLength) {
                            return 'Bio must not exceed ' + bioMaxLength + ' characters';
                        }
                    }
                });

                this.$('.bio').addClass('can-edit');
            }
            else if (!$content.text()) {
                $content.text(bioOtherEmptyText).addClass(bioEmptyClass);
            }

            return this;
        },

        bioErrorPopover: function() {
            this.bioErrPopover = (new ErrorPopover({
                message: bioSaveErrorMessage,
                title: 'Bio Save Error',
                placement: 'top',
                el: this.$('.bio')[0]
            })).render();
        },

        closeBioPopover: function() {
            if (this.bioErrPopover) {
                this.bioErrPopover.remove();
            }
        },

        remove: function() {
            this.closeBioPopover();

            this.$('.bio > .content').editable('destroy');

            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
