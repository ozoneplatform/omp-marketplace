package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.Agency

@Service
class AgencyRestService extends AdminRestService<Agency> {

    @Autowired
    public AgencyRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, Agency.class, null, null)
    }

    //Keep CGLIB happy
    AgencyRestService() {}

    @Override
	public Collection<Agency> getAll() {
		def crit = Agency.createCriteria()
		List<Agency> agencyList = crit.list{
			order("title", "asc")
		}
		agencyList
	}
}
