package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.Holders
import marketplace.Profile
import marketplace.ScoreCardItem
import marketplace.controller.MarketplaceIntegrationTestCase
import spock.lang.Ignore

@Ignore
@Integration
@Rollback
class ScoreCardServiceTests extends MarketplaceIntegrationTestCase{


	def scoreCardService
	def serviceItemActivityService
	def searchableService
    def serviceItemService

	def initialScoreCardItemCount

    def owner

    def config = Holders.config

	void setup() {
//		super.setUp();
		initialScoreCardItemCount = ScoreCardItem.count()

        owner = new Profile(username: 'testOwner')
	}

	protected void tearDown() {
		unindexFlushAndRefresh()
		super.tearDown()
	}

	private void unindexFlushAndRefresh(){
		searchableService.unindexAll()
		searchableService.flushAndRefreshIndex()
		ScoreCardItem.list().each{ item ->
			item.delete()
		}
	}



	//Test scoreCardService.save
	void testSave(){
		when:
		ScoreCardItem item = null;

		item = ScoreCardItem.build(question : "This is a standard question", description: "This is a standard question")
		item = ScoreCardItem.get(item.id)

		then:
		//Verify the "question" field is what it started out to be
		item.question == "This is a standard question"

		when:
		item.question = "foo"
		scoreCardService.saveScoreCardItem(item)
		item = ScoreCardItem.get(item.id)

		then:
		//Verify the question field has changed
		item.question == "foo"
	}



	//Test scoreCardService.delete
	void testDelete(){
		when:
		//Get a handle on the number of existing score card items
		def initialScoreCardItemCount = ScoreCardItem.count()

		//Create two new items, one which will be deleted
		ScoreCardItem itemToDelete = ScoreCardItem.build(question : "To be deleted!", description: "To be deleted!")

		//Delete the item
		scoreCardService.deleteScoreCardItem(itemToDelete)

		then:
		//Verify the item does not exist!
		ScoreCardItem.get(itemToDelete.id) == null

		//Verify that the score card item count is the original count + 1 (for the one that was created but not deleted)
		ScoreCardItem.count() == initialScoreCardItemCount   //one is remaining, thats the current one
	}
}
