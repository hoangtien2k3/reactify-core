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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * The GatewayPluginProperties class is a configuration properties class for
 * managing gateway plugin settings in a Spring application. It holds various
 * properties related to request and response logging as well as request data
 * handling configurations.
 * </p>
 *
 * <p>
 * This class is annotated with @ConfigurationProperties, allowing it to be
 * populated with values from the application's configuration (e.g.,
 * application.yml or application.properties) under the prefix
 * "spring.plugin.config".
 * </p>
 *
 * <p>
 * It implements the InitializingBean interface, allowing for custom
 * initialization logic to be executed after the properties are set.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@ConfigurationProperties(value = "spring.plugin.config")
public class GatewayPluginProperties implements InitializingBean {

    /**
     * Constant <code>GATEWAY_PLUGIN_PROPERTIES_PREFIX="spring.plugin.config"</code>
     * The prefix for the properties related to the gateway plugin configuration.
     */
    public static final String GATEWAY_PLUGIN_PROPERTIES_PREFIX = "spring.plugin.config";

    /**
     * Indicates whether to read request data. Default is false.
     */
    private Boolean readRequestData = false;

    /**
     * Indicates whether to read response data. Default is false.
     */
    private Boolean readResponseData = false;

    /**
     * Indicates whether to log request data. Default is false.
     */
    private boolean logRequest = false;

    /**
     * Indicates whether to log response data. Default is true.
     */
    private boolean logResponse = true;

    /**
     * A list of headers to hide in the logs. Default is an empty list.
     */
    private List<String> hideHeaderList = Collections.emptyList();

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(hideHeaderList)) {
            // Convert all headers in hideHeaderList to lowercase
            hideHeaderList = hideHeaderList.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
    }

    public GatewayPluginProperties() {}

    public Boolean getReadRequestData() {
        return readRequestData;
    }

    public void setReadRequestData(Boolean readRequestData) {
        this.readRequestData = readRequestData;
    }

    public Boolean getReadResponseData() {
        return readResponseData;
    }

    public void setReadResponseData(Boolean readResponseData) {
        this.readResponseData = readResponseData;
    }

    public boolean isLogRequest() {
        return logRequest;
    }

    public void setLogRequest(boolean logRequest) {
        this.logRequest = logRequest;
    }

    public boolean isLogResponse() {
        return logResponse;
    }

    public void setLogResponse(boolean logResponse) {
        this.logResponse = logResponse;
    }

    public List<String> getHideHeaderList() {
        return hideHeaderList;
    }

    public void setHideHeaderList(List<String> hideHeaderList) {
        this.hideHeaderList = hideHeaderList;
    }
}
