/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến
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
package com.reactify.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * <p>
 * The {@code WebSecurityCorsFilter} class configures CORS (Cross-Origin
 * Resource Sharing) settings for a Spring WebFlux application.
 * </p>
 *
 * <p>
 * This configuration allows all origins, methods, and headers for requests to
 * the application. The maximum age for caching the preflight response is set to
 * 3600 seconds (1 hour).
 * </p>
 *
 * <p>
 * This class implements the
 * {@link org.springframework.web.reactive.config.WebFluxConfigurer} interface
 * to customize the CORS mappings for the WebFlux framework.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
@EnableWebFlux
public class WebSecurityCorsFilter implements WebFluxConfigurer {

    /** {@inheritDoc} */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
