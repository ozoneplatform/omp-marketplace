define([
    '../../views/BaseView',
    '../../rejectionListing/CreateRejectionJustificationView',
    '../../events/EventBus',
    'marketplace',
    'handlebars',
    'underscore',
    'bootstrap'
],
function(BaseView, RejectionJustificationView, EventBus, Marketplace, Handlebars, _, $) {

    return BaseView.extend({

        className: 'settings-pane',

        template: Handlebars.compile(
            '<h4>Settings</h4>' +
            '<div class="visible">' +
                '<h5 class="title">Visible</h5>' +
                '<div class="btn-group pull-right">' +
                    '<button class="btn btn-inside">Inside</button>' +
                    '<button class="btn btn-outside">Outside</button>' +
                '</div>' +
                '<p class="description">This listing can be seen and used by users in other stores.</p>' +
            '</div>' +
            '<div class="enabled">' +
                '<h5 class="title">Enabled</h5>' +
                '<div class="btn-group pull-right" data-toggle="buttons-radio">' +
                    '<button class="btn btn-disable">Disable</button>' +
                    '<button class="btn btn-enable">Enable</button>' +
                '</div>' +
                '<p class="description">This listing can be seen and used by other users in this store.</p>' +
            '</div>' +
            '<div class="approved control-group">' +
                '<h5 class="title">Approved</h5>' +
                '<div class="btn-group pull-right">' +
                    '<button class="btn btn-reject">Reject</button>' +
                    '<button class="btn btn-approve">Approve</button>' +
                '</div>' +
                '<p class="description">This listing meets the requirements of this store.</p>' +
                '<p class="help-inline hide"></p>' +
            '</div>' +
            '<div class="admin-actions">' +
                '<button class="btn btn-small btn-primary btn-edit">Edit</button>' +
                '<button class="btn btn-small btn-copy">Copy</button>' +
                '<button class="btn btn-small btn-danger btn-delete pull-right">Delete</button>' +
            '</div>'
        ),

        events: {
            'click .btn-inside': 'inside',
            'click .btn-outside': 'outside',
            'click .btn-reject': 'reject',
            'click .btn-approve': 'approve',
            'click .btn-disable': 'disable',
            'click .btn-enable': 'enable',
            'click .btn-edit': 'edit',
            'click .btn-copy': 'copy',
            'click .btn-delete': 'deleteListing'
        },

        data: function () {
            var id = this.model.get('id');

            return _.extend({}, this.model.attributes, {
                isPending: this.model.isPending(),
                isApproved: this.model.isApproved(),
                isInProgress: this.model.isInProgress()
            });
        },

        initialize: function (options) {
            BaseView.prototype.initialize.call(this, options);

            this.listenTo(
                this.model,
                'change:approvalStatus change:isOutside change:isEnabled',
                function () {
                    this.update(this.data());
                }
            );
        },

        render: function () {
            var data = this.data();
            this.$el.append(this.template(data));
            this.update(data);
            return this;
        },

        update: function (data) {
            var $active,
                isUserAdmin = Marketplace.user.isAdmin,
                isInsideOutsideSelectionManual = _.findWhere(Marketplace.appconfigs, {code: 'store.insideOutside.behavior'}).value === 'ADMIN_SELECTED',
                isEditAllowedAfterApproval = isUserAdmin ? true : _.findWhere(Marketplace.appconfigs, {code: 'store.allow.owner.to.edit.approved.listing'}).value === 'true';

            this.$el.children('.visible, .approved')[data.isInProgress !== false ? 'hide' : 'show']();

            // inside & outside buttons
            if(data.isOutside === false) {
                $active = this.$('.btn-inside');
            }
            else if(data.isOutside === true) {
                $active = this.$('.btn-outside');
            }
            else {
                this.resetRadioGroup(this.$('.btn-outside'));
            }
            isInsideOutsideSelectionManual ?
                this.activateAndToggleRadioBtn($active, isUserAdmin ? false : true) :
                this.activateAndToggleRadioBtn($active, true);

            if(!isUserAdmin) {
                this.disableRadioGroup(this.$('.btn-outside'));
            }

            // enable & disable buttons
            $active = data.isEnabled === true ? this.$('.btn-enable') : this.$('.btn-disable');
            this.activateAndToggleRadioBtn($active, false);

            // approve & reject buttons
            if(data.isPending) {
                this.resetRadioGroup(this.$('.btn-reject'));
            }
            else {
                if(data.isApproved) {
                    $active = this.$('.btn-approve');
                }
                else {
                    $active = this.$('.btn-reject');
                }
                this.activateAndToggleRadioBtn($active, true);
            }
            if(!isUserAdmin) {
                this.disableRadioGroup(this.$('.btn-reject'));
                this.$('.btn-delete').remove();
            }

            if(this.model.isApproved() && !isEditAllowedAfterApproval) {
                this.$('.btn-edit').attr('disabled', true);
            }
        },

        popoverContainer: function () {
            return this.$el.parents('.modal-body');
        },

        inside: function (evt) {
            if(this.model.get('isOutside') === false || !Marketplace.user.isAdmin) { return; }
            var me = this,
                $btn = $(evt.target),
                content = me.model.get('isOutside') === null ?
                            'Clicking "OK" will make this an "inside" listing, meaning that it will not be visible to other franchise stores.' :
                            'Clicking "OK" will change the listing from "outside" to "inside." This listing will not be visible to other franchise stores. Note, it will still be accessible to users or franchise stores who may have previously imported the listing while it was "outside".';

            $btn.confirm({
                trigger: 'manual',
                placement: 'top',
                container: this.popoverContainer(),
                title: 'Set Listing to Inside',
                content: content,
                ok: function() {
                    me.model.save('isOutside', false);
                }
            }).confirm('toggle');
        },

        outside: function (evt) {
            if(this.model.get('isOutside') === true || !Marketplace.user.isAdmin) { return; }
            var me = this,
                $btn = $(evt.target);

            $btn.confirm({
                trigger: 'manual',
                placement: 'top',
                container: this.popoverContainer(),
                title: 'Set Listing to Outside',
                content: 'Clicking "OK" will make this an "outside" listing, meaning that it will be exposed to all users of Apps Mall across all participating agencies.',
                ok: function() {
                    me.model.save('isOutside', true);
                }
            }).confirm('toggle');
        },

        reject: function (evt) {
            if(!this.model.isSubmitted() || !Marketplace.user.isAdmin) { return; }
            var me = this,
                $btn = $(evt.target),
                rejectionJustificationView = new RejectionJustificationView({model: this.model});

            rejectionJustificationView.render().promise().then(function () {
                $btn.confirm({
                    trigger: 'manual',
                    cls: 'rejection-justification-confirm',
                    placement: 'top',
                    container: me.popoverContainer(),
                    title: 'Rejection Justification',
                    html: true,
                    content: rejectionJustificationView.el,
                    ok: function () {
                        rejectionJustificationView.submit().fail(function () {
                            me.showError($btn, 'Failed to reject listing.');
                        });
                    }
                }).confirm('toggle').one('hidden.bs.confirm', function () {
                    $btn.confirm('destroy');
                    rejectionJustificationView.remove();
                });
            });
        },

        approve: function (evt) {
            if(!this.model.isSubmitted() || !Marketplace.user.isAdmin) { return; }
            var $btn = $(evt.target);

            if(this.model.get('isOutside') === null) {
                this.showError($btn, 'Listing cannot be approved until it is set to Inside or Outside.');
            }
            else {
                this.model.approve();
                this.hideError($btn);
            }
        },

        disable: function (evt) {
            this.model.save('isEnabled', false);
        },

        enable: function (evt) {
            this.model.save('isEnabled', true);
        },

        edit: function () {
            EventBus.trigger('edit:listing', this.model);
            // window.location = Marketplace.context + '/serviceItem/edit/' + this.model.get('id');
        },

        copy: function () {
            EventBus.trigger('copy:listing', this.model);
            // window.location = Marketplace.context + '/serviceItem/copy/' + this.model.get('id');
        },

        deleteListing: function (evt) {
            if(!Marketplace.user.isAdmin) { return; }
            var me = this,
                $btn = $(evt.target);

            $btn.confirm({
                trigger: 'manual',
                placement: 'top',
                container: this.popoverContainer(),
                title: 'Delete Listing',
                content: 'Are you sure you want to delete this listing? This action cannot be undone.',
                ok: function() {
                    me.model.destroy({
                        wait: true
                    }).done(function () {
                        window.location = window.location.href.replace(/#.*/, '');
                    });
                }
            }).confirm('toggle');
        },

        activateAndToggleRadioBtn: function ($btn, disable) {
            $btn && $btn.addClass('active').removeAttr('disabled')
                        .siblings('.btn').removeClass('active').attr('disabled', disable);
        },

        disableRadioGroup: function ($btn) {
            $btn && $btn.attr('disabled', true)
                        .siblings('.btn').attr('disabled', true);
        },

        resetRadioGroup: function ($btn) {
            $btn && $btn.removeClass('active').removeAttr('disabled')
                        .siblings('.btn').removeClass('active').removeAttr('disabled');
        },

        showError: function ($btn, messsage) {
            $btn.closest('.control-group')
                    .addClass('error')
                    .children('.help-inline').removeClass('hide')
                    .text(messsage);
        },

        hideError: function ($btn) {
            $btn.closest('.control-group')
                    .removeClass('error')
                    .children('.help-inline').addClass('hide');
        }

    });

});
