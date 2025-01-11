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

/**
 * <p>
 * The ProxyProperties class is a record that holds configuration properties for
 * an HTTP/HTTPS proxy. It specifies whether the proxy is enabled and the host
 * and port settings for both HTTP and HTTPS connections.
 * </p>
 *
 * <p>
 * The default constructor initializes the proxy as disabled (not enabled) and
 * sets all host and port values to null.
 * </p>
 *
 * @param enable
 *            a boolean indicating whether the proxy is enabled
 * @param httpHost
 *            the host for HTTP connections
 * @param httpPort
 *            the port for HTTP connections
 * @param httpsHost
 *            the host for HTTPS connections
 * @param httpsPort
 *            the port for HTTPS connections
 * @author hoangtien2k3
 */
public record ProxyProperties(boolean enable, String httpHost, Integer httpPort, String httpsHost, Integer httpsPort) {
    /**
     * <p>
     * Constructor for ProxyProperties.
     * </p>
     */
    public ProxyProperties() {
        this(false, null, null, null, null);
    }
}
