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
package com.reactify.filter.http;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * Implementation of
 * {@link org.springframework.http.client.reactive.ClientHttpRequest} that
 * caches the body of the HTTP response for later use.
 * </p>
 *
 * <p>
 * This class allows the body of a response to be stored in memory, enabling it
 * to be accessed multiple times if necessary. It works with reactive
 * programming constructs, specifically with the Project Reactor's
 * {@link reactor.core.publisher.Flux} and {@link reactor.core.publisher.Mono}.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * CachedBodyOutputMessage response = new CachedBodyOutputMessage(exchange, headers);
 * response.writeWith(dataBufferPublisher);
 * }
 * </pre>
 *
 * @author hoangtien2k3
 */
public class CachedBodyOutputMessage implements ReactiveHttpOutputMessage {

    private final DataBufferFactory bufferFactory;
    private final HttpHeaders httpHeaders;

    /**
     * Flag indicating whether the body has been cached.
     */
    private boolean cached = false;

    /**
     * The body of the HTTP response, stored as a {@link Flux<DataBuffer>}.
     * Initially, it is set to an error state until the body is defined.
     */
    private Flux<DataBuffer> body =
            Flux.error(new IllegalStateException("The body is not set. Did handling complete with success?"));

    public Flux<DataBuffer> getBody() {
        return body;
    }

    /**
     * <p>
     * Constructor for {@code CachedBodyOutputMessage}.
     * </p>
     *
     * <p>
     * Initializes the message with the provided
     * {@link org.springframework.web.server.ServerWebExchange} and
     * {@link org.springframework.http.HttpHeaders}.
     * </p>
     *
     * @param exchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     *            to retrieve the response's buffer factory
     * @param httpHeaders
     *            a {@link org.springframework.http.HttpHeaders} object containing
     *            the headers for the HTTP response
     */
    public CachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
        this.bufferFactory = exchange.getResponse().bufferFactory();
        this.httpHeaders = httpHeaders;
    }

    /** {@inheritDoc} */
    @Override
    public void beforeCommit(@NotNull Supplier<? extends Mono<Void>> action) {}

    /** {@inheritDoc} */
    @Override
    public boolean isCommitted() {
        return false;
    }

    /**
     * Checks whether the body has been cached.
     *
     * @return {@code true} if the body has been cached; {@code false} otherwise
     */
    boolean isCached() {
        return this.cached;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public DataBufferFactory bufferFactory() {
        return this.bufferFactory;
    }

    /**
     * {@inheritDoc}
     *
     * Writes the provided body to this output message and caches it.
     */
    @NotNull
    public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
        this.body = Flux.from(body);
        this.cached = true;
        return Mono.empty();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Mono<Void> writeAndFlushWith(@NotNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body).flatMap(p -> p));
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Mono<Void> setComplete() {
        return writeWith(Flux.empty());
    }
}
