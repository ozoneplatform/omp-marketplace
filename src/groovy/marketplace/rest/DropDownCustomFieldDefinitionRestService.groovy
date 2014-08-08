package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.DropDownCustomFieldDefinition

@Service
class DropDownCustomFieldDefinitionRestService extends
        AdminRestService<DropDownCustomFieldDefinition> {

    @Autowired
    public DropDownCustomFieldDefinitionRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, DropDownCustomFieldDefinition.class, null, null)
    }

    //Keep CGLIB happy
    DropDownCustomFieldDefinitionRestService() {}
}
