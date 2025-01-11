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
@DependsOn("webClientFactory")
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
