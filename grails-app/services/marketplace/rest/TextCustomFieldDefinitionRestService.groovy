package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import grails.core.GrailsApplication

import marketplace.TextCustomFieldDefinition

@Service
class TextCustomFieldDefinitionRestService extends AdminRestService<TextCustomFieldDefinition> {

    @Autowired
    public TextCustomFieldDefinitionRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, TextCustomFieldDefinition.class, null, null)
    }

    //Keep CGLIB happy
    TextCustomFieldDefinitionRestService() {}
}

