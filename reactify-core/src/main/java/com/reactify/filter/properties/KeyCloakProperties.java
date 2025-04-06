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
 * The KeyCloakProperties class is a record that holds configuration properties
 * for connecting to a Keycloak server. This includes the client ID and client
 * secret required for authentication.
 * </p>
 *
 * <p>
 * This class provides a concise way to encapsulate the properties necessary for
 * Keycloak integration, facilitating the management of authentication
 * credentials in a Spring application.
 * </p>
 *
 * @author hoangtien2k3
 */
public class KeyCloakProperties {

    /**
     * the client ID used for authenticating with the Keycloak server
     */
    private String clientId;

    /**
     * the client secret used for authenticating with the Keycloak server
     */
    private String clientSecret;

    public KeyCloakProperties() {}

    public KeyCloakProperties(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
