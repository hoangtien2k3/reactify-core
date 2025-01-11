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
import lombok.Builder;
import lombok.Data;

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
@Data
@Builder
public class TokenUser {
    private String id;
    private String name;
    private String username;
    private String email;

    @JsonProperty("individual_id")
    private String individualId;

    @JsonProperty("organization_id")
    private String organizationId;

    /**
     * Constructs a new instance of {@code TokenUser}.
     */
    public TokenUser() {}

    /**
     * Constructs a new instance of {@code TokenUser} with the specified parameters.
     *
     * @param id
     *            The unique identifier for the user.
     * @param name
     *            The name of the user.
     * @param username
     *            The username of the user.
     * @param email
     *            The email address of the user.
     * @param individualId
     *            The individual ID associated with the user.
     * @param organizationId
     *            The organization ID associated with the user.
     */
    public TokenUser(
            String id, String name, String username, String email, String individualId, String organizationId) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.individualId = individualId;
        this.organizationId = organizationId;
    }
}
