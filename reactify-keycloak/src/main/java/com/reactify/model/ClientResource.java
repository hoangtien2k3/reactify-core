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

import java.util.List;

public class ClientResource {
    private String id;
    private String name;
    private String type;
    private List<ResourcePermission> permissions;

    public ClientResource() {}

    public ClientResource(String id, String name, String type, List<ResourcePermission> permissions) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.permissions = permissions;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ResourcePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ResourcePermission> permissions) {
        this.permissions = permissions;
    }
}
