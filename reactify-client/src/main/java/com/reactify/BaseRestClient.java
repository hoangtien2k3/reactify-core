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
package com.reactify;

import java.util.Optional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <p>
 * BaseRestClient interface provides basic REST client operations.
 * </p>
 *
 * @param <T>
 *            the type of the response body
 * @author hoangtien2k3
 */
public interface BaseRestClient<T> {

    /**
     * <p>
     * Sends a GET request to the specified URL with optional headers and payload.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} instance to use for the request
     * @param url
     *            the target URL for the GET request
     * @param headerList
     *            a {@link MultiValueMap} of headers to include in the request
     * @param payload
     *            a {@link MultiValueMap} of query parameters to send with the
     *            request
     * @param resultClass
     *            the {@link Class} of the expected response body type
     * @return a {@link Mono} containing the response body wrapped in an
     *         {@link Optional}, or an empty Optional if not found
     */
    Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass);

    /**
     * <p>
     * getRaw.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerMap
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link MultiValueMap} object
     * @return a {@link Mono} object
     */
    Mono<String> getRaw(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload);

    /**
     * <p>
     * post.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link Object} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Mono} object
     */
    Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * <p>
     * postFormData.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param formData
     *            a {@link MultiValueMap} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Mono} object
     */
    Mono<Optional<T>> postFormData(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            Class<?> resultClass);

    /**
     * <p>
     * delete.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link MultiValueMap} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Mono} object
     */
    Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass);

    /**
     * <p>
     * processReturn.
     * </p>
     *
     * @param response
     *            a {@link String} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Optional} object
     */
    Optional<T> processReturn(String response, Class<?> resultClass);

    /**
     * <p>
     * callApiCertificateFileService.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link Object} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Mono} object
     */
    Mono<String> callApiCertificateFileService(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * <p>
     * callPostBodyJson.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link Object} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Mono} object
     */
    Mono<Optional<T>> callPostBodyJson(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * <p>
     * proxyClient.
     * </p>
     *
     * @param proxyHost
     *            a {@link String} object
     * @param proxyPort
     *            a {@link Integer} object
     * @param proxyEnable
     *            a {@link Boolean} object
     * @return a {@link WebClient} object
     */
    WebClient proxyClient(String proxyHost, Integer proxyPort, Boolean proxyEnable);

    /**
     * <p>
     * proxyHttpClient.
     * </p>
     *
     * @param proxyHost
     *            a {@link String} object
     * @param proxyPort
     *            a {@link Integer} object
     * @return a {@link WebClient} object
     */
    WebClient proxyHttpClient(String proxyHost, Integer proxyPort);

    /**
     * <p>
     * callPostBodyJsonForLocalDateTime.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link Object} object
     * @param resultClass
     *            a {@link Class} object
     * @return a {@link Mono} object
     */
    Mono<Optional<T>> callPostBodyJsonForLocalDateTime(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * <p>
     * postRawBodyJson.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param url
     *            a {@link String} object
     * @param headerList
     *            a {@link MultiValueMap} object
     * @param payload
     *            a {@link Object} object
     * @return a {@link Mono} object
     */
    Mono<String> postRawBodyJson(
            WebClient webClient, String url, MultiValueMap<String, String> headerList, Object payload);

    /**
     * <p>
     * getRawWithFixedUri.
     * </p>
     *
     * @param webClient
     *            a {@link WebClient} object
     * @param uri
     *            a {@link String} object
     * @param headerMap
     *            a {@link MultiValueMap} object
     * @return a {@link Mono} object
     */
    Mono<String> getRawWithFixedUri(WebClient webClient, String uri, MultiValueMap<String, String> headerMap);
}
