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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Factory class for providing instances of {@link javax.xml.bind.Unmarshaller}
 * for specific classes.
 * </p>
 *
 * <p>
 * This class leverages caching to avoid repeated creation of
 * {@link javax.xml.bind.Unmarshaller} instances for the same class, improving
 * performance when unmarshalling XML to Java objects.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * <pre>
 * {@code
 * Unmarshaller unmarshaller = UnmarshallerFactory.getInstance(MyClass.class);
 * MyClass obj = (MyClass) unmarshaller.unmarshal(new StringReader(xmlString));
 * }
 *
 * This class does not provide a default constructor as it is designed
 * to be instantiated with specific parameters. The absence of a default
 * constructor ensures that instances of this class are created in a valid state.
 *
 * </pre>
 *
 * @see Unmarshaller
 * @see JAXBContext
 * @see JAXBException
 * @since 1.0
 * @version 1.0
 * @author hoangtien2k3
 */
public class UnmarshallerFactory {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(UnmarshallerFactory.class);

    /**
     * Cache to hold {@link Unmarshaller} instances for specific classes.
     */
    private static final Map<Class<?>, Unmarshaller> instance = new ConcurrentHashMap<>();

    /**
     * <p>
     * Provides an {@link javax.xml.bind.Unmarshaller} instance for the specified
     * class. If an {@link javax.xml.bind.Unmarshaller} has already been created for
     * the class, it is retrieved from the cache. Otherwise, a new instance is
     * created and cached.
     * </p>
     *
     * @param clz
     *            the {@link java.lang.Class} type for which to retrieve an
     *            {@link javax.xml.bind.Unmarshaller}
     * @return an {@link javax.xml.bind.Unmarshaller} instance configured for the
     *         specified class, or {@code null} if an error occurs during
     *         instantiation
     */
    public static Unmarshaller getInstance(Class<?> clz) {
        Objects.requireNonNull(clz, "Class must not be null");
        return instance.computeIfAbsent(clz, key -> {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(key);
                return jaxbContext.createUnmarshaller();
            } catch (JAXBException e) {
                log.error("Failed to create Unmarshaller for class: {}", key.getName(), e);
                return null;
            }
        });
    }
}
