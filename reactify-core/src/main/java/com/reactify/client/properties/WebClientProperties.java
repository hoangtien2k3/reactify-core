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
package com.reactify.client.properties;

import com.reactify.filter.properties.*;
import java.util.List;
import java.util.Objects;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

/**
 * Configuration properties for a WebClient instance, encapsulating connection,
 * authorization, and monitoring settings.
 * <p>
 * This class is designed to hold various configuration parameters for a
 * WebClient, including endpoint details, authorization credentials, connection
 * pooling, request timeouts, retries, logging options, monitoring, and proxy
 * configurations.
 *
 * @author hoangtien2k3
 */
public class WebClientProperties {

    /**
     * The name identifier for the WebClient configuration, used for reference
     * purposes.
     */
    private String name;

    /** The base address (URL) that the WebClient will connect to. */
    private String address;

    /** The username used for basic authentication, if required by the server. */
    private String username;

    /**
     * The password used for basic authentication, corresponding to the username.
     */
    private String password;

    /**
     * The authorization token or header for authenticated requests. Typically used
     * for token-based authorization (e.g., Bearer token).
     */
    private String authorization;

    /**
     * Connection pool settings to control resource allocation for HTTP connections,
     * allowing for efficient management of multiple requests.
     */
    private PoolProperties pool = new PoolProperties();

    /**
     * Timeout settings for various stages of the HTTP request lifecycle, helping
     * prevent stalled connections and ensuring timely responses.
     */
    private TimeoutProperties timeout = new TimeoutProperties();

    /**
     * Retry settings to handle network or server errors by attempting the request
     * multiple times under defined conditions.
     */
    private RetryProperties retry = new RetryProperties();

    /**
     * Logging settings to configure request and response logging levels, enabling
     * better tracking of HTTP communication details for debugging.
     */
    private ClientLogProperties log = new ClientLogProperties();

    /**
     * Monitoring settings for tracking WebClient performance metrics, such as
     * request durations and error rates, for observability purposes.
     */
    private MonitoringProperties monitoring = new MonitoringProperties();

    /**
     * Proxy configuration for requests, allowing routing through an intermediary
     * server, often required in restricted networks or for security purposes.
     */
    private ProxyProperties proxy = new ProxyProperties();

    /**
     * A list of custom filters to modify or inspect each request and response
     * processed by the WebClient, allowing for additional processing or logging.
     */
    private List<ExchangeFilterFunction> customFilters;

    /**
     * Indicates if internal OAuth2 should be used for requests. If true, the
     * WebClient will include an OAuth2 token in the request headers.
     */
    private boolean internalOauth = false;

    /**
     * Constructs a new instance of {@code WebClientProperties}.
     */
    public WebClientProperties() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public PoolProperties getPool() {
        return pool;
    }

    public void setPool(PoolProperties pool) {
        this.pool = pool;
    }

    public TimeoutProperties getTimeout() {
        return timeout;
    }

    public void setTimeout(TimeoutProperties timeout) {
        this.timeout = timeout;
    }

    public RetryProperties getRetry() {
        return retry;
    }

    public void setRetry(RetryProperties retry) {
        this.retry = retry;
    }

    public ClientLogProperties getLog() {
        return log;
    }

    public void setLog(ClientLogProperties log) {
        this.log = log;
    }

    public MonitoringProperties getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(MonitoringProperties monitoring) {
        this.monitoring = monitoring;
    }

    public ProxyProperties getProxy() {
        return proxy;
    }

    public void setProxy(ProxyProperties proxy) {
        this.proxy = proxy;
    }

    public List<ExchangeFilterFunction> getCustomFilters() {
        return customFilters;
    }

    public void setCustomFilters(List<ExchangeFilterFunction> customFilters) {
        this.customFilters = customFilters;
    }

    public boolean isInternalOauth() {
        return internalOauth;
    }

    public void setInternalOauth(boolean internalOauth) {
        this.internalOauth = internalOauth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WebClientProperties that)) return false;
        return isInternalOauth() == that.isInternalOauth()
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getAddress(), that.getAddress())
                && Objects.equals(getUsername(), that.getUsername())
                && Objects.equals(getPassword(), that.getPassword())
                && Objects.equals(getAuthorization(), that.getAuthorization())
                && Objects.equals(getPool(), that.getPool())
                && Objects.equals(getTimeout(), that.getTimeout())
                && Objects.equals(getRetry(), that.getRetry())
                && Objects.equals(getLog(), that.getLog())
                && Objects.equals(getMonitoring(), that.getMonitoring())
                && Objects.equals(getProxy(), that.getProxy())
                && Objects.equals(getCustomFilters(), that.getCustomFilters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getName(),
                getAddress(),
                getUsername(),
                getPassword(),
                getAuthorization(),
                getPool(),
                getTimeout(),
                getRetry(),
                getLog(),
                getMonitoring(),
                getProxy(),
                getCustomFilters(),
                isInternalOauth());
    }
}
