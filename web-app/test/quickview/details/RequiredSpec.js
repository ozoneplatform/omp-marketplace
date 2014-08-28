/*global describe, beforeEach, afterEach, it, expect */
define([
    'quickview/details/Required',
    'views/CategorizedServiceItemList',
    'backbone',
    'sinon',
    'underscore',
    'jquery'
], function(Required, CategorizedServiceItemList, Backbone, sinon, _, $) {
    'use strict';


    describe('Quickview Required View', function() {
        var view,
            model,
            requiredData = [{
                id: 1,
                title: 'required listing 1',
                imageSmallUrl: 'https://localhost/1',
                types: {
                    id: 1,
                    title: 'App Component'
                }
            }, {
                id: 2,
                title: 'required listing 2',
                imageSmallUrl: 'https://localhost/2',
                types: {
                    id: 1,
                    title: 'App Component'
                }
            }],
            requiredByData = [{
                id: 1,
                title: 'requiring listing 1',
                imageSmallUrl: 'https://localhost/3',
                types: {
                    id: 1,
                    title: 'App Component'
                }
            }, {
                id: 2,
                title: 'requiring listing 2',
                imageSmallUrl: 'https://localhost/4',
                types: {
                    id: 2,
                    title: 'Web App'
                }
            }],
            deferred,
            methodsToStub = ['render', 'remove'],
            classesToStub = [CategorizedServiceItemList],
            context = 'https://localhost/test-context';

        function stubbedCollection(data) {
            var Model = Backbone.Model.extend({
                getDefaultIconUrl: $.noop
            });

            var collection = new Backbone.Collection(data, {
                model: Model
            });

            sinon.stub(collection, 'fetch', function() {
                return deferred.promise();
            });

            return collection;
        }

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            model = new Backbone.Model();
            model.includedItems = sinon.spy(_.bind(stubbedCollection, null, requiredData));
            model.requiredByItems = sinon.spy(_.bind(stubbedCollection, null, requiredByData));
            model.set('isAffiliated', true);

            deferred = new $.Deferred();

            view = new Required({ serviceItemModel: model, context: context });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(View) {
                    View.prototype[method].restore();
                });
            });
        });

        it('creates sections for the required and requiring listings', function() {
            deferred.resolve();

            expect(view.$('.required-container').length).to.be(2);
            expect($(view.$('.required-container h5')[0]).text()).to.be('Requires');
            expect($(view.$('.required-container h5')[1]).text()).to.be('Required By');
        });

        it('passes the correct listings to the correct CategorizedServiceItemList', function() {
            deferred.resolve();

            var firstListView = view.$('.categorized-service-item-list').first().data('view'),
                secondListView = $(view.$('.categorized-service-item-list')[1]).data('view');

            expect(firstListView).to.be.a(CategorizedServiceItemList);
            expect(secondListView).to.be.a(CategorizedServiceItemList);

            expect(firstListView.collection).to.be(view.requiredItemsCollection);
            expect(secondListView.collection).to.be(view.requiredByItemsCollection);

            expect(firstListView.isAffiliated).to.be(model.get('isAffiliated'));
            expect(secondListView.isAffiliated).to.be(model.get('isAffiliated'));

            expect(firstListView.affiliatedUrl).to.be(context);
            expect(secondListView.affiliatedUrl).to.be(context);
        });
    });
});
