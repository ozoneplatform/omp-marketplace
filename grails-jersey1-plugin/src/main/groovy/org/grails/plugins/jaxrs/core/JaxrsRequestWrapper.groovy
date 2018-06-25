/*
 * Copyright 2009 the original author or authors.
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
package org.grails.plugins.jaxrs.core

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * A request wrapper that decorates {@link HttpServletRequest#getRequestURI()}.
 *
 * @author Martin Krasser
 *
 * @see JaxrsFilter
 */
class JaxrsRequestWrapper extends HttpServletRequestWrapper {
    /**
     * Constructor.
     *
     * @param request
     */
    JaxrsRequestWrapper(HttpServletRequest request) {
        super(request)
    }

    /**
     * Returns the request URI based on the attribute written by
     * {@link JaxrsFilter}.
     *
     * @return the request URI.
     *
     * @see JaxrsUtil#REQUEST_URI_ATTRIBUTE_NAME
     */
    @Override
    String getRequestURI() {
        return (String) getAttribute(JaxrsUtil.REQUEST_URI_ATTRIBUTE_NAME)
    }

    /**
     * Always returns the empty string.
     *
     * @return an empty string.
     */
    @Override
    String getServletPath() {
        return ''
    }
}
