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

import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware

import javax.ws.rs.Consumes
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.Provider
import java.lang.annotation.Annotation
import java.lang.reflect.Type

import static org.grails.plugins.jaxrs.provider.ConverterUtils.getDefaultEncoding
import static org.grails.plugins.jaxrs.provider.ConverterUtils.xmlToMap

/**
 * A message body reader that converts an XML entity stream to a map than can be
 * used to construct Grails domain objects. Any JAX-RS resource method that
 * defines a {@link Map} as parameter and consumes either <code>text/xml</code>
 * or <code>application/xml</code> will be passed a map created from an XML
 * request entity:
 * <p/>
 * <p/>
 * <pre>
 * &#064;Path('/notes')
 * &#064;Produces('text/xml')
 * class NotesResource {*
 *      &#064;POST
 *      &#064;Consumes('text/xml')
 *      Response addNote(Map properties) {*          // create ne Note domain object
 *          def note = new Note(properties).save()
 *}*
 *}*
 *
 * </pre>
 *
 * @author Martin Krasser
 */
@Provider
@Consumes(["text/xml", "application/xml"])
class XMLReader extends MessageBodyReaderSupport<Map> implements GrailsApplicationAware {

    private GrailsApplication grailsApplication

    void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
    }

    @Override
    Map readFrom(Class<Map> type, Type genericType,
                 Annotation[] annotations, MediaType mediaType,
                 MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
        throws IOException, WebApplicationException {

        String encoding = ConverterUtils.getEncoding(httpHeaders, mediaType, getDefaultEncoding(grailsApplication))

        // Convert XML to map used for constructing domain objects
        return xmlToMap(entityStream, encoding)
    }

    @Override
    Map readFrom(MultivaluedMap<String, String> httpHeaders,
                 InputStream entityStream) throws IOException,
        WebApplicationException {
        // TODO: Fix MessageBodyReaderSupport abstract method
        throw new RuntimeException("This should never be called, because we override the readFrom(all-parameters) method.")
    }
}
