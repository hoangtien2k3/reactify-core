/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reactify.factory;

import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * <p>
 * A factory class for managing and caching JAXB
 * {@link javax.xml.bind.Marshaller} instances, providing functionality to
 * convert Java objects to XML format.
 * </p>
 *
 * <p>
 * This class maintains a cache of marshaller instances for different classes to
 * avoid the cost of repeatedly creating new instances. It enables pretty-print
 * formatting of XML output by default.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * String xmlOutput = MarshallerFactory.convertObjectToXML(myObject, MyClass.class);
 * System.out.println(xmlOutput);
 * }
 * </pre>
 *
 * @author hoangtien2k3
 * @since 1.0
 * @version 1.0
 */
public class MarshallerFactory {

    private static final Logger log = Logger.getLogger(MarshallerFactory.class.getName());

    /**
     * Cache to hold {@link MarshallerFactory} instances for specific classes.
     */
    private static final Map<Class<?>, Marshaller> instance = new ConcurrentHashMap<>();

    /**
     * Converts a given Java object to its XML representation.
     *
     * <p>
     * This method checks if a cached {@link javax.xml.bind.Marshaller} instance is
     * available for the provided class. If not, it creates a new marshaller, caches
     * it, and then converts the object to XML format. The XML output is formatted
     * for readability.
     * </p>
     *
     * <p>
     * If the conversion fails due to a {@link javax.xml.bind.JAXBException}, an
     * error message is logged, and an empty string is returned.
     * </p>
     *
     * @param obj
     *            the object to be converted to XML
     * @param cls
     *            the {@link java.lang.Class} of the object being converted, used
     *            for creating and retrieving the appropriate
     *            {@link javax.xml.bind.Marshaller} instance
     * @return a {@link java.lang.String} representing the XML format of the object,
     *         or an empty string if an error occurs during conversion
     * @throws IllegalArgumentException
     *             if {@code obj} or {@code cls} is null
     */
    public static String convertObjectToXML(Object obj, Class<?> cls) {
        Objects.requireNonNull(obj, "Object to convert must not be null");
        Objects.requireNonNull(cls, "Class of the object must not be null");

        try {
            Marshaller marshaller = instance.computeIfAbsent(cls, key -> {
                try {
                    Marshaller m = JAXBContext.newInstance(key).createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    return m;
                } catch (JAXBException ex) {
                    log.log(Level.SEVERE, "Failed to create marshaller for class: " + key.getName(), ex);
                    throw new RuntimeException("Failed to create marshaller", ex);
                }
            });

            StringWriter sw = new StringWriter();
            marshaller.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            log.log(Level.SEVERE, "Failed to convert object to XML", ex);
            return "";
        } catch (RuntimeException ex) {
            log.log(Level.SEVERE, "Unexpected error during XML conversion", ex);
            return "";
        }
    }
}
