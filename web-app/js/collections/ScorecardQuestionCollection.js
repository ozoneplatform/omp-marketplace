define([
    './BaseCollection',
    '../models/ScorecardQuestion',
    'marketplace'
],
function(BaseCollection, ScoreCardQuestion, Marketplace) {

    return BaseCollection.extend({

        model: ScoreCardQuestion,

        url: function() {
            return (Marketplace.context + '/scoreCardItem/getScoreCardData');
        },
        
        parse: function (resp) {
            return resp.ScoreCardList;
        }

    });

});