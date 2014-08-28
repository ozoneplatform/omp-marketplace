define([
    'quickview/details/Intents', 
    'models/ServiceItem',
    'jquery', 'underscore', 'sinon', 'bootstrap'
], function(Intents, ServiceItem, $, _, sinon) {

    describe('Quickview: Details tab intents', function() {
        var view, model, server;

        beforeEach(function () {
            server = sinon.fakeServer.create();
            
            model = new ServiceItem({
                intents: [{
                	action: {
                		title: 'Action'
                	},
                	dataType:{
                		title: 'Data Type'               		
                	},
                    send: true,
                    receive: true
                },
                {
                	action: {
                		title: 'Action 2'
                	},
                	dataType:{
                		title: 'Data Type 2'               		
                	},
                    send: false,
                    receive: false
                }]
            });

            view = new Intents({
            	model: model
            });
        });

        afterEach(function () {
            view.remove();
        });


        it('renders correctly.', function() {

            expect(view.$el.find('.intents').length).to.be(1);
            expect(view.$el.find('.send').length).to.be(1);
            expect(view.$el.find('.receive').length).to.be(1);

            //There should be two rows
            expect(view.$el.find('thead').next().find("tr").length).to.be(2);
            
        });
        
        
        it('getIntents returns the correct data.', function(){
        	var intentsData = view.getIntents();
        	
        	var firstRow = intentsData[0];
            expect(firstRow.action).to.be('Action');
            expect(firstRow.dataType).to.be('Data Type');
            expect(firstRow.send).to.be(true);
            expect(firstRow.receive).to.be(true);
            
        	var secondRow = intentsData[1];
            expect(secondRow.action).to.be('Action 2');
            expect(secondRow.dataType).to.be('Data Type 2');
            expect(secondRow.send).to.be(false);
            expect(secondRow.receive).to.be(false);
        });

    });

});