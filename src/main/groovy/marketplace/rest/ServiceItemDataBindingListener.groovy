package marketplace.rest

import groovy.transform.CompileStatic

import grails.databinding.events.DataBindingListenerAdapter

import marketplace.Relationship
import marketplace.ServiceItem


@CompileStatic
class ServiceItemDataBindingListener extends DataBindingListenerAdapter {

    private static final Set<String> TRACKED_PROPERTIES =
            ["isOutside", "approvalStatus", "isEnabled"] as Set

    boolean supports(Class<?> clazz) {
        return clazz == ServiceItem
    }

    Boolean beforeBinding(Object target, Object errors) {
        ServiceItem serviceItem = (ServiceItem) target

        serviceItem.trackChanges()

        serviceItem.clearChangelog()

        serviceItem.setOldValue('relationships',
                                serviceItem.relationships?.collect { clone(it) } as Set)

        serviceItem.setOldValue('customFields', cloneList(serviceItem.customFields))

        serviceItem.setOldValue('owners', cloneList(serviceItem.owners))

        return true
    }

    Boolean beforeBinding(Object target, String propertyName, Object newValue, Object errors) {
        ServiceItem serviceItem = (ServiceItem) target

        if (TRACKED_PROPERTIES.contains(propertyName)) {
            def currentValue = serviceItem.getProperty(propertyName)
            if (newValue != currentValue) {
                serviceItem.setOldValue(propertyName, currentValue)
            }
        }

        return true
    }

    private static Map clone(Relationship relationship) {
        [id          : relationship.id,
         relatedItems: cloneList(relationship.relatedItems)]
    }

    private static List cloneList(Collection values) {
        values != null ? new ArrayList(values) : null
    }

}
