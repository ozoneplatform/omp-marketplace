package marketplace

import static grails.util.Holders
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.*
import org.junit.Ignore

@Ignore
class ScoreCardServiceTests extends MarketplaceIntegrationTestCase{


	def scoreCardService
	def serviceItemActivityService
	def searchableService
    def serviceItemService

	def initialScoreCardItemCount

    def owner

    def config = Holders.config

	void setUp() {
		super.setUp();
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

		ScoreCardItem item = null;

		item = ScoreCardItem.build(question : "This is a standard question", description: "This is a standard question")
		item = ScoreCardItem.get(item.id)

		//Verify the "question" field is what it started out to be
		assertThat(item.question, is(equalTo("This is a standard question")))

		item.question = "foo"
		scoreCardService.saveScoreCardItem(item)
		item = ScoreCardItem.get(item.id)


		//Verify the question field has changed
		assertThat(item.question, is(equalTo("foo")))

	}



	//Test scoreCardService.delete
	void testDelete(){

		//Get a handle on the number of existing score card items
		def initialScoreCardItemCount = ScoreCardItem.count()

		//Create two new items, one which will be deleted
		ScoreCardItem itemToDelete = ScoreCardItem.build(question : "To be deleted!", description: "To be deleted!")

		//Delete the item
		scoreCardService.deleteScoreCardItem(itemToDelete)

		//Verify the item does not exist!
		assertThat(ScoreCardItem.get(itemToDelete.id), is(nullValue()))

		//Verify that the score card item count is the original count + 1 (for the one that was created but not deleted)

		assertThat(ScoreCardItem.count(), is(equalTo(initialScoreCardItemCount)))   //one is remaining, thats the current one

	}

}
