/*global describe, beforeEach, afterEach, it, expect */
define([
    'profile/ThemeView',
    'views/LoadMask',
    'backbone',
    'jquery',
    'sinon',
    'marketplace'
], function(ThemeView, LoadMask, Backbone, $, sinon, Marketplace) {
    'use strict';

    describe('ThemeView', function() {
        var view,
            attrs = {
                display_name: 'test theme',
                name: 'test_theme',
                screenshots: [{
                    url: 'test.png',
                    description: 'screenshot 1 description'
                }, {
                    url: 'test2.png'
                }]
            },
            model,
            oldContext,
            oldTheme;

        beforeEach(function() {
            sinon.stub($.fn, 'tooltip');

            model = new Backbone.Model(attrs);
            view = new ThemeView({model: model});

            oldContext = Marketplace.context;
            Marketplace.context = 'https://localhost/marketplace-test';
            oldTheme = Marketplace.theme;
            Marketplace.theme = { name: '' };
        });

        afterEach(function() {
            view.remove();
            $.fn.tooltip.restore();

            Marketplace.context = oldContext;
            Marketplace.theme = oldTheme;
        });

        it('renders as an li tag', function() {
            expect(view.$el.prop('tagName')).to.be('LI');
        });

        it('renders the theme display name in a header', function() {
            view.render();

            expect(view.$('h5').text()).to.be(attrs.display_name);
        });

        it('renders an img for each screenshot', function() {
            view.render();

            var screenshots = view.$('a.screenshots > img');

            expect(screenshots.size()).to.be(2);
            expect($(screenshots[0]).attr('src')).to.be('https://localhost/marketplace-test/test.png');
            expect($(screenshots[1]).attr('src')).to.be('https://localhost/marketplace-test/test2.png');
        });

        it('creates a bootstrap tooltip for screenshots with descriptions', function() {
            view.render();

            expect($.fn.tooltip.calledOnce).to.be.ok();
            expect($.fn.tooltip.thisValues[0].get(0))
                .to.be(view.$('a.screenshots > img').get(0));

            view.remove();
            expect($.fn.tooltip.calledTwice).to.be.ok();
            expect($.fn.tooltip.thisValues[1].get(0))
                .to.be(view.$('a.screenshots > img').get(0));
            expect($.fn.tooltip.args[1]).to.eql(['destroy']);
        });

        it('sets the active-theme class if this theme is currently active', function() {
            //not currently the active theme
            Marketplace.theme.name = 'asdf';
            view.render();
            expect(view.$el.hasClass('active-theme')).to.be(false);

            view.remove();
            view = new ThemeView({model: model});

            //is the active theme
            Marketplace.theme.name = attrs.name;
            view.render();

            expect(view.$el.hasClass('active-theme')).to.be(true);
        });

        it('applies this theme and refreshes the page when Apply is clicked', function() {
            var deferred = $.Deferred();

            sinon.stub($, 'ajax', function() {
                return deferred.promise();
            });
            sinon.stub(LoadMask, 'show');
            sinon.stub(LoadMask, 'hide');
            sinon.stub(Marketplace, 'error');

            view.render().$el.appendTo(document.body);
            view.$('button.apply-theme-btn').click();

            expect($.ajax.calledOnce).to.be.ok();
            expect($.ajax.calledWithMatch({
                url: 'https://localhost/marketplace-test/theme/selectTheme',
                method: 'post',
                data: {theme: attrs.name}
            })).to.be.ok();
            expect(LoadMask.show.calledOnce).to.be.ok();

            //note: stubbing window.location.reload does not work reliably, so resolving the
            //deferred cannot be tested
            //deferred.resolve();



            deferred = $.Deferred();
            view.$('button.apply-theme-btn').click();

            expect($.ajax.calledTwice).to.be.ok();
            expect(LoadMask.show.calledTwice).to.be.ok();

            deferred.reject();

            expect(LoadMask.hide.calledOnce).to.be.ok();
            expect(Marketplace.error.calledOnce).to.be.ok();

            $.ajax.restore();
            LoadMask.show.restore();
            LoadMask.hide.restore();
            Marketplace.error.restore();
        });

        it('calls the magnific popup gallery when the screenshots are clicked', function() {
            view.render().$el.appendTo(document.body);

            sinon.stub($.magnificPopup, 'open');

            view.$('a.screenshots > img:nth-child(2)').click();

            expect($.magnificPopup.open.calledWithMatch({
                type: 'image',
                gallery: {
                    enabled: true
                },
                items: [{
                    src: attrs.screenshots[0].url
                }, {
                    src: attrs.screenshots[1].url
                }]
            }, 1));

            $.magnificPopup.open.restore();
        });

        it('includes the "thumb" as a screenshot if it is not a duplicate', function() {
            //thumb is duplicate
            model.set('thumb', attrs.screenshots[1].url);
            view.render();

            expect(view.$('a.screenshots > img').size()).to.be(2);

            view.remove();


            model.set('thumb', 'thumb.png');
            view.render();

            expect(view.$('a.screenshots > img').size()).to.be(3);
        });
    });
});
