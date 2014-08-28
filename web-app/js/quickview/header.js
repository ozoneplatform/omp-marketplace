define(
[
    '../views/BaseView',
    'backbone',
    'handlebars',
    'jquery',
    'underscore',
    'raty',
    'marketplace',
    'bootstrap'
],
function(BaseView, Backbone, Handlebars, $, _, Raty, Marketplace) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'quickview-header',

        tpl: Handlebars.compile(
            '<div class="quickview-header-content">' +
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
                '<span class="service-item-icon-wrapper">' +
                    '<img class="service-item-icon" src="{{imageMediumUrl}}" data-fallback="{{imageUrlFallback}}" />' +
                    '<div class="service-item-is-added-icon"></div>' +
                '</span>' +
                '<span class="service-item">'+
                    '<h3 class="service-item-title" title="{{title}}">{{title}}</h3>'+
                    '{{#if versionName}}'+
                        '<div class="service-item-version" title="{{versionName}}">Version {{versionName}}</div>'+
                    '{{/if}}' +
                    '{{#if agency}}' +
                    '<p class="service-item-agency" title="{{agency.title}}">' +
                        '<img class="service-item-agency-icon" src="{{agency.iconUrl}}" data-fallback="{{agency.iconUrlFallback}}" />' +
                        '{{agency.title}}' +
                    '</p>'+
                    '{{/if}}' +
                    '{{#if showReviewTotatls}}' +
                        '<span class="service-item-ratings"></span>'+
                        '<span class="service-item-ratings-total">({{totalVotes}})</span>'+
                    '{{/if}}' +
                    '{{#if showReviewPrompt}}' +
                        '<span class="service-item-review-prompt" id="go-to-reviews-tab"><a href="#quickview/{{id}}/reviews">{{reviewPrompt}}</a></span>' +
                    '{{/if}}' +
                '</span>' +
                '{{#unless isSubmitted}}' +
                    '<button class="btn btn-large btn-success btn-submit">Submit</button>'+
                '{{/unless}}' +
                '{{#if showAddbutton}}' +
                    '<button class="btn btn-large btn-primary btn-add">{{addButtonText}}</button>'+
                '{{/if}}' +
            '</div>'
        ),

        serviceItemModel: null,

        $icon: null,

        $ratings: null,

        addTooltip: 'Adding this listing to OWF adds any listings it requires.',

        isItemInOwf: false, // True when OWF user already has this "listing"

        isReviewedByMe: false, // Current user has reviewed this "listing"

        events: {
            'click .btn-add' : 'addOrStart',
            'click .btn-submit' : 'submit'
        },

        initialize: function (options) {
            var me = this;

            SuperClass.prototype.initialize.call(me, options);

            me.listenTo(me.serviceItemModel, 'change', function() {
                me.render();
                me.checkServiceItemInOwf();
            });

            me.checkServiceItemInOwf();

            var reviews = me.serviceItemModel.reviews();

            var onReviewChange = function(model, changeType) {
                if (model && model.get('username') &&
                    model.get('username') === Marketplace.user.username) {
                    if (changeType === 'add') {
                        me.isReviewedByMe = true;
                    } else if (changeType === 'remove') {
                        me.isReviewedByMe = reviews.findWhere({username: Marketplace.user.username}) !== undefined;
                    }
                }

                me.render();
            };

            me.listenTo(reviews, 'add', function(newItem) {
                onReviewChange(newItem, 'add');
            });

            me.listenTo(reviews, 'remove', function(oldItem) {
                onReviewChange(oldItem, 'remove');
            });
        },

        /**
         * Updates the view.
         */
        update: function (model) {
            var changed = model.changed,
                avgRate = changed.avgRate,
                totalVotes = changed.totalVotes,
                readOnly, $count;

            // update avgRate if changed
            // set readonly to false before updating for the update to work.
            if(avgRate) {
                this.$ratings
                    .data('readonly', false)
                    .raty('setScore', avgRate)
                    .attr('title', 'Average Rating: ' + avgRate)
                    .data('readonly', true);
            }

            if(_.isNumber(totalVotes)) {
                this.$el.find('.service-item-ratings-total').html('(' + changed.totalVotes +')');
            }
        },

        /*
         * Gets the data object needed to render the header Handlebars template.
         * @returns {Object} the data object needed to render the header Handlebars template
         */
        data: function() {
            var data = this.serviceItemModel.toJSON();
            var defaultListingIcon = this.getDefaultIconUrl();
            if (!data.imageMediumUrl) {
                data.imageMediumUrl = defaultListingIcon;
            }
            data.imageUrlFallback = defaultListingIcon;

            if(data.agency) {
                data.agency.iconUrlFallback = Marketplace.context + '/themes/common/images/agency/agencyDefault.png';
            }

            data.isAffiliated = this.serviceItemModel.isAffiliated;
            data.context = this.serviceItemModel.context;
            // Ensure start button is hidden for stack if current OWF
            // version is not able to start a stack
            data.showAddbutton = Marketplace.widget.isWidget() ?
                (this.serviceItemModel.isAddable() && (!this.serviceItemModel.isStack() || !this.isItemInOwf || Marketplace.widget.isStackLaunchable())) :
                    (this.serviceItemModel.isAddable() && !this.serviceItemModel.isStack() && this.serviceItemModel.hasValidLaunchUrl());
            data.addButtonText = Marketplace.widget.isWidget() && this.serviceItemModel.isOzoneAware() && !this.isItemInOwf ? 'Add' : 'Start';
            data.isSubmitted = this.serviceItemModel.isSubmitted();

            data.isReviewed = data.totalComments > 0 || data.totalVotes > 0;
            data.reviewPrompt = data.isReviewed ? 'Review this' : 'Be the first to review this';

            if (data.types && data.types.title) {
                data.reviewPrompt += ' ' + data.types.title;
            } else {
                data.reviewPrompt += ' listing';
            }

            data.showReviewPrompt = this.isItemInOwf && !this.isReviewedByMe && !data.isAffiliated;
            data.showReviewTotatls = !data.showReviewPrompt || data.isReviewed;

            return data;
        },

        /**
         * Renders the view.
         * @returns {HeaderView} this for chaining purposes
         */
        render: function() {
            var me = this,
                data = me.data();

            me.cleanup();

            me.$el.html(me.tpl(data));

            me.$icon = me.$el.find('img');

            // TODO: abstract into a plugin
            me.$icon.on('load.fallback, error.fallback', function(evt) {
                var $this = $(this);
                $this.off('.fallback');

                if(evt.type === 'error') {
                    $this.attr('src', $this.data('fallback'));
                }
            });

            me.$ratings = me.$el.find('.service-item-ratings').raty({
                hints: ['', '', '', '', ''],
                readOnly: true,
                score: data.avgRate
            })
            .attr('title', 'Average Rating: ' + data.avgRate);

            if(data.showAddbutton) {
                me.$el.find('.service-item-add-button').tooltip({
                    title: me.addTooltip,
                    placement: 'bottom',
                    delay: { show: 100, hide: 0 }
                });
            }

            if (me.isItemInOwf) {
                me.$el.find('.service-item-is-added-icon').show();
            }

            me.$el.find('#go-to-reviews-tab').click(function() {
                $('a[href="#quickview-reviews"]').trigger('click');
            });

            return me;
        },

        /*
         * Gets the default icon URL for the service item which this QuickView modal displays.
         * @returns {String} the default icon URL for the service item which this QuickView modal displays
         */
        getDefaultIconUrl: function() {
            var typeId = this.serviceItemModel.get('types').id;

            return Marketplace.context + '/images/types/' + typeId;
        },

        /*
         * Add the service item which this QuickView modal displays to OWF.
         */
        addOrStart: function(evt) {
            var me = this;
            var id = me.serviceItemModel.id;
            var uuid = me.serviceItemModel.get('uuid');
            var icon = me.$icon.get(0);
            var url = me.serviceItemModel.isAffiliated && this.serviceItemModel.context;
            var launchUrl = me.serviceItemModel.get('launchUrl');

            if(Marketplace.widget.isWidget()) {
                var $btn = $(evt.target).attr('disabled', true);
                var oldText = $btn.text();
                var oldIsItemInOwf = me.isItemInOwf;

                var doRefresh = function(result) {
                    if(Marketplace.widget.canCheckIfUserHasListing()) {
                        me.checkServiceItemInOwf(function(newIsInOwf) {
                            // Reset button text if the above 'check' function
                            // did not cause a re-render
                            if (oldIsItemInOwf === newIsInOwf) {
                                $btn.text(oldText);
                                $btn.attr('disabled', false);
                            }
                        });
                    }
                    else {
                        // < OWF 7.11
                        me.$('.btn-add').hide();
                    }
                };

                // Change text to indicate results will not be immediate
                $btn.text(oldText + 'ing...');

                if (me.serviceItemModel.isOzoneAware()) {
                    if (me.serviceItemModel.isStack()) {
                        var data = me.serviceItemModel.toJSON();

                        if (me.isItemInOwf) {
                            Marketplace.widget.launchStack(data, icon, doRefresh);
                        } else {
                            Marketplace.widget.addStackToOwf(uuid, icon, function(result) {
                                // See notes in Marketplace.widget.isAlreadyInOwf
                                // function for the reason behind this hack.
                                if (!Marketplace.widget._recentlyAddedStackCache) {
                                    Marketplace.widget._recentlyAddedStackCache = [];
                                }

                                if (data.owfProperties && data.owfProperties.stackContext) {
                                    var stackContext = data.owfProperties.stackContext;

                                    if (Marketplace.widget._recentlyAddedStackCache.indexOf(stackContext) < 0) {
                                        Marketplace.widget._recentlyAddedStackCache.push(stackContext);
                                    }
                                }

                                doRefresh();
                            });
                        }
                    } else {
                        Marketplace.widget.addWidgetToOwf(id, icon, url, me.isItemInOwf, doRefresh);
                    }
                }
                else {
                    me.serviceItemModel.get('opensInNewBrowserTab') ?
                        this.confirmStart(evt, function () {
                            window.open(launchUrl);
                            $btn.text(oldText);
                            $btn.attr('disabled', false);
                        }) :
                        Marketplace.widget.startWebappInOwf(id, icon, url, true, doRefresh);
                }
            }
            else {
                window.open(launchUrl);
            }
        },

        confirmStart: function (evt, okCallback) {
            var me = this,
                $btn = $(evt.target);

            $btn.confirm({
                trigger: 'manual',
                placement: 'bottom',
                cls: 'confirm-start-in-owf',
                container: this.$el.parents('.modal-body'),
                title: 'Start in New Tab',
                content: 'Due to listing configuration, this listing cannot be added to your "My Apps" display.',
                ok: okCallback
            }).confirm('toggle');
        },

        submit: function (evt) {
            var me = this,
                $btn = $(evt.target).attr('disabled', true);

            me.serviceItemModel.submit().done(function () {
                var addButtonText = Marketplace.widget.isWidget() && me.serviceItemModel.isOzoneAware() ? 'Add' : 'Start';
                me.serviceItemModel.isAddable() ?
                    $btn.removeClass('btn-submit')
                        .addClass('btn-add').text(addButtonText).removeAttr('disabled') :
                    $btn.remove();
            });
        },

        cleanup: function () {
            if (this.$icon && this.$icon.length > 0) {
                this.$icon.off('.icon');
            }
            if (this.$ratings && this.$ratings.length > 0) {
                this.$ratings.raty('destroy');
            }

            var $addButton = this.$el.find('.service-item-add-button');
            $addButton.length === 1 && $addButton.tooltip('destroy');
        },

        /*
         * Destroys the view. Calls the super class.
         * @returns {HeaderView} this for chaining purposes
         */
        remove: function () {
            this.cleanup();
            return SuperClass.prototype.remove.call(this);
        },

        /**
         * Tests if the current user has the current service item in OWF.
         * Updates the <i>isItemInOwf</i> property and triggers a re-render
         * only if needed.
         */
        checkServiceItemInOwf: function(callback) {
            var me = this;
            var data = me.serviceItemModel.toJSON();
            var isAddedGuid = data.uuid;

            var renderIfChanged = function(newIsItemInOwfValue) {
                if (me.isItemInOwf !== newIsItemInOwfValue) {
                    me.isItemInOwf = newIsItemInOwfValue;
                    me.render();
                }

                callback && callback(newIsItemInOwfValue);
            };

            if (data.owfProperties && data.owfProperties.stackContext) {
                isAddedGuid = data.owfProperties.stackContext;
            }

            Marketplace.widget.isAlreadyInOwf(isAddedGuid, renderIfChanged);
        }

    });
});
