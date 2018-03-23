package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import org.hibernate.Criteria

import grails.core.GrailsApplication

import marketplace.Category

@Service
class CategoryRestService extends AdminRestService<Category> {

    @Autowired
    public CategoryRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, Category.class, null, null)
    }

    //Keep CGLIB happy
    CategoryRestService() {}
}

