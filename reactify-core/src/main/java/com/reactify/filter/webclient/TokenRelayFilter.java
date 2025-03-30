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
package com.reactify.filter.webclient;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The {@code TokenRelayFilter} class is an implementation of Spring WebFlux's
 * {@link ExchangeFilterFunction}. It is designed to relay an OAuth2 token from
 * the current security context to outgoing HTTP requests made by a
 * {@link WebClient}. This is particularly useful in microservices architectures
 * where the same token needs to be propagated across service boundaries without
 * generating a new one.
 * </p>
 *
 * <p>
 * This filter intercepts outgoing requests, extracts the token from the current
 * {@link SecurityContext}, and adds it to the {@code Authorization} header of
 * the request. If no token is found, the request proceeds without modification.
 * </p>
 *
 * <p>
 * The class uses Lombok annotations to reduce boilerplate code, such as logging
 * and constructor generation.
 * </p>
 *
 * @see ExchangeFilterFunction
 * @see WebClient
 * @see SecurityContext
 * @see JwtAuthenticationToken
 * @author hoangtien2k3
 */
public class TokenRelayFilter implements ExchangeFilterFunction {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(TokenRelayFilter.class);

    public TokenRelayFilter() {}

    /**
     * <p>
     * Filters the outgoing HTTP request by adding an OAuth2 token from the current
     * security context to the {@code Authorization} header. If no token is found,
     * the request proceeds without modification.
     * </p>
     *
     * @param request
     *            the outgoing HTTP request to be intercepted
     * @param next
     *            the next filter or final HTTP call in the chain
     * @return a {@link Mono} emitting the {@link ClientResponse} after the request
     *         is processed
     * @throws NullPointerException
     *             if the {@code request} or {@code next} parameter is null
     */
    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request, ExchangeFunction next) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    // Fetch token from authentication mechanism
                    String token = Optional.ofNullable(authentication)
                            .filter(auth -> auth instanceof JwtAuthenticationToken)
                            .map(auth ->
                                    ((JwtAuthenticationToken) auth).getToken().getTokenValue())
                            .orElse(null);
                    if (token == null) {
                        log.debug("No token found in the security context. Proceeding with the original request.");
                        return next.exchange(request);
                    }
                    // Add token to the request header
                    ClientRequest newRequest = ClientRequest.from(request)
                            .headers(headers -> headers.setBearerAuth(token))
                            .build();
                    // Proceed with the modified request
                    return next.exchange(newRequest);
                })
                .switchIfEmpty(next.exchange(request));
    }
}
