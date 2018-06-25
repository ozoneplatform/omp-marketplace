package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import grails.core.GrailsApplication

import marketplace.TextAreaCustomFieldDefinition

@Service
class TextAreaCustomFieldDefinitionRestService extends
        AdminRestService<TextAreaCustomFieldDefinition> {

    @Autowired
    public TextAreaCustomFieldDefinitionRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, TextAreaCustomFieldDefinition.class, null, null)
    }

    //Keep CGLIB happy
    TextAreaCustomFieldDefinitionRestService() {}
}

