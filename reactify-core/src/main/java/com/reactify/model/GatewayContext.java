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
package com.reactify.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * <p>
 * The GatewayContext class is designed to encapsulate the context of a gateway
 * request. It holds information regarding the request and response data,
 * headers, and the processing state of the gateway. This class is useful for
 * managing the data flow in a gateway system, allowing for caching and easy
 * access to request/response details throughout the processing lifecycle.
 * </p>
 *
 * <p>
 * It includes flags for controlling whether to read request and response data,
 * and it provides storage for various components of the request such as the
 * body, headers, and form data. The class also tracks the start time of the
 * request for performance monitoring.
 * </p>
 *
 * @author hoangtien2k3
 */
@Getter
@Setter
@ToString
public class GatewayContext {

    /**
     * Constructs a new instance of {@code GatewayContext}.
     */
    public GatewayContext() {}

    /** Constant <code>CACHE_GATEWAY_CONTEXT="cacheGatewayContext"</code> */
    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    /** whether read request data */
    protected Boolean readRequestData = true;

    /** whether read response data */
    protected Boolean readResponseData = true;

    /** cache json body */
    protected String requestBody;

    /** cache Response Body */
    protected Object responseBody;

    /** request headers */
    protected HttpHeaders requestHeaders;

    /** cache form data */
    protected MultiValueMap<String, String> formData;

    /** cache all request data include:form data and query param */
    protected MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);

    /** Gateway Start time of request */
    protected Long startTime;
}
