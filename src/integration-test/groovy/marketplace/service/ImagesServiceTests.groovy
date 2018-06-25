package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.Images
import ozone.marketplace.enums.ImageType
import spock.lang.Specification

@Integration
@Rollback
class ImagesServiceTests extends Specification {

	def imagesService

	void testGetImageByCriteria() {
		when:
		//save and then retrieve an AVATAR image
		Images image = new Images(
            type: ImageType.AVATAR,
            isDefault: true,
            bytes: [0,1,2] as byte[]
        )
		image.save(flush:true, failOnError:true);
        def images = imagesService.getImageByCriteria(ImageType.AVATAR, true)
		then:
		null != images
		null != images[0].type
		ImageType.AVATAR == images[0].type
		image.id == images[0].id
	}

}
