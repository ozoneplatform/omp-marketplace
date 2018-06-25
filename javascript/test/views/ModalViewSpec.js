/*global describe, beforeEach, it, expect */
define([
    'views/ModalView',
    'marketplace',
    'sinon',
    'underscore',
    'jquery'
], function(ModalView, Marketplace, sinon, _, $) {
    'use strict';

    describe('ModalView', function() {
        var view;

        beforeEach(function() {
            view = new ModalView();
        });

        it('has a modal back button if backTitle is defined', function() {
            var backTitle = 'Old Modal',
                backButton;

            view.render();

            backButton = view.$('.modal-back');
            expect(backButton[0]).to.not.be.ok();

            view.remove();
            view = new ModalView({ backTitle: backTitle });
            view.render();

            backButton = view.$('.modal-back');
            expect(backButton[0]).to.be.ok();
            expect(backButton.text()).to.be('Back to Old Modal');


            view.remove();
            view = new ModalView({ backTitle: null });
            view.render();

            backButton = view.$('.modal-back');
            expect(backButton[0]).to.be.ok();
            expect(backButton.text()).to.be('Back');
        });

        it('calls toPreviousModal on the router when the back button is clicked', function() {
            Marketplace.mainRouter = Marketplace.mainRouter || {};
            _.extend(Marketplace, { mainRouter: { toPreviousModal: $.noop }});
            var stub = sinon.stub(Marketplace.mainRouter, 'toPreviousModal');

            view = new ModalView({ backTitle: 'Title' });
            view.render().$el.appendTo(document.body);

            $('.modal-back').click();

            expect(stub.calledOnce).to.be.ok();

            view.remove();
            stub.restore();
        });
    });
});
