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

import com.reactify.client.BaseSoapClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import com.reactify.util.DataWsUtil;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * BaseSoapClientImpl is an implementation of the BaseSoapClient interface that
 * provides a set of methods to call SOAP APIs using WebClient. This class
 * encapsulates SOAP request/response processing logic and supports XML parsing,
 * error handling, and header setup for SOAP requests.
 *
 * <p>
 * This implementation supports different types of API calls: - Generic API call
 * to retrieve a result of a specified class type. - A raw API call that returns
 * the raw XML response. - A specific API call for fetching the KHDN profile.
 * </p>
 *
 * <p>
 * Dependencies: - Uses Spring WebFlux's WebClient for making asynchronous HTTP
 * calls. - Utilizes custom utility classes (e.g., DataUtil, DataWsUtil) for
 * data processing. - Implements error handling through custom
 * BusinessException.
 * </p>
 *
 * @param <T>
 *            The type of the result object expected from the API response.
 * @author hoangtien2k3
 */
@Slf4j
@Service
public class BaseSoapClientImpl<T> implements BaseSoapClient<T> {

    /**
     * {@inheritDoc}
     *
     * Executes a SOAP API call and returns an Optional result of the specified
     * type. This method performs the following steps: - Sets up headers and sends a
     * POST request. - Processes the response as a String and attempts to parse it.
     * - If the response is empty or parsing fails, returns an Optional.empty.
     */
    @Override
    public Mono<Optional<T>> call(
            WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass) {
        log.info("Soap service payload client: {}", payload);
        MultiValueMap<String, String> header = getHeaderForCallSoap(headerList);
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseSoapClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> {
                    log.info("Soap Response {}", response);
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Optional.<T>empty();
                    }
                    log.info("callRaw soap WS resp: {}", response);
                    String formattedSOAPResponse = DataWsUtil.formatXML(response);
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            Constants.XmlConst.TAG_OPEN_RETURN,
                            Constants.XmlConst.TAG_CLOSE_RETURN);
                    if (DataUtil.isNullOrEmpty(realData)) {
                        return Optional.<T>empty();
                    }
                    T result;
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        @SuppressWarnings("unchecked")
                        T stringResponse = (T) response;
                        result = stringResponse;
                    } else {
                        result = DataWsUtil.xmlToObj(
                                Constants.XmlConst.TAG_OPEN_RETURN + realData + Constants.XmlConst.TAG_CLOSE_RETURN,
                                resultClass);
                    }
                    if (result == null) {
                        log.error("Exception when parse data");
                        return Optional.<T>empty();
                    }
                    return Optional.of(result);
                })
                .doOnError(err -> log.error("Call error when use method framework call", err));
    }

    /**
     * {@inheritDoc}
     *
     * Alternative implementation of the `call` method with a similar API call
     * structure. Provides the same functionality as `call` but can be used
     * independently as needed.
     */
    @Override
    public Mono<Optional<T>> callV2(
            WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass) {
        log.info("Soap service payload client: {}", payload);
        MultiValueMap<String, String> header = getHeaderForCallSoap(headerList);
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseSoapClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> {
                    log.info("Soap Response {}", response);
                    if (DataUtil.isNullOrEmpty(response)) {
                        return Optional.<T>empty();
                    }
                    log.info("callRaw soap WS resp: {}", response);
                    String formattedSOAPResponse = DataWsUtil.formatXML(response);
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            Constants.XmlConst.TAG_OPEN_RETURN,
                            Constants.XmlConst.TAG_CLOSE_RETURN);
                    if (DataUtil.isNullOrEmpty(realData)) {
                        return Optional.<T>empty();
                    }
                    T result;
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        @SuppressWarnings("unchecked")
                        T stringResponse = (T) response;
                        result = stringResponse;
                    } else {
                        result = DataWsUtil.xmlToObj(
                                Constants.XmlConst.TAG_OPEN_RETURN + realData + Constants.XmlConst.TAG_CLOSE_RETURN,
                                resultClass);
                    }
                    if (result == null) {
                        log.error("Exception when parse data");
                        return Optional.<T>empty();
                    }
                    return Optional.of(result);
                })
                .doOnError(err -> log.error("Call error when use method framework call", err));
    }

    /**
     * {@inheritDoc}
     *
     * Calls the SOAP API and returns the raw XML response as a String. - Processes
     * headers, sends a POST request, and retrieves the raw response. - If the
     * response is empty, returns an error with a specified message.
     */
    @Override
    public Mono<String> callRaw(WebClient webClient, Map<String, String> headerList, String payload) {
        log.info("Soap service payload client: {}", payload);
        MultiValueMap<String, String> header = getHeaderForCallSoap(headerList);
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseSoapClientImpl::handleErrorResponse)
                .onStatus(Objects::nonNull, clientResponse -> {
                    log.info("Status code {}", clientResponse.statusCode());
                    log.info("Full {}", clientResponse);
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.error(new BusinessException("Call raw", "null")))
                .map(response -> {
                    log.info("callRaw soap WS resp: {}", response);
                    return DataUtil.safeToString(response);
                })
                .log();
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
    public static Mono<Throwable> handleErrorResponse(ClientResponse response) {
        return response.bodyToMono(String.class).flatMap(errorBody -> {
            log.info("log when call error {}", errorBody);
            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody));
        });
    }

    /**
     * Constructs headers for a SOAP request, setting default values if no headers
     * are provided.
     *
     * @param headerList
     *            The headers to include in the SOAP request.
     * @return A MultiValueMap with SOAP request headers.
     */
    private MultiValueMap<String, String> getHeaderForCallSoap(Map<String, String> headerList) {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        if (DataUtil.isNullOrEmpty(headerList)) {
            header.set(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE);
            header.set(Constants.SoapHeaderConstant.X_B3_TRACEID, Constants.SoapHeaderConstant.X_B3_TRACEID_VALUE_SOAP);
        } else {
            header.setAll(headerList);
        }
        return header;
    }
}
