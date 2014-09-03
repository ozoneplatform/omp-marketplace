package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.CheckBoxCustomFieldDefinition

@Service
class CheckBoxCustomFieldDefinitionRestService extends
        AdminRestService<CheckBoxCustomFieldDefinition> {

    @Autowired
    public CheckBoxCustomFieldDefinitionRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, CheckBoxCustomFieldDefinition.class, null, null)
    }

    //Keep CGLIB happy
    CheckBoxCustomFieldDefinitionRestService() {}
}

