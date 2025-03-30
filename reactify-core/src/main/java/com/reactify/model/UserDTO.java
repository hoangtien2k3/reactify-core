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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * <p>
 * The UserDTO class is a Data Transfer Object (DTO) that represents the user
 * information to be exchanged between the client and the server. This class is
 * typically used for transferring user-related data, such as during
 * authentication or authorization processes.
 * </p>
 *
 * <p>
 * The class utilizes Lombok annotations to minimize boilerplate code, providing
 * a clear and concise representation of user data.
 * </p>
 *
 * @author hoangtien2k3
 */
public class UserDTO {
    @JsonProperty("sub")
    private String id;

    @JsonProperty("preferred_username")
    private String username;

    /**
     * Constructs a new instance of {@code UserDTO}.
     */
    public UserDTO() {}

    public UserDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return Objects.equals(getId(), userDTO.getId()) && Objects.equals(getUsername(), userDTO.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername());
    }
}
