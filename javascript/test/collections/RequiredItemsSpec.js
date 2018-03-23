/*global describe, beforeEach, afterEach, expect, it */
define([
    'collections/RequiredItems',
    'collections/BaseCollection',
    'backbone',
    'sinon'
], function(RequiredItems, BaseCollection, Backbone, sinon) {
    'use strict';

    describe('RequiredItems', function() {
        var collection,
            id = 10,
            context = 'test-context';

        beforeEach(function() {
            collection = new RequiredItems([], {
                id: id,
                context: context
            });
        });

        it('is a subclass of BaseCollection', function() {
            expect(collection).to.be.a(BaseCollection);
        });

        it('uses the requiredServiceItems URL', function() {
            expect(collection.url()).to
                .be('test-context/api/serviceItem/10/requiredServiceItems/');
        });

        it('uses the getRequiredItems fallback URL', function() {
            expect(collection.fallbackUrl()).to
                .be('test-context/public/serviceItem/getRequiredItems/10');
        });

        it('removes the current listing from itself if necessary', function() {
            collection.set([{
                id: 2
            }, {
                id: id
            }], {parse: true});

            expect(collection.size()).to.be(1);
            expect(collection.at(0).get('id')).to.be(2);
        });

        describe('sync behavior', function() {
            var args = ['GET', new Backbone.Model(), null],
                superStub,
                deferreds;

            beforeEach(function() {
                deferreds = [];

                superStub = sinon.stub(BaseCollection.prototype, 'sync', function() {
                    var deferred = new $.Deferred();
                    deferreds.push(deferred);
                    return deferred;
                });
            });

            afterEach(function() {
                superStub.restore();
            });

            it("uses the superclass sync if it isn't affiliated", function() {
                collection.sync.apply(collection, args);

                expect(superStub.calledOnce).to.be.ok();
                expect(superStub.calledWithExactly.apply(superStub, args)).to.be.ok();
            });

            it('uses both the default sync and a fallback sync if it is affiliated', function() {
                collection.isAffiliated = true;

                collection.sync.apply(collection, args);

                expect(superStub.calledTwice).to.be.ok();
                expect(superStub.calledWithExactly.apply(superStub, args)).to.be.ok();
                expect(superStub.calledWithExactly(args[0], args[1],
                        {url: collection.fallbackUrl()})).to.be.ok();
            });

            it('resolves the deferred when either sync completes', function(done) {
                var promise1, promise2;

                collection.isAffiliated = true;

                promise1 = collection.sync.apply(collection, args);

                expect(deferreds.length).to.be(2);
                deferreds[0].resolve('resolved 1');

                //----------------------------

                deferreds = [];

                promise2 = collection.sync.apply(collection, args);

                expect(deferreds.length).to.be(2);
                deferreds[1].resolve('resolved 2');

                $.when(promise1, promise2).then(function() {
                    expect(arguments).to.contain('resolved 1');
                    expect(arguments).to.contain('resolved 2');

                    done();
                });
            });

            it('rejects the deferred if both syncs fail', function(done) {
                var promise;

                collection.isAffiliated = true;

                promise = collection.sync.apply(collection, args);

                expect(deferreds.length).to.be(2);
                deferreds[0].reject('rejected 1');

                expect(promise.state()).to.be('pending');

                promise.then(function() {
                    throw new Error("promise was unexpectedly resolved");
                }, function() {
                    done();
                });

                deferreds[1].reject('rejected 2');
                expect(promise.state()).to.be('rejected');
            });
        });
    });
});
