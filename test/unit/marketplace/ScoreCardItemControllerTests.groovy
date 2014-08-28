package marketplace

import grails.test.mixin.web.ControllerUnitTestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import grails.test.*
import org.codehaus.groovy.grails.web.json.JSONObject
import grails.converters.*

@TestMixin([ControllerUnitTestMixin, DomainClassUnitTestMixin])
class ScoreCardItemControllerTests {

	def scoreCardItemController
    
    //Test the method addScoreCardQuestion
    void testSave() {
//        mockDomain(ScoreCardItem)
//        def scoreCardService = mockFor(ScoreCardService)
//        scoreCardItemController = new ScoreCardItemController()
//        
//        scoreCardItemController.scoreCardService = scoreCardService.createMock()
//        
//        response.JSON = {"question": "Question?", "description": "Description"};
//        
        
    }
    
    void testDelete() {
        
    }
    
    void testUpdate() {
        
    }


	//Test the method getScoreCardData
	void testGetScoreCardData() {
            mockDomain(ScoreCardItem)

		    //Mock the service
			def scoreCardService = mockFor(ScoreCardService)

			scoreCardItemController = new ScoreCardItemController()

			//Set the mocked service in the controller
			scoreCardItemController.scoreCardService = scoreCardService.createMock()

			//This is the function that will return the mock list when the service is invoked
			scoreCardService.demand.getScoreCardItemByType(new Boolean(true)) {
				return [new ScoreCardItem(id: 10, question: "This is a question", description: "This is a description")]
			}

			//Make the controller call
			def result = scoreCardItemController.getScoreCardData()

			//Get the response
			def jsonResponse = scoreCardItemController.response.contentAsString

			//Convert the response to JSONObject
			def jsonObject = new JSONObject(jsonResponse)
			def sc = jsonObject.ScoreCardList

			assertNotNull sc
	}

	//Test the default action which does not render anything
	void testShow(){
		scoreCardItemController = new ScoreCardItemController()
		scoreCardItemController.show()
		assertEquals "", scoreCardItemController.response.contentAsString
	}
}
