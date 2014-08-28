package marketplace.rest

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import org.springframework.security.access.AccessDeniedException

import marketplace.rest.DomainObjectNotFoundException
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass

import static ozone.utils.Utils.singleOrCollectionDo

import marketplace.validator.DomainValidator

import marketplace.Sorter

/**
 * Parent class of services designed to support the
 * jaxrs-based Rest layer
 */
@Transactional
abstract class RestService<T> {

    protected final GrailsApplication grailsApplication

    //the domain class that this class deals with.
    //should be set by the subclass
    protected final Class<T> DomainClass
    protected final GrailsDomainClass grailsDomainClass

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

        this.DomainClass = DomainClass
        this.grailsDomainClass = grailsApplication.getDomainClass(DomainClass.name)
        this.validator = validator
        this.sorter = sorter
    }

    //Keep CGLIB happy
    protected RestService() {}

    public void deleteById(Long id) {
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
    public T updateById(Long id, T dto, boolean skipValidation=false) {
        //ensure that the ID from the request body and the ID
        //from the URL match
        if (dto.id != id) {
            throw new IllegalArgumentException(
                "Attempt to update resource with different id")
        }

        T toUpdate = getById(id)

        //we need an extra copy that doesn't get changed by the update
        //so we can pass it to postprocess.  This creates a shallow copy
        Map old = new HashMap()
        old.putAll(toUpdate.properties)
        old.id = toUpdate.id   //the above does not copy the id
        copyCollections(old)

        preprocess(dto)

        if (!skipValidation) {
            validator?.validateChanges(toUpdate, dto)
        }
        authorizeUpdate(toUpdate)

        bind(toUpdate, dto)

        postprocess(toUpdate, old)

        return save(toUpdate)
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
    protected T update(T existing, Map updates, boolean skipValidation = false) {
        T old = makeOldCopy(existing)
        authorizeUpdate(existing)

        //use untyped variable to get around groovy closure/generics bug
        def existingObj = existing
        updates.entrySet().each { update ->
            if (update.key in DomainClass.bindableProperties) {
                existingObj[update.key] = update.value
            }
        }

        if (!skipValidation) {
            validator?.validateChanges(old, existing)
        }

        postprocess(existing, old)

        return save(existing)
    }

    private Map makeOldCopy(T toUpdate) {
        Map old = new HashMap()
        old.putAll(toUpdate.properties)
        old.id = toUpdate.id   //the above does not copy the id
        copyCollections(old)

        return old
    }

    /**
     * Go through all properties of the object and make shallow copies of all collections.
     * This insulates this object from changes to those collections.  This is used to
     * keep the "old" copy of the domain object from getting changed
     */
    private void copyCollections(obj) {
        grailsDomainClass.persistentProperties.grep {
            obj[it.name] instanceof Collection
        }.each { property ->
            def oldCollection = obj[property.name]

            //TODO add support for more than just sets and lists
            if (oldCollection instanceof List) {
                obj[property.name] = new ArrayList(oldCollection)
            }
            else if (oldCollection instanceof SortedSet) {
                obj[property.name] = new TreeSet(oldCollection)
            }
            else {
                obj[property.name] = new HashSet(oldCollection)
            }
        }
    }

    /**
     * Overridable method that implements the binding of new
     * properties from a dto to an existing domain object.
     * This default implementation binds only properties that
     * are present in the bindableProperties list.
     *
     * @param obj The existing domain object that will be updated
     * @param dto The dto that contains the new property set
     * of the object
     */
    protected void bind(T obj, T dto) {
        //originally DataBindingUtils was used for this, but for some reason it would
        //create empty subobjects for all relationship properties not specificied in the
        //include list

        //These cast to def get us past a groovy 1.7.8 bug with generic instantiation
        def persistedObj = obj
        def incomingDto = dto
        grailsDomainClass.persistentProperties.each { property ->
            def key = property.name
            def value = incomingDto[key]
            def existingVal = persistedObj[key]

            if (DomainClass.bindableProperties.contains(key)) {
                if (existingVal instanceof Collection || value instanceof Collection) {
                    existingVal?.clear()

                    //use the addTo* methods to add collection elements. This
                    //allows back-references to be set automatically
                    def addMethodName = "addTo" + key.capitalize()
                    value.each {
                        persistedObj."$addMethodName"(it)
                    }
                }
                else {
                    persistedObj[key] = value
                }
            }
        }
    }

    /**
     * DTOs for a PUT will often come in with subobjects which may
     * or may not be missing fields, and which shouldn't be taken
     * as full new objects so much as references to existing
     * subobjects.  Therefore what we want to do is take any
     * subobjects which have their id filled in and replace them
     * with the actual domain object of that type with that id,
     * loaded from the database.  This method takes care of that.
     *
     * Subobject properties listed in the modifiableReferenceProperties
     * list will be treated differently.  They will essentially be treated
     * as part of the main object itself, and will thus be recursed into
     *
     * @param grailsDomainClass The GrailsDomainClass for the class of dto
     *
     * @param dto The parent object to perform subobject
     * marshalling on.
     */
    protected void marshallSubObjects(GrailsDomainClass grailsDomainClass, dto) {
        /**
         * invoke GORM 'get' method to retrieve from db
         * by id.
         * @param parent The parent object (the dto or collection)
         * @param index The property name or collection index of
         * the subobject being handled
         * @param type The expected Class of the subobject. Must
         * be a domain class type
         */
        def getSubObjectFromDb = { obj ->
            if (obj) {
                def retval = obj.getClass().get(obj.id)
                if (retval == null) {
                    throw new IllegalArgumentException("Attempted to find non-existant object " +
                        "of type ${obj.class} with id ${obj.id}")
                }

                return retval
            }
        }

        if (dto == null) return

        grailsDomainClass.properties.grep {
            dto.class.bindableProperties.contains it.name
        }.each { property ->
            //recurse into modifiableReferenceProps instead of marshalling
            //them
            if (grailsDomainClass.clazz.modifiableReferenceProperties.contains(property.name)) {
                //property.referencedDomainClass would be better, but it doesn't appear to work
                //during unit tests
                GrailsDomainClass referencedClass = grailsApplication.getDomainClass(
                    property.referencedPropertyType.name)

                singleOrCollectionDo(dto[property.name],
                    this.&marshallSubObjects.curry(referencedClass))
            }
            else {
                //if direct reference to subobject
                if (grailsApplication.isDomainClass(property.type)) {
                    dto[property.name] = getSubObjectFromDb(dto[property.name])
                }

                //if reference to collection of subobjects
                else if (grailsApplication.isDomainClass(property.referencedPropertyType)) {
                    dto[property.name] = dto[property.name].collect(getSubObjectFromDb)
                }
            }
        }
    }

    /**
     * Go through the object's modifiableReferenceProperties and remove ids to ensure that
     * a new object is created
     */
    protected void stripModifiableReferenceIds(T dto) {
        def stripId = { obj ->
            if (obj != null) {
                obj.id = null
                this.stripModifiableReferenceIds(obj)
            }
        }

        def incomingDto = dto   //This gets us past an ugly groovy 1.7.8 bug
        incomingDto.modifiableReferenceProperties.each { property ->
            singleOrCollectionDo(incomingDto[property], stripId)
        }
    }

    /**
     * Save the domain object
     */
    protected T save(T obj) {
        obj.save(failOnError: true)
    }


    @Transactional(readOnly=true)
    public T getById(Long id) {
        T obj = DomainClass.get(id)

        if (!obj) {
            throw new DomainObjectNotFoundException(DomainClass, id)
        }

        authorizeView(obj)

        obj
    }

    @Transactional(readOnly=true)
    public Collection<T> getAll(Integer offset, Integer max) {
        DomainClass.createCriteria().list(offset: offset, max: max) {
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
    public T createFromDto(T dto, boolean skipValidation=false) {
        if (!skipValidation) {
            populateDefaults(dto)
        }

        preprocess(dto)

        if (!skipValidation) {
            validator?.validateNew(dto)
        }


        authorizeCreate(dto)

        //we cannot just save the dto because we need to make sure
        //only the allowed properties are saved.  bind ensures this
        T newObj = DomainClass.metaClass.invokeConstructor()
        bind(newObj, dto)


        postprocess(newObj)

        return save(newObj)
    }

    public Collection<T> createFromDtoCollection(Collection<T> dtos) {
        dtos.collect {
            createFromDto(it)
        }
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
    protected final void authorizeView(T obj) throws AccessDeniedException {
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
    protected void preprocess(T dto) {
        marshallSubObjects(grailsDomainClass, dto)
        stripModifiableReferenceIds(dto)
    }

    /**
     * Perform tasks that should occur after a domain object is updated, but before save is
     * called on it.  This is called after both create and update.  If other domain objects
     * need to be explicitly persisted, this is a good place to do it.  This method may choose
     * to itself save the updated object
     * @param updated The object that was updated
     * @param original The original copy of the object, if there was one (would be null for
     * a create)
     */
    protected void postprocess(T updated, Map original = null) {}

    /**
     * populate default field values in this ServiceItem
     */
    protected void populateDefaults(T dto) {}
}
