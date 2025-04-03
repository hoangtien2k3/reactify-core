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
package com.reactify.model.request;

import java.util.List;

public class EmployeePermissionRequest {
    private String clientId;
    private String roleId;
    private String roleCode;
    private String policyId;
    private List<EmployeePermissionGroup> permissionGroupList;

    public EmployeePermissionRequest() {}

    public EmployeePermissionRequest(
            String clientId,
            String roleId,
            String roleCode,
            String policyId,
            List<EmployeePermissionGroup> permissionGroupList) {
        this.clientId = clientId;
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.policyId = policyId;
        this.permissionGroupList = permissionGroupList;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public List<EmployeePermissionGroup> getPermissionGroupList() {
        return permissionGroupList;
    }

    public void setPermissionGroupList(List<EmployeePermissionGroup> permissionGroupList) {
        this.permissionGroupList = permissionGroupList;
    }
}
