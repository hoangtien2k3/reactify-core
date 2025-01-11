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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * The {@code KeycloakGrantedAuthoritiesConverter} class implements a
 * {@link org.springframework.core.convert.converter.Converter} that converts a
 * JWT token issued by Keycloak into a collection of Spring Security
 * {@link org.springframework.security.core.GrantedAuthority}.
 * </p>
 *
 * <p>
 * This converter extracts both realm roles and client-specific roles from the
 * JWT claims and combines them into a single set of granted authorities.
 * </p>
 *
 * <p>
 * The roles are extracted from the claims:
 * </p>
 * <ul>
 * <li>{@code realm_access.roles} for realm roles</li>
 * <li>{@code resource_access.clientId.roles} for client roles, where
 * {@code clientId} is the ID of the client</li>
 * </ul>
 *
 * @author hoangtien2k3
 */
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String ROLES = "roles";
    private static final String CLAIM_REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";

    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private final String clientId;

    /**
     * Constructs a new instance of {@code KeycloakGrantedAuthoritiesConverter}.
     *
     * @param clientId
     *            the client ID used to identify the Keycloak client.
     */
    public KeycloakGrantedAuthoritiesConverter(String clientId) {
        this.clientId = clientId;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<GrantedAuthority> convert(@NotNull Jwt jwt) {
        var realmRoles = realmRoles(jwt);
        var clientRoles = clientRoles(jwt, clientId);

        Collection<GrantedAuthority> authorities = Stream.concat(realmRoles.stream(), clientRoles.stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        authorities.addAll(defaultGrantedAuthorities(jwt));

        return authorities;
    }

    private Collection<GrantedAuthority> defaultGrantedAuthorities(Jwt jwt) {
        return Optional.ofNullable(defaultAuthoritiesConverter.convert(jwt)).orElse(emptySet());
    }

    /**
     * <p>
     * Extracts realm roles from the JWT.
     * </p>
     *
     * @param jwt
     *            a {@link org.springframework.security.oauth2.jwt.Jwt} object
     *            containing the claims
     * @return a {@link java.util.List} of realm role names
     */
    @SuppressWarnings("unchecked")
    protected List<String> realmRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap(CLAIM_REALM_ACCESS))
                .map(realmAccess -> (List<String>) realmAccess.get(ROLES))
                .orElse(emptyList());
    }

    /**
     * <p>
     * Extracts client-specific roles from the provided JWT for the given client ID.
     * </p>
     *
     * @param jwt
     *            a {@link org.springframework.security.oauth2.jwt.Jwt} object
     *            representing the token
     * @param clientId
     *            a {@link java.lang.String} representing the client ID for which
     *            roles are to be extracted
     * @return a {@link java.util.List} of client roles, or an empty list if none
     *         are found
     */
    @SuppressWarnings("unchecked")
    protected List<String> clientRoles(Jwt jwt, String clientId) {
        if (ObjectUtils.isEmpty(clientId)) {
            return emptyList();
        }
        return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                .map(resourceAccess -> (Map<String, List<String>>) resourceAccess.get(clientId))
                .map(clientAccess -> clientAccess.get(ROLES))
                .orElse(emptyList());
    }
}
