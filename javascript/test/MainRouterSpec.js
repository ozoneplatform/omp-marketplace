/*global describe, before, beforeEach, afterEach, it, expect, sinon */
define([
    'MainRouter',
    'profile/Window',
    'quickview/index',
    'affiliatedSearch/views/AffiliatedResultsGridView',
    'spa/views/admin/agency/AgencyAdminPageView',
    'backbone',
    'jquery',
    'underscore'
], function(MainRouter, ProfileWindow, Quickview, AffiliatedResultsGridView, AgencyAdminView,
        Backbone, $, _) {
    'use strict';

    describe('MainRouter', function() {
        var router,
            title = 'Modal Title',
            methodsToStub = ['initialize', 'render', 'route'],
            classesToStub =
                [ProfileWindow, Quickview, AffiliatedResultsGridView, AgencyAdminView];

        function makeRouteTest(ViewClass, route, expectedParameters) {
            return function() {
                router.navigate(route, {trigger: true});

                expect(ViewClass.prototype.initialize.calledOnce).to.be.ok();
                expect(ViewClass.prototype.initialize.calledWith(expectedParameters))
                    .to.be.ok();

                expect(ViewClass.prototype.render.calledOnce).to.be.ok();

                router.navigate('', {trigger: true});
            };
        }

        beforeEach(function() {
            _.each(classesToStub, function(View) {
                _.each(methodsToStub, function(method) {
                    if (View.prototype[method]) {
                        sinon.stub(View.prototype, method, function() {
                            return this;
                        });
                    }
                });

                if (View.prototype.getTitle) {
                    sinon.stub(View.prototype, 'getTitle', function() {
                        return title;
                    });
                }

                sinon.stub(View.prototype, 'remove', function() {
                    this.$el.remove();
                });
            });
        });

        afterEach(function() {
            _.each(classesToStub, function(View) {
                _.each(methodsToStub.concat(['remove', 'getTitle']), function(method) {
                    if (View.prototype[method]) {
                        View.prototype[method].restore();
                    }
                });
            });
        });

        before(function() {
            var mainContainer = $('<div class="mainContainer">')
                .append('<div class="original-content">asdf</div>');

            mainContainer.appendTo(document.body);

            router = new MainRouter({ mainContainer: mainContainer });

            Backbone.history.start();
        });

        it('creates an element to serve as a container for modals', function() {
            expect($('.modal-container.bootstrap-active')).to.be.ok();
        });

        it('opens the profile window in response to #profile routes',
            makeRouteTest(ProfileWindow, '#profile/1/blah', {
                tab: 'blah',
                profileId: '1'
            })
        );

        it('opens the quickview window in response to #quickview routes',
            makeRouteTest(Quickview, '#quickview/1/blah', {
                tab: 'blah',
                serviceItemId: '1'
            })
        );

        it('opens the affiliated quickview window in response to #quickview-affiliated routes',
            makeRouteTest(Quickview,
               '#quickview-affiliated/https%3A%2F%2Fwidgethome%3A8443%2Fmarketplace/1/blah',
               {
                    tab: 'blah',
                    serviceItemId: '1',
                    isAffiliated: true,
                    context: 'https://widgethome:8443/marketplace'
                })
        );

        it('opens the affiliated search results in response to #affiliated routes',
            makeRouteTest(AffiliatedResultsGridView,'#affiliated/1/%7B%22a%22%3A%201%7D', {
                serverId: '1',
                searchParamsJSON: '{"a": 1}'
            })
        );

        it('opens the agency admin page in response to #agency routes',
            makeRouteTest(AgencyAdminView,'#agency', {})
        );

        it('only allows one modal to be open at a time', function() {
            router.navigate('#quickview/1', {trigger: true});
            router.navigate('#profile/1', {trigger: true});

            expect($('.quickview')[0]).to.not.be.ok();

            router.navigate('', {trigger: true});
        });

        it('closes the modal when a non-modal is navigated to', function() {
            router.navigate('#quickview/1', {trigger: true});
            router.navigate('#agency', {trigger: true});

            expect($('.quickview')[0]).to.not.be.ok();

            router.navigate('', {trigger: true});
        });

        it('adds non-modals into the mainContainer', function() {
            router.navigate('#agency', {trigger: true});

            expect($('.mainContainer > .agency-admin-page')[0]).to.be.ok();

            router.navigate('', {trigger: true});
        });

        it('only allows one non-modal to be present at a time', function() {
            router.navigate('#affiliated/1/%7B%22a%22%3A%201%7D', {trigger: true});
            router.navigate('#agency', {trigger: true});

            expect($('.mainContainer > .agency-admin-page')[0]).to.be.ok();
            expect($('.mainContainer > .affiliated-results-grid-view')[0]).to.not.be.ok();

            router.navigate('', {trigger: true});
        });

        it('hides the original contents of the mainContainer when a non-modal is displayed',
                function() {
            router.navigate('#agency', {trigger: true});

            expect($('.original-content').is(':visible')).to.not.be.ok();

            router.navigate('', {trigger: true});

            expect($('.original-content').is(':visible')).to.be.ok();
        });

        it('does not hide the non-modal content when a modal is displayed', function() {
            router.navigate('#profile/1', {trigger: true});

            expect($('.original-content').is(':visible')).to.be.ok();

            router.navigate('#agency', {trigger: true});
            router.navigate('#profile/1', {trigger: true});

            expect($('.original-content').is(':visible')).to.not.be.ok();
            expect($('.agency-admin-page').is(':visible')).to.be.ok();

            router.navigate('', {trigger: true});
        });

        it('removes the modal when closeModal is called', function() {
            router.navigate('#profile/1', {trigger: true});

            router.closeModal();

            expect($('.modal-container').children().length).to.be(0);
        });

        it('restores the url of the current non-modal when closeModal is called', function() {
            router.navigate('#profile/1', {trigger: true});
            router.closeModal();

            expect(window.location.hash).to.not.be.ok();

            router.navigate('#agency', {trigger: true});
            router.navigate('#profile/1', {trigger: true});
            router.closeModal();

            expect(window.location.hash).to.be('#agency');

            router.navigate('#agency', {trigger: true});
            router.navigate('#profile/1', {trigger: true});
            router.navigate('#quickview/1', {trigger: true});
            router.closeModal();

            expect(window.location.hash).to.be('#agency');

            router.navigate('', {trigger: true});
            router.navigate('#profile/1', {trigger: true});
            router.navigate('#quickview/1', {trigger: true});
            router.closeModal();

            expect(window.location.hash).to.not.be.ok();
        });

        it('calls the route function of the existing modal if the main route is the same',
                function() {
            router.navigate('#profile/1/profile', {trigger: true});
            router.navigate('#profile/1/myListings', {trigger: true});

            expect(ProfileWindow.prototype.initialize.calledOnce).to.be.ok();
            expect(ProfileWindow.prototype.route.calledOnce).to.be.ok();
            expect(ProfileWindow.prototype.route.calledWith({
                profileId: '1',
                tab: 'myListings'
            })).to.be.ok();
        });

        //TODO add test like the above but for non-modals.  Right now we have no non-modals
        //that have subRoutes so that can't be tested

        it('goes back to the previous modal when toPreviousModal is called', function() {
            router.navigate('#quickview/1', {trigger: true});
            router.navigate('#profile/1', {trigger: true});

            router.toPreviousModal();

            expect($('.profile-window')[0]).to.not.be.ok();
            expect($('.quickview')[0]).to.be.ok();

            router.navigate('#profile/1', {trigger: true});
            router.navigate('#quickview/1', {trigger: true});

            router.toPreviousModal();

            expect($('.profile-window')[0]).to.be.ok();

            router.toPreviousModal();

            expect($('.quickview')[0]).to.be.ok();
        });

        it('clears the previous modal stack when a non-modal is directed to', function() {
            router.navigate('#quickview/1', {trigger: true});
            router.navigate('#agency', {trigger: true});
            router.navigate('#profile/1', {trigger: true});

            router.toPreviousModal();

            expect($('.quickview')[0]).to.not.be.ok();

            router.navigate('#agency', {trigger: true});
            router.navigate('#profile/1', {trigger: true});
            router.showDefault();
            router.navigate('#quickview/1', {trigger: true});

            router.toPreviousModal();

            expect($('.profile-window')[0]).to.not.be.ok();
        });

        it('sets the modal backTitle property from the getTitle function of the previous modal',
                function() {
            router.navigate('#quickview/1', {trigger: true});
            title = 'Test Listing 1';
            router.navigate('#profile/1', {trigger: true});

            expect(Quickview.prototype.getTitle.calledOnce).to.be.ok();
            expect(Quickview.prototype.getTitle.returned('Test Listing 1')).to.be.ok();
            expect(ProfileWindow.prototype.initialize.calledWithMatch({
                backTitle: 'Test Listing 1'
            })).to.be.ok();
        });

        it('sets the modal backTitle property correctly when going back',
                function() {
            router.navigate('#quickview/1', {trigger: true});
            title = 'Test Listing 1';
            router.navigate('#profile/1', {trigger: true});
            title = 'Test Profile 1';
            router.navigate('#quickview/2', {trigger: true});

            router.toPreviousModal();

            expect(ProfileWindow.prototype.initialize.calledTwice).to.be.ok();
            expect(ProfileWindow.prototype.initialize.alwaysCalledWithMatch({
                backTitle: 'Test Listing 1'
            })).to.be.ok();
        });
    });
});
