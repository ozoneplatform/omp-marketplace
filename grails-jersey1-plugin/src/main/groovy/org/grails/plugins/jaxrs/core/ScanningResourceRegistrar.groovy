package org.grails.plugins.jaxrs.core

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils

import javax.ws.rs.Path
import javax.ws.rs.ext.Provider

/**
 * An implementation of {@link ResourceRegistrar} that provides classpath scanning abilities to the registrar.
 */
class ScanningResourceRegistrar implements ResourceRegistrar {
    /**
     * A list of classpath packages to scan for resource classes.
     */
    private List<String> packages = []

    /**
     * Constructor that takes a single classpath to scan in.
     *
     * @param pkg Name of the classpath to scan.
     */
    ScanningResourceRegistrar(String pkg) {
        packages.add(pkg)
    }

    /**
     * Constructor that takes a list of classpaths to scan in.
     *
     * @param pkgs List of names of classpaths to scan.
     */
    ScanningResourceRegistrar(List<String> pkgs) {
        packages.addAll(pkgs)
    }
    /**
     * Provides a list of classes that should be registered as JAX-RS resources, including both
     * root resources and reader/writer providers.
     *
     * @return A list of resources to register with the {@link JaxrsContext}.
     */
    @Override
    List<Class<?>> getResourceClasses() {
        return scan()
    }

    /**
     * Scans for and returns all JAX-RS resources in the configured classpath packages.
     *
     * @return All found JAX-RS resources.
     */
    private List<Class<?>> scan() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path))
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider))

        List<Class<?>> classes = []

        packages.each {
            classes.addAll(scanner.findCandidateComponents(it).collect {
                ClassUtils.resolveClassName(it.getBeanClassName(), ClassUtils.getDefaultClassLoader())
            })
        }

        return classes
    }
}
