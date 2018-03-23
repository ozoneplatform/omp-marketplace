package marketplace.rest

import grails.core.GrailsApplication
import org.grails.datastore.gorm.GormEntity

import org.springframework.security.access.prepost.PreAuthorize

import marketplace.Sorter
import marketplace.validator.DomainValidator

/**
 * A REST service class that requires admin access for
 * non-read-only operations
 */
class AdminRestService<T extends GormEntity> extends RestService<T> {

    protected AdminRestService(GrailsApplication grailsApplication,
                               Class<T> DomainClass,
                               DomainValidator validator,
                               Sorter<T> sorter)
    {
        super(grailsApplication, DomainClass, validator, sorter)
    }

    protected AdminRestService() {}

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(Long id) {
        super.deleteById(id)
    }

    @PreAuthorize("hasRole('ADMIN')")
    T updateById(Long id, T dto, boolean skipValidation=false) {
        super.updateById(id, dto, skipValidation)
    }

    @PreAuthorize("hasRole('ADMIN')")
    T createFromDto(T dto, boolean skipValidation=false) {
        super.createFromDto(dto, skipValidation)
    }

}
