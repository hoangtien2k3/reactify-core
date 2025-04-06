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
package com.reactify.filter.properties;

import com.reactify.model.logging.HttpLogRequest;
import com.reactify.model.logging.HttpLogResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * The HttpLogProperties class is a configuration properties class for managing
 * HTTP logging settings in a Spring application. It is designed to hold
 * properties related to logging HTTP requests and responses.
 * </p>
 *
 * <p>
 * This class is annotated with @ConfigurationProperties, allowing it to be
 * populated with values from the application's configuration (e.g.,
 * application.yml or application.properties) under the prefix
 * "application.http-logging". Invalid fields in the configuration will be
 * ignored.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@ConfigurationProperties(prefix = "application.http-logging", ignoreInvalidFields = true)
public class HttpLogProperties {

    /**
     * Properties related to HTTP request logging.
     */
    private HttpLogRequest request = new HttpLogRequest();

    /**
     * Properties related to HTTP response logging.
     */
    private HttpLogResponse response = new HttpLogResponse();

    public HttpLogProperties() {}

    public HttpLogProperties(HttpLogRequest request, HttpLogResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpLogRequest getRequest() {
        return request;
    }

    public void setRequest(HttpLogRequest request) {
        this.request = request;
    }

    public HttpLogResponse getResponse() {
        return response;
    }

    public void setResponse(HttpLogResponse response) {
        this.response = response;
    }
}
