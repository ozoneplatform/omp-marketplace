/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(
[
    '../../views/BaseView',
    '../../models/ServiceItemTagModel',
    '../../events/EventBus',
    'backbone',
    'handlebars',
    'jquery',
    'underscore',
    'underscore.string',
    'marketplace',
    'select2'
],
function(BaseView, ServiceItemTagModel, EventBus, Backbone, Handlebars, $, _, _s, Marketplace) {

    var SuperClass = BaseView;


    return SuperClass.extend({

        tagName: 'div',

        className: 'tag-lookup-panel',

        serviceItemModel: null,

        tpl: Handlebars.compile(
            '<div class="select2-container tag-lookup" style="display:none">' +
                '<select class="selectpicker"></select> ' +
            '</div>' +
            '<form style="display:inline"><span class="btn-field-add btn-field-add-tag"><a href="#" class="btn btn-small">+</a><span class="add-tag-text">Add</span></span></form>' +
            '<div class="tag-lookup-panel-hide">&nbsp</div>'
        ),

        events: {
            "click .btn-field-add" : "doAdd",
            "click .tag-lookup-panel-hide" : "reset"
        },

        initialize: function (options) {
            this.serviceItemModel = options.serviceItemModel;
            this.serviceItemTagModel = new ServiceItemTagModel({serviceItemId : this.serviceItemModel.get("id") });
            return this.render();
        },

        doAdd: function(e) {
            this.renderTagLookup();

            e.preventDefault();
            $(e.currentTarget).hide();
            this.$el.find(".tag-lookup").show();
            this.$select2.select2('open');
        },

        doSave: function() {
            this.trigger("tag:add-new", this.serviceItemTagModel);
        },

        render: function() {
            this.$el.html(this.tpl());
            return this;
        },

        renderTagLookup: function() {
            var me = this,
                titles;

            var cacheTagTitles = function () {
                titles = {};
                me.serviceItemModel.tags().reduce(function (memo, model) {
                    var title = model.get('tag') === undefined ? model.get('title') : model.get('tag').title;
                    memo[title.toLowerCase()] = true;
                    return memo;
                }, titles);

                return titles;
            };

            me.$select2 = me.$el.find(".tag-lookup").select2({
                minimumInputLength: 1,
                minimumResultsForSearch: -1,
                formatInputTooShort: function (input, min) { return 'Start typing'; },
                formatNoMatches: function(input) { return 'Tag already exists';},
                containerCssClass: 'tag-select2-container',
                dropdownCssClass: 'tag-select2-drop',
                maximumInputLength: 16,
                multiple: true,
                ajax: {
                    url: Marketplace.context + "/api/tag/search",
                    quietMillis: 200,
                    data: function (term) {
                        return {
                            title: term,
                            max: 10
                        };
                    },
                    results: function(data, page) {
                        //Get the id and title from the query results
                        var queriedTags = _.map(data, function(item) {
                            return {id: item.id, title: item.title};
                        });

                        cacheTagTitles();

                        //Filter out the existing id's from the queried list
                        var filtered = _.filter(queriedTags, function(val) {
                            return !titles[val.title.toLowerCase()];
                        });

                        return {results : filtered};
                    }
                },
                createSearchChoice: function(term, data) {
                    function inTitles(term) {
                        return !!titles[term.toLowerCase()];
                    }

                    function inData(term) {
                        var lowerTerm = term.toLowerCase();

                        return !!_.find(data, function(d) {
                            return d.title.toLowerCase() === lowerTerm;
                        });
                    }

                    term = _s.trim(term);
                    // check both local collection and response before creating a local tag
                    return (term && (inTitles(term) || inData(term))) ? null : {
                        id: 1,
                        title: term
                    };
                },
                formatResult: function (item) {
                    return item.title;
                },
                formatSelection: function(item) {
                    return item.title;
                }
            }).on("change", function(e) {
                me.handleSelection(e);
            });
        },

        //If there is no selection or what is entered is already in the list of existing tabs then we do not want to set the model
        handleSelection: function(e) {
            this.$select2.select2('destroy');
            this.$select2.hide();
            this.$select2 = null;
            this.serviceItemTagModel.set("title", e.added.title);
            this.doSave();
        },

        reset:function() {
            this.$el.find(".btn-field-add-tag").show();
            this.$el.find(".tag-lookup").hide();
        },

        remove: function () {
            this.$select2 && this.$select2.select2('destroy');
            SuperClass.prototype.remove.call(this);
        }

    });
});
