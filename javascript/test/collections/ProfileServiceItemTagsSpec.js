/*global describe, beforeEach, it, expect */
define([
    'collections/ProfileServiceItemTags',
    'models/ProfileServiceItemWithTags',
    'models/Profile',
    'marketplace'
], function(ProfileServiceItemTags, ProfileServiceItemWithTags, Profile, Marketplace) {
    'use strict';

    describe('ProfileServiceItemTags', function() {
        var collection,
            profileId = 123,
            profile = new Profile({ id: profileId });

        beforeEach(function() {
            collection = new ProfileServiceItemTags([], { profile: profile });
        });

        it('uses ProfileServiceItemWithTags as its model', function() {
            expect(collection.model).to.be(ProfileServiceItemWithTags);
        });

        it('sets its profile property from the init options', function () {
            expect(collection.profile).to.be(profile);
        });

        it('constructs its URL from the Marketplace context and its profile id', function() {
            var oldContext = Marketplace.context;
            Marketplace.context = 'test-context';

            expect(collection.url()).to.be('test-context/api/profile/123/tag');

            Marketplace.context = oldContext;
        });

        //see the comment on the parse method for the definition of "correctly"
        it('parses data correctly', function() {
            var inputData = [{
                    id: 1,
                    serviceItem: {
                        id: 1,
                        title: 'listing 1'
                    },
                    tag: {
                        id: 1,
                        title: 'tag 1'
                    }
                }, {
                    id: 2,
                    serviceItem: {
                        id: 1,
                        title: 'listing 1'
                    },
                    tag: {
                        id: 2,
                        title: 'tag 2'
                    }
                }, {
                    id: 3,
                    serviceItem: {
                        id: 2,
                        title: 'listing 2'
                    },
                    tag: {
                        id: 3,
                        title: 'tag 3'
                    }
                }],
                expectedOutput = [{
                    title: 'listing 1',
                    id: 1,
                    tags: [{
                        id: 1,
                        title: 'tag 1'
                    }, {
                        id: 2,
                        title: 'tag 2'
                    }]
                }, {
                    title: 'listing 2',
                    id: 2,
                    tags: [{
                        id: 3,
                        title: 'tag 3'
                    }]
                }],
                actualOutput = collection.parse(inputData);

            expect(actualOutput).to.eql(expectedOutput);
        });
    });
});
