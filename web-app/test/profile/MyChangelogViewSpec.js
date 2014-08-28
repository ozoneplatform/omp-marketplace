/*global describe, beforeEach, afterEach, it, expect */
define([
    'profile/MyChangelogView',
    'views/changelog/ChangelogView',
    'views/LoadMoreListView',
    'backbone',
    'sinon',
    'underscore'
], function(MyChangelogView, ChangelogView, LoadMoreListView, Backbone, sinon, _) {
    'use strict';

    describe('MyChangelogView', function() {
        var view,
            model,
            modelAttrs = {
                id: 1,
                username: 'test'
            },
            fetchStub,
            isSelf,
            methodsToStub = ['render', 'remove'],
            classesToStub = [LoadMoreListView];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            sinon.stub(LoadMoreListView.prototype, 'initialize', function() {
                this.listView = { ItemView: Backbone.View.extend() };
            });

            model = new Backbone.Model(modelAttrs);
            model.changelogs = sinon.spy(function() {
                var retval = new Backbone.Collection();
                retval.fetch = fetchStub = sinon.spy();

                return retval;
            });

            model.isSelf = sinon.spy(function() {
                return isSelf;
            });

            isSelf = true;

            view = new MyChangelogView({ model: model });
        });

        afterEach(function() {
            _.each(methodsToStub.concat('initialize'), function(method) {
                _.each(classesToStub, function(View) {
                    View.prototype[method].restore();
                });
            });
        });

        it('is a ChangelogView', function() {
            expect(view).to.be.a(ChangelogView);
        });

        it('has a title of "Recent Activity', function() {
            view.render();

            expect(view.title).to.be('Recent Activity');
        });

        it('sets its lists empty text to ""', function() {
            expect(view.changelogs.listView.emptyText).to.be('');
        });

        it('sets its ItemViews globalActivity flag to true', function() {
            expect(view.changelogs.listView.ItemView.prototype.globalActivity).to.be(true);
        });

        it('has the correct subheader for yourself', function() {
            isSelf = true;
            view.render();

            expect(view.$('> h6').text()).to.be('Recent changes to your listings.');
        });

        it('has the correct subheader for yourself', function() {
            isSelf = false;
            view.render();

            expect(view.$('> h6').text()).to.be("Recent changes to this user's listings.");
        });
    });
});
