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
package com.reactify.util;

import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Utility class for handling HTTP requests. Provides methods to extract the
 * real IP address from request headers and to generate offline cache keys.
 *
 * @author hoangtien2k3
 */
public class RequestUtils {

    // Define constants for header names
    private static final String HEADER_X_ORIGINAL_FORWARDED_FOR = "x-original-forwarded-for";
    private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HEADER_X_REAL_IP = "X-Real-IP";
    private static final String UNKNOWN = "unknown";

    /**
     * Retrieves the real IP address from the request headers. Checks various
     * headers to find the IP address, falling back to the remote address if
     * necessary.
     *
     * @param request
     *            the ServerHttpRequest from which to extract the IP address
     * @return the real IP address as a String
     */
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst(HEADER_X_ORIGINAL_FORWARDED_FOR);
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HEADER_X_FORWARDED_FOR);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HEADER_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HEADER_WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getFirst(HEADER_X_REAL_IP);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        if (ip != null && ip.length() > 15 && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    /**
     * Generates an offline cache key using the provided IP address and port.
     *
     * @param ip
     *            the IP address to be used in the cache key
     * @param port
     *            the port number to be used in the cache key
     * @return the generated cache key as a String, or null if the IP address is
     *         null
     */
    public static String getOfflineCacheKey(String ip, int port) {
        if (ip != null) {
            return ip + ":" + port;
        }
        return null;
    }
}
