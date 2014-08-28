package marketplace.rest

import javax.annotation.security.RolesAllowed

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.validator.DomainValidator
import marketplace.Sorter

/**
 * A REST service class that requires admin access for
 * non-read-only operations
 */
class AdminRestService<T> extends RestService<T> {

    protected AdminRestService(GrailsApplication grailsApplication, Class<T> DomainClass,
            DomainValidator validator, Sorter<T> sorter) {
        super(grailsApplication, DomainClass, validator, sorter)
    }

    protected AdminRestService() {}

    //NOTE: Unit testing the RolesAllowed annotation
    //probably is not possible.  It should be tested
    //via REST integration tests
    @RolesAllowed('ROLE_ADMIN')
    public void deleteById(Long id) {
        super.deleteById(id)
    }

    @RolesAllowed('ROLE_ADMIN')
    public T updateById(Long id, T dto) {
        return super.updateById(id, dto)
    }

    @RolesAllowed('ROLE_ADMIN')
    public T createFromDto(T dto) {
        return super.createFromDto(dto)
    }
}
