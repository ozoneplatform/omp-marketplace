define([
    'jquery',
    'underscore',
    'backbone'

], function($, _, Backbone){

    var cardModel =   Backbone.Model.extend({

        defaults: {
            // Title of Card/Step
            title:          'Card title',
            // Instructions paragraph
            instructions:   'Card instructions',
            // True if the active step
            active: false,
            // True if this step has been visited
            visited: false,
            // True if card has been requested by Navigation
            switchToMe: false

        },

        initialize: function() {



        }

    });

    return cardModel;

});