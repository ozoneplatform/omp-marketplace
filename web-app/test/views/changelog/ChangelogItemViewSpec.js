/*global describe, afterEach, it, expect*/
define([
    'views/changelog/ChangelogItemView',
    '../../fixtures/changelogs.js',
    'models/Changelog'
], function(ChangelogItemView, ChangelogFixtures, Changelog) {
    'use strict';

    describe('Quickview: changelog item view', function() {
        var view, model, stub;

        function initModelAndView (modelAttrs) {
            view && view.remove();

            model = new Changelog(modelAttrs);
            view = new ChangelogItemView({
                model: model
            });
            view.render();
        }

        afterEach(function () {
            view && view.remove();
            stub && stub.restore();
            stub = null;
        });

        it('shows correct action.', function() {
            initModelAndView(ChangelogFixtures.CREATED);
            expect(view.action()).to.be('Listing Created');

            initModelAndView(ChangelogFixtures.INSIDE);
            expect(view.action()).to.be('Listing set to Inside');

            initModelAndView(ChangelogFixtures.OUTSIDE);
            expect(view.action()).to.be('Listing set to Outside');

            initModelAndView(ChangelogFixtures.APPROVED);
            expect(view.action()).to.be('Listing Approved');

            initModelAndView(ChangelogFixtures.REQUIREMENT_ADDED);
            expect(view.action()).to.be('Listing is now required by <h5 class="requires">app comp 1 wayne</h5>');

            initModelAndView(ChangelogFixtures.REQUIREMENT_REMOVED);
            expect(view.action()).to.be('Listing no longer requires <h5 class="requires">2 Web app wayne</h5>');

            initModelAndView(ChangelogFixtures.SCORECARD_UPDATED);
            expect(view.action()).to.be('Listing Scorecard Updated');

            initModelAndView(ChangelogFixtures.REJECTED);
            expect(view.action()).to.be('Listing Rejected');

            initModelAndView(ChangelogFixtures.MODIFIED);
            expect(view.action()).to.be('Listing Modified');
        });

        it('is expandable for rejected and modified lisings.', function () {
            initModelAndView(ChangelogFixtures.REJECTED);
            expect(view.isExpandable()).to.be.ok();

            initModelAndView(ChangelogFixtures.MODIFIED);
            expect(view.isExpandable()).to.be.ok();

            initModelAndView(ChangelogFixtures.CREATED);
            expect(view.isExpandable()).to.not.be.ok();
        });

        it('sets correct attributes.', function () {
            initModelAndView(ChangelogFixtures.REJECTED);
            expect(view.attributes()).to.eql({
                'data-toggle': 'collapse',
                'data-target': '#' + view.id() + ' > .collapse'
            });

            initModelAndView(ChangelogFixtures.CREATED);
            expect(view.attributes()).to.not.be.ok();
        });

    });

});
