/*global describe, beforeEach, it, expect */
define([
    'models/ProfileServiceItemWithTags',
    'models/Profile',
    'collections/ServiceItemTagsCollection'
], function(ProfileServiceItemWithTags, Profile, ServiceItemTagsCollection) {
    'use strict';

    describe('ProfileServiceItemWithTags', function() {
        var model,
            profileId = 1,
            attrs = {
                title: 'listing 1',
                id: 1,
                tags: [{
                    id: 2,
                    title: 'tag 1'
                }, {
                    id: 3,
                    title: 'tag 2'
                }]
            };

        beforeEach(function() {
            model = new ProfileServiceItemWithTags(attrs, {
                collection: { profile: new Profile({ id: profileId }) }
            });
        });

        it('exposes its tags as a SerivceItemTagsCollection', function() {
            var tagsCollection = model.tags();

            expect(tagsCollection).to.be.a(ServiceItemTagsCollection);

            expect(tagsCollection.at(0).attributes).to.eql({
                serviceItemId: 1,
                id: 2,
                tag: {
                    id: 2,
                    title: 'tag 1'
                },
                createdBy: { id: profileId }
            });
            expect(tagsCollection.at(1).attributes).to.eql({
                serviceItemId: 1,
                id: 3,
                tag: {
                    id: 3,
                    title: 'tag 2'
                },
                createdBy: { id: profileId }
            });
        });
    });
});
