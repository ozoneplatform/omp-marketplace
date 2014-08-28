package marketplace

import grails.converters.JSON
import ozone.marketplace.enums.MarketplaceApplicationSetting
import org.codehaus.groovy.grails.web.json.JSONArray



class ScoreCardItemController extends BaseMarketplaceRestController {

    def scoreCardService

    static defaultAction = "show"

    // Displays the gsp page with score card adminstrator functions
    def show = {
        
	}
    
    // Save a score card question
    def save = {
        log.info "Save with ${request?.JSON}"
        
        
        def scoreCardItem = new ScoreCardItem()
		scoreCardItem.properties = request?.JSON
		
		try{
			scoreCardService.saveScoreCardQuestion(scoreCardItem)
			
		} catch (Exception e){
            log.error "Error creating new scorecard question: " + e
			return
		}
        
		render ([scoreCardItem as JSON])
    }
    
    // Update a score card question
    def update = {
        log.info "Update with ${request?.JSON} and id ${params.id}"
        
        def scoreCardItem = ScoreCardItem.get(params.id)
        log.info "${scoreCardItem.question}"
        def newProperties = request?.JSON

        scoreCardItem.with {
            question = newProperties.question
            description = newProperties.description
            image = newProperties.image
            showOnListing = newProperties.showOnListing
        }
        
        try {
            scoreCardService.saveScoreCardQuestion(scoreCardItem);
        } catch (Exception e) {
            log.error "Error updating existing scorecard question: " + e
            return
        }
        
        render ([scoreCardItem as JSON])
        
    }
    
    // Delete a score card question
    def delete = {
        log.info "Delete id: ${params.id}"
		def scoreCardItem = ScoreCardItem.get(params.id as long)
		
        try {
            scoreCardService.deleteScoreCardQuestion(scoreCardItem)
        } catch (Exception e) {
            log.error "Error deleting existing scorecard question"
            return
        }
        
        render ([success: true] as JSON)
    }
	
	//Gets a list of score card questions
	def getScoreCardData = {
		
		def map = [:]
		map.put("ScoreCardList", ScoreCardItem.list())
		
		render map as JSON;
	}

    // Check application configuration for score card enabled
	def beforeInterceptor = {
		if(!marketplaceApplicationConfigurationService.is(MarketplaceApplicationSetting.SCORE_CARD_ENABLED)){
			throw new RuntimeException("Score Card Functionality is not enabled")
		}
	}
}
