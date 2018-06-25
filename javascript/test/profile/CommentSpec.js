/*global describe, beforeEach, it, expect */
define([
    'profile/Comment',
    'backbone',
    'underscore'
], function(Comment, Backbone, _) {
    'use strict';

    describe('Profile Window Comment View', function() {
        var view,
            model,
            attrs = {
                serviceItem: {
                    title: 'test listing',
                    id: 1
                }
            };

        beforeEach(function() {
            model = new Backbone.Model(attrs);

            view = new Comment({ model: model });
        });

        it('uses the ServiceItem title as its title', function() {
            expect(_.result(view, 'title')).to.be(attrs.serviceItem.title);
        });

        it('wraps its title in a link to the quickview', function() {
            view.render();

            var linkEl = view.$('a'),
                titleEl = linkEl.children('.item-review-title');

            expect(linkEl).to.be.ok();
            expect(linkEl.attr('href')).to.be('#quickview/' + attrs.serviceItem.id + '/reviews');
            expect(titleEl).to.be.ok();
        });
    });
});
