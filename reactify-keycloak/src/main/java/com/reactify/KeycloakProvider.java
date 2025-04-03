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
package com.reactify;

import java.util.HashMap;
import java.util.Map;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Configuration
@Component
public class KeycloakProvider {

    private static final Map<String, ClientRepresentation> clientMap = new HashMap<>();
    private volatile Keycloak keycloak = null;

    @Value("${keycloak.serverUrl}")
    public String serverURL;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.clientId}")
    public String clientID;

    @Value("${keycloak.clientSecret}")
    public String clientSecret;

    // volatile and Double-Checked Locking -> Thread-Safe.
    public Keycloak getInstance() {
        if (keycloak == null) {
            synchronized (KeycloakProvider.class) {
                if (keycloak == null) {
                    keycloak = KeycloakBuilder.builder()
                            .serverUrl(serverURL)
                            .realm(realm)
                            .clientId(clientID)
                            .clientSecret(clientSecret)
                            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                            .build();
                }
            }
        }
        return keycloak;
    }

    public RealmResource getRealmResource() {
        return getInstance().realm(realm);
    }

    public Mono<ClientRepresentation> getClient(String clientId) {
        var clients = getRealmResource().clients().findByClientId(clientId);
        if (clients == null) {
            return Mono.empty();
        }
        for (ClientRepresentation clientRepresentation : clients) {
            if (clientRepresentation.isEnabled()) {
                return Mono.justOrEmpty(clientRepresentation);
            }
        }
        return Mono.empty();
    }

    public Mono<String> getKCIdFromClientId(String clientId) {
        return getClient(clientId).flatMap(rs -> Mono.just(rs.getId()));
    }

    public Mono<ClientRepresentation> getClientWithSecret(String clientId) {
        return getClient(clientId).flatMap(clientRepresentation -> {
            var result = getRealmResource()
                    .clients()
                    .get(clientRepresentation.getId())
                    .getSecret();
            clientRepresentation.setSecret(result.getValue());
            return Mono.just(clientRepresentation);
        });
    }
}
