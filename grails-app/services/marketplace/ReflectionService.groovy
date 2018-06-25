package marketplace

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import grails.core.GrailsApplication
import grails.gorm.validation.ConstrainedProperty
import grails.gorm.validation.DefaultConstrainedProperty
import grails.util.Holders
import grails.validation.Constrained
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Association
import org.grails.datastore.mapping.model.types.Basic
import org.grails.datastore.mapping.model.types.Custom
import org.grails.datastore.mapping.model.types.Embedded
import org.grails.datastore.mapping.model.types.EmbeddedCollection
import org.grails.datastore.mapping.model.types.Identity
import org.grails.datastore.mapping.model.types.ManyToMany
import org.grails.datastore.mapping.model.types.ManyToOne
import org.grails.datastore.mapping.model.types.OneToMany
import org.grails.datastore.mapping.model.types.OneToOne
import org.grails.datastore.mapping.model.types.Simple
import org.grails.datastore.mapping.model.types.ToMany
import org.grails.datastore.mapping.model.types.ToOne
import org.grails.datastore.mapping.reflect.ClassPropertyFetcher
import org.grails.validation.ConstrainedDelegate

import org.springframework.beans.factory.annotation.Autowired

import static com.google.common.base.Preconditions.checkNotNull


@CompileStatic
class ReflectionService {

    private final GrailsApplication grailsApplication
    private final MappingContext mappingContext

    private static final Map<Class, Set<String>> BINDABLE_PROPERTIES_CACHE = [:]
    private static final Map<Class, Set<String>> MODIFIABLE_PROPERTIES_CACHE = [:]
    private static final Map<Class, Set<String>> AUDITABLE_PROPERTIES_CACHE = [:]

    private static MappingContext getMappingContext() {
        Holders.grailsApplication.mappingContext
    }

    @Autowired
    ReflectionService(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
        this.mappingContext = grailsApplication.mappingContext
    }

    static boolean isDomainClass(Class clazz) {
        getMappingContext().isPersistentEntity(clazz)
    }

    static Class getReferencedPropertyType(PersistentProperty property) {
        if (property instanceof Association) {
            PersistentEntity entity = property.associatedEntity
            if (entity != null) {
                return entity.javaClass
            } else if (property instanceof Basic) {
                return property.componentType
            }
        } else if (property instanceof Simple || property instanceof Identity) {
            return property.type
        }
        return null
    }

    static boolean isPropertyChanged(GormEntity entity, String propertyName) {
        if (!isPropertyDirty(entity, propertyName)) return false

        def newValue = entity[propertyName]
        def oldValue = getPreviousValue(entity, propertyName)

        newValue != oldValue
    }

    static boolean isPropertyDirty(GormEntity entity, String propertyName) {
        if (entity instanceof ChangeLogging) {
            return entity.isPropertyChanged(propertyName) || entity.isDirty(propertyName)
        }

        entity.isDirty(propertyName)
    }

    static Object getPreviousValue(GormEntity entity, String propertyName) {
        if (entity instanceof ChangeLogging && entity.isPropertyChanged(propertyName)) {
            return entity.getOldValue(propertyName)
        }

        entity.getPersistentValue(propertyName)
    }

    static Set<String> getBindablePropertiesOld(Class type) {
        ClassPropertyFetcher.getStaticPropertyValue(type, "bindableProperties", List) as Set
    }

    static Set<String> getModifiableReferencePropertiesOld(Class type) {
        ClassPropertyFetcher.getStaticPropertyValue(type, "modifiableReferenceProperties", List) as Set
    }

    @CompileDynamic
    static Map<String, Constrained> getConstrainedProperties(Class clazz) {
        clazz.constrainedProperties
    }

