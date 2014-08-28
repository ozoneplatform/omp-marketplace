/*global describe, beforeEach, afterEach, it, expect */
define([
    'profile/ProfileTab',
    'backbone',
    'sinon',
    'underscore'
], function(ProfileTab, Backbone, sinon, _) {
    'use strict';

    describe('ProfileTab', function() {
        var view,
            myId = 10,
            attrs = {
                displayName: 'Test User',
                username: 'testUser',
                email: 'testUser@nowhere.com',
                bio: '',
                createdDate: '2014-02-25T17:05:13Z',
                id: myId,
                animationsEnabled: false
            },
            model,
            Marketplace;

        beforeEach(function() {
            Marketplace = {
                mainRouter: { closeModal: undefined },
                user: { id: myId },
                context: window.Marketplace.context
            };

            model = new Backbone.Model(attrs);
            model.isSelf = sinon.spy(function() {
                return Marketplace.user.id === myId;
            });
            model.isAdmin = sinon.spy(function() {
                return Marketplace.user.isAdmin;
            });

            view = new ProfileTab({ model: model });
        });

        afterEach(function() {
            view.remove();
        });

        it('has a title of "Profile"', function() {
            expect(view.title).to.be('Profile');

            view.render();
            expect(view.$('.info h4').text()).to.be('Profile');
        });

        it('has information for yourself', function() {
            Marketplace.user.id = myId;

            view.render();

            expect(view.$('.info')[0]).to.be.ok();
        });

        it('displays the correct information details', function() {
            //return the dd element corresponding to the specified dt contents
            function getDd(key) {
                return view.$(".info dt:contains('" + key + "') + dd");
            }

            view.render();

            _.each({
                Name: attrs.displayName,
                Username: attrs.username,
                Email: attrs.email,
                "Member Since": '02/25/2014'
            }, function(value, key) {
                expect(getDd(key).text()).to.be(value);
            });

            expect(getDd('Email').children('a').attr('href')).to.be('mailto:' + attrs.email);
        });

        it('has the correct placeholder text for your own bio', function() {
            view.render();

            expect(view.$('.bio').text()).to.be('Click to enter a little bit about yourself');
        });

        it("has the correct placeholder text for others' bios", function() {
            Marketplace.user.id = myId + 1;
            view = new ProfileTab({ model: model });

            view.render();

            expect(view.$('.bio').text()).to.be('No bio');
        });

        it('attaches the editable plugin to the bio if you are an admin', function() {
            Marketplace.user.isAdmin = true;
            Marketplace.user.id = myId + 1;
            view.render();

            expect(view.$('.bio > .content').data('editable')).to.be.ok();
        });

        it('attaches the editable plugin to the bio if you are on your own profile',
                function() {
            Marketplace.user.id = myId;
            Marketplace.user.isAdmin = false;
            view.render();

            expect(view.$('.bio > .content').data('editable')).to.be.ok();
        });

        it('does not attach the editable plugin if you are not an admin or on your own profile',
                function() {
            Marketplace.user.id = myId + 1;
            view = new ProfileTab({ model: model });
            Marketplace.user.isAdmin = false;
            view.render();

            expect(view.$('.bio > .content').data('editable')).to.not.be.ok();
        });

        it('saves the profile when the bio is edited', function() {
            var stub = sinon.stub(view.model, 'save', function() {
                    return $.Deferred().promise();
                }),
                el,
                bioText = 'test bio';

            Marketplace.user.id = myId;
            view.render().$el.appendTo(document.body);
            el = view.$('.bio > .content');

            //simulate opening, changing, and submitting the bio
            el.editable('show');
            view.$('.editable-input > textarea').val(bioText);
            view.$('.bio .editable-submit').click();

            expect(stub.calledOnce).to.be.ok();
            expect(stub.calledWith({ bio: bioText })).to.be.ok();

            stub.restore();
        });

        it('validates the bio when it is no more than 1000 characters', function() {
            var stub = sinon.stub(view.model, 'save', function() {
                    return $.Deferred().promise();
                }),
                el,
                bioText =   'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ';

            Marketplace.user.id = myId;
            view.render();
            el = view.$('.bio > .content');

            //simulate opening, changing, and submitting the bio
            el.editable('show');
            view.$('.editable-input > textarea').val(bioText);
            view.$('.bio .editable-submit').click();

            expect(stub.calledOnce).to.be.ok();
            expect(stub.calledWith({ bio: bioText })).to.be.ok();

            stub.restore();
        });

        it('does not validate the bio when it is more than 1000 characters', function() {
            var stub = sinon.stub(view.model, 'save', function() {
                    return $.Deferred().promise();
                }),
                el,
                bioText =   'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa ' +
                            'aaaaaaaaa aaaaaaaaa aaaaaaaaa aaaaaaaaa a';

            Marketplace.user.id = myId;
            view.render();
            el = view.$('.bio > .content');

            //simulate opening, changing, and submitting the bio
            el.editable('show');
            view.$('.editable-input > textarea').val(bioText);
            view.$('.bio .editable-submit').click();

            expect(stub.called).to.not.be.ok();
            expect(view.$('.editable-container .control-group').hasClass('error')).to.be.ok();

            stub.restore();
        });

        it('removes editable, and any popovers on remove', function() {
            view.render();
            view.bioErrorPopover();

            var editableSpy = sinon.spy(view.$('.bio > .content').data('editable'), 'destroy'),
                //svitchSpy = sinon.spy(view.$('input[type=checkbox]').data('svitch'), 'destroy'),
                bioPopoverSpy = sinon.spy(view.$('.bio').data('bs.popover'), 'destroy');

            view.remove();

            expect(editableSpy.calledOnce).to.be.ok();
            //svitch apparently has no destroy method
            //expect(svitchSpy.calledOnce).to.be.ok();
            expect(bioPopoverSpy.calledOnce).to.be.ok();
        });

        it('displays an error popover if the bio cannot be saved', function() {
            var stub = sinon.stub(view.model, 'save', function() {
                    var deferred = $.Deferred();
                    deferred.reject();

                    return deferred.promise();
                }),
                el,
                bioText = 'test bio';

            Marketplace.user.id = myId;
            view.render();
            el = view.$('.bio > .content');

            //simulate opening, changing, and submitting the bio
            el.editable('show');
            view.$('.editable-input > textarea').val(bioText);
            view.$('.bio .editable-submit').click();

            expect(view.$('.bio + .popover')[0]).to.be.ok();

            stub.restore();
        });


        it('closes the bio popover when the ok button on it is clicked', function(done) {
            view.render().$el.appendTo(document.body);

            view.bioErrorPopover();

            expect(view.$('.bio + .popover')).to.be.ok();
            view.$('.bio + .popover button').click();

            //popover removal is apparently asynchronous
            setTimeout(function() {
                expect(view.$('.bio + .popover')[0]).to.not.be.ok();

                view.remove();
                done();
            }, 500);
        });
    });
});
