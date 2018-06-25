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
package org.grails.plugins.jaxrs.provider

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyReader
import java.lang.annotation.Annotation
import java.lang.reflect.Type

import static org.grails.plugins.jaxrs.provider.ProviderUtils.getReaderTypeArgument

/**
 * Base class for simple message body readers.
 *
 * @param < T >  type of object to be read from the request entity.
 * @author Martin Krasser
 */
abstract class MessageBodyReaderSupport<T> extends ProviderSupport implements MessageBodyReader<T> {

    MessageBodyReaderSupport() {
        setTypeArgument(getReaderTypeArgument(this))
    }

    /**
     * @see #isSupported(Class, Type, Annotation [ ], MediaType)
     */
    boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isSupported(type, genericType, annotations, mediaType)
    }

    T readFrom(Class<T> type, Type genericType,
                      Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
        throws IOException, WebApplicationException {
        return readFrom(httpHeaders, entityStream)
    }

    /**
     * Reads the request entity from the input stream and returns an object of
     * type given by this class type parameter.
     *
     * @param httpHeaders HTTP request headers.
     * @param entityStream request entity input stream.
     * @return object representation of entity stream.
     * @throws IOException
     * @throws WebApplicationException
     */
    abstract T readFrom(MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
        throws IOException, WebApplicationException
}
