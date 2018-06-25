package org.grails.plugins.jaxrs.core

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import java.lang.reflect.Modifier

import grails.core.GrailsApplication
import grails.web.mapping.UrlMappings
import org.grails.plugins.jaxrs.artefact.ResourceArtefactHandler
import org.grails.plugins.jaxrs.provider.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.ServletResponse
import javax.servlet.ServletResponseWrapper
import javax.ws.rs.HttpMethod
import javax.ws.rs.Path
import java.lang.annotation.Annotation
import java.lang.reflect.Method

/**
 * Provides utility methods to the JAX-RS plugin.
 *
 * @author Bud Byrd
 */
@CompileStatic
class JaxrsUtil {
    /**
     * Name of the request attribute for storing the request URI.
     */
    static final String REQUEST_URI_ATTRIBUTE_NAME = 'org.grails.jaxrs.request.uri'

    /**
     * JAX-RS context instance.
     */
    JaxrsContext jaxrsContext

    /**
     * URL mappings holder.
     */
    UrlMappings grailsUrlMappingsHolder

    /**
     * Grails application bean.
     */
    GrailsApplication grailsApplication

    /**
     * Logger.
     */
    Logger log = LoggerFactory.getLogger(JaxrsUtil)

    private List<Class> findResourceArtefacts() {
        grailsApplication.getArtefacts(ResourceArtefactHandler.TYPE)*.clazz
    }

    /**
     * Inspects every registered resource class for its root path, and
     * parses the path for use with URL mappings.
     *
     * @return A list of root paths used for URL mappings.
     */
    List<String> retrieveMappingsList() {
        List<Class> candidates = []

        candidates.addAll(findResourceArtefacts())
        candidates.addAll(jaxrsContext.applicationConfig.classes)

        candidates = candidates.findAll { !Modifier.isAbstract(it.modifiers) }

        List<String> paths = []
        candidates.each { candidate ->
            Path rootPath = getRootPath(candidate)
            getResourcePaths(candidate).each { path ->
                paths.add(buildMappingFromPath(rootPath, path))
            }
        }

        return paths.unique()
    }

    /**
     * A helper method that unwraps a request until all wrappers are removed.
     *
     * @param servletResponse
     * @return
     */
    ServletResponse unwrap(ServletResponse servletResponse) {
        if (servletResponse instanceof ServletResponseWrapper) {
            return unwrap(servletResponse.response)
        }
        return servletResponse
    }

    /**
     * Configures the JAX-RS Application configuration by adding built-in providers
     * and scanning Grails artefacts for additional providers and resources that
     * should be included.
     *
     * @param context
     * @param grailsApplication
     */
    void setupJaxrsContext() {
        jaxrsContext.providerInitParameters = getProviderInitParameters()

        JaxrsApplicationConfig config = jaxrsContext.applicationConfig

        config.reset()
        config.classes << XMLWriter
        config.classes << XMLReader
        config.classes << JSONWriter
        config.classes << JSONReader
        config.classes << DomainObjectReader
        config.classes << DomainObjectWriter

        config.classes.addAll(grailsApplication.getArtefacts('Resource').toList().collect { (Class<?>) it.clazz })
        config.classes.addAll(grailsApplication.getArtefacts('Provider').toList().collect { (Class<?>) it.clazz })

        grailsApplication.mainContext.getBeansOfType(ResourceRegistrar).values().each {
            config.classes.addAll(it.getResourceClasses())
        }
    }

    @CompileDynamic
    void configureMappings() {
        List<String> mappings = retrieveMappingsList()

        if (log.isTraceEnabled()) {
            mappings.each {
                log.trace("Setting URL mapping for jaxrs controller: ${it}")
            }
        }

        grailsUrlMappingsHolder.addMappings {
            mappings.each { pattern ->
                "${pattern}"(controller: "jaxrs")
            }
            // "/application.wadl"(controller: "jaxrs")
        }
    }

    /**
     * Returns the root path of the given resource class as defined by the {@link Path} annotation.
     *
     * If there is no {@link Path} annotation, <code>null</code> is returned.
     *
     * @param clazz Class to get the root resource path for.
     * @return The root resource path, or null.
     */
    Path getRootPath(Class<?> clazz) {
        if (!clazz) {
            return null
        }

        Path path = clazz.getAnnotation(Path)

        if (path) {
            return path
        }

        return getRootPath(clazz.getSuperclass())
    }

    /**
     * Returns a list of paths associated with any methods in the given class.
     *
     * @param clazz Class to scan for {@link Path} annotations on its methods.
     * @return List of paths.
     */
    List<Path> getResourcePaths(Class<?> clazz) {
        if (!clazz) {
            return []
        }

        List<Path> paths = []

        clazz.getMethods().toList().each { method ->
            if (isJaxrsResource(method)) {
                paths.add(method.getAnnotation(Path))
            }
        }

        paths.addAll(getResourcePaths(clazz.getSuperclass()))

        return paths
    }

    /**
     * Build a URL mapping string from the given root path and method path.
     *
     * @param root Path annotation assigned at the class level.
     * @param method Path annotation assigned at the method level.
     * @return A compiled URL mapping.
     */
    String buildMappingFromPath(Path root, Path method) {
        return "${root?.value() ?: ''}/${method?.value() ?: ''}"
            .replaceAll(/\/+/, '/').replaceAll(/\{[^}]*\}/, '*').replaceAll(/\/$/, '')
    }

    /**
     * Returns any extra provider initialization parameters configured by the application.
     *
     * @param application
     * @return
     */
    @CompileDynamic
    Map<String, String> getProviderInitParameters() {
        ConfigObject config = grailsApplication.config.org.grails.jaxrs.provider.init.parameters

        return config.flatten()
    }

    /**
     * Determines whether the given {@link Method} is a JAX-RS resource.
     *
     * @param method Method to check.
     * @return Whether the given {@link Method} is a JAX-RS resource.
     */
    boolean isJaxrsResource(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().getAnnotation(HttpMethod)) {
                return true
            }
        }
        return false
    }
}
