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
package com.reactify.config.security.keycloak;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * The {@code KeycloakConfiguration} class is a configuration class that
 * provides beans for integrating Keycloak with Spring Security in a reactive
 * application. This class defines converters that transform JWT tokens issued
 * by Keycloak into Spring Security's
 * {@link org.springframework.security.core.GrantedAuthority} and
 * {@link org.springframework.security.authentication.AbstractAuthenticationToken}.
 *
 * @author hoangtien2k3
 */
@Configuration
public class KeycloakConfiguration {

    /**
     * Constructs a new instance of {@code KeycloakConfiguration}.
     */
    public KeycloakConfiguration() {}

    /**
     * Creates a converter that extracts granted authorities from a JWT token issued
     * by Keycloak. The {@code clientId} is used to identify the client for which
     * the authorities are to be extracted.
     *
     * @param clientId
     *            the client ID of the Keycloak client
     * @return a {@link Converter} that converts a {@link Jwt} to a
     *         {@link Collection} of {@link GrantedAuthority}
     */
    @Bean
    Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter(
            @Value("${spring.security.oauth2.keycloak.client-id}") String clientId) {
        return new KeycloakGrantedAuthoritiesConverter(clientId);
    }

    /**
     * Creates a converter that converts a JWT token into a reactive
     * {@link AbstractAuthenticationToken}. This converter uses the provided granted
     * authorities converter to obtain the authorities from the JWT.
     *
     * @param converter
     *            the converter that extracts granted authorities from a JWT
     * @return a {@link Converter} that converts a {@link Jwt} to a reactive
     *         {@link Mono} of {@link AbstractAuthenticationToken}
     */
    @Bean
    Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter(
            Converter<Jwt, Collection<GrantedAuthority>> converter) {
        return new ReactiveKeycloakJwtAuthenticationConverter(converter);
    }
}
