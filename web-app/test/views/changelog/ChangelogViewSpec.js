/*global describe, beforeEach, afterEach, it, expect*/
define([
    'views/changelog/ChangelogView',
    'models/ServiceItem',
    '../../fixtures/changelogs.js',
    'jquery',
    'underscore',
    'sinon'
], function(ChangelogView, ServiceItem, ChangelogFixtures, $, _, sinon) {
    'use strict';

    describe('Quickview: Admin tab, Changelog view', function() {
        var view, model, stub, server;

        beforeEach(function () {
            server = sinon.fakeServer.create();

            model = new ServiceItem({});

            view = new ChangelogView({
                model: model
            });
        });

        afterEach(function () {
            view && view.remove();

            server.restore();

            stub && stub.restore();
            stub = null;
        });

        it('renders correctly.', function () {
            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify(ChangelogFixtures.RESPONSE)
            );

            view.render();

            expect(view.$('h4').html()).to.be('Changelog');
            expect(view.changelogs.listView.$el.children().length).to.be(9);
        });

    });

});
