package grails.plugins.elasticsearch.mapping

import groovy.transform.CompileStatic

import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import grails.util.GrailsClassUtils
import grails.util.GrailsNameUtils
import org.grails.core.util.ClassPropertyFetcher
import org.grails.datastore.mapping.model.PersistentEntity

import static java.util.Collections.emptyList


@CompileStatic
class DomainEntity {

    private final DomainReflectionService reflectionService

    private final Class<?> entityClass

    private final GrailsDomainClass grailsDomainClass
    private final PersistentEntity persistentEntity

    private final Map<String, DomainProperty> propertyCache = [:]

    private Map<String, Class<?>> _associationMap

    DomainEntity(DomainReflectionService reflectionService, Class<?> entityClass) {
        this(reflectionService, null, null, entityClass)
    }

    DomainEntity(DomainReflectionService reflectionService, GrailsDomainClass entityDelegate,
                 PersistentEntity persistentEntity)
    {
        this(reflectionService, entityDelegate, persistentEntity, entityDelegate.clazz)
    }

    private DomainEntity(DomainReflectionService reflectionService,
                         GrailsDomainClass grailsDomainClass,
                         PersistentEntity persistentEntity,
                         Class<?> entityClass)
    {
        this.persistentEntity = persistentEntity
        this.reflectionService = reflectionService
        this.entityClass = entityClass
        this.grailsDomainClass = grailsDomainClass
    }

    Class<?> getType() {
        entityClass
    }

    String getFullName() {
        grailsDomainClass?.fullName
    }

    String getPackageName() {
        grailsDomainClass?.packageName
    }

    MetaClass getDelegateMetaClass() {
        grailsDomainClass?.metaClass
    }

    boolean isRoot() {
        grailsDomainClass?.root ?: false
    }

    DomainProperty getIdentifier() {
        getPropertyAdapter(grailsDomainClass?.identifier)
    }

    String getIdentifierName() {
        grailsDomainClass?.identifier?.name
    }

    String getDefaultPropertyName() {
        grailsDomainClass?.propertyName
    }

    String getPropertyNameRepresentation() {
        GrailsNameUtils.getPropertyName(type)
    }

    boolean hasProperty(String name) {
        grailsDomainClass?.hasProperty(name) ?: false
    }

    DomainProperty getPropertyByName(String name) {
        getPropertyAdapter(grailsDomainClass?.getPropertyByName(name))
    }

    Collection<DomainProperty> getProperties() {
        allProperties
    }

    Collection<DomainProperty> getAllProperties() {
        def properties = grailsDomainClass?.properties?.toList()
        if (!properties) return emptyList()

        properties.collect { getPropertyAdapter(it) }
    }

    Collection<DomainProperty> getPersistentProperties() {
        def properties = grailsDomainClass?.persistentProperties?.toList()
        if (!properties) return emptyList()

        properties.collect { getPropertyAdapter(it) }
    }

    Object getInitialPropertyValue(String name) {
        grailsDomainClass?.getPropertyValue(name)
    }

    boolean isPropertyInherited(DomainProperty property) {
        GrailsClassUtils.isPropertyInherited(entityClass, property.name)
    }

    Class<?> getRelatedClassType(String propertyName) {
        getAssociationForProperty(propertyName)
    }

    Map<String, Class<?>> getAssociationMap() {
        if (!_associationMap) {
            _associationMap = getMergedConfigurationMap(type, GrailsDomainClassProperty.HAS_MANY)

            getProperties().each {
                if (reflectionService.isDomainEntity(it.type)) {
                    _associationMap[it.name] = it.type
                }
            }
        }

        _associationMap
    }

    boolean isAssociation(String propertyName) {
        associationMap.containsKey(propertyName)
    }

    Class<?> getAssociationForProperty(String propertyName) {
        associationMap[propertyName]
    }

    @Override
    String toString() {
        "DomainEntity{type=${type.canonicalName}}"
    }

    private static Map getMergedConfigurationMap(Class<?> clazz, String propertyName) {
        Map configurationMap = getStaticPropertyValue(clazz, propertyName, Map) ?: new HashMap<>()

        Class<?> superClass = clazz
        while (superClass != Object.class) {
            superClass = superClass.getSuperclass()
            Map superRelationshipMap = getStaticPropertyValue(superClass, propertyName, Map)
            if (superRelationshipMap != null && superRelationshipMap != configurationMap) {
                configurationMap.putAll(superRelationshipMap)
            }
        }
        return configurationMap;
    }

    private static <T> T getStaticPropertyValue(Class<?> clazz, String propertyName, Class<T> propertyClass) {
        ClassPropertyFetcher.forClass(clazz).getStaticPropertyValue(propertyName, propertyClass)
    }

    private DomainProperty getPropertyAdapter(GrailsDomainClassProperty property) {
        if (!property) return null

        propertyCache.computeIfAbsent(property.name) {
            new DomainProperty(reflectionService, this, property)
        }
    }

}
