define([
    'quickview/admin/SettingsView',
    'models/ServiceItem',
    '../../../fixtures/listings.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(SettingsView, ServiceItem, ListingFixtures, $, _, sinon, Marketplace) {

    describe('Quickview: Admin tab, Settings view', function() {
        var view, model;

        function initModelAndView (modelAttrs) {
            view && view.remove();

            model = new ServiceItem(modelAttrs);
            view = new SettingsView({
                model: model
            });
            view.render();
        }

        function validateButtonStates (inside, outside, disable, enable, reject, approve) {
            var $inside = view.$('.btn-inside'),
                $outside = view.$('.btn-outside'),
                $disable = view.$('.btn-disable'),
                $enable = view.$('.btn-enable'),
                $reject = view.$('.btn-reject'),
                $approve = view.$('.btn-approve'),
                isUserAdmin = Marketplace.user.isAdmin,
                isInsideOutsideSelectionManual = _.findWhere(Marketplace.appconfigs, {code: 'store.insideOutside.behavior'}).value === 'ADMIN_SELECTED';

            function isActive($btn, disabled) {
                return $btn.hasClass('active') && (disabled === true ? $btn.is(':disabled') : true);
            }

            inside ? expect(isActive($inside, !isUserAdmin)).to.be.ok() : expect(isActive($inside, !isUserAdmin || !isInsideOutsideSelectionManual)).to.not.be.ok();
            outside ? expect(isActive($outside, !isUserAdmin)).to.be.ok() : expect(isActive($outside, !isUserAdmin || !isInsideOutsideSelectionManual)).to.not.be.ok();

            disable ? expect(isActive($disable)).to.be.ok() : expect(isActive($disable)).to.not.be.ok();
            enable ? expect(isActive($enable)).to.be.ok() : expect(isActive($enable)).to.not.be.ok();

            reject ? expect(isActive($reject, !isUserAdmin)).to.be.ok() : expect(isActive($reject, true)).to.not.be.ok();
            approve ? expect(isActive($approve, !isUserAdmin)).to.be.ok() : expect(isActive($approve, true)).to.not.be.ok();
        }

        afterEach(function () {
            view.remove();
        });

        it('renders correctly for user.', function() {
            Marketplace.user.isAdmin = false;

            initModelAndView(ListingFixtures.APPROVED_INSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(true, false, false, true, false, true);

            initModelAndView(ListingFixtures.APPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(false, true, false, true, false, true);

            initModelAndView(ListingFixtures.APPROVED_INSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(true, false, true, false, false, true);

            initModelAndView(ListingFixtures.APPROVED_OUTSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(false, true, true, false, false, true);

            initModelAndView(ListingFixtures.UNAPPROVED_INSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(true, false, false, true, true, false);

            initModelAndView(ListingFixtures.UNAPPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(false, true, false, true, true, false);

            initModelAndView(ListingFixtures.UNAPPROVED_INSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(true, false, true, false, true, false);

            initModelAndView(ListingFixtures.UNAPPROVED_OUTSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(false, true, true, false, true, false);
        });

        it('renders correctly for admin.', function() {
            Marketplace.user.isAdmin = true;

            initModelAndView(ListingFixtures.APPROVED_INSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(true, false, false, true, false, true);

            initModelAndView(ListingFixtures.APPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(false, true, false, true, false, true);

            initModelAndView(ListingFixtures.APPROVED_INSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(true, false, true, false, false, true);

            initModelAndView(ListingFixtures.APPROVED_OUTSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(false, true, true, false, false, true);

            initModelAndView(ListingFixtures.UNAPPROVED_INSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(true, false, false, true, true, false);

            initModelAndView(ListingFixtures.UNAPPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            validateButtonStates(false, true, false, true, true, false);

            initModelAndView(ListingFixtures.UNAPPROVED_INSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(true, false, true, false, true, false);

            initModelAndView(ListingFixtures.UNAPPROVED_OUTSIDE_DISABLED_APP_COMPONENT);
            validateButtonStates(false, true, true, false, true, false);
        });

        it('updates on change.', function() {
            function testUpdate () {
                initModelAndView(ListingFixtures.APPROVED_INSIDE_ENABLED_APP_COMPONENT);
                validateButtonStates(true, false, false, true, false, true);

                model.set(ListingFixtures.APPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
                validateButtonStates(false, true, false, true, false, true);
            }
            Marketplace.user.isAdmin = true;
            testUpdate();

            Marketplace.user.isAdmin = false;
            testUpdate();
        });

        it('disables inside button if all listings are outside.', function() {
            _.findWhere(Marketplace.appconfigs, {code: 'store.insideOutside.behavior'}).value = "ALL_OUTSIDE";

            initModelAndView(ListingFixtures.APPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            expect(view.$('.btn-inside').is(':disabled')).to.be.ok();
        });

        it('disables outside button if all listings are inside.', function() {
            _.findWhere(Marketplace.appconfigs, {code: 'store.insideOutside.behavior'}).value = "ALL_INSIDE";

            initModelAndView(ListingFixtures.APPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            expect(view.$('.btn-outside').is(':disabled')).to.be.ok();
        });

    });

});