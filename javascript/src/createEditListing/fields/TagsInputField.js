define([
    '../jsonForm',
    'jquery',
    'underscore',
    '../../tag/views/TagLookupView',
    '../../tag/views/TagView',
    '../../tag/views/TagPanelView',
    '../../views/BaseView',
    '../../models/ServiceItemTagModel',
    'marketplace'
], function (JsonForm, $, _, TagLookupView, TagView, TagPanelView, BaseView, ServiceItemTagModel, Marketplace) {

    var FormFieldTypes = JsonForm.FormFieldTypes;

    var TagsInputField = TagPanelView.extend({

        init: function(options) {
            this.initialize(options);
        },

        initialize: function (options) {
            BaseView.prototype.initialize.call(this, options);
            this.tagViews = [];
            this.serviceItemModel = options.serviceItemModel;
            this.collection = this.serviceItemModel.tags();
        },

        render: function() {
            this.doRender();
            this.listenTo(this.tagLookupView, 'tag:add-new', this.doSave);
            return this;
        },

        //Normally the subview would hanlde the crud operation; however, since this panel will be used in difference contexts (createe/edit) its better
        //to have the operation at a higher level.
        doSave: function(model) {
            var attributes = model.toJSON();
            var m = new ServiceItemTagModel(_.extend(attributes, {
                createdBy: { id: Marketplace.user.id }
            }));
            this.collection.push(m);
            _.each(this.tagViews, function(view) {
                view.remove();
            });
            this.tagLookupView.remove();
            this.render();
        },

        //Iterate through the collection and create a new tag view for each ServiceItemTagModel
        doRender: function() {
            var me = this;
            this.collection.each(function(model) {
                var tagView = new TagView({model : model});
                me.tagViews.push(tagView);
                me.listenTo(tagView, 'tag:remove', me.doRemove);
                me.$el.append(tagView.el);
            });
            this.tagLookupView = new TagLookupView({panel: this, serviceItemModel : this.serviceItemModel});
            this.$el.append(this.tagLookupView.el);
        }
    });

    FormFieldTypes.register('tags', TagsInputField);

    return TagsInputField;

});