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
     * Generates an offline cache key based on the given IP and port.
     *
     * @param ip
     *            the client IP address
     * @param port
     *            the client port
     * @return the formatted cache key, or null if IP is null
     */
    public static String getOfflineCacheKey(String ip, int port) {
        return ip != null ? ip + ":" + port : null;
    }

    /**
     * Checks whether the given IP is local (127.0.0.1 or ::1).
     *
     * @param ip
     *            the IP address to check
     * @return true if the IP is local, false otherwise
     */
    public static boolean isLocalAddress(String ip) {
        return "127.0.0.1".equals(ip) || "localhost".equals(ip) || "::1".equals(ip);
    }

    /**
     * Checks if the current request is coming from localhost or not.
     *
     * @param request
     *            the {@link ServerHttpRequest}
     * @return true if the request comes from localhost
     */
    public static boolean isLocalRequest(ServerHttpRequest request) {
        String ip = getIpAddress(request);
        return isLocalAddress(ip);
    }

    /**
     * Iterates through multiple headers to find the first non-empty and
     * non-"unknown" value.
     *
     * @param headers
     *            the {@link HttpHeaders} object
     * @param headerNames
     *            list of header keys to try
     * @return the first valid header value, or null if none found
     */
    private static String getFirstValidHeader(HttpHeaders headers, String... headerNames) {
        for (String header : headerNames) {
            String value = headers.getFirst(header);
            if (value != null && !value.isEmpty() && !UNKNOWN.equalsIgnoreCase(value)) {
                return value;
            }
        }
        return null;
    }
}
