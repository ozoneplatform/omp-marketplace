package org.grails.plugins.jaxrs.servlet

import org.grails.plugins.jaxrs.core.JaxrsApplicationConfig
import org.grails.plugins.jaxrs.core.JaxrsServletConfig

import javax.servlet.Servlet

interface ServletFactory {
    /**
     * Create the servlet instance for the implementation-specific provider.
     *
     * @param applicationConfig
     * @param servletConfig
     * @return
     */
    Servlet createServlet(JaxrsApplicationConfig applicationConfig, JaxrsServletConfig servletConfig)

    /**
     * Returns the fully qualified name of the class that will serve as the runtime delegate
     * for JAX-RS.
     *
     * @return
     */
    String getRuntimeDelegateClassName()
}
