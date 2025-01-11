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

import com.reactify.util.ReactiveOAuth2Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * <p>
 * The ClientRegistrationFilter class is a configuration class that defines a
 * Spring bean for managing authorized OAuth2 clients in a reactive application
 * context. It leverages the Spring Security framework to facilitate OAuth2
 * client registration and authorization.
 * </p>
 *
 * <p>
 * This class contains a method to create an instance of
 * {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager},
 * which is used to manage authorized clients for OAuth2 operations in a
 * reactive environment.
 * </p>
 *
 * <p>
 * It is annotated with
 * {@link org.springframework.context.annotation.Configuration}, indicating that
 * it provides Spring configuration.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
public class ClientRegistrationFilter {

    /**
     * Constructs a new instance of {@code ClientRegistrationFilter}.
     */
    public ClientRegistrationFilter() {}

    /**
     * <p>
     * Creates a
     * {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager}
     * bean.
     * </p>
     *
     * @param clientRegistrationRepository
     *            a
     *            {@link org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository}
     *            object that manages the client registrations.
     * @param authorizedClientService
     *            a
     *            {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService}
     *            object that manages the authorized clients.
     * @return a
     *         {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager}
     *         object configured with the provided repositories.
     */
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        return ReactiveOAuth2Utils.createAuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
    }
}
