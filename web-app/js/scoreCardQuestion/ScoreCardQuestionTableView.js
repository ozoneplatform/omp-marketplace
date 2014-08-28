define([
    '../views/BaseView',
    '../views/ListView',
    './ScoreCardQuestionView',
    './CreateEditScoreCardQuestionView',
    '../collections/ScorecardQuestionCollection',
    'underscore',
    'jquery',
    'marketplace'
],
function(BaseView, ListView, ScoreCardQuestionView, CreateEditScoreCardQuestionView, ScoreCardQuestionCollection, _, $, Marketplace) {
    'use strict';

    return BaseView.extend({

        tagName: 'table',

        className: 'table',

        template:
            '<thead>' +
                '<td><h5>Icon</h5></td>' +
                '<td><h5>Question</h5></td>' +
                '<td><h5>Description</h5></td>' +
                '<td><h5>Visible</h5></td>' +
            '</thead>' +
            '<tbody id="table-body-scorecards"></tbody>',

//        events: {
//            'click .delete': 'deleteQuestion'
//        },

        render: function () {
            this.$el.append(this.template);

            this.renderQuestions();

            return this;
        },

        renderQuestions: function() {
            this.listView = new ListView({
                collection: this.collection,
                ItemView: ScoreCardQuestionView,
                el: this.$el.find('#table-body-scorecards')
            });
            this.listView.render();
            return this;
        },

        remove: function () {
            this.listView.remove();
            BaseView.prototype.remove.call(this);
        }
    });
});
