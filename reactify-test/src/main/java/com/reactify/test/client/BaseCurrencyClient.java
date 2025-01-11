package com.reactify.test.client;

import com.reactify.test.model.GeoPluginResponse;
import reactor.core.publisher.Mono;

public interface BaseCurrencyClient {

    Mono<GeoPluginResponse> getBaseCurrency(String baseCurrency);

}
