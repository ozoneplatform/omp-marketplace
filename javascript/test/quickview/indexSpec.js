/*global describe, beforeEach, afterEach, it, expect */
define([
    'quickview/index',
    'quickview/main',
    'sinon',
    'underscore'
], function(Quickview, QuickViewMainView, sinon, _) {
    'use strict';

    describe('Quickview index view', function() {
        var view,
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [QuickViewMainView];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            view = new Quickview();
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    View.prototype[method].restore();
                });
            });
        });

        it('defers getTitle to its main view', function() {
            var title = 'Title', retval;
            sinon.stub(view.mainView, 'getTitle', function() { return title; });

            retval = view.getTitle();

            expect(retval).to.be(title);
        });
    });
});
