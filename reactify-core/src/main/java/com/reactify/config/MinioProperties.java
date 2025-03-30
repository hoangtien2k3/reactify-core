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

import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MinioProperties class holds the configuration properties for connecting to
 * the Minio object storage service. It is used to bind the properties prefixed
 * with "minio" defined in the application's configuration files.
 * </p>
 *
 * <p>
 * The properties include the base URL for the Minio server, access credentials,
 * and the bucket name used for storing objects.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * <p>
     * The base URL of the Minio server.
     * </p>
     *
     */
    private String baseUrl;

    /**
     * <p>
     * The public URL for accessing the Minio server.
     * </p>
     *
     */
    private String publicUrl;

    /**
     * <p>
     * The access key used for authenticating with the Minio server.
     * </p>
     *
     */
    private String accessKey;

    /**
     * <p>
     * The secret key used for authenticating with the Minio server.
     * </p>
     *
     */
    private String secretKey;

    /**
     * <p>
     * The name of the bucket used for storing objects in Minio.
     * </p>
     *
     */
    private String bucket;

    public MinioProperties() {}

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinioProperties that)) return false;
        return Objects.equals(getBaseUrl(), that.getBaseUrl())
                && Objects.equals(getPublicUrl(), that.getPublicUrl())
                && Objects.equals(getAccessKey(), that.getAccessKey())
                && Objects.equals(getSecretKey(), that.getSecretKey())
                && Objects.equals(getBucket(), that.getBucket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseUrl(), getPublicUrl(), getAccessKey(), getSecretKey(), getBucket());
    }
}
