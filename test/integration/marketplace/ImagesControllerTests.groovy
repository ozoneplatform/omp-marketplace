package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import ozone.marketplace.controller.*;
import grails.util.Holders

import ozone.marketplace.enums.ImageType

@TestMixin(IntegrationTestMixin)
class ImagesControllerTests extends MarketplaceIntegrationTestCase {
	def imagesService
    def sessionFactory
	def config = Holders.config

	void setUp() {
		super.setUp()
		controller = new ImagesController()
		controller.imagesService = imagesService

	}

/*	void resetController() {
		controller.response.committed = false
		controller.response.reset()
	}
*/
	/*  TODO: DFG, May not need if method should go away b/c it is not used.  Determining this otherwise
	 * how to test success needs rework since it does not return a message on success though code assumes it does.
	void testDelete() {
		Images image = new Images(
            type: ImageType.SERVICEITEM,
            bytes: [0,0,0] as byte[]
        )
		image.save(flush:true, failOnError:true)
		controller.params.id = image.id.toString()
		controller.delete()
		assert "delete.success" == controller.flash.message
	}
	*/


	void testGet() {
		Images image = new Images(
            contentType: "image/png",
            type: ImageType.SERVICEITEM,
            bytes: [0,0,0] as byte[]
        )
		image.save(flush:true, failOnError:true)
		controller.params.id = image.id.toString()
		controller.get()
		assert 200 == controller.response.status
		assert "image/png" == controller.response.contentType

	}
}
