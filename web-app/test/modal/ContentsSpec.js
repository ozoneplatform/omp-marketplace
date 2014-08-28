/*global describe, beforeEach, afterEach, it, expect */
define([
    'modal/Contents',
    'backbone',
    'sinon',
    'jquery'
], function(Contents, Backbone, sinon, $) {
    'use strict';

    var ContentsSubClass = Contents.extend({
        BodyViewClass: Backbone.View,
        HeaderViewClass: Backbone.View
    });

    describe('modal Contents', function() {
        var view,
            model,
            modelAttrs = {
                attr1: 'test 1',
                attr2: 'test 2'
            };

        beforeEach(function() {
            model = new Backbone.Model(modelAttrs);
            sinon.stub(model, 'fetch', function() {
                return $.Deferred().promise();
            });
            view = new ContentsSubClass({ model: model });
        });

        afterEach(function() {
            model.fetch.restore();
        });

        it('gets its title from its model and title property', function() {
            view.titleProperty = 'attr1';
            expect(view.getTitle()).to.be('test 1');

            view.titleProperty = 'attr2';
            expect(view.getTitle()).to.be('test 2');
        });
    });
});

