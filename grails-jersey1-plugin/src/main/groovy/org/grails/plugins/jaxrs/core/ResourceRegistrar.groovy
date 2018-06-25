package org.grails.plugins.jaxrs.core

/**
 * An interface defining a class that is responsible for providing the plugin with a list of
 * resource classes to add the {@link JaxrsContext} during application initialization.
 */
interface ResourceRegistrar {
    /**
     * Provides a list of classes that should be registered as JAX-RS resources, including both
     * root resources and reader/writer providers.
     *
     * @return A list of resources to register with the {@link JaxrsContext}.
     */
    List<Class<?>> getResourceClasses()
}
