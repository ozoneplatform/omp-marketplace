package marketplace.rest

import groovy.transform.CompileStatic

import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable


@CompileStatic
class DomainReflection {

    private DomainReflection() {}

    static <E extends GormEntity & DirtyCheckable, V> V getOriginalValue(E entity, String propertyName, Class<V> propertyType) {
        def value
        if (entity.hasChanged(propertyName)) {
            value = entity.getOriginalValue(propertyName)
        } else {
            value = entity[propertyName]
        }

        return value != null ? propertyType.cast(value) : null
    }

}
