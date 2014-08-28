define([
    '../views/BaseView',
    './ScoreCardQuestionView',
    './ScoreCardQuestionTableView',
    './CreateEditScoreCardQuestionView',
    '../models/ScorecardQuestion',
    '../collections/ScorecardQuestionCollection',
    '../events/EventBus',
    '../views/LoadMask',
    'underscore',
    'jquery',
    'marketplace'
],
function(BaseView, ScoreCardQuestionView, ScoreCardQuestionTableView, CreateEditScoreCardQuestionView, ScoreCardModel, ScorecardQuestionCollection, EventBus, LoadMask, _, $, Marketplace) {

    return BaseView.extend({

        template:
            '<h1>Scorecard Questions</h1>' +
            '<p></p>' +
            '<table class="scorecard-questions"></table>' +
            '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>',

        events: {
            'click .btn-field-add': 'addQuestion',
            'click .edit': 'editQuestion',
            'click .delete': 'deleteQuestion'
        },

        initialize: function (options) {
            this.collection = new ScorecardQuestionCollection();
            BaseView.prototype.initialize.call(this, options);
        },

        render: function () {
            this.$el.append(this.template);

            LoadMask.show();
            this.collection.fetch().then(_.bind(this.renderQuestionsView, this));

            return this;
        },

        renderQuestionsView: function () {
            Marketplace.hideLoadMask();

            if(this.collection.length === 0) {
                return;
            }

            this.ScoreCardQuestionTableView = new ScoreCardQuestionTableView({
                collection: this.collection,
                el: this.$el.children('.scorecard-questions')
            });
            this.ScoreCardQuestionTableView.render();
        },

        remove: function () {
            this.ScoreCardQuestionTableView && this.ScoreCardQuestionTableView.remove();
            BaseView.prototype.remove.call(this);
        },

        addQuestion: function() {
            CreateEditScoreCardQuestionView.create();
        },

        editQuestion: function(evt) {
            var $btn = $(evt.target),
                model = $btn.parents('tr').data('view').model;
            CreateEditScoreCardQuestionView.edit(model);
        },

        deleteQuestion: function(evt) {
            evt.preventDefault();
            var $btn = $(evt.target),
                model = $btn.parents('tr').data('view').model;

            $btn.confirm({
                trigger: 'manual',
                placement: 'bottom',
                title: 'Delete Question',
                content: 'Are you sure you want to delete this question?',
                ok: function() {
                    model.destroy({
                        wait: true
                    });
                }
            }).confirm('show');

        }
    })
});
