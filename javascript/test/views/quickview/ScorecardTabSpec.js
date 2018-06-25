define([
    'quickview/Scorecard',
    'models/ServiceItem',
    'models/ScorecardQuestion',
    'jquery', 'underscore', 'sinon', 'bootstrap'
], function(Scorecard, ServiceItem, ScoreCardQuestion, $, _, sinon) {

    describe('Quickview: Scorecard tab', function() {
        var view, model, server;

        beforeEach(function () {
            server = sinon.fakeServer.create();
            
            question = new ScoreCardQuestion({
                question: "question 1",
                description: "description",
                id: 1
            });

            model = new ServiceItem({
                satisfiedScoreCardItems: [question]
            });

            view = new Scorecard({
                model: model
            });
        });

        afterEach(function () {
            view.remove();
            server.restore();
        });

        function respondAndRender () {
            view = view.render();

            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify({
                    "ScoreCardList": [
                        {id: 1, question: 'question 1', description: 'description', showOnListing: true},
                        {id: 2, question: 'question 2', description: 'description', showOnListing: true}
                    ]
                })
            );
        }

        it('renders correctly.', function() {
            respondAndRender();
            expect(view.$el.find('input[type="checkbox"]').length).to.be(2);
            expect(view.$el.find('input[type="checkbox"]:checked').length).to.be(1);

            expect(view).to.be.ok();
        });


        it('saves scorecard question on switch toggle.', function () {
            respondAndRender();

            var $checked = view.$el.find('input[type="checkbox"]:checked');

            $checked.svitch('off');
            view._save({
                currentTarget: $checked[0]
            });
            expect(_.find(view.model.get('satisfiedScoreCardItems'), {id: $checked.attr('id')})).to.be(undefined);
        });
    });

});
