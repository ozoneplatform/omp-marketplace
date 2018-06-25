/**
 * Copyright 2012 Bud Byrd
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
 *
 * Modified from original source at: https://automation-dude.com/jax-rs-and-hashmaps/
 **/

package marketplace.rest

import groovy.transform.CompileStatic
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider
import java.lang.annotation.Annotation
import java.lang.reflect.Type

import grails.converters.JSON
import grails.converters.XML
import grails.core.GrailsApplication
import org.grails.plugins.jaxrs.provider.ProviderUtils
import org.grails.web.converters.Converter

import static org.grails.plugins.jaxrs.provider.ConverterUtils.getDefaultJSONEncoding
import static org.grails.plugins.jaxrs.provider.ConverterUtils.getDefaultXMLEncoding


@Provider
@Produces(['text/xml', 'application/xml', 'text/json', 'application/json'])
@CompileStatic
class HashMapWriter implements MessageBodyWriter<Object> {

    GrailsApplication grailsApplication

    boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        isXmlOrJsonType(mediaType) && HashMap.isAssignableFrom(type)
    }

    @Override
    void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations,
                 MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException
    {
        OutputStreamWriter writer
        Converter converter
        if (ProviderUtils.isXmlType(mediaType)) {
            writer = new OutputStreamWriter(entityStream, getDefaultXMLEncoding(grailsApplication))
            converter = new XML(object)
        }
        else {
            writer = new OutputStreamWriter(entityStream, getDefaultJSONEncoding(grailsApplication))
            converter = new JSON(object)
        }
        converter.render(writer)
    }

    @Override
    long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        -1
    }

    private static boolean isXmlOrJsonType(MediaType mediaType) {
        ProviderUtils.isXmlType(mediaType) || ProviderUtils.isJsonType(mediaType)
    }

}
