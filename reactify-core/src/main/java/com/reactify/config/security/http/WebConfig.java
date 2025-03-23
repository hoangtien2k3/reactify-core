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
package com.reactify.config.security.http;

import com.reactify.config.WhiteListProperties;
import com.reactify.model.WhiteList;
import com.reactify.util.DataUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The {@code WebConfig} class is a configuration class for setting up security
 * settings in a Spring WebFlux application. It configures the security filter
 * chain, including authentication and authorization rules, CORS settings, and
 * other web security features.
 * </p>
 *
 * <p>
 * This class utilizes annotations from Spring Security to enable WebFlux
 * security and method security for reactive applications.
 * </p>
 *
 * <p>
 * The class uses the {@link com.reactify.config.WhiteListProperties} to
 * configure a whitelist of endpoints that can be accessed without
 * authentication.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(WhiteListProperties.class)
public class WebConfig {

    private final WhiteListProperties whiteListProperties;

    /**
     * Constructs a new instance of {@code WebConfig}.
     *
     * @param whiteListProperties
     *            the properties containing whitelisted entities.
     */
    public WebConfig(WhiteListProperties whiteListProperties) {
        this.whiteListProperties = whiteListProperties;
    }

    /**
     * <p>
     * Configures the security filter chain for the application. This method sets up
     * the CSRF protection, CORS configuration, authorization rules, and JWT
     * authentication.
     * </p>
     *
     * @param http
     *            a
     *            {@link org.springframework.security.config.web.server.ServerHttpSecurity}
     *            object to configure security settings
     * @param jwtAuthenticationConverter
     *            a {@link org.springframework.core.convert.converter.Converter}
     *            object for JWT to authentication conversion
     * @return a
     *         {@link org.springframework.security.web.server.SecurityWebFilterChain}
     *         object representing the security filter chain
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http, Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        List<WhiteList> whiteListList = whiteListProperties.getWhiteList();
        if (!DataUtil.isNullOrEmpty(whiteListList)) {
            for (WhiteList whiteList : whiteListList) {
                String uri = whiteList.uri();
                log.info("whitelist: {}", uri);
                List<String> methods = whiteList.methods();
                if (!DataUtil.isNullOrEmpty(methods)) {
                    for (String method : methods) {
                        HttpMethod convertedMethod = HttpMethod.valueOf(method);
                        http.authorizeExchange(authorize ->
                                authorize.pathMatchers(convertedMethod, uri).permitAll());
                    }
                } else {
                    http.authorizeExchange(
                            authorize -> authorize.pathMatchers(uri).permitAll());
                }
            }
        }
        http.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .authorizeExchange(authorize -> authorize.anyExchange().authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)));
        return http.build();
    }

    /**
     * <p>
     * Configures the CORS settings for the application. This method allows
     * cross-origin requests and specifies the allowed origins, methods, and
     * headers.
     * </p>
     *
     * @return a {@link CorsConfigurationSource} object that provides the CORS
     *         configuration
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setMaxAge(7200L);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
