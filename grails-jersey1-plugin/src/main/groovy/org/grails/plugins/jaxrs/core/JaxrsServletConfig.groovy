package org.grails.plugins.jaxrs.core

import javax.servlet.ServletConfig
import javax.servlet.ServletContext

/**
 * A servlet configuration that allows for modification of the class list.
 *
 * @author Bud Byrd
 */
class JaxrsServletConfig implements ServletConfig {
    /**
     * Servlet initialization parameters.
     */
    final String servletName

    /**
     * The name to use for the servlet.
     */
    final ServletContext servletContext

    /**
     * A semicolor-separated list of classpaths to include for resource and provider scanning.
     */
    final Hashtable<String, String> initParameters

    /**
     * Constructor.
     *
     * @param servletName
     * @param extraClassPaths
     * @param initParameters
     */
    JaxrsServletConfig(ServletContext servletContext, String servletName, Hashtable<String, String> initParameters) {
        this.servletContext = servletContext
        this.servletName = servletName
        this.initParameters = initParameters
    }

    /**
     * Returns the value of a given initialization parameter.
     *
     * @param name
     * @return
     */
    String getInitParameter(String name) {
        return initParameters.get(name)
    }

    /**
     * Returns the enumeration of all initialization parameters.
     *
     * @return
     */
    Enumeration<String> getInitParameterNames() {
        return initParameters.keys()
    }
}
