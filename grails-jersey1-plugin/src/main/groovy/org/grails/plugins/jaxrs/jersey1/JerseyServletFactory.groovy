package org.grails.plugins.jaxrs.jersey1

import com.sun.jersey.api.core.DefaultResourceConfig
import com.sun.jersey.api.core.PackagesResourceConfig
import com.sun.jersey.api.core.ResourceConfig
import com.sun.jersey.spi.container.servlet.WebConfig
import com.sun.jersey.spi.spring.container.servlet.SpringServlet
import grails.core.GrailsApplication
import org.grails.plugins.jaxrs.core.JaxrsApplicationConfig
import org.grails.plugins.jaxrs.core.JaxrsServletConfig
import org.grails.plugins.jaxrs.servlet.ServletFactory

import javax.servlet.Servlet
import javax.servlet.ServletException

/**
 * A servlet factory that handles the Jersey 1.x JAX-RS implementation.
 *
 * @author Bud Byrd
 */
class JerseyServletFactory implements ServletFactory {
    /**
     * Grails Application bean.
     */
    GrailsApplication grailsApplication

    /**
     * Create the servlet instance for the implementation-specific provider.
     *
     * @param applicationConfig
     * @param servletConfig
     * @return
     */
    @Override
    Servlet createServlet(JaxrsApplicationConfig applicationConfig, JaxrsServletConfig servletConfig) {
        setupServletConfig(servletConfig)

        return new SpringServlet() {
            @Override
            protected ResourceConfig getDefaultResourceConfig(Map<String, Object> props, WebConfig webConfig) throws ServletException {
                return new DefaultResourceConfig(applicationConfig.getClasses())
            }
        }
    }

    /**
     * Returns the fully qualified name of the class that will serve as the runtime delegate
     * for JAX-RS.
     *
     * @return
     */
    @Override
    String getRuntimeDelegateClassName() {
        return "com.sun.jersey1.server.impl.provider.RuntimeDelegateImpl"
    }

    /**
     * Add additional servlet configuration options specific to Jersey.
     *
     * @param servletConfig
     */
    void setupServletConfig(JaxrsServletConfig servletConfig) {
        String extra = getProviderExtraPaths()

        if (extra && !servletConfig.getInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES)) {
            servletConfig.getInitParameters().put(PackagesResourceConfig.PROPERTY_PACKAGES, extra)
        }
    }

    /**
     * Returns any extra classpaths configured by the application.
     *
     * @param application
     * @return
     */
    String getProviderExtraPaths() {
        // TODO: change this path to be jersey1-specific
        def paths = grailsApplication.config.org.grails.jaxrs.provider.extra.paths

        if (!(paths instanceof String) || !paths) {
            return null
        }

        return paths
    }
}
