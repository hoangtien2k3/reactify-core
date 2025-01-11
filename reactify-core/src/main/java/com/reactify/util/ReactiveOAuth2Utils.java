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

import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * Utility class for data manipulation and processing. This class contains
 * static methods for various data-related operations.
 */
public class ReactiveOAuth2Utils {

    /**
     * Constructs a new instance of {@code ReactiveOAuth2Utils}.
     */
    public ReactiveOAuth2Utils() {}

    /**
     * <p>
     * createAuthorizedClientManager.
     * </p>
     *
     * @param clientRegistrationRepository
     *            a
     *            {@link org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository}
     *            object
     * @param authorizedClientService
     *            a
     *            {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService}
     *            object
     * @return a
     *         {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager}
     *         object
     */
    public static ReactiveOAuth2AuthorizedClientManager createAuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
