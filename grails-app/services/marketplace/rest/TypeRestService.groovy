package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import grails.core.GrailsApplication

import marketplace.Types

@Service
class TypeRestService extends AdminRestService<Types> {

    @Autowired
    public TypeRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, Types.class, null, null)
    }

    //Keep CGLIB happy
    TypeRestService() {}
}
