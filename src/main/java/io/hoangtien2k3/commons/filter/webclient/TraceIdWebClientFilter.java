//package io.hoangtien2k3.commons.filter.webclient;
//
//import brave.Tracer;
//import brave.Tracing;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.ClientRequest;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.ExchangeFunction;
//import reactor.core.publisher.Mono;
//
//@Component
//@RequiredArgsConstructor
//public class TraceIdWebClientFilter implements ExchangeFilterFunction {
//    private final Tracer tracer;
//
//    @Override
//    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction
//            next) {
//        return Mono.subscriberContext()
//                .map(ctx -> Tracing.current().tracer().currentSpan())
//                .map(span -> {
//                    request.headers().set("X-B3-TRACE-ID", span.context().traceId() + "");
//                    return request;
//                })
//                .flatMap(next::exchange);
//    }
//}
