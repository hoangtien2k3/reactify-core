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

import com.reactify.constants.Constants;
import com.reactify.filter.properties.HttpLogProperties;
import com.reactify.model.GatewayContext;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * <p>
 * The {@code GatewayContextFilter} class implements a filter for processing
 * HTTP requests in a reactive web environment. It intercepts incoming requests
 * and extracts relevant context information, such as headers and request
 * bodies, for logging or further processing. This filter is intended for
 * non-production environments (as indicated by the @Profile annotation).
 * </p>
 *
 * <p>
 * The filter can conditionally enable or disable logging of request and
 * response data based on configuration properties specified in the
 * {@link com.reactify.filter.properties.HttpLogProperties} class. It also
 * supports handling of both JSON and form data content types.
 * </p>
 *
 * <p>
 * This filter is registered as a Spring component, allowing it to be
 * automatically discovered and applied to incoming requests.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@Log4j2
public class GatewayContextFilter implements WebFilter, Ordered {

    private final HttpLogProperties httpLogProperties;
    private final CodecConfigurer codecConfigurer;

    /**
     * Constructs a new instance of {@code GatewayContextFilter}.
     *
     * @param httpLogProperties
     *            the properties for logging HTTP requests and responses.
     * @param codecConfigurer
     *            the codec configurer for configuring message codecs.
     */
    public GatewayContextFilter(HttpLogProperties httpLogProperties, CodecConfigurer codecConfigurer) {
        this.httpLogProperties = httpLogProperties;
        this.codecConfigurer = codecConfigurer;
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves the order value for this filter. The order determines the sequence
     * in which filters are applied. This filter is set to the highest precedence.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * {@inheritDoc}
     *
     * Applies the filter logic to the incoming HTTP request.
     *
     * <p>
     * The filter checks if the current request's endpoint should be logged based on
     * the configuration properties. If logging is enabled, it collects relevant
     * request data and stores it in a {@link GatewayContext} instance.
     * </p>
     *
     * <p>
     * If the content type of the request is JSON, it will invoke the
     * {@link #readBody(ServerWebExchange, WebFilterChain, GatewayContext)} method
     * to read and process the JSON body. For form data, it calls
     * {@link #readFormData(ServerWebExchange, WebFilterChain, GatewayContext)}.
     * </p>
     */
    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        boolean enableRequest = httpLogProperties.getRequest().enable();
        boolean enableResponse = httpLogProperties.getResponse().enable();
        if (Constants.EXCLUDE_LOGGING_ENDPOINTS.contains(request.getPath().toString())
                || (!enableRequest && !enableResponse)) {
            return chain.filter(exchange);
        }
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setReadRequestData(httpLogProperties.getRequest().enable());
        gatewayContext.setReadResponseData(httpLogProperties.getResponse().enable());
        HttpHeaders headers = request.getHeaders();
        gatewayContext.setRequestHeaders(headers);
        gatewayContext.setStartTime(System.currentTimeMillis());
        if (!gatewayContext.getReadRequestData()) {
            exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
            log.debug("[GatewayContext]Properties Set To Not Read Request Data");
            return chain.filter(exchange);
        }
        gatewayContext.getAllRequestData().addAll(request.getQueryParams());
        /*
         * save gateway context into exchange
         */
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
        MediaType contentType = headers.getContentType();
        if (headers.getContentLength() > 0) {
            if (MediaType.APPLICATION_JSON.includes(contentType)) {
                return readBody(exchange, chain, gatewayContext);
            }
            if (MediaType.APPLICATION_FORM_URLENCODED.includes(contentType)) {
                return readFormData(exchange, chain, gatewayContext);
            }
        }
        log.debug("[GatewayContext]ContentType:{},Gateway context is set with {}", contentType, gatewayContext);
        return chain.filter(exchange);
    }

    /**
     * Reads and processes form data from the incoming request.
     *
     * <p>
     * This method retrieves the form data and adds it to the
     * {@link GatewayContext}. It then rewrites the request body with the encoded
     * form data, allowing further processing down the filter chain.
     * </p>
     *
     * @param exchange
     *            the current server exchange, containing the request and response.
     * @param chain
     *            the filter chain to continue processing the request.
     * @param gatewayContext
     *            the context object to store request data.
     * @return a {@link Mono<Void>} representing the completion of the form data
     *         processing.
     */
    private Mono<Void> readFormData(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return exchange.getFormData()
                .doOnNext(multiValueMap -> {
                    gatewayContext.setFormData(multiValueMap);
                    gatewayContext.getAllRequestData().addAll(multiValueMap);
                    log.debug("[GatewayContext]Read FormData Success");
                })
                .then(Mono.defer(() -> {
                    MediaType contentType = headers.getContentType();
                    Charset charset = (contentType != null && contentType.getCharset() != null)
                            ? contentType.getCharset()
                            : StandardCharsets.UTF_8;
                    MultiValueMap<String, String> formData = gatewayContext.getFormData();
                    /*
                     * formData is empty just return
                     */
                    if (null == formData || formData.isEmpty()) {
                        return chain.filter(exchange);
                    }

                    // Repackage form data without blocking calls
                    return Flux.fromIterable(formData.entrySet())
                            .flatMap(entry -> {
                                String entryKey = entry.getKey();
                                List<String> entryValue = entry.getValue();

                                return Flux.fromIterable(entryValue)
                                        .publishOn(Schedulers.boundedElastic())
                                        .map(value -> entryKey + "=" + URLEncoder.encode(value, charset));
                            })
                            .collectList()
                            .map(encodedEntries -> String.join("&", encodedEntries))
                            .flatMap(formDataBodyString -> {
                                /*
                                 * get data bytes
                                 */
                                byte[] bodyBytes = formDataBodyString.getBytes(charset);
                                int contentLength = bodyBytes.length;

                                // Prepare headers
                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.putAll(exchange.getRequest().getHeaders());
                                httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
                                httpHeaders.setContentLength(contentLength);

                                /*
                                 * use BodyInserter to InsertFormData Body
                                 */
                                BodyInserter<String, ReactiveHttpOutputMessage> bodyInserter =
                                        BodyInserters.fromValue(formDataBodyString);
                                CachedBodyOutputMessage cachedBodyOutputMessage =
                                        new CachedBodyOutputMessage(exchange, httpHeaders);
                                log.debug("[GatewayContext] Rewrite Form Data :{}", formDataBodyString);

                                return bodyInserter
                                        .insert(cachedBodyOutputMessage, new BodyInserterContext())
                                        .then(Mono.defer(() -> {
                                            ServerHttpRequestDecorator decorator =
                                                    new ServerHttpRequestDecorator(exchange.getRequest()) {
                                                        @NotNull
                                                        @Override
                                                        public HttpHeaders getHeaders() {
                                                            return httpHeaders;
                                                        }

                                                        @NotNull
                                                        @Override
                                                        public Flux<DataBuffer> getBody() {
                                                            return cachedBodyOutputMessage.getBody();
                                                        }
                                                    };
                                            return chain.filter(exchange.mutate()
                                                    .request(decorator)
                                                    .build());
                                        }));
                            });
                }));
    }

    /**
     * Reads and processes the JSON body from the incoming request.
     *
     * <p>
     * This method captures the request body as a String, allowing it to be
     * processed and logged within the context of the gateway. The request is then
     * mutated to replace its body with the cached version.
     * </p>
     *
     * @param exchange
     *            the current server exchange, containing the request and response.
     * @param chain
     *            the filter chain to continue processing the request.
     * @param gatewayContext
     *            the context object to store request data.
     * @return a {@link Mono<Void>} representing the completion of the JSON body
     *         processing.
     */
    private Mono<Void> readBody(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            DataBufferUtils.retain(dataBuffer);
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(DataBufferUtils.retain(dataBuffer)));

            /*
             * repackage ServerHttpRequest
             */
            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @NotNull
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }
            };
            ServerWebExchange mutatedExchange =
                    exchange.mutate().request(mutatedRequest).build();
            return ServerRequest.create(mutatedExchange, codecConfigurer.getReaders())
                    .bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        if (objectValue != null)
                            objectValue = objectValue.replaceAll("\r", "").replaceAll("\n", "");
                        gatewayContext.setRequestBody(objectValue);
                        log.debug("[GatewayContext]Read JsonBody Success");
                    })
                    .then(chain.filter(mutatedExchange));
        });
    }
}
