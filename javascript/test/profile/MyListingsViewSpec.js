/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/MyListingsView',
    'views/CategorizedServiceItemList',
    'models/Profile',
    'collections/ProfileServiceItems',
    'backbone',
    'underscore'
], function(MyListingsView, CategorizedServiceItemList, Profile, ProfileServiceItems,
        Backbone, _) {
    'use strict';

    describe('Profile Window My Listings View', function() {
        var view,
            profileAttrs = {
                id: 1
            },
            profileModel = new Profile(profileAttrs),
            fetchDeferred,
            categorizedListAddOneStub,
            fetchStub,
            collectionData = [
                new Backbone.Model({
                    title: 'listing 1',
                    approvalStatus: 'In Progress',
                    types: {
                        title: 'type 2'
                    }
                }),
                new Backbone.Model({
                    title: 'listing 2',
                    approvalStatus: 'In Progress',
                    types: {
                        title: 'type 1'
                    }
                }),
                new Backbone.Model({
                    title: 'listing 3',
                    approvalStatus: 'Approved',
                    types: {
                        title: 'type 1'
                    }
                }),
                new Backbone.Model({
                    title: 'listing 4',
                    approvalStatus: 'Approved',
                    types: {
                        title: 'type 1'
                    }
                })
            ],
            isSelf,
            methodsToStub = ['initialize', 'render', 'remove'];

        beforeEach(function() {
            fetchStub = sinon.stub(ProfileServiceItems.prototype, 'fetch', function() {
                return fetchDeferred.promise();
            });

            _.each(methodsToStub, function(method) {
                _.each([CategorizedServiceItemList], function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            categorizedListAddOneStub =
                sinon.stub(CategorizedServiceItemList.prototype, 'addOne');

            profileModel.isSelf = sinon.spy(function() {
                return isSelf;
            });

            isSelf = true;

            fetchDeferred = $.Deferred();
            view = new MyListingsView({ model: profileModel });
        });

        afterEach(function() {
            fetchStub.restore();
            _.each(methodsToStub, function(method) {
                _.each([CategorizedServiceItemList], function(View) {
                    View.prototype[method].restore();
                });
            });
            categorizedListAddOneStub.restore();
            view.remove();
        });

        it('starts a collection fetch on initialize, and renders upon completion', function() {
            fetchDeferred.resolve();

            expect(view.initialFetchComplete).to.be.ok();
        });

        it('has the title "Listings"', function() {
            var expectedTitle = 'Listings',
                headerText;

            fetchDeferred.resolve();

            //need text from this element ONLY, without child element text
            //based on
            //http://stackoverflow.com/questions/11362085/jquery-get-text-for-element-without-children-text
            headerText = view.$('h4').clone()
                        .children()
                        .remove()
                        .end()
                        .text();

            expect(headerText).to.be(expectedTitle);
        });

        it('sorts its collection by approvalStatus, type, and title', function() {
            view.collection.set(collectionData);

            expect(view.collection.at(0).get('title')).to.be('listing 2');
            expect(view.collection.at(1).get('title')).to.be('listing 1');
            expect(view.collection.at(2).get('title')).to.be('listing 3');
            expect(view.collection.at(3).get('title')).to.be('listing 4');
        });

        it('displays the total count of the collection', function() {
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            expect(view.$('h4 .count').text()).to.be('4');
        });

        it('creates a group for each approvalStatus', function() {
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            var groupHeaders = view.$('h5'),
                groupTitles = _.map(groupHeaders, function(header) {
                    return $(header).clone()
                                .children()
                                .remove()
                                .end()
                                .text();
                }),
                groupCounts = _.map(groupHeaders, function(header) {
                    return $(header).children('.count').text();
                });

            expect(groupTitles.length).to.be(2);
            expect(groupTitles).to.contain('In Progress');
            expect(groupTitles).to.contain('Approved');

            expect(groupCounts[groupTitles.indexOf('In Progress')]).to.be('2');
            expect(groupCounts[groupTitles.indexOf('Approved')]).to.be('2');

            var inProgressView = view.childViews[groupTitles.indexOf('In Progress')],
                approvedView = view.childViews[groupTitles.indexOf('Approved')],
                inProgressTitles = inProgressView.collection.map(function(model) {
                    return model.get('title');
                }),
                approvedTitles = approvedView.collection.map(function(model) {
                    return model.get('title');
                });

            expect(inProgressView.collection.length).to.be(2);
            expect(approvedView.collection.length).to.be(2);

            expect(inProgressTitles).to.contain('listing 1');
            expect(inProgressTitles).to.contain('listing 2');
            expect(approvedTitles).to.contain('listing 3');
            expect(approvedTitles).to.contain('listing 4');
        });

        it('has the correct subtitle when it is yourself and it isnt empty', function() {
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            expect(view.$('> h6').text()).to.be('Listings you own.');
        });

        it('has the correct subtitle when it is another user and it isnt empty', function() {
            isSelf = false;
            view.collection.set(collectionData);
            fetchDeferred.resolve();

            expect(view.$('> h6').text()).to.be('Listings owned by this user.');
        });

        it('has the correct subtitle when it is yourself and it is empty', function() {
            view.collection.reset();
            fetchDeferred.resolve();

            expect(view.$('> h6').text()).to.be('You do not own any listings.');
        });

        it('has the correct subtitle when it is another user and it is empty', function() {
            isSelf = false;
            view.collection.reset();
            fetchDeferred.resolve();

            expect(view.$('> h6').text()).to.be('This user does not own any listings.');
        });
    });
});
