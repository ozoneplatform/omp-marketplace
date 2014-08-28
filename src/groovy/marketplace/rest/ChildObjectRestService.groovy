package marketplace.rest

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.Sorter

import marketplace.validator.DomainValidator

/**
 * A service that facilitates the handling of domain objects that are
 * subordinate to some other, such as RejectionListings with respect to
 * ServiceItems. The child class must have a reference to the parent
 */
abstract class ChildObjectRestService<P, T> extends RestService<T> {
    private final Class<P> ParentClass

    protected final String parentPropertyName
    protected final String parentBackrefPropertyName

    protected final RestService<P> parentClassRestService

    /**
     * @param ParentClass The class representing the parent of the domain object that this service
     * deals with.
     * @param parentPropertyName The name of the property on this domain class that references
     * the parent domain class
     * @param parentBackrefPropertyName The name of the property on the parent class that
     * references this class
     * @param parentClassRestService The RestService for the parent class
     * @param grailsApplication The GrailsApplication
     * @param DomainClass The domain class that this service manipulates
     * @param validator The optional DomainValidator
     * @param sorter The Sorter used to return the bulk results (optional)
     */
    ChildObjectRestService(Class<P> ParentClass, String parentPropertyName,
            String parentBackrefPropertyName, GrailsApplication grailsApplication,
            Class<T> DomainClass, RestService<P> parentClassRestService,
            DomainValidator<T> validator, Sorter<T> sorter) {
        super(grailsApplication, DomainClass, validator, sorter)

        this.ParentClass = ParentClass
        this.parentPropertyName = parentPropertyName
        this.parentBackrefPropertyName = parentBackrefPropertyName
        this.parentClassRestService = parentClassRestService
    }

    protected ChildObjectRestService() {}

    public T createFromParentIdAndDto(Long parentId, T dto) {
        dto[parentPropertyName] = makeParentDto(parentId)
        return super.createFromDto(dto)
    }

    public T updateByParentId(Long parentId, Long id, T dto) {
        dto[parentPropertyName] = makeParentDto(parentId)
        return updateById(id, dto)
    }

    /**
     * Get the items that are have the matching parent and which this user is authorized to view
     */
    @Transactional(readOnly=true)
    public List<T> getByParentId(Long parentId, Integer offset=null, Integer max=null) {
        //ensure that we can view the parent
        parentClassRestService.getById(parentId)

        return DomainClass.createCriteria().list(max: max, offset: offset) {
            "${this.parentPropertyName}" {
                eq('id', parentId)
            }

            if (this.sorter) {
                order(sorter.sortField, sorter.direction.name().toLowerCase())
            }
        }
    }

    /**
     * Directly creating a child object with this method is not supported. Use
     * createFromParentIdAndDto instead
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public T createFromDto(T dto) {
        throw new UnsupportedOperationException("Child objects cannot be created without " +
            "reference to the parent")
    }

    /**
     * This default implementation of authorizeView delegates to the same method for the parent
     * REST service
     */
    @Override
    protected boolean canView(T obj) {
        parentClassRestService.canView(getParent(obj))
    }

    /**
     * Convenience method to get the parent object.  This method does not do security checks.
     */
    private P getParent(T obj) {
        obj[parentPropertyName]
    }

    /**
     * Create a dto of the parent class that contains only the id
     */
    private P makeParentDto(Long id) {
        P parentDto = ParentClass.metaClass.invokeConstructor()
        parentDto.id = id

        return parentDto
    }
}
