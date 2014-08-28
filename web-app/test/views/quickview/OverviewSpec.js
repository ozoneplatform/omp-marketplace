define([
    'quickview/Overview',
    'models/ServiceItem',
    'jquery', 'underscore', 'sinon'
], function(Overview, ServiceItem, $, _, sinon) {

    describe('Quickview: Overview tab', function() {
        var view, model;

        beforeEach(function () {
            model = new ServiceItem({
                screenshots: [{
                    smallImageUrl: 'http://small1',
                    largeImageUrl: 'http://large1'
                }, {
                    smallImageUrl: 'http://small2',
                    largeImageUrl: 'http://large2'
                }]
            })
            view = new Overview({
                serviceItemModel: model
            });
        });

        afterEach(function () {
            view.remove();
        });

        it('renders screenshots.', function() {
            view = view.render();

            expect(view.$el.find('img').length).to.be(2);

            view.onActivateOnce();

            expect(view.$el.find('.icon-chevron-left').length).to.be(1);
            expect(view.$el.find('.icon-chevron-right').length).to.be(1);
        });

        it('shows large screenshots on click.', function () {
            view = view.render();

            var stub = sinon.stub($.magnificPopup, 'open');

            view.showScreenshots($.Event());

            var largeScreenshots = _.map(model.get('screenshots'), function (ss) {
                return {src: ss.largeImageUrl};
            });

            expect(stub.calledOnce).to.be(true);

            expect(stub.calledWith({
                type: 'image',
                gallery: {
                    enabled: true
                },
                items: largeScreenshots
            }, 0)).to.be(true);

            stub.restore();
        });

    });

});