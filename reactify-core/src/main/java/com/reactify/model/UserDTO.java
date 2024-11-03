/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
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
import lombok.Data;

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
@Data
public class UserDTO {
    @JsonProperty("sub")
    private String id;

    @JsonProperty("preferred_username")
    private String username;

    /**
     * Constructs a new instance of {@code UserDTO}.
     */
    public UserDTO() {}
}
