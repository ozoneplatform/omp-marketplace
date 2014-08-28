define([
    'jquery',
    'underscore',
    'backbone',
    '../models/CardModel'

], function($, _, Backbone, CardModel){

    var cardsCollection =   Backbone.Collection.extend({

        model: CardModel,

        initialize: function() {


        }



    });

    return cardsCollection;

});

