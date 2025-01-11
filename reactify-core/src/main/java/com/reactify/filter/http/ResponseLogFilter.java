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

import static reactor.core.scheduler.Schedulers.single;

import com.reactify.model.GatewayContext;
import com.reactify.util.DataUtil;
import com.reactify.util.LogUtils;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The ResponseLogFilter class is a WebFilter implementation that logs the
 * response body of HTTP requests in a Spring WebFlux application. It decorates
 * the server response to intercept the response body and log it before sending
 * it to the client.
 * </p>
 *
 * <p>
 * This class utilizes Project Reactor's Mono and Flux to handle the response
 * body in a non-blocking way. It also provides functionality to adapt the
 * response for further processing.
 * </p>
 *
 * <p>
 * The maximum memory size for response data is set to 50 MB, ensuring efficient
 * handling of large responses.
 * </p>
 *
 * @author hoangtien2k3
 */
@Log4j2
@Component
public class ResponseLogFilter implements WebFilter, Ordered {
    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(cl -> cl.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
            .build();

    /**
     * Constructs a new instance of {@code ResponseLogFilter}.
     */
    public ResponseLogFilter() {}

    /**
     * Converts an InputStream to a byte array.
     *
     * @param inStream
     *            the InputStream to be converted
     * @return byte array containing the data from the InputStream
     */
    private static byte[] toByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception ignore) {

        }
        return in_b;
    }

    /**
     * {@inheritDoc}
     *
     * Filters the server exchange, logging the response body if it is of a legal
     * media type.
     */
    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && !gatewayContext.getReadResponseData()) {
            log.debug("[ResponseLogFilter]Properties Set Not To Read Response Data");
            return chain.filter(exchange);
        }
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @NotNull
            @Override
            public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
                final MediaType contentType = super.getHeaders().getContentType();
                if (LogUtils.legalLogMediaTypes.contains(contentType)) {
                    if (body instanceof Mono) {
                        final Mono<DataBuffer> monoBody = (Mono<DataBuffer>) body;
                        return super.writeWith(
                                monoBody.publishOn(single()).map(buffer -> logRequestBody(buffer, exchange)));
                    } else if (body instanceof Flux) {
                        final Flux<DataBuffer> monoBody = (Flux<DataBuffer>) body;
                        return super.writeWith(
                                monoBody.publishOn(single()).map(buffer -> logRequestBody(buffer, exchange)));
                    }
                }
                return super.writeWith(body);
            }

            @NotNull
            @Override
            public Mono<Void> writeAndFlushWith(@NotNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    /**
     * Logs the response body by converting it to a byte array and setting it in the
     * GatewayContext.
     *
     * @param buffer
     *            the DataBuffer containing the response body
     * @param exchange
     *            the current server exchange
     * @return a wrapped DataBuffer containing the logged response body
     */
    private DataBuffer logRequestBody(DataBuffer buffer, ServerWebExchange exchange) {
        InputStream dataBuffer = buffer.asInputStream();
        byte[] bytes = toByteArray(dataBuffer);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (!DataUtil.isNullOrEmpty(gatewayContext)) {
            gatewayContext.setResponseBody(new String(bytes));
        }
        DataBufferUtils.release(buffer);
        return nettyDataBufferFactory.wrap(bytes);
    }

    /**
     * {@inheritDoc}
     *
     * Returns the order value of this filter.
     */
    @Override
    public int getOrder() {
        return 6;
    }

    /**
     * ResponseAdapter class is a wrapper around the response to provide a reactive
     * stream representation of the response body along with the headers, status
     * code, and cookies.
     */
    public static class ResponseAdapter implements ClientHttpResponse {
        private final Flux<DataBuffer> flux;
        private final HttpHeaders headers;
        private final HttpStatus statusCode;
        private final MultiValueMap<String, ResponseCookie> cookies;

        /**
         * Constructs a ResponseAdapter with the specified parameters.
         *
         * @param body
         *            the response body as a Publisher
         * @param headers
         *            the response headers
         * @param statusCode
         *            the HTTP status code
         * @param cookies
         *            the response cookies
         */
        public ResponseAdapter(
                Publisher<? extends DataBuffer> body,
                HttpHeaders headers,
                HttpStatus statusCode,
                MultiValueMap<String, ResponseCookie> cookies) {
            this.headers = headers;
            this.statusCode = statusCode;
            this.cookies = cookies;
            if (body instanceof Flux) {
                flux = (Flux<DataBuffer>) body;
            } else {
                flux = ((Mono<DataBuffer>) body).flux();
            }
        }

        /**
         * Returns the response body as a Flux of DataBuffer.
         *
         * @return the response body
         */
        @NotNull
        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        /**
         * Returns the response headers.
         *
         * @return the response headers
         */
        @NotNull
        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         * Returns the HTTP status code of the response.
         *
         * @return the HTTP status code
         */
        @NotNull
        @Override
        public HttpStatus getStatusCode() {
            return statusCode;
        }

        /**
         * Returns the response cookies.
         *
         * @return the response cookies
         */
        @NotNull
        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return cookies;
        }
    }
}