    static Set<String> getBindableProperties(Class type) {
        checkNotNull(type, "type must not be null")

        def cachedValues = BINDABLE_PROPERTIES_CACHE.get(type)
        if (cachedValues != null) {
            return cachedValues
        }

        Set<String> bindableProperties = new HashSet<>()

        Map<String, Constrained> properties = getConstrainedProperties(type)
        properties.forEach { String propertyName, Constrained constrained ->
            def property = getConstrainedProperty(constrained)

            def isBindable = property.getMetaConstraintValue('bindable')
            if (isBindable != null && !(isBindable instanceof Boolean)) {
                throw new IllegalArgumentException("${type.name} '${propertyName}' property 'bindable' constraint must be a boolean value")
            }

            if (isBindable || isBindable == null) {
                bindableProperties.add(propertyName)
            }
        }

        BINDABLE_PROPERTIES_CACHE.put(type, bindableProperties)

        bindableProperties
    }

    static Set<String> getModifiableReferenceProperties(Class type) {
        checkNotNull(type, "type must not be null")

        def cachedValues = MODIFIABLE_PROPERTIES_CACHE.get(type)
        if (cachedValues != null) {
            return cachedValues
        }

        Set<String> modifiableProperties = new HashSet<>()

        Map<String, Constrained> properties = getConstrainedProperties(type)
        properties.forEach { String propertyName, Constrained constrained ->
            DefaultConstrainedProperty property = getConstrainedProperty(constrained)

            def isModifiable = property.getMetaConstraintValue('modifiable')
            if (isModifiable != null && !(isModifiable instanceof Boolean)) {
                throw new IllegalArgumentException("${type.name} '${propertyName}' property 'modifiable' constraint must be a boolean value")
            }

            if (isModifiable == true) {
                modifiableProperties.add(propertyName)
            }
        }

        MODIFIABLE_PROPERTIES_CACHE.put(type, modifiableProperties)

        modifiableProperties
    }


    static Set<String> getAuditableProperties(Class type) {
        checkNotNull(type, "type must not be null")

        def cachedValues = AUDITABLE_PROPERTIES_CACHE.get(type)
        if (cachedValues != null) {
            return cachedValues
        }

        Set<String> bindableProperties = getBindableProperties(type)

        Set<String> auditableProperties = new HashSet<>()

        Map<String, Constrained> properties = getConstrainedProperties(type)
        properties.forEach { String propertyName, Constrained constrained ->
            DefaultConstrainedProperty property = getConstrainedProperty(constrained)

            def isAuditable = property.getMetaConstraintValue('auditable')
            if (isAuditable != null && !(isAuditable instanceof Boolean)) {
                throw new IllegalArgumentException("${type.name} '${propertyName}' property 'auditable' constraint must be a boolean value")
            }

            if ((isAuditable || isAuditable == null) && bindableProperties.contains(propertyName)) {
                auditableProperties.add(propertyName)
            }
        }

        AUDITABLE_PROPERTIES_CACHE.put(type, auditableProperties)

        auditableProperties
    }

    private static DefaultConstrainedProperty getConstrainedProperty(Constrained constrained) {
        if (!(constrained instanceof ConstrainedDelegate)) {
            throw new IllegalArgumentException("Unsupported Constrained type: ${constrained.class.canonicalName}")
        }

        def property = ((ConstrainedDelegate) constrained).property
        if (!(property instanceof DefaultConstrainedProperty)) {
            throw new IllegalArgumentException("Unsupported ConstrainedProperty type: ${property.class.canonicalName}")
        }

        (DefaultConstrainedProperty) property
    }

    static void formatProperty(PersistentProperty property) {
        """property:
          |  name:   ${property.name}
          |  type:   ${property.type.name}
          |  owner:  ${property.owner.name}"
          |  Simple:       ${property instanceof Simple}
          |  Custom:       ${property instanceof Custom}
          |  Association:  ${property instanceof Association}
          |    EmbedColl:    ${property instanceof EmbeddedCollection}
          |    ToOne:        ${property instanceof ToOne}   
          |      Embedded:     ${property instanceof Embedded}
          |      OneToOne:     ${property instanceof OneToOne}   
          |      ManyToOne:    ${property instanceof ManyToOne}
          |    ToMany:       ${property instanceof ToMany}
          |      Basic:        ${property instanceof Basic}
          |      OneToMany:    ${property instanceof OneToMany}
          |      ManyToMany    ${property instanceof ManyToMany}""".stripMargin()
    }

}
