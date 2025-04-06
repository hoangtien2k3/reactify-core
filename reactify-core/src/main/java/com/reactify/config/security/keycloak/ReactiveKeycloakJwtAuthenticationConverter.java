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
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * ReactiveKeycloakJwtAuthenticationConverter class.
 * </p>
 *
 * @see org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter
 * @author hoangtien2k3
 */
public final class ReactiveKeycloakJwtAuthenticationConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private static final String USERNAME_CLAIM = "preferred_username";
    private final Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    /**
     * <p>
     * Constructor for ReactiveKeycloakJwtAuthenticationConverter.
     * </p>
     *
     * @param jwtGrantedAuthoritiesConverter
     *            a {@link org.springframework.core.convert.converter.Converter}
     *            object
     */
    public ReactiveKeycloakJwtAuthenticationConverter(
            Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter =
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwtGrantedAuthoritiesConverter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<AbstractAuthenticationToken> convert(@NotNull Jwt jwt) {
        return Objects.requireNonNull(this.jwtGrantedAuthoritiesConverter.convert(jwt))
                .collectList()
                .map((authorities) -> new JwtAuthenticationToken(jwt, authorities, extractUsername(jwt)));
    }

    /**
     * <p>
     * extractUsername.
     * </p>
     *
     * @param jwt
     *            a {@link Jwt} object
     * @return a {@link String} object
     */
    private String extractUsername(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (claims.containsKey(USERNAME_CLAIM)) {
            return (String) claims.get(USERNAME_CLAIM);
        }
        return jwt.getSubject();
    }
}
