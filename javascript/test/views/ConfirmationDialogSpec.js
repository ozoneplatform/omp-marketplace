/*global describe, beforeEach, afterEach, it, expect */
define([
    'views/ConfirmationDialog',
    'views/Dialog',
    'marketplace'
], function(ConfirmationDialog, Dialog, Marketplace) {
    'use strict';

    describe('ConfirmationDialog', function() {
        var view,
            title = 'Title',
            text = 'Lorem Ipsum???';

        beforeEach(function() {
            view = new ConfirmationDialog({ title: title, text: text });
        });

        afterEach(function() {
            $(document.body).empty();
        });

        it('is a subclass of dialog', function() {
            expect(view).to.be.a(Dialog);
        });

        it('resolves its promise and removes itself when the ok button is clicked', function() {
            var promise = view.promise();

            view.show();
            expect($('.confirmation-dialog').length).to.be.ok();

            $('.confirmation-dialog .ok').click();

            expect(promise.state()).to.be('resolved');
            expect($('.confirmation-dialog').length).to.not.be.ok();
        });

        it('rejects its promise and removes itself when the cancel button is clicked',
                function() {
            var promise = view.promise();

            view.show();
            expect($('.confirmation-dialog').length).to.be.ok();

            $('.confirmation-dialog button.close-dialog').click();

            expect(promise.state()).to.be('rejected');
            expect($('.confirmation-dialog').length).to.not.be.ok();
        });

        it('rejects its promise and removes itself when the X button is clicked',
                function() {
            var promise = view.promise();

            view.show();
            expect($('.confirmation-dialog').length).to.be.ok();

            $('.confirmation-dialog .close').click();

            expect($('.confirmation-dialog').length).to.not.be.ok();
            expect(promise.state()).to.be('rejected');
        });

        it('has a static method to show it', function() {
            ConfirmationDialog.show(title, text);

            expect($('body > .confirmation-dialog').length).to.not.be(0);
        });

        it('has a Marketplace  method to show it', function() {
            Marketplace.showConfirmationDialog(title, text);

            expect($('body > .confirmation-dialog').length).to.not.be(0);
        });
    });
});
