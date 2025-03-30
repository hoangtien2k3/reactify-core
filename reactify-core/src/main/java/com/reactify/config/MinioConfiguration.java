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

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * MinioConfiguration class is responsible for configuring the Minio client used
 * for interacting with the Minio object storage service.
 * </p>
 *
 * <p>
 * This configuration class initializes a {@link io.minio.MinioClient} bean
 * based on the properties defined in
 * {@link com.reactify.config.MinioProperties}. It enables the Minio client only
 * if the "minio.enabled" property is set to true.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
public class MinioConfiguration {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(MinioConfiguration.class);

    private final MinioProperties minioProperties;

    /**
     * Constructs a new instance of {@code MinioConfiguration}.
     *
     * @param minioProperties
     *            the properties used to configure MinIO connection settings.
     */
    public MinioConfiguration(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    /**
     * <p>
     * Creates a {@link io.minio.MinioClient} bean configured with the Minio server
     * endpoint and access credentials. The client is only created if the
     * "minio.enabled" property is set to true.
     * </p>
     *
     * @return a {@link io.minio.MinioClient} object configured to connect to the
     *         Minio server.
     * @throws java.lang.RuntimeException
     *             if there is an error during Minio client configuration.
     */
    @Bean
    @ConditionalOnProperty(value = "minio.enabled", havingValue = "true", matchIfMissing = false)
    public MinioClient minioClient() {
        log.info("Configuring minio client: {}", minioProperties.getBaseUrl());
        return MinioClient.builder()
                .endpoint(minioProperties.getBaseUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
