/*global expect, it, describe, beforeEach, afterEach */
define([
    'profile/MyTagsView',
    'tag/views/TagView',
    'views/ListView',
    'backbone',
    'sinon',
    'underscore'
], function(MyTagsView, TagView, ListView, Backbone, sinon, _) {
    'use strict';


    describe('My Tags View', function() {
        var view,
            serviceItemTagsCollection,
            serviceItemTags = [{
                id: 1,
                title: 'listing 1',
                tags: [{
                    id: 1,
                    title: 'tag 1'
                }, {
                    id: 2,
                    title: 'tag 2'
                }]
            }, {
                id: 2,
                title: 'listing 2',
                tags: [{
                    id: 3,
                    title: 'tag 3'
                }]
            }],
            model,
            fetchDeferred,
            isSelf,
            methodsToStub = ['initialize', 'remove'],
            classesToStub = [TagView];

        //stubbed out classes for the models and collections needed by this view
        var ServiceItemTagModel = Backbone.Model.extend({
                tags: function() {
                    return new Backbone.Collection(this.get('tags'));
                }
            }),

            ProfileModel = Backbone.Model.extend({
                serviceItemTags: function() {
                    return serviceItemTagsCollection;
                },

                isSelf: function() { return isSelf; }
            });

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            sinon.stub(TagView.prototype, 'render', function() {
                this.$el.text(this.model.get('title'));
                return this;
            });

            serviceItemTagsCollection = new Backbone.Collection(serviceItemTags, {
                model: ServiceItemTagModel
            });

            fetchDeferred = $.Deferred();

            sinon.stub(serviceItemTagsCollection, 'fetch', function() {
                return fetchDeferred.promise();
            });

            sinon.spy(MyTagsView.prototype, 'render');

            model = new ProfileModel();

            isSelf = true;

            view = new MyTagsView({ model: model });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    View.prototype[method].restore();
                });
            });

            TagView.prototype.render.restore();
            MyTagsView.prototype.render.restore();
        });

        it('is a ListView of ListViews of TagViews', function() {
            var subViewInitStub = sinon.stub(view.ItemView.prototype, 'initialize');

            expect(view).to.be.a(ListView);
            expect(new view.ItemView()).to.be.a(ListView);
            expect(new view.ItemView.prototype.ItemView()).to.be.a(TagView);

            subViewInitStub.restore();
        });

        it('has a css class of my-tags', function() {
            expect(view.$el.hasClass('my-tags')).to.be.ok();
        });

        it('automatically fetches and renders when the fetch completes', function() {
            expect(serviceItemTagsCollection.fetch.calledOnce).to.be.ok();
            expect(view.render.calledOnce).to.not.be.ok();

            fetchDeferred.resolve();

            expect(view.render.calledOnce).to.be.ok();
        });

        it('has the count in its header', function() {
            fetchDeferred.resolve();

            expect(view.$('.count').text()).to.be('3');
        });

        it('is a table with the header as its caption and two columns', function() {
            fetchDeferred.resolve();

            expect(view.$el.is('table')).to.be.ok();

            //the 3 comes from the count
            expect(view.$('caption > h4').text()).to.be('My Tags3');

            expect(view.$('th').length).to.be(2);

            var columnTitles = _.map(view.$('th'), function(th) { return $(th).text(); });

            expect(columnTitles).to.contain('Listing');
            expect(columnTitles).to.contain('Tags');
        });

        it('has a row for each serviceItem', function() {
            fetchDeferred.resolve();

            var rows = view.$('> tbody > tr'),
                serviceItems = _.map(rows, function(row) {
                    return $(row).children('td:not(.tags)').text();
                });

            expect(rows.length).to.be(2);
            expect(serviceItems).to.contain('listing 1');
            expect(serviceItems).to.contain('listing 2');
        });

        it('has a view for each tag for each listing', function() {
            fetchDeferred.resolve();

            var rows = view.$('> tbody > tr'),

                //list of jquery objects, one for each row.  Each jquery object
                //holds the tag els for that row
                tags = _.map(rows, function(row) {
                    return $(row).children('.tags').children();
                }),
                //list of lists of titles of tags
                tagTitles = _.map(tags, function(tags) {
                    return _.map(tags, function(tag) {
                       return $(tag).text();
                    });
                });

            expect(tags.length).to.be(2);
            expect(tags[0].length).to.be(2);
            expect(tags[1].length).to.be(1);

            expect(tagTitles[0]).to.contain('tag 1');
            expect(tagTitles[0]).to.contain('tag 2');
            expect(tagTitles[1]).to.contain('tag 3');
        });

        it('hides the column headers and shows a message in the caption when the empty text ' +
                'is shown', function() {
            fetchDeferred.resolve();

            view.$el.appendTo(document.body);

            view.showEmptyText();

            expect(view.$('caption .empty-text')).to.be.ok();
            expect(view.$('thead').is(':visible')).to.be(false);

            view.remove();
        });

        it('shows the column headers and removes the empty message when empty text is hidden',
                function() {
            fetchDeferred.resolve();

            view.$el.appendTo(document.body);

            view.showEmptyText();
            view.hideEmptyText();

            expect(view.$('caption .empty-text')[0]).to.not.be.ok();
            expect(view.$('thead').is(':visible')).to.be(true);

            view.remove();
        });

        it('has the correct subtitle when it is yourself and it isnt empty', function() {
            view.render();

            expect(view.$('caption > h6').text()).to.be('Tags you added.');
        });

        it('has the correct subtitle when it is another user and it isnt empty', function() {
            isSelf = false;
            view = new MyTagsView({ model: model });
            view.render();

            expect(view.$('caption > h6').text()).to.be('Tags added by this user.');
        });

        it('has the correct subtitle when it is yourself and it is empty', function() {
            view.collection.reset();

            expect(view.$('caption > h6').text()).to.be('You have not added any tags.');
        });

        it('has the correct subtitle when it is another user and it is empty', function() {
            isSelf = false;
            view = new MyTagsView({ model: model });
            view.collection.reset();

            expect(view.$('caption > h6').text()).to.be('This user has not added any tags.');
        });
    });
});
