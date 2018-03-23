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

define([
    './CollectionView',
    '../serviceItem/views/badge/ItemBadgeBlockView',
    'jquery',
    'underscore'
], function(CollectionView, ItemBadgeBlockView, $, _) {
    return CollectionView.extend({
        className: 'listings-grid-view',
        CollectionItemView: ItemBadgeBlockView,

        modelEvents: _.defaults({
            sync: 'showCurrentPage'
        }, CollectionView.prototype.modelEvents),

        /**
         * @cfg (Required) The page size used by the backing collection.
         * This is needed in order to handle animating in new pages as they arrive
         */
        pageSize: null,

        $currentPage: null,

        addOne: function(item) {
            //create a new page if needed
            if (!this.$currentPage || this.$currentPage.children().size() >= this.pageSize) {
                this.$currentPage = $('<div class="page">');

                //the page will be shown once it is fully populated and the collection's
                //sync event fires
                this.$currentPage.hide();

                this.$el.append(this.$currentPage);
            }

            return CollectionView.prototype.addOne.call(this, item, this.$currentPage);
        },

        showCurrentPage: function() {
            this.$currentPage.show(1000);
        }
    });
});
