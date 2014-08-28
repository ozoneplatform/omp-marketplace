package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin


import ozone.marketplace.enums.ImageType;
import grails.test.*

@TestMixin(IntegrationTestMixin)
class ImagesServiceTests {

	def imagesService

	void testGetImageByCriteria() {
		//save and then retrieve an AVATAR image
		Images image = new Images(
            type: ImageType.AVATAR,
            isDefault: true,
            bytes: [0,1,2] as byte[]
        )
		image.save(flush:true, failOnError:true);
        def images = imagesService.getImageByCriteria(ImageType.AVATAR, true)
		assert null != images
println "images = $images"
		assert null != images[0].type
		assert ImageType.AVATAR == images[0].type
		assert image.id == images[0].id
	}

}
