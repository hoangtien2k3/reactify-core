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
package com.reactify.test.client.impl;

import com.reactify.BaseRestClient;
import com.reactify.test.client.BaseCurrencyClient;
import com.reactify.test.model.GeoPluginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientTestFactory")
public class BaseCurrencyClientImpl implements BaseCurrencyClient {

    @Qualifier("baseCurrencyClient")
    private final WebClient baseCurrencyClient;

    private final BaseRestClient<GeoPluginResponse> baseRestClientQualifier;

    @Override
    public Mono<GeoPluginResponse> getBaseCurrency(String baseCurrency) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.set("base_currency", baseCurrency);
        return baseRestClientQualifier.get(baseCurrencyClient, "/json.gp", null, req, GeoPluginResponse.class)
                .flatMap(optionalResponse -> optionalResponse
                        .map(Mono::just)
                        .orElseGet(Mono::empty));
    }

}
