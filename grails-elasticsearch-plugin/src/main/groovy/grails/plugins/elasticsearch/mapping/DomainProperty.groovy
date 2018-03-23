package grails.plugins.elasticsearch.mapping

import groovy.transform.CompileStatic

import grails.core.GrailsDomainClassProperty


@CompileStatic
class DomainProperty {

    private final DomainReflectionService reflectionService

    private final DomainEntity owningEntity
    private final GrailsDomainClassProperty delegate

    DomainProperty(DomainReflectionService reflectionService,
                   DomainEntity owningEntity,
                   GrailsDomainClassProperty propertyDelegate)
    {
        this.reflectionService = reflectionService
        this.owningEntity = owningEntity
        this.delegate = propertyDelegate
    }

    boolean isPersistent() {
        delegate.persistent
    }

    DomainEntity getDomainEntity() {
        owningEntity
    }

    Class<?> getType() {
        delegate.type
    }

    String getName() {
        delegate.name
    }

    Class<?> getReferencedPropertyType() {
        (association) ? associationType : type
    }

    boolean isAssociation() {
        domainEntity.isAssociation(name)
    }

    Class<?> getAssociationType() {
        domainEntity.getAssociationForProperty(name)
    }

    DomainEntity getReferencedDomainEntity() {
        (association) ? reflectionService.getDomainEntity(associationType) : null
    }

    String getTypePropertyName() {
        delegate.typePropertyName
    }

    @Override
    String toString() {
        "DomainProperty{name=$name, type=${type.simpleName}, domainClass=$domainEntity.fullName}"
    }

}
