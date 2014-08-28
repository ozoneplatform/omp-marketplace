/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/MyReviewsView',
    'profile/Comment',
    'views/ListView',
    'collections/ProfileItemComments',
    'models/Profile',
    'backbone',
    'underscore',
    'jquery'
], function(MyReviewsView, Comment, ListView, ProfileItemComments, Profile, Backbone, _, $) {
    'use strict';

    describe('My Reviews View', function() {
        var view,
            profileAttrs = {
                id: 1
            },
            profileModel = new Profile(profileAttrs),
            fetchDeferred,
            fetchStub,
            collectionData = [
                new Backbone.Model({
                    text: 'blah blah',
                    rate: 5,
                    serviceItem: {
                        title: 'test listing 1',
                        id : 1
                    }
                }),
                new Backbone.Model({
                    text: 'bad',
                    rate: 1,
                    serviceItem: {
                        title: 'test listing 1',
                        id : 1
                    }
                })
            ],
            isSelf;

        beforeEach(function() {
            fetchStub = sinon.stub(ProfileItemComments.prototype, 'fetch', function() {
                return fetchDeferred.promise();
            });

            profileModel.isSelf = sinon.spy(function() {
                return isSelf;
            });
            isSelf = true;

            fetchDeferred = $.Deferred();
            view = new MyReviewsView({ model: profileModel });
        });

        afterEach(function() {
            fetchStub.restore();
        });

        it('is a listView of Comments', function() {
            expect(view).to.be.a(ListView);
            expect(view.ItemView).to.be(Comment);
        });

        it('creates a ProfileItemComments collection', function() {
            var collection = view.collection;

            expect(collection).to.be.a(ProfileItemComments);
            expect(view.collection).to.be(collection);
            expect(collection.profileUrl).to.be(_.result(profileModel, 'url'));
        });

        it('automatically fetches the collection and then renders itself', function() {
            var renderStub = sinon.stub(MyReviewsView.prototype, 'render', function() {
                    return this;
                });

            fetchDeferred.resolve();

            expect(fetchStub.calledOnce).to.be.ok();

            renderStub.restore();
        });

        it('has a header with the collection size', function() {
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            expect(view.$('> h4')[0]).to.be.ok();
            expect(view.$('.count')[0]).to.be.ok();
            expect(view.$('.count').text()).to.be('2');
        });

        it('has the correct subtitle when it is yourself and it isnt empty', function() {
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            expect(view.$('> h6').text()).to.be('Your reviews of listings.');
        });

        it('has the correct subtitle when it is another user and it isnt empty', function() {
            isSelf = false;
            view = new MyReviewsView({ model: profileModel });
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            expect(view.$('> h6').text()).to.be("This user's reviews of listings.");
        });

        it('has the correct subtitle when it is yourself and it is empty', function() {
            view.collection.reset();

            expect(view.$('> h6').text()).to.be('You have not reviewed any listings.');
        });

        it('has the correct subtitle when it is another user and it is empty', function() {
            isSelf = false;
            view = new MyReviewsView({ model: profileModel });
            view.collection.reset();

            expect(view.$('> h6').text()).to.be('This user has not reviewed any listings.');
        });
    });
});
