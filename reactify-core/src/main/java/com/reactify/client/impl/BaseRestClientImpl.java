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
package com.reactify.client.impl;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import com.reactify.util.ObjectMapperUtil;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.ProxyProvider;

/**
 * BaseRestClientImpl provides a base implementation of the
 * {@link com.reactify.client.BaseRestClient} interface. This class uses
 * Spring's {@link org.springframework.web.reactive.function.client.WebClient}
 * for making REST API calls and handling various HTTP operations (GET, POST,
 * DELETE ...). Each method utilizes reactive types like
 * {@link reactor.core.publisher.Mono}, {@link reactor.core.publisher.Flux} for
 * asynchronous and non-blocking execution, which is useful in a WebFlux
 * environment. The class includes handling for error scenarios, logging, and
 * response parsing.
 *
 * @param <T>
 *            the type of the response body expected from the API call.
 * @author hoangtien2k3
 */
@Slf4j
@Service
public class BaseRestClientImpl<T> implements BaseRestClient<T> {

    /**
     * {@inheritDoc}
     *
     * Executes a GET request with specified headers and query parameters.
     */
    @Override
    public Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload,
            Class<?> resultClass) {
        log.info("Rest service payload client: {}", payload);
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParams(getSafePayload(payload))
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerMap)))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    log.info("Rest response {}", response);
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Optional.<T>empty();
                    }
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        @SuppressWarnings("unchecked")
                        T stringResponse = (T) response;
                        return Optional.of(stringResponse);
                    }
                    T result = DataUtil.parseStringToObject(response, resultClass);
                    return Optional.ofNullable(result);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("Exception call get rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * {@inheritDoc}
     *
     * Executes a GET request and returns the raw response as a string.
     */
    @Override
    public Mono<String> getRaw(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParams(getSafePayload(payload))
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerMap)))
                .retrieve()
                .bodyToMono(String.class)
                .map(DataUtil::safeToString)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call rest api ", e);
                    return Mono.just("");
                });
    }

    /**
     * {@inheritDoc}
     *
     * Executes a POST request with specified headers and payload.
     */
    @Override
    public Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        log.info("Payload when calling post: {}", payload);
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseRestClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api error: ", e);
                    String responseError = e.getResponseBodyAsString(StandardCharsets.UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * {@inheritDoc}
     *
     * Executes a POST request with form data as payload.
     */
    @Override
    public Mono<Optional<T>> postFormData(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            Class<?> resultClass) {
        if (formData == null) {
            formData = new LinkedMultiValueMap<>();
        }
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * {@inheritDoc}
     *
     * Processes the raw string response and maps it to the specified result class.
     */
    @Override
    public Optional<T> processReturn(String response, Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(response)) {
            return Optional.empty();
        }
        T result = DataUtil.parseStringToObject(response, resultClass);
        return Optional.ofNullable(result);
    }

    /**
     * {@inheritDoc}
     *
     * Executes a DELETE request with specified headers and query parameters.
     */
    @Override
    public Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParams(getSafePayload(payload))
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call delete rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * Ensures a safe MultiValueMap for payload, returning an empty map if the input
     * is null or empty.
     *
     * @param payload
     *            the payload map to check.
     * @return the original payload if not empty, otherwise an empty
     *         LinkedMultiValueMap.
     */
    private MultiValueMap<String, String> getSafePayload(MultiValueMap<String, String> payload) {
        return !DataUtil.isNullOrEmpty(payload) ? payload : new LinkedMultiValueMap<>();
    }

    /**
     * {@inheritDoc}
     *
     * Sends a POST request to an external certificate file service API with error
     * handling.
     */
    @Override
    public Mono<String> callApiCertificateFileService(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseRestClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> response)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    return Mono.just(responseError);
                });
    }

    /**
     * {@inheritDoc}
     *
     * Sends a POST request with JSON payload and maps the response to the specified
     * class type.
     */
    @Override
    public Mono<Optional<T>> callPostBodyJson(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        log.info("payload when call post {}", payload);

        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(Objects.requireNonNull(ObjectMapperUtil.convertObjectToJson(payload)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseRestClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * {@inheritDoc}
     *
     * Configures an HTTPS WebClient with optional proxy settings.
     */
    @Override
    public WebClient proxyClient(String proxyHost, Integer proxyPort, Boolean proxyEnable) {
        ConnectionProvider connectionProvider = ConnectionProvider.builder(Constants.POOL.REST_CLIENT_POLL)
                .maxConnections(2000)
                .pendingAcquireMaxCount(2000)
                .build();
        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .responseTimeout(Duration.ofMillis(10000))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);
        if (proxyEnable) {
            SslContext sslContext;
            try {
                sslContext = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
            } catch (Exception ex) {
                return null;
            }
            httpClient = httpClient
                    .proxy(proxy ->
                            proxy.type(ProxyProvider.Proxy.HTTP).host(proxyHost).port(proxyPort))
                    .secure(t -> t.sslContext(sslContext));
        }
        var clientConnector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024); // config max memory in byte
                })
                .clientConnector(clientConnector)
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * Configures an HTTP WebClient with proxy settings for non-secure connections.
     */
    @Override
    public WebClient proxyHttpClient(String proxyHost, Integer proxyPort) {
        HttpClient httpClient = HttpClient.create()
                .proxy(proxy ->
                        proxy.type(ProxyProvider.Proxy.HTTP).host(proxyHost).port(proxyPort));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * Sends a POST request with JSON payload containing LocalDateTime values and
     * maps the response.
     */
    @Override
    public Mono<Optional<T>> callPostBodyJsonForLocalDateTime(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(Objects.requireNonNull(ObjectMapperUtil.convertObjectToJsonForLocalDateTime(payload)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseRestClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * {@inheritDoc}
     *
     * Sends a POST request with raw JSON payload, handling errors by logging.
     */
    @Override
    public Mono<String> postRawBodyJson(
            WebClient webClient, String url, MultiValueMap<String, String> headerList, Object payload) {
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(Objects.requireNonNull(ObjectMapperUtil.convertObjectToJson(payload)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseRestClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(DataUtil::safeToString)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call rest api ", e);
                    return Mono.just("");
                });
    }

    /**
     * {@inheritDoc}
     *
     * Sends a GET request to a specified URI, with headers and error handling.
     */
    @Override
    public Mono<String> getRawWithFixedUri(WebClient webClient, String uri, MultiValueMap<String, String> headerMap) {
        return webClient
                .get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerMap)))
                .retrieve()
                .bodyToMono(String.class)
                .map(DataUtil::safeToString)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call rest api ", e);
                    return Mono.just("");
                });
    }

    /**
     * Ensures a safe MultiValueMap for headers, with a default "Content-Type:
     * application/json" if empty.
     *
     * @param headerMap
     *            the header map to check and potentially initialize.
     * @return the original header map if not empty, otherwise a new map with a JSON
     *         Content-Type.
     */
    private MultiValueMap<String, String> getSafeRestHeader(MultiValueMap<String, String> headerMap) {
        if (!DataUtil.isNullOrEmpty(headerMap)) {
            return headerMap;
        }
        headerMap = new LinkedMultiValueMap<>();
        headerMap.set(Constants.HeaderType.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headerMap;
    }

    /**
     * Handles error responses from a Spring WebFlux ClientResponse.
     * <p>
     * This method takes a ClientResponse as input, extracts the error body as a
     * String, and converts it into a Mono that emits a BusinessException containing
     * the error message. The method is designed to be used in error handling
     * scenarios where the response indicates an error (non-2xx status code).
     *
     * @param response
     *            The ClientResponse to handle, which may contain error details.
     * @return A Mono that emits a {@link BusinessException} with the error message
     *         from the response body. If the response body cannot be read, the
     *         error will be propagated.
     */
    public static Mono<? extends Throwable> handleErrorResponse(ClientResponse response) {
        return response.bodyToMono(String.class).flatMap(errorBody -> {
            log.info("log when call error {}", errorBody);
            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody));
        });
    }
}
