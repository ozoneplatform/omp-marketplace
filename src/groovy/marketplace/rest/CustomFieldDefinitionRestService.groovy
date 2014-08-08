package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.CustomFieldDefinition

@Service
class CustomFieldDefinitionRestService extends AdminRestService<CustomFieldDefinition> {

    @Autowired
    public CustomFieldDefinitionRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, CustomFieldDefinition.class, null, null)
    }

    //Keep CGLIB happy
    CustomFieldDefinitionRestService() {}
}
