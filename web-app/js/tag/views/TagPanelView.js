define(
[
    '../../views/BaseView',
    './TagLookupView',
    './TagView',
    'jquery',
    'underscore',
    'bootstrap'
],
function(BaseView, TagLookupView, TagView, $, _) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'tag-panel',

        serviceItemModel: null,

        tagViews: null,

        initialize: function (options) {
            BaseView.prototype.initialize.call(this, options);
            this.serviceItemModel = options.serviceItemModel;
            this.collection = this.serviceItemModel.tags();
            this.tagViews = [];
            this.render();
        },

        render: function() {
            var me = this;
            this.collection.fetch().complete(function() {
                me.doRender();
                me.listenTo(me.tagLookupView, 'tag:add-new', me.doSave)
            });
        },

        //Normally the subview would hanlde the crud operation; however, since this panel will be used in difference contexts (createe/edit) its better
        //to have the operation at a higher level.
        doSave: function(model) {
            var me = this;
            var tag = [model];
            me.serviceItemModel.saveTags(tag).then(function() {
                _.each(me.tagViews, function(view) {
                   view.remove();
                });
                me.tagLookupView.remove();
                me.render();
            });
        },

        //Iterate through the collection and create a new tag view for each ServiceItemTagModel
        doRender: function() {
            var me = this;
            this.collection.each(function(model) {
                model.set('serviceItemId', me.serviceItemModel.get('id'));
                var tagView = new TagView({model : model});
                me.tagViews.push(tagView);
                me.listenTo(tagView, 'tag:remove', me.doRemove);
                me.$el.append(tagView.el);
            });
            this.tagLookupView = new TagLookupView({panel: this, serviceItemModel : this.serviceItemModel});
            this.$el.append(this.tagLookupView.el);
        },

        remove: function () {
            _.invoke(this.tagViews, 'remove');
            SuperClass.prototype.remove.call(this);
        }

    });
});
