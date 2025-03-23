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
package com.reactify.config;

import brave.Tracing;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * TracingConfiguration class provides configuration for tracing in the
 * application using the Brave library. This class defines beans for the
 * {@link io.micrometer.tracing.Tracer} and {@link brave.Tracing} objects, which
 * are essential for distributed tracing and monitoring of requests across
 * microservices.
 * </p>
 *
 * <p>
 * It configures the tracing system with the default settings and creates a
 * no-op Tracer instance when tracing is not needed.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
public class TracingConfiguration {

    /**
     * <p>
     * Creates a Tracer bean. This method returns a NOOP Tracer instance which means
     * that it does not perform any actual tracing. You may replace it with a real
     * tracer instance as needed.
     * </p>
     *
     * @param tracing
     *            a {@link brave.Tracing} object that provides tracing capabilities.
     * @return a {@link io.micrometer.tracing.Tracer} object configured for tracing
     *         operations.
     */
    @Bean
    public Tracer tracer(Tracing tracing) {
        return BraveTracer.NOOP;
    }

    /**
     * <p>
     * Creates a Tracing bean. This method configures the Tracing instance with the
     * default settings. You may customize the Tracing configuration based on your
     * application's requirements.
     * </p>
     *
     * @return a {@link brave.Tracing} object used for creating trace spans.
     */
    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder().build();
    }
}
