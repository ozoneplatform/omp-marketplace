/*
 * Copyright 2013 Next Century Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

define(
[
    '../views/BaseView',
    'backbone',
    'jquery',
    'underscore'
],
function(BaseView, Backbone, $, _) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        // events: {
        //     'click > *': 'onItemClick',
        //     'focusin > *': '_selectFocusedItem',
        //     'focusout > *': 'clearSelection'
        // },

        modelEvents: {
            'add'       : 'addOne',
            'remove'    : 'removeOne',
            'reset'     : 'render'
        },

        // backbone view for a list item
        ItemView: null,

        className: 'list-group',

        // class to add when item is active or focused
        activeClassName: 'list-item-active',

        // currently selected item (backbone view)
        activeItem: null,

        // all list item views
        views: null,

        // map of model and view, key = model cid, value = a view instance
        modelViewMap: null,

        // previous filter function
        _filterFn: null,

        emptyText: '<div class="empty">No items found!</div>',

        //a document fragment in which to store rendered children before our render is called
        preRenderFragment: null,

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);

            this.views = [];
            this.modelViewMap = {};

            this.preRenderFragment = $(document.createDocumentFragment());
        },

        $content: function () {
            return this.$body || this.$el;
        },

        render: function () {
            this.removeAll().addAll();

            this.$content().append(this.preRenderFragment);
            this.preRenderFragment = null;

            return this;
        },

        filter: function (filterFn) {
            var me = this;

            me._filterFn = filterFn;

            me.collection.each(function (model, index) {
                var view = me.views[index];
                me.filterOne(view, model);
            });
        },

        filterOne: function (view, model) {
            this._filterFn && (this._filterFn(model) ? view.show() : view.hide());
        },

        removeFilters: function () {
            _.invoke(this.views, 'show');
        },

        createOne: function (model) {
            var options = { model: model };
            this.itemViewOptions && _.extend(options, this.itemViewOptions);
            var view = new this.ItemView(options);
            return view;
        },

        addOne: function(model, collection, options) {
            var view = this.createOne( model ),
                index = options.at,
                $el = this.preRenderFragment || this.$content();

            view.render();

            if(this.views.length === 0) {
                $el.empty();
            }

            if(index === 0) {
                $el.prepend(view.el);
                this.views.splice(0, 0, view);
            }
            else if(index === null || index === undefined ||
                    index >= $el.children().length) {
                $el.append(view.el);
                this.views.push(view);
            }
            else {
                $el.children(':nth-child(' + index + ')').after(view.el);
                this.views.splice(index, 0, view);
            }

            this.modelViewMap[model.cid] = view;

            this.filterOne(view, model);

            this.hideEmptyText();

            return view;
        },

        addAll: function() {
            var me = this;

            me.collection.length > 0 ?
                me.collection.each(function (model, index) {
                    me.addOne(model, me.collection, {
                        at: index
                    });
                }) :
                me.showEmptyText();
        },

        showEmptyText: function () {
            this.$emptyText= this.$emptyText || $(this.emptyText);
            this.$el.append(this.$emptyText);
        },

        hideEmptyText: function () {
            this.$emptyText && this.$emptyText.remove();
        },

        removeOne: function (viewOrModel) {
            var view;
            if(viewOrModel instanceof Backbone.Model) {
                view = this.modelViewMap[viewOrModel.cid];
                delete this.modelViewMap[viewOrModel.cid];
            }
            view.remove();
            this.views = _.without(this.views, view);
        },

        removeAll: function () {
            if(this.views && this.views.length > 0) {
                _.invoke(this.views, 'remove');
            }

            this.hideEmptyText();

            this.views= [];
            this.modelViewMap = {};
            return this;
        },

        // handle item clicks
        // selects clicked item
        onItemClick: function (evt) {
            this.selectItem($(evt.currentTarget));
        },

        selectItem: function ($el) {
            this.clearSelection();

            this.activeItem = $el.data('view');
            $el.addClass( this.activeClassName );
        },

        clearSelection: function () {
            if(this.activeItem) {
                this.activeItem.$el.removeClass( this.activeClassName );
                delete this.activeItem;
            }
        },

        _selectFocusedItem: function (evt) {
            this.selectItem($(evt.target));
        },

        remove: function () {
            this.removeAll();
            this.$emptyText && this.$emptyText.remove();
            return SuperClass.prototype.remove.call(this);
        }
    });
});
