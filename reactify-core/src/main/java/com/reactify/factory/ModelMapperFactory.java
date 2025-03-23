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

import org.modelmapper.ModelMapper;

/**
 * <p>
 * Factory class for providing a singleton instance of
 * {@link org.modelmapper.ModelMapper}.
 * </p>
 *
 * <p>
 * This class is designed to provide a single, globally accessible
 * {@link org.modelmapper.ModelMapper} instance, which can be reused across the
 * application for object mapping tasks. This approach optimizes performance by
 * avoiding the creation of multiple {@link org.modelmapper.ModelMapper}
 * instances.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * ModelMapper mapper = ModelMapperFactory.getInstance();
 * DestinationObject dest = mapper.map(sourceObject, DestinationObject.class);
 * }
 * </pre>
 *
 * <p>
 * This singleton instance is thread-safe as the
 * {@link org.modelmapper.ModelMapper} class itself is designed for concurrent
 * use.
 * </p>
 *
 * @see ModelMapper
 * @see <a href="https://modelmapper.org/">ModelMapper Documentation</a>
 * @since 1.0
 * @version 1.0
 * @author hoangtien2k3
 */
public class ModelMapperFactory {

    /**
     * A singleton instance of {@link ModelMapper} used throughout the application
     * for mapping between different object models.
     */
    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * Provides access to the singleton {@link org.modelmapper.ModelMapper}
     * instance.
     *
     * <p>
     * This method allows any part of the application to retrieve the singleton
     * {@link org.modelmapper.ModelMapper} instance, promoting consistency and
     * efficiency in object mapping.
     * </p>
     *
     * @return a {@link org.modelmapper.ModelMapper} instance, used for mapping
     *         between object types
     */
    public static ModelMapper getInstance() {
        return modelMapper;
    }
}
