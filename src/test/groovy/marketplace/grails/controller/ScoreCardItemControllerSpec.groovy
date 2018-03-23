package marketplace

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.web.json.JSONObject
import spock.lang.Specification

class ScoreCardItemControllerSpec extends Specification implements ControllerUnitTest<ScoreCardItemController>, DataTest {

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
		when:
		def card = new ScoreCardItem(id: 10, question: "This is a question", description: "This is a description")
		mockDomain(ScoreCardItem, [card])

		//Make the controller call
		def result = controller.getScoreCardData()

		//Get the response
		def jsonResponse = controller.response.contentAsString
		//Convert the response to JSONObject
		def jsonObject = new JSONObject(jsonResponse)
		def sc = jsonObject.ScoreCardList

		then:
		sc != null
	}

	//Test the default action which does not render anything
	void testShow(){
		when:
//		scoreCardItemController = new ScoreCardItemController()
		controller.show()
		then:
		controller.response.contentAsString == ""
		"" == response.contentAsString
	}
}
