/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'views/CategorizedServiceItemList',
    'models/ServiceItem',
    'backbone',
    'underscore',
    'jquery'
], function(CategorizedServiceItemList, ServiceItem, Backbone, _, $) {
    'use strict';

    describe('CategorizedServiceItemList', function() {
        var view,
            collectionData = [
                new ServiceItem({
                    title: 'listing 1',
                    id: 1,
                    isEnabled: true,
                    imageSmallUrl: '/asdf/1',
                    types: {
                        title: 'App Component'
                    }
                }),
                new ServiceItem({
                    title: 'listing 2',
                    id: 2,
                    isEnabled: false,
                    imageSmallUrl: '/asdf/2',
                    types: {
                        title: 'App Component'
                    }
                }),
                new ServiceItem({
                    title: 'listing 3',
                    id: 3,
                    isEnabled: true,
                    imageSmallUrl: '/asdf/3',
                    types: {
                        title: 'Web App'
                    }
                })
            ],
            collection;

        beforeEach(function() {
            collection = new Backbone.Collection(collectionData);

            view = new CategorizedServiceItemList({ collection: collection });

            view.render();
        });

        afterEach(function() {
            view.remove();
        });

        it('creates a subsection for each type of ServiceItem', function() {
            var sections = view.$('.service-item-subsection'),
                titles = _.map(sections, function(section) {
                    return $(section).children('h6').text();
                });

            expect(titles.length).to.be(2);
            expect(titles).to.contain('App Component');
            expect(titles).to.contain('Web App');
        });

        it('returns itself from render', function() {
            expect(view.render()).to.be(view);
        });

        it('creates a line for each service item in the right collection', function() {
            var sections = view.$('.service-item-subsection'),
                //a map from type name to list of ServiceItem ids
                idMap = _.object(_.map(sections, function(section) {
                    var title = $(section).children('h6').text(),
                        link = $(section).find('a'),
                        hrefs = _.map(link, function(a) { return $(a).attr('href'); });

                    return [
                        title,
                        _.map(hrefs, function(href) {
                            return parseInt(href.match(/#quickview\/(.*)/)[1], 10);
                        })
                    ];
                }));

            expect(idMap['App Component']).to.contain(collectionData[0].id);
            expect(idMap['App Component']).to.contain(collectionData[1].id);
            expect(idMap['Web App']).to.contain(collectionData[2].id);
        });

        it('uses the ServiceItems title and imageSmallUrl in its line', function() {
            var links = view.$('ul a');

            expect(links.length).to.be(3);
            _.each(links, function(link) {
                var $link = $(link),
                    img = $link.children('img'),
                    span = $link.children('span'),
                    id = parseInt(img.data('id'), 10),
                    model = collection.get(id);

                expect(img.attr('src')).to.be(model.get('imageSmallUrl'));
                expect(span.text()).to.be(model.get('title'));
                expect(span.attr('title')).to.be(model.get('title'));

                expect($link.attr('href')).to.be('#quickview/' + model.id);
            });
        });

        it('produces affiliated quickview links if isAffiliated is true', function() {
            view.remove();
            view = new CategorizedServiceItemList({
                collection: collection,
                isAffiliated: true,
                affiliatedUrl: 'https://localhost/test-context'
            });
            view.render();

            var links = view.$('ul a');

            expect(links.length).to.be(3);
            _.each(links, function(link) {
                var $link = $(link),
                    img = $link.children('img'),
                    id = parseInt(img.data('id'), 10),
                    model = collection.get(id);

                expect($link.attr('href')).to.be(
                    '#quickview-affiliated/https%3A%2F%2Flocalhost%2Ftest-context/' + model.id);
            });
        });

        it('adds the disabled class to the lines for disabled ServiceItems', function() {
            var disabledEls = view.$('li.disabled');

            expect(disabledEls.length).to.be(1);
            expect(disabledEls.find('img').data('id')).to.be(2);
        });

        it('toggles the collapse of each section when the header is clicked', function() {
            var sectionEl = $(view.$('.service-item-subsection')[0]),
                titleEl = sectionEl.children('.subsection-title'),
                listEl = sectionEl.children('ul'),
                collapsePluginSpy = sinon.spy();

            view.$el.appendTo(document.body);
            listEl.data('collapse', { toggle: collapsePluginSpy });

            titleEl.click();

            expect(sectionEl.hasClass('collapsed')).to.be(true);
            expect(collapsePluginSpy.calledOnce).to.be.ok();

            titleEl.click();

            expect(sectionEl.hasClass('collapsed')).to.be(false);
            expect(collapsePluginSpy.calledTwice).to.be.ok();
        });
    });
});
