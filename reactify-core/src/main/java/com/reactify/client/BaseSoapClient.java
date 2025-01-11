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
package com.reactify.client;

import java.util.Map;
import java.util.Optional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * A base interface for SOAP client operations, providing methods to call SOAP
 * services with various configurations and handle responses.
 * <p>
 * This interface is designed to facilitate SOAP communication by defining
 * essential methods for making requests, processing raw responses, and parsing
 * data. It leverages a generic type parameter {@code T} to support diverse
 * response types.
 *
 * @param <T>
 *            the type of response object that will be returned by SOAP client
 *            calls.
 * @author hoangtien2k3
 */
public interface BaseSoapClient<T> {
    /**
     * Makes a SOAP call with the specified WebClient instance, HTTP headers, and
     * payload, returning an optional result wrapped in a reactive Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the SOAP call.
     * @param headerList
     *            a map of HTTP headers to include in the request.
     * @param payload
     *            the SOAP request payload as a string.
     * @param resultClass
     *            the class of the expected response object type.
     * @return a Mono containing an Optional result of type {@code T}, or an empty
     *         Optional if no data was returned.
     */
    Mono<Optional<T>> call(WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass);

    /**
     * Makes a raw SOAP call and returns the unprocessed response as a string
     * wrapped in a reactive Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the SOAP call.
     * @param headerList
     *            a map of HTTP headers to include in the request.
     * @param payload
     *            the SOAP request payload as a string.
     * @return a Mono containing the raw response as a string.
     */
    Mono<String> callRaw(WebClient webClient, Map<String, String> headerList, String payload);

    /**
     * Parses the SOAP response data into an instance of the specified class type.
     *
     * @param realData
     *            the raw response data as a string.
     * @param clz
     *            the class to which the data should be converted.
     * @return an instance of type {@code T} containing the parsed response data.
     */
    T parseData(String realData, Class<?> clz);

    /**
     * Calls a specific API to retrieve the customer profile data using the provided
     * WebClient instance, HTTP headers, and payload. The response is returned as an
     * optional result wrapped in a reactive Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the SOAP call.
     * @param headerList
     *            a map of HTTP headers to include in the request.
     * @param payload
     *            the SOAP request payload as a string.
     * @param resultClass
     *            the class of the expected response object type.
     * @return a Mono containing an Optional result of type {@code T}, representing
     *         the customer profile data, or an empty Optional if no data was
     *         returned.
     */
    Mono<Optional<T>> callApiGetProfileKHDN(
            WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass);

    /**
     * Makes a SOAP call using version 2 of the API configuration, with the provided
     * WebClient instance, HTTP headers, and payload. Returns an optional result
     * wrapped in a reactive Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the SOAP call.
     * @param headerList
     *            a map of HTTP headers to include in the request.
     * @param payload
     *            the SOAP request payload as a string.
     * @param resultClass
     *            the class of the expected response object type.
     * @return a Mono containing an Optional result of type {@code T}, or an empty
     *         Optional if no data was returned.
     */
    Mono<Optional<T>> callV2(WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass);
}
