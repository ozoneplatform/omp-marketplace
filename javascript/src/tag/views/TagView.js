/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(
[
    '../../views/BaseView',
    'backbone',
    'handlebars',
    'jquery',
    'underscore',
    'marketplace'
],
function(BaseView, Backbone, Handlebars, $, _, Marketplace) {

    var SuperClass = BaseView;

    function tagSearchUrl (tagTitle) {
        return Marketplace.context + '/serviceItem/search?queryString=' + encodeURIComponent('tags.tag.title:"' + tagTitle + '"');
    }

    return SuperClass.extend({

        tagName: 'span',

        className: 'tag-container',

        tpl: Handlebars.compile(
            '<a class="tag-link" href="{{ searchUrl }}">' +
                '{{ title }}' +
            '</a>' +
            '{{#if allowDelete}}' +
                '<span class="tag-remove">×</span>' +
            '{{else}}' +
            '{{/if}}'
        ),

        events: {
            "click .tag-remove" : "doRemove"
        },

        doRemove: function() {
            if(this.allowDelete()) {
                this.model.destroy();
                this.remove();
            }
        },

        initialize: function () {
          return this.render();
        },

        render: function() {

            var title = this.model.get('tag') === undefined ? this.model.get('title') : this.model.get('tag').title;

            var configs = {
                title: title,
                context: Marketplace.context,
                allowDelete:  this.allowDelete(),
                searchUrl: tagSearchUrl(title)
            };
            this.$el.html(this.tpl(configs));
            return this;
        },

        allowDelete: function() {
            return Marketplace.user.isAdmin === true ||
                Marketplace.user.id === this.model.get('createdBy').id;
        }
    });
});
