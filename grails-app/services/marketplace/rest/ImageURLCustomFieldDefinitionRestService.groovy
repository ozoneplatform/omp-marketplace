package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import grails.core.GrailsApplication

import marketplace.ImageURLCustomFieldDefinition

@Service
class ImageURLCustomFieldDefinitionRestService extends
        AdminRestService<ImageURLCustomFieldDefinition> {

    @Autowired
    public ImageURLCustomFieldDefinitionRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, ImageURLCustomFieldDefinition.class, null, null)
    }

    //Keep CGLIB happy
    ImageURLCustomFieldDefinitionRestService() {}
}
