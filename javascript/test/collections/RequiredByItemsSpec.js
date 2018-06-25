/*global describe, beforeEach, expect, it */
define([
    'collections/RequiredByItems',
    'collections/RequiredItems'
], function(RequiredByItems, RequiredItems) {
    'use strict';

    describe('RequiredByItems', function() {
        var collection,
            id = 10,
            context = 'test-context';

        beforeEach(function() {
            collection = new RequiredByItems([], {
                id: id,
                context: context
            });
        });

        it('is a subclass of RequiredItems', function() {
            expect(collection).to.be.a(RequiredItems);
        });

        it('uses the requiringServiceItems URL', function() {
            expect(collection.url()).to
                .be('test-context/api/serviceItem/10/requiringServiceItems/');
        });

        it('uses the getRequiredByItems fallback URL', function() {
            expect(collection.fallbackUrl()).to
                .be('test-context/relationship/getRequiredByItems/10');
        });
    });
});
