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
package com.reactify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * <p>
 * The TokenUser class represents a user token with associated properties that
 * are typically used in authentication and authorization processes. This class
 * is designed to hold user information that is extracted from a token, such as
 * during a login or authentication operation.
 * </p>
 *
 * <p>
 * The class utilizes Lombok annotations for boilerplate code reduction, making
 * it easier to manage user data.
 * </p>
 *
 * @author hoangtien2k3
 */
@JsonIgnoreProperties
public class TokenUser {
    private String id;
    private String name;
    private String username;
    private String email;

    @JsonProperty("individual_id")
    private String individualId;

    @JsonProperty("organization_id")
    private String organizationId;

    public TokenUser() {}

    private TokenUser(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.username = builder.username;
        this.email = builder.email;
        this.individualId = builder.individualId;
        this.organizationId = builder.organizationId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String username;
        private String email;
        private String individualId;
        private String organizationId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder individualId(String individualId) {
            this.individualId = individualId;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public TokenUser build() {
            return new TokenUser(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndividualId() {
        return individualId;
    }

    public void setIndividualId(String individualId) {
        this.individualId = individualId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenUser tokenUser)) return false;
        return Objects.equals(getId(), tokenUser.getId())
                && Objects.equals(getName(), tokenUser.getName())
                && Objects.equals(getUsername(), tokenUser.getUsername())
                && Objects.equals(getEmail(), tokenUser.getEmail())
                && Objects.equals(getIndividualId(), tokenUser.getIndividualId())
                && Objects.equals(getOrganizationId(), tokenUser.getOrganizationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUsername(), getEmail(), getIndividualId(), getOrganizationId());
    }
}
