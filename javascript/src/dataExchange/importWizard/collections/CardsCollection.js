define([
    'jquery',
    'underscore',
    'backbone',
    '../models/CardModel'

], function($, _, Backbone, CardModel){

    var cardsCollection =   Backbone.Collection.extend({
        model: CardModel
    });

    return cardsCollection;

});

