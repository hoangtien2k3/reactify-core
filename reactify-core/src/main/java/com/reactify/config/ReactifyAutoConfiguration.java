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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Autoconfiguration for the reactify-core library.
 * <p>
 * Automatically scans and registers components in the <code>com.reactify</code>
 * package, enabling seamless integration with Spring Boot.
 * </p>
 *
 * <h3>Usage:</h3>
 *
 * <pre>
 * Add the reactify-core dependency, and Spring Boot will configure its components automatically.
 * </pre>
 *
 * @author @hoangtien2k3
 * @since 1.3.0
 */
@Configuration
@ComponentScan(basePackages = "com.reactify")
public class ReactifyAutoConfiguration {}
