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
    'underscore'
], function(_) {
    /**
     * @event fetchMore
     * Fires when more records are fetched into the collection.
     *
     * @param currentCount The number of records currently in the collection client-side
     * @param totalCount The number of total possible records according to the server
     */

    /**
     * A mixin for backbone collections that adds logic for dealing with
     * a paged backend, where the collection does not discard previous pages, but rather
     * appends the results of successive page fetches to the items that it already has
     */
    return {
        initialize: function(options) {
            this.pageSize = options.pageSize;
            this.offset = 0;
        },

        /*
         * Fetch the next page of items and add it to the end of the collection
         */
        fetchMore: function(options) {
            var me = this,
                fetchOpts = _.defaults({
                    data: {
                        offset: this.offset,
                        max: this.pageSize
                    },
                    remove: false
                }, options);

            this.offset += this.pageSize;

            return this.fetch(fetchOpts).done(function(response) {
                me.trigger('fetchMore', me.size(), response.total);
            });
        }
    };
});
