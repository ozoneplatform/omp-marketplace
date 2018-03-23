/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/ItemCommentTab',
    'profile/MyReviewsView',
    'profile/MyTagsView',
    'models/Profile',
    'underscore'
], function(ItemCommentTab, MyReviewsView, MyTagsView, Profile, _) {
    'use strict';

    describe('Profile Window Reviews Tab', function() {
        var view,
            profileAttrs = {
                id: 1
            },
            profileModel = new Profile(profileAttrs),
            methodsToStub = ['initialize', 'render', 'remove'];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each([MyReviewsView, MyTagsView], function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            view = new ItemCommentTab({ model: profileModel });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each([MyReviewsView, MyTagsView], function(View) {
                    View.prototype[method].restore();
                });
            });
        });

        it('has the title "Reviews & Tags"', function() {
            var expectedTitle = 'Reviews & Tags';

            expect(view.title).to.be(expectedTitle);
        });

        it('has the css class item-comment-tab', function() {
            expect(view.$el.hasClass('item-comment-tab')).to.be.ok();
        });

        it('has a MyListingsView and a ChangelogView', function() {
            expect(view.reviewsView).to.be.a(MyReviewsView);
            expect(view.tagsView).to.be.a(MyTagsView);

            expect(view.reviewsView.initialize.calledWith({
                model: view.model
            })).to.be.ok();
            expect(view.tagsView.initialize.calledWith({
                model: view.model
            })).to.be.ok();
        });

        it('renders it children views into itself', function() {
            view.render();

            expect(view.reviewsView.$el.parent()[0]).to.be(view.el);
            expect(view.tagsView.$el.parent()[0]).to.be(view.el);
        });

        it('removes its children on remove', function() {
            view.render();
            view.remove();

            expect(view.reviewsView.remove.calledOnce).to.be.ok();
            expect(view.tagsView.remove.calledOnce).to.be.ok();
        });
    });
});
