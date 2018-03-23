define([
    'backbone',
    'underscore'
],
function(Backbone, _) {

    return Backbone.Model.extend({

        areRelatedItemsUpdated: function () {
            return this.isRemovedAsRequirement() || this.isRequirementsRemoved() || this.isRequirementsAdded() || this.isAddedAsRequirement();
        },

        isRemovedAsRequirement: function () {
            return this.get('action').description === 'Removed as a requirement';
        },

        isRequirementsRemoved: function () {
            return this.get('action').description === 'Requirements removed';
        },

        isRequirementsAdded: function () {
            return this.get('action').description === 'New requirements added';
        },

        isAddedAsRequirement: function () {
            return this.get('action').description === 'Added as a requirement';
        },

        isSetToOutside: function () {
            return this.get('action').description === 'Outside';
        },

        isSetToInside: function () {
            return this.get('action').description === 'Inside';
        },

        isApproved: function () {
            return this.get('action').description === 'Approved';
        },

        isRejected: function () {
            return this.get('action').description === 'Rejected';
        },

        isEnabled: function () {
            return this.get('action').description === 'Enabled';
        },

        isDisabled: function () {
            return this.get('action').description === 'Disabled';
        },

        isSubmitted: function () {
            return this.get('action').description === 'Submitted';
        },

        isModified: function () {
            return this.get('action').description === 'Modified';
        },

        isCreated: function () {
            return this.get('action').description === 'Created';
        },

        isTagAdded: function () {
            return this.get('action').description === 'Tag Created';
        },

        isTagRemoved: function () {
            return this.get('action').description === 'Tag Removed';
        },

        isReviewDeleted: function () {
            return this.get('action').description === 'Review Deleted';
        },

        isReviewEdited: function () {
            return this.get('action').description === 'Review Edited';
        }

    });

});
