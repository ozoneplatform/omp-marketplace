/*global describe, beforeEach, afterEach, it, expect */
define([
    'quickview/main',
    'quickview/header',
    'quickview/body',
    'tag/views/TagPanelView',
    'sinon',
    'underscore'
], function(QuickViewMainView, Header, Body, TagPanelView, sinon, _) {
    'use strict';

    describe('Quickview index view', function() {
        var view,
            modelAttrs = {
                id: 1,
                title: 'Listing 1'
            },
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [Header, Body, TagPanelView];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            view = new QuickViewMainView({
                serviceItemId: modelAttrs.id,
                context: ''
            });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    View.prototype[method].restore();
                });
            });
        });

        it('gets its title from the serviceItemModel', function() {
            view.serviceItemModel.set(modelAttrs);

            expect(view.getTitle()).to.be(modelAttrs.title);
        });
    });
});

