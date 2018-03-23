package marketplace

import grails.config.Config

import grails.gorm.transactions.Transactional
import grails.util.Holders

import marketplace.configuration.MarketplaceApplicationConfigurationService


class ScoreCardService {

    MarketplaceApplicationConfigurationService marketplaceApplicationConfigurationService

    AccountService accountService

    ProfileService profileService

    Config config = Holders.config
    
    // Score Card Question Section
    
    @Transactional(readOnly=true)
    def getScoreCardItem(id){
        return ScoreCardItem.get(id)
    }
    
    @Transactional(readOnly=false)
    def saveScoreCardQuestion(ScoreCardItem scoreCardQuestion){
        try {
            log.info "${scoreCardQuestion.createdDate}"
            scoreCardQuestion.save(failOnError: true)
        } catch (Exception e) {
            log.error "Error saving scorecard question with id " + scoreCardQuestion.id + ": " + e
            throw new Exception(e)
        }
    }
    
    // Delete an existing ScoreCardQuestion 
    @Transactional(readOnly=false)
    def deleteScoreCardQuestion(ScoreCardItem scoreCardQuestion){
        try {
            scoreCardQuestion.delete(failOnError: true)
        } catch (Exception e){
            log.error "Error deleting scoreCardItem with id " + scoreCardQuestion.id + ": " + e
            throw e
        }
    }
}
