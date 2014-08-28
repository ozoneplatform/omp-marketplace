/*global describe, beforeEach, it, expect */
define([
    'collections/ProfileChangelogCollection',
    'collections/ChangelogCollection'
], function(ProfileChangelogCollection, ChangelogCollection) {
    'use strict';

    describe('ProfileChangelogCollection', function() {
        var collection,
            profileId = 1234,
            context = '/test-context';

        beforeEach(function() {
            collection = new ProfileChangelogCollection([], {
                id: profileId,
                context: context
            });
        });

        it('is a ChangelogCollection', function() {
            expect(collection).to.be.a(ChangelogCollection);
        });

        it('uses a URL for the profile activities', function() {
            expect(collection.url())
                .to.be('/test-context/api/profile/1234/serviceItem/activity/');
        });
    });
});
