/*
 * Copyright 2009 - 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugins.jaxrs

import grails.test.mixin.TestFor
import org.grails.plugins.jaxrs.core.JaxrsApplicationConfig
import org.grails.plugins.jaxrs.core.JaxrsContext
import org.grails.plugins.jaxrs.core.JaxrsServletConfig
import org.grails.plugins.jaxrs.core.JaxrsUtil
import org.grails.plugins.jaxrs.servlet.ServletFactory
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification

import javax.servlet.Servlet
import javax.servlet.ServletConfig
import javax.servlet.ServletContext
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Martin Krasser
 * @author Bud Byrd
 */
@TestFor(JaxrsController)
class JaxrsControllerSpec extends Specification {
    JaxrsContext jaxrsContext
    JaxrsUtil jaxrsUtil

    def setup() {
        ServletFactory servletFactory = new ServletFactory() {
            @Override
            Servlet createServlet(JaxrsApplicationConfig applicationConfig, JaxrsServletConfig servletConfig) {
                return new HttpServlet() {
                    void service(HttpServletRequest request, HttpServletResponse response) {
                        MockHttpServletResponse mockResponse = (MockHttpServletResponse) response
                        mockResponse.writer.println(request.requestURI)
                        mockResponse.writer.flush()
                    }

                    void init(ServletConfig config) {
                        // nothing to do
                    }
                }
            }

            @Override
            String getRuntimeDelegateClassName() {
                return 'foo.bar'
            }
        }

        ServletContext servletContext = new MockServletContext()

        jaxrsContext = new JaxrsContext()
        jaxrsContext.jaxrsServletFactory = servletFactory
        jaxrsContext.servletContext = servletContext
        controller.jaxrsContext = jaxrsContext

        jaxrsUtil = new JaxrsUtil()
        controller.jaxrsUtil = jaxrsUtil

        jaxrsContext.init()
    }

    def 'Ensure requests get handed off correctly'() {
        setup:
        controller.request.method = 'GET'
        controller.request.setAttribute(JaxrsUtil.REQUEST_URI_ATTRIBUTE_NAME, '/test')

        when:
        controller.handle()

        then:
        controller.response.contentAsString.trim() == '/test'
    }
}

