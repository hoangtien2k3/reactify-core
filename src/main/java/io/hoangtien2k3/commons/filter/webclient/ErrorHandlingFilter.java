/// *
// * Copyright 2024 author - Hoàng Anh Tiến
// *
// * Permission is hereby granted, free of charge, to any person obtaining a
/// copy
// * of this software and associated documentation files (the "Software"), to
/// deal
// * in the Software without restriction, including without limitation the
/// rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// */
// package io.hoangtien2k3.commons.filter.webclient; // package
/// io.hoangtien2k3.commons.filter.webclient;
//
// import org.springframework.web.reactive.function.client.ClientRequest;
// import org.springframework.web.reactive.function.client.ClientResponse;
// import
// org.springframework.web.reactive.function.client.ExchangeFilterFunction;
// import org.springframework.web.reactive.function.client.ExchangeFunction;
// import reactor.core.publisher.Mono;
//
// public class ErrorHandlingFilter implements ExchangeFilterFunction {
// @Override
// public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction
// next) {
// return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
// clientResponse.statusCode();
// if (clientResponse.statusCode().is5xxServerError() ||
/// clientResponse.statusCode().is4xxClientError()) {
// return clientResponse.bodyToMono(String.class)
// .flatMap(errorBody -> {
// return Mono.error(new
// CustomWebClientResponseException(errorBody, clientResponse.statusCode()));
// });
// } else {
// return Mono.just(clientResponse);
// }
// });
// }
// }
