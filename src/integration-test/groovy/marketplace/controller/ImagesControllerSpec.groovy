package marketplace.controller

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.Holders
import marketplace.Images
import marketplace.ImagesController
import marketplace.controller.ControllerTestMixin
import ozone.marketplace.enums.ImageType
import spock.lang.Specification

@Integration
@Rollback
class ImagesControllerSpec extends Specification implements ControllerTestMixin<ImagesController>{
	def imagesService
//	def config = Holders.config

	void setupData() {
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
		setup:
		setupData()
		when:
		Images image = new Images(
            contentType: "image/png",
            type: ImageType.SERVICEITEM,
            bytes: [0,0,0] as byte[]
        )
		image.save(flush:true, failOnError:true)
		controller.params.id = image.id.toString()
		controller.get()
		then:
		200 == controller.response.status
		"image/png" == controller.response.contentType

	}
}
