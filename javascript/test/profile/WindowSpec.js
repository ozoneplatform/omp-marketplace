/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/Window',
    'profile/Contents'
], function(Window, Contents) {
    'use strict';

    describe('Profile Window', function() {
        var view,
            contentsInitStub,
            constructorArgs;

        beforeEach(function() {
            contentsInitStub = sinon.stub(Contents.prototype, 'initialize');
            constructorArgs = { profileId: 1 };
            view = new Window(constructorArgs);
        });

        afterEach(function() {
            contentsInitStub.restore();
        });

        it('has a Profile Contents', function() {
            expect(view.contents).to.be.a(Contents);
            expect(contentsInitStub.calledOnce).to.be.ok();
            expect(contentsInitStub.calledWith(constructorArgs)).to.be.ok();
        });

        it('has the profile-window class', function() {
            expect(view.$el.hasClass('profile-window')).to.be.ok();
        });

        it('Passes route calls on to its arguments', function() {
            var routeStub = sinon.stub(view.contents, 'route'),
                contentsRenderStub = sinon.stub(view.contents, 'render'),
                args = {tab: 'profile'};

            view.contents.render();
            view.route.call(view, args);

            expect(routeStub.calledOnce).to.be.ok();

            routeStub.restore();
            contentsRenderStub.restore();
        });

        it('renders the contents to its body', function() {
            view.render();

            expect(view.$body.children()[0]).to.be(view.contents.el);
        });

        it('defers getTitle to its Contents', function() {
            var title = 'Title',
                stub = sinon.stub(view.contents, 'getTitle', function() { return title; }),
                retval = view.getTitle();

            expect(retval).to.be(title);
            expect(stub.calledOnce).to.be.ok();
        });
    });
});
