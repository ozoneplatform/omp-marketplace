package marketplace.rest

import groovy.transform.CompileStatic

import grails.databinding.events.DataBindingListenerAdapter
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable

import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomField
import marketplace.FieldValue
import marketplace.OwfProperties
import marketplace.Profile


@CompileStatic
class AuditableDataBindingListener extends DataBindingListenerAdapter {

    private static final Set<Class> CLASSES = [OwfProperties,
                                               CustomField,
                                               CustomFieldDefinition,
                                               DropDownCustomField,
                                               FieldValue,
                                               Profile] as Set<Class>

    boolean supports(Class<?> clazz) {
        return CLASSES.contains(clazz)
    }

    Boolean beforeBinding(Object target, Object errors) {
        DirtyCheckable entity = (DirtyCheckable) target

        entity.trackChanges()

        return true
    }

}
