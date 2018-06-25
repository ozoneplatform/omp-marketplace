define([
    '../BaseView',
    'handlebars',
    'moment',
    'underscore',
    'jquery',
    'marketplace'
], function(BaseView, Handlebars, moment, _, $, Marketplace) {
    'use strict';

    return BaseView.extend({

        escapeHtml: Handlebars.Utils.escapeExpression,

        tagName: 'li',

        id: function () {
            return this._id || (this._id = _.uniqueId('changelog-'));
        },

        className: function () {
            return 'list-item collapsed' + (this.isExpandable() ? ' collapsible': '');
        },

        template: Handlebars.compile(
            '<div class="row">' +
                '{{{timestamp}}}' +
                '<div class="action span"><i class="{{iconClassName}}"></i> {{{action}}}</div>' +
                '<div class="author span">' +
                    '<a href="#profile/{{authorId}}" title="{{author}}">{{author}}</a>' +
                '</div>' +
            '</div>' +
            '{{{details}}}'
        ),

        listingTemplate: Handlebars.compile(
            '<h5 class="inline">' +
                '{{#if showItemLinks}}' +
                    '<a href="#quickview/{{id}}' +
                            '{{#if quickviewTab}}' +
                                '/{{quickviewTab}}' +
                            '{{/if}}' +
                            '" class="quickview-link">' +
                        '{{title}}' +
                    '</a>' +
                '{{else}}' +
                    '{{title}}' +
                '{{/if}}' +
            '</h5>'
        ),

        isExpandable: function () {
            var rejectionReason = this.model.get('rejectionListing'),
                changeDetails = this.model.get('changeDetails'),
                isTagEvent = this.model.isTagAdded() || this.model.isTagRemoved();

            if(isTagEvent || this.model.isReviewDeleted()) {
                return false;
            }

            return ((rejectionReason && rejectionReason.justification) || (_.isArray(changeDetails) && changeDetails.length > 0));
        },

        timestamp: function () {
            var activityTimestamp = this.model.get('activityTimestamp'),
                date = moment(activityTimestamp).format('llll'),
                fuzzyDate = moment(activityTimestamp).fromNow(),
                html = '<div class="timestamp span" title="' + date + '">';

            if(this.isExpandable()) {
                return html + '<div class="arrow"></div> ' + fuzzyDate + '</div>';
            }
            return html + '<div class="arrow invisible"></div> ' + fuzzyDate +'</div>';
        },

        action: function () {
            var me = this,
                action = this.model.get('action').description,
                relationshipItems = this.model.get('relatedItems'),
                serviceItem = this.model.get('serviceItem');

            function itemData (item) {
                var showItemLinks = me.globalActivity && item.id;
                return _.extend({
                    showItemLinks: showItemLinks,
                    quickviewTab: me.quickviewTab
                }, item);
            }

            function relationshipToString (relationshipItems) {
                var array = _.map(relationshipItems, function (relation) {
                    return me.globalActivity ? me.listingTemplate(itemData(relation)) :
                        '<h5 class="requires">' + me.escapeHtml(relation.title) + '</h5>';
                });

                if(array.length > 2) {
                    var last = array.pop();
                    array[array.length - 1] = array[array.length - 1] + ' and ' + last;
                    return array.join(', ');
                }
                else {
                    return array.join(' and ');
                }
            }

            function singleValueChange (model) {
                return model.get('changeDetails')[0].newValue;
            }

            var listingHtml = this.globalActivity ? this.listingTemplate(itemData(serviceItem)) : 'Listing';

            if(this.model.isRequirementsRemoved()) {
                return listingHtml + ' no longer requires ' + relationshipToString(relationshipItems);
            }
            else if(this.model.isRequirementsAdded()) {
                return listingHtml + ' now requires ' + relationshipToString(relationshipItems);
            }
            else if(this.model.isAddedAsRequirement()) {
                return listingHtml + ' is now required by ' + relationshipToString(relationshipItems);
            } else if(this.model.isRemovedAsRequirement()) {
                return listingHtml + ' is no longer required by ' + relationshipToString(relationshipItems);
            }
            else if(this.model.isSetToOutside() || this.model.isSetToInside()) {
                return listingHtml + ' set to ' + action;
            } else if(this.model.isTagAdded()) {
                return 'Tag ' + singleValueChange(this.model) + ' Added To ' + listingHtml;
            } else if(this.model.isTagRemoved()) {
                return 'Tag ' + singleValueChange(this.model) + ' Deleted From ' + listingHtml;
            } else if(this.model.isReviewDeleted()) {
                return singleValueChange(this.model) + '\'s Review Removed From ' + listingHtml;
            }
            return listingHtml + ' ' + action;
        },

        author: function () {
            return this.model.get('author').name;
        },

        authorUserName: function () {
            return this.model.get('author').username;
        },

        authorId: function() {
            return this.model.get('author').id;
        },

        context: function () {
            return Marketplace.context;
        },

        iconClassName: function () {
            if(this.model.isRequirementsRemoved() || this.model.isRequirementsAdded() || this.model.isAddedAsRequirement() || this.model.isRemovedAsRequirement()) {
                return 'icon-requires';
            }
            else if(this.model.isSetToOutside()) {
                return 'icon-external-link';
            }
            else if(this.model.isSetToInside()) {
                return 'icon-inside';
            }
            else if(this.model.isEnabled() || this.model.isDisabled()) {
                return 'icon-off';
            }
            else if(this.model.isApproved()) {
                return 'icon-thumbs-up';
            }
            else if(this.model.isRejected()) {
                return 'icon-thumbs-down';
            }
            else if(this.model.isSubmitted()) {
                return 'icon-time';
            }
            else if(this.model.isModified()) {
                return 'icon-edit';
            }
            else if(this.model.isCreated()) {
                return 'icon-leaf';
            } else if(this.model.isTagAdded() || this.model.isTagRemoved()) {
                return 'icon-tag';
            } else if(this.model.isReviewEdited() || this.model.isReviewDeleted()) {
                return 'icon-star';
            }
            return 'Listing ';
        },

        details: function () {
            var me = this,
                rejectionListing = this.model.get('rejectionListing'),
                changeDetails = this.model.get('changeDetails');

            var html = '<ul class="changelog-details collapse">';
            if(rejectionListing) {
                var justification = rejectionListing.justification,
                    description = rejectionListing.description;

                if(!(justification || description)) {
                    return;
                }

                html += '<li><span class="reason">Reason: </span><span>' + this.escapeHtml(justification ? justification.title : '') + '</span></li>';
                html += '<li><span class="details">Details: </span><span>' + this.escapeHtml(description ? description : '') + '</span></li>';
            }
            else if(_.isArray(changeDetails) && changeDetails.length > 0) {
                html += _.map(changeDetails, function (change) {
                    //since the stack descriptor is just a large blob of JSON, do not show the before/after value in the UI
                    if(change.displayName === 'stackDescriptor') {
                        return '<li><span class="new-value">The stack descriptor changed</span></li>';
                    } else if(change.displayName === 'satisfiedScoreCardItems'){
                        return '<li>Satisfied Score Card Items changed from <span class="old-value">' + change.oldValue + '</span> to <span class="new-value">' + change.newValue + '</span></li>';
                    }
                    var oldValue = me.escapeHtml(change.oldValue || 'None');
                    var newValue = me.escapeHtml(change.newValue || 'None');
                    return '<li><span class="field">' + me.escapeHtml(change.displayName) + '</span> changed from <span class="old-value">' + oldValue + '</span> to <span class="new-value">' + newValue + '</span></li>';
                }).join('');
            }

            return html + '</ul>';
        },

        attributes: function () {
            if(!this.isExpandable()) { return; }
            return {
                'data-toggle': 'collapse',
                'data-target': '#' + this.id() + ' > .collapse'
            };
        },

        render: function () {
            this.$el.append(this.template(this));
            return this;
        }
    });
});
