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
package org.grails.plugins.jaxrs

import org.grails.plugins.jaxrs.provider.MessageBodyWriterSupport
import spock.lang.Specification

import javax.ws.rs.core.MultivaluedMap

/**
 * @author Martin Krasser
 * @author Bud Byrd
 */
class ProviderSupportSpec extends Specification {
    class TestX {

    }

    class TestY extends TestX {

    }

    def 'Ensure provider support with inheritance'() {
        setup:
        MessageBodyWriterSupport provider = new MessageBodyWriterSupport<TestX>() {
            protected void writeTo(TestX t, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) {
                void
            }
        }

        expect:
        provider.isSupported(TestX, null, null, null)
        provider.isSupported(TestY, null, null, null)
        !provider.isSupported(ArrayList, null, null, null)
    }

    def 'Ensure provider support without inheritance'() {
        setup:
        MessageBodyWriterSupport provider = new MessageBodyWriterSupport<TestY>() {
            protected void writeTo(TestY t, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) {
                void
            }
        }

        expect:
        !provider.isSupported(TestX, null, null, null)
        provider.isSupported(TestY, null, null, null)
        !provider.isSupported(ArrayList, null, null, null)
    }

    def 'Ensure provider support for Lists'() {
        setup:
        MessageBodyWriterSupport provider = new MessageBodyWriterSupport<List<TestX>>() {
            protected void writeTo(List<TestX> t, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) {
                void
            }
        }

        expect:
        !provider.isSupported(TestX, null, null, null)
        !provider.isSupported(TestY, null, null, null)
        provider.isSupported(ArrayList, null, null, null)
    }
}
