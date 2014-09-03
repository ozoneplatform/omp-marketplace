package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.State

@Service
class StateRestService extends AdminRestService<State> {

    @Autowired
    public StateRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, State.class, null, null)
    }

    //Keep CGLIB happy
    StateRestService() {}
}
