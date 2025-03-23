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
package com.reactify.filter.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 * <p>
 * Implementation of the
 * {@link org.springframework.web.reactive.function.BodyInserter.Context}
 * interface, providing a context for inserting a body into a WebFlux HTTP
 * request.
 * </p>
 *
 * <p>
 * This class is responsible for managing the
 * {@link org.springframework.web.reactive.function.client.ExchangeStrategies}
 * used for writing HTTP messages, as well as providing default values for the
 * message writers, server request, and hints.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * BodyInserterContext context = new BodyInserterContext();
 * List<HttpMessageWriter<?>> writers = context.messageWriters();
 * }
 * </pre>
 *
 * @see BodyInserter.Context
 * @see ExchangeStrategies
 * @since 1.0
 * @version 1.0
 * @author hoangtien2k3
 */
public class BodyInserterContext implements BodyInserter.Context {

    /**
     * The {@link ExchangeStrategies} instance that defines how HTTP message writers
     * and readers are configured in this context.
     */
    private final ExchangeStrategies exchangeStrategies;

    /**
     * <p>
     * Default constructor for {@code BodyInserterContext} that initializes the
     * context with default
     * {@link org.springframework.web.reactive.function.client.ExchangeStrategies}.
     * </p>
     */
    public BodyInserterContext() {
        this.exchangeStrategies = ExchangeStrategies.withDefaults();
    }

    /**
     * <p>
     * Constructor for {@code BodyInserterContext} that allows specifying custom
     * {@link org.springframework.web.reactive.function.client.ExchangeStrategies}.
     * </p>
     *
     * @param exchangeStrategies
     *            an
     *            {@link org.springframework.web.reactive.function.client.ExchangeStrategies}
     *            object defining the strategies for HTTP message handling
     */
    public BodyInserterContext(ExchangeStrategies exchangeStrategies) {
        this.exchangeStrategies = exchangeStrategies;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Returns the list of {@link HttpMessageWriter} instances available for writing
     * HTTP messages.
     * </p>
     */
    @NotNull
    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return exchangeStrategies.messageWriters();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Provides an empty {@link Optional} as there is no server request associated
     * with this context.
     * </p>
     */
    @NotNull
    @Override
    public Optional<ServerHttpRequest> serverRequest() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Returns an empty map of hints. This implementation does not provide any
     * additional hints for message writers or readers.
     * </p>
     */
    @NotNull
    @Override
    public Map<String, Object> hints() {
        return Collections.emptyMap();
    }
}
