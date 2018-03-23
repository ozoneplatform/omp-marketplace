/*global describe, beforeEach, afterEach, it, expect */
define([
    'views/Dialog',
    'marketplace'
], function(Dialog, Marketplace) {
    'use strict';

    describe('Dialog', function() {
        var view,
            title = 'Title',
            text = 'Lorem Ipsum ...';

        beforeEach(function() {
            view = new Dialog({ title: title, text: text });
        });

        afterEach(function() {
            $(document.body).empty();
        });

        it('has the bootstrap-active and dialog classes', function() {
            expect(view.$el.hasClass('bootstrap-active')).to.be.ok();
            expect(view.$el.hasClass('dialog')).to.be.ok();
        });

        it('renders a header with its title', function() {
            view.render();

            expect(view.$('.title').text()).to.be(title);
        });

        it('renders a paragraph with its text', function() {
            view.render();

            expect(view.$('p').text()).to.be(text);
        });

        it('appends itself to the body when show is called', function() {
            view.show();

            expect($('body > .dialog').length).to.not.be(0);
        });

        it('removes itself when its close buttons are clicked', function() {
            view.show();
            view.$('h3 .close').click();

            expect($('body > .dialog').length).to.be(0);

            view = new Dialog({ title: title, text: text });
            view.show();
            view.$('button.close-dialog').click();

            expect($('body > .dialog').length).to.be(0);
        });

        it('has a static method to show it', function() {
            Dialog.show(title, text);

            expect($('body > .dialog').length).to.not.be(0);
        });

        it('has a Marketplace  method to show it', function() {
            Marketplace.showDialog(title, text);

            expect($('body > .dialog').length).to.not.be(0);
        });
    });
});
