/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
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
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class MarshallerFactory {

    private static final Map<Class<?>, Marshaller> instance = new HashMap<>();

    /**
     * Constructs a new instance of {@code MarshallerFactory}.
     */
    public MarshallerFactory() {}

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
     * @throws java.lang.IllegalArgumentException
     *             if {@code obj} or {@code cls} is null
     */
    public static String convertObjectToXML(Object obj, Class<?> cls) {
        Marshaller marshaller = instance.get(cls);
        String xmlTxt = "";
        try {
            // create an instance of `JAXBContext`
            if (marshaller == null) {
                marshaller = JAXBContext.newInstance(cls).createMarshaller();
                // enable pretty-print XML output
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                instance.put(cls, marshaller);
            }

            // write XML to `StringWriter`
            StringWriter sw = new StringWriter();

            // convert book object to XML
            marshaller.marshal(obj, sw);
            xmlTxt = sw.toString();

        } catch (JAXBException ex) {
            log.error("Convert Object To XML  fail: ", ex);
        }
        return xmlTxt;
    }
}
