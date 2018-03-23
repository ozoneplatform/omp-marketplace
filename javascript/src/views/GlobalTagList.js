define([
    '../collections/Tags',
    'backbone',
    'handlebars',
    'underscore',
    'bootstrap'
],

function (TagsCollection, Backbone, Handlebars, _, $) {
    var SuperClass = Backbone.View,

        FONT_SIZE_MIN = 10,
        FONT_SIZE_MAX = 34,
        CLOUD_SIZE = 120,

        indexTpl = Handlebars.compile(
            '<li class="tag-list-index"><h2>{{letter}}</h2></li>'
        ),

        listItemTpl = Handlebars.compile(
            '<li class="tag-list-item"><a class="item-link" href="{{searchUrl}}">{{title}}</a>'+
            '{{#if isAdmin}}<span class="tag-list-item-container"><span class="item-count">{{itemCount}}</span>'+
            '<span class="btn-field-remove"><a data-tag-id="{{id}}" class="btn btn-small delete-tag-btn">-</a></span></span>{{/if}}</li>'
        ),

        cloudItemTpl = Handlebars.compile(
            '<a class="cloud-item" href="{{searchUrl}}" style="font-size: {{fontSize}}pt">{{title}}</a> '
        ),

        headerTpl = Handlebars.compile(
            '<div class="tag-list-header"><h1>Tags</h1><span class="tag-list-selector"><h4>view </h4> <i class="icon-list"></i> <i class="icon-cloud"></i></span></div>'
        );

    function tagSearchUrl (tagTitle) {
        return Marketplace.context + '/serviceItem/search?queryString=' + encodeURIComponent('tags.tag.title:"' + tagTitle + '"');
    }

    function tagSortByTitle (tag) {
        return tag.get('title').charAt(0).toUpperCase();
    }

    return SuperClass.extend({

        tagName: 'div',

        className: 'tag-list bootstrap-active',

        events: {
            'click .delete-tag-btn': 'deleteTag',
            'click .icon-cloud': 'renderAsCloud',
            'click .icon-list': 'renderAsList'
        },

        render: function () {
            var me = this;

            function doRender () {
                me.listenTo(me.collection, 'remove', me.renderAsList);
                me.renderAsList();
            }

            if(this.collection) {
                doRender();
            } else {
                this.collection = new TagsCollection();
                this.collection.fetch().done(doRender);
            }

            return this;
        },

        renderAsList: function () {
            var html = headerTpl() + '<ul class="tag-column-1">',
                lastLetter = '',
                column = 1,
                oneFourth = Math.floor(this.collection.models.length / 4),
                switchToNextColumnAt = oneFourth,

                models = _.sortBy(this.collection.models, tagSortByTitle);

            _.each(models, function (item, index) {
                var currLetter = item.get('title').charAt(0).toUpperCase();

                if(index > switchToNextColumnAt && column < 4) {
                    column += 1;
                    switchToNextColumnAt += oneFourth;
                    html += '</ul><ul class="tag-column-' + column + '">';
                }

                if(lastLetter !== currLetter) {
                    html += indexTpl({ letter: currLetter.toUpperCase() });
                    lastLetter = currLetter;
                }

                var data = _.extend({}, item.attributes);
                data.searchUrl = tagSearchUrl(data.title);
                data.isAdmin = Marketplace.user.isAdmin;

                html += listItemTpl(data);
            });

            html += '</ul>';
            this.$el.html(html);
            $('.icon-list').addClass('icon-active');
            $('.icon-cloud').removeClass('icon-active');
            return this;
        },

        renderAsCloud: function () {
            var html = headerTpl() + '<div class="tag-cloud">',
                maxCount = 0,
                minCount = 999999999;

            function tagSortByCount (tag) {
                return tag.get('itemCount');
            }

            function fontSize (itemCount) {
                if(maxCount === minCount) {
                    return FONT_SIZE_MIN;
                }

                var multiple = (itemCount - minCount) / (maxCount - minCount),
                    fontRange = FONT_SIZE_MAX - FONT_SIZE_MIN;

                return FONT_SIZE_MIN + multiple * fontRange;
            }

            function checkCounts (tag) {
                var itemCount = tag.get('itemCount');
                if(itemCount > maxCount) {
                    maxCount = itemCount;
                }

                if(itemCount < minCount) {
                    minCount = itemCount;
                }
            }

            function appendTagHtml (tag) {
                var data = _.extend({}, tag.attributes);
                data.fontSize = fontSize(tag.get('itemCount'));
                data.searchUrl = tagSearchUrl(data.title);
                html += cloudItemTpl(data);
            }

            _.chain(this.collection.models)
                .sortBy(tagSortByCount)
                .last(CLOUD_SIZE)
                .each(checkCounts)
                .sortBy(tagSortByTitle)
                .each(appendTagHtml);

            html += '</div>';
            this.$el.html(html);
            $('.icon-cloud').addClass('icon-active');
            $('.icon-list').removeClass('icon-active');
            return this;
        },

        deleteTag: function (evt) {
            var $btn = $(evt.target),
                modelId = $btn.data('tag-id'),
                model = this.collection.get(modelId);

            $btn.confirm({
                trigger: 'manual',
                placement: 'right',
                title: 'Remove Tag',
                content: 'Clicking "OK" will remove the tag from the system.',
                ok: function () {
                    model.destroy();
                }
            }).confirm('toggle');
        }
    });
});
