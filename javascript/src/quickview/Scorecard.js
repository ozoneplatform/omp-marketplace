define([
    '../views/tabbable/TabPaneView',
    '../views/ListView',
    '../views/BaseView',
    '../collections/ScorecardQuestionCollection',
    'handlebars',
    'jquery',
    'underscore',
    'backbone',
    'bootstrap'
],
function(TabPaneView, ListView, BaseView, ScoreCardQuestionCollection, Handlebars, $, _, Backbone) {

    var Scorecards = ListView.extend({

        tagName: 'ul',

        emptyText: '<div class="empty">Unable to retrieve scorecards.</div>',

        ItemView: BaseView.extend({

            tagName: 'li',

            className: 'row',

            template: Handlebars.compile(
                '{{#if showOnListing}}' +
                '<div class="span1">{{#if image}}<img src="{{image}}">{{/if}}</div>' +
                '<div class="span9">' +
                    '<p class="question">{{question}}</p>' +
                    '<p class="description">{{description}}</p>' +
                '</div>' +
                '<div class="span">' +
                    '<input type="checkbox" id="{{id}}" class="ios brand-success {{#if isSatisfied}}checked{{/if}}" {{#if isSatisfied}}checked{{/if}}/>' +
                '</div>' +
                '{{/if}}'

            ),

            data: function () {
                return this.model.toJSON();
            },

            render: function () {
                this.$el.append(this.template(this.data()));
                this.$switch = this.$el.find('input[type="checkbox"]').svitch();
                return this;
            }
        }),

        initialize: function (options) {
            ListView.prototype.initialize.call(this, options);
        }

    });

    return TabPaneView.extend({

        title: 'Scorecard',

        className: 'quickview-scorecard',

        href: function () {
            return 'quickview-' + this.title.toLowerCase();
        },

        events: {
            'change input[type="checkbox"]': 'save'
        },

        _save: function (evt) {
            var checked = $(evt.currentTarget).is(':checked');
            var curr = this.model.scorecards();
            if(checked){
                // add it to the current list
                curr.push({id: evt.currentTarget.id});
            } else {
                // remove it from the current list
                curr.pop({id: evt.currentTarget.id});
            }
            this.model.set('satisfiedScoreCardItems', curr);
            this.model.save().fail(_.bind(function () {
                if(this.$switch){
                    this.$switch.svitch('toggle');
                }
            }, this));
        },

        save: _.debounce(function (evt) {
            this._save(evt);
        }, 200),

        render: function () {
            this.collection = new ScoreCardQuestionCollection();
            this.collection.fetch().then(
                _.bind(this.renderScorecardsView, this));
            return this;
        },

        renderScorecardsView: function (siCollection) {
            var coll = new Backbone.Collection();
            var ids = this.model.scorecards();

            _.each(siCollection.ScoreCardList, function (question) {
                if(_.find(ids, {id: question.id})){
                    question.isSatisfied = true;
                } else {
                    question.isSatisfied = false;
                }
                coll.add(question);
            });
            this.scorecards = new Scorecards({
                collection: coll
            });
            this.$el.append(this.scorecards.render().el);

            return this;
        },

        remove: function () {
            this.scorecards && this.scorecards.remove();
            TabPaneView.prototype.remove.call(this);
            return this;
        }

    });

});
