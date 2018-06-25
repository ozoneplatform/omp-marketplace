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
package org.grails.plugins.jaxrs.artefact

import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.MessageBodyWriter
import java.lang.annotation.Annotation
import java.lang.reflect.Method

/**
 * Provides condition methods to determine whether a given class is a valid
 * JAX-RS class.
 *
 * @author Martin Krasser
 */
class JaxrsClassHelper {
    /**
     * Determines whether a given annotation can be considered a JAX-RS annotation.
     *
     * @param annotation
     * @return
     */
    static boolean jaxrsAnnotationCondition(Annotation annotation) {
        return annotation.toString().contains('javax.ws.rs')
    }

    /**
     * Determiunes whether a class has any JAX-RS annotations.
     *
     * @param clazz
     * @return
     */
    static boolean jaxrsClassCondition(Class<?> clazz) {
        return clazz.declaredAnnotations.any { jaxrsAnnotationCondition it }
    }

    /**
     * Determines whether a method has any JAX-RS annotations.
     *
     * @param method
     * @return
     */
    static boolean jaxrsMethodCondition(Method method) {
        return method.declaredAnnotations.any { jaxrsAnnotationCondition it }
    }

    /**
     * Determines whether a class has any methods that have any JAX-RS annotations.
     *
     * @param clazz
     * @return
     */
    static boolean jaxrsMethodsCondition(Class<?> clazz) {
        return clazz.declaredMethods.any { jaxrsMethodCondition it }
    }

    /**
     * Returns <code>true</code> if the given class is a valid JAX-RS class,
     * <code>false</code> otherwise. A class in considered a JAX-RS class if
     * there is either a JAX-RS annotation present on class-level or on
     * method-level and none of the JAX-RS provider interfaces is implemented.
     *
     * @param clazz class to be checked.
     * @return
     */
    static boolean isJaxrsResource(Class clazz) {
        !isJaxrsProvider(clazz) && walkJaxrsResource(clazz, JaxrsClassHelper.&isJaxrsNode)
    }

    /**
     * Returns whether the given class has JAX-RS annotations or has methods
     * that have JAX-RS annotations.
     *
     * @param clazz
     * @return
     */
    static boolean isJaxrsNode(Class clazz) {
        jaxrsClassCondition(clazz) || jaxrsMethodsCondition(clazz)
    }

    /**
     * Returns <code>true</code> if the <code>clazz</code> either implements
     * {@link MessageBodyReader}, {@link MessageBodyWriter} or
     * {@link ExceptionMapper}.
     *
     * @param clazz
     * @return <code>true</code> if the class is a JAX-RS provider.
     */
    static boolean isJaxrsProvider(Class clazz) {
        (MessageBodyReader.isAssignableFrom(clazz)) ||
            (MessageBodyWriter.isAssignableFrom(clazz)) ||
            (ExceptionMapper.isAssignableFrom(clazz))
    }

    /**
     * Walks up the class and interface hierarchy applying of <code>clazz</code>
     * applying <code>visitor</code> as predicate. Whenever the visitor
     * returns <code>true</code> the walk is stopped and <code>true</code> is
     * returned by this method, <code>false</code> otherwise.
     *
     * @param clazz class from where the hierarchy walk is started.
     * @param visitor visitor applied to each element in the hierarchy.
     */
    private static boolean walkJaxrsResource(Class clazz, Closure visitor) {
        if (!clazz) {
            return false
        }

        return (visitor(clazz) ||
            walkJaxrsResource(clazz.superclass, visitor) ||
            clazz.interfaces.any { walkJaxrsResource(it, visitor) }
        )
    }
}
