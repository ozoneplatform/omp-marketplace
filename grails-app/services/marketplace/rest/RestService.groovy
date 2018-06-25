package marketplace.rest

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.web.databinding.DataBinder
import org.grails.datastore.gorm.GormEntity

import org.springframework.security.access.AccessDeniedException

import marketplace.Sorter
import marketplace.validator.DomainValidator

/**
 * Parent class of services designed to support the
 * jaxrs-based Rest layer
 */
@Transactional
abstract class RestService<T extends GormEntity> implements DataBinder {

    protected final GrailsApplication grailsApplication

    //the domain class that this class deals with.
    //should be set by the subclass
    protected final Class<T> domainClass

    protected final DomainValidator<T> validator

    protected final Sorter sorter

    /**
     * This class expects the domain objects that it deals with to include
     * the following static fields:
     *
     * bindableProperties: A whitelist of properties that this service is allowed to
     * bind on update.  This prevents properties that should be read-only
     * from being changeable by the caller
     *
     * modifiableReferenceProperties: A list of properties that contain references to other
     * domain objects that can be modified or created by
     * PUTs or POSTs to this service.  Reference properties not
     * listed here will be treated as references to separated
     * objects, where the reference can be changed to a different
     * object, but the object itself cannot be modified.
     * Example: for ServiceItem, owfProperties would be in this
     * list, but Agency would not be
     *
     * modifiableReferenceProperties should be a subset of bindableProperties
     */

    protected RestService(GrailsApplication grailsApplication, Class<T> DomainClass,
                          DomainValidator<T> validator, Sorter<T> sorter) {
        this.grailsApplication = grailsApplication
        this.domainClass = DomainClass
        this.validator = validator
        this.sorter = sorter
    }

    //Keep CGLIB happy
    protected RestService() {}

    @Transactional(readOnly=true)
    T getById(Long id) {
        T obj = domainClass.get(id)

        if (!obj) {
            throw new DomainObjectNotFoundException(domainClass, id)
        }

        authorizeView(obj)

        obj
    }

    @Transactional(readOnly=true)
    Collection<T> getAll(Integer offset, Integer max) {
        domainClass.createCriteria().list(offset: offset, max: max) {
            if (this.sorter) {
                order(sorter.sortField, sorter.direction.name().toLowerCase())
            }
        }
    }

    /**
     * @param skipValidation Set to true to skip the usage of the DomainValidator.  This should
     * only be used for special cases like Import where the normal governance workflow should
     * be skipped.  This flag will also cause defaults not to be populated
     */
    @Transactional
    T createFromDto(T entity, boolean skipValidation = false) {
        if (!skipValidation) {
            populateDefaults(entity)
        }

        preprocess(entity)

        if (!skipValidation) {
            validator?.validateNew(entity)
        }

        authorizeCreate(entity)

        postprocess(entity, true)

        return save(entity)
    }

    Collection<T> createFromDtoCollection(Collection<T> entities) {
        entities.collect { createFromDto(it) }
    }


    void deleteById(Long id) {
        T obj = getById(id)

        authorizeUpdate(obj)

        //judging from GRAILS-7699, delete without flush
        //isn't particularly reliable, at least in unit tests
        //if not also certain production databases
        obj.delete(flush: true)
    }

    /**
     * @param skipValidation Set to true to skip the usage of the DomainValidator.  This should
     * only be used for special cases like Import where the normal governance workflow should
     * be skipped
     */
    T updateById(Long id, T entity, boolean skipValidation=false) {
        authorizeUpdate(entity)

        preprocess(entity)

        if (!skipValidation) {
            validator?.validateChanges(entity)
        }

        postprocess(entity, false)

        return save(entity)
    }

    /**
     * An alternative update method meant for service-to-service calls.
     * This method avoids the need for services to create "fake" DTOs
     * to pass to each other.  This method does not validate that the
     * changes in the updates map are authorized or valid. This method
     * is also currently limited to simple updates - no subobject
     * updates
     * @param skipValidation If true, the DomainValidator is not run
     */
    protected T update(T entity, Map<String, ?> updates, boolean skipValidation = false) {
        authorizeUpdate(entity)

        bindData(entity, updates)

        if (!skipValidation) {
            validator?.validateChanges(entity)
        }

        postprocess(entity, false)

        return save(entity)
    }


    /**
     * Save the domain object
     */
    @Transactional
    protected T save(T obj) {
        obj.save(failOnError: true)
    }

    /**
     * @return whether or not the current user is allowed to view this object
     */
    protected boolean canView(T obj) {
        true
    }

    /**
     * Authorize changes on the given existing object.
     *
     * This method should be overridden by subclasses in order to
     * enforce complex authorization checks (such as admin + owner
     * for ServiceItem
     *
     * The default implementation delegates to authorizeView since, at a minimum,
     * people generally cannot update objects that they cannot view
     *
     * @param existing The existing service item being updated or
     * deleted.  This method should be called before the object
     * is updated.
     *
     * @throws AccessDeniedException if this user is not authorized
     */
    protected void authorizeUpdate(T existing) throws AccessDeniedException {
        authorizeView(existing)
    }

    /**
     * Authorize the creation of a new object of type T.
     * The default implementation just defers to the same logic as authorizeUpdate
     *
     * @param newObject The currently unsaved new object being created
     *
     * @throws AccessDeniedException if this user is not authorized
     */
    protected void authorizeCreate(T newObject) throws AccessDeniedException {
        authorizeUpdate(newObject)
    }

    /**
     * Authorize the viewing of a single object.  Note that this method is used by getById, but
     * is NOT used by getAll.  It is recommended that subclasses add filtering rules into the
     * getAll database criteria in order to avoid breaking paging by filtering the query results.
     *
     * @param obj The object being viewed
     *
     * @throws AccessDeniedException if this user is not authorized
     */
    protected void authorizeView(T obj) throws AccessDeniedException {
        if (!canView(obj)) {
            throw new AccessDeniedException("Unauthorized attempt to view ${obj.class} $obj")
        }
    }

    /**
     * Perform tasks that should occur before a domain object is saved.  This is called before
     * both create and update.  Subclasses should be sure to call the superclass method first
     * before their own logic runs.  This method can modify the dto But should not independently
     * create and persist other domain objects and validation of the dto is not performed until
     * after this method is called.
     * @param dto The dto being updated or created
     */
    protected void preprocess(T dto) {}

    /**
     * Perform tasks that should occur after a domain object is updated, but before save is
     * called on it.  This is called after both create and update.  If other domain objects
     * need to be explicitly persisted, this is a good place to do it.  This method may choose
     * to itself save the updated object
     * @param updated The object that was updated
     * @param original The original copy of the object, if there was one (would be null for
     * a create)
     */
    protected void postprocess(T updated, boolean isNew) {}

    /**
     * populate default field values in this ServiceItem
     */
    protected void populateDefaults(T dto) {}
}
