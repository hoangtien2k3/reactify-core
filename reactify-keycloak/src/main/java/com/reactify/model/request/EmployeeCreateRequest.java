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

import java.time.LocalDateTime;
import java.util.List;

public class EmployeeCreateRequest {
    private FileDTO image;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime birthday;
    private String gender;
    private String code;
    private Integer status;
    private String address;
    private List<EmployeePositionRequest> employeePositionRequestList;
    private LocalDateTime probationDay;
    private LocalDateTime startWorkingDay;
    private String username;
    private String emailAccount;
    private Boolean sendEmail;
    private Integer accountStatus;
    private List<EmployeePermissionRequest> employeePermissionRequestList;
    private Boolean isEditable;

    public EmployeeCreateRequest() {}

    public EmployeeCreateRequest(
            FileDTO image,
            String name,
            String phone,
            String email,
            LocalDateTime birthday,
            String gender,
            String code,
            Integer status,
            String address,
            List<EmployeePositionRequest> employeePositionRequestList,
            LocalDateTime probationDay,
            LocalDateTime startWorkingDay,
            String username,
            String emailAccount,
            Boolean sendEmail,
            Integer accountStatus,
            List<EmployeePermissionRequest> employeePermissionRequestList,
            Boolean isEditable) {
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.code = code;
        this.status = status;
        this.address = address;
        this.employeePositionRequestList = employeePositionRequestList;
        this.probationDay = probationDay;
        this.startWorkingDay = startWorkingDay;
        this.username = username;
        this.emailAccount = emailAccount;
        this.sendEmail = sendEmail;
        this.accountStatus = accountStatus;
        this.employeePermissionRequestList = employeePermissionRequestList;
        this.isEditable = isEditable;
    }

    public FileDTO getImage() {
        return image;
    }

    public void setImage(FileDTO image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<EmployeePositionRequest> getEmployeePositionRequestList() {
        return employeePositionRequestList;
    }

    public void setEmployeePositionRequestList(List<EmployeePositionRequest> employeePositionRequestList) {
        this.employeePositionRequestList = employeePositionRequestList;
    }

    public LocalDateTime getProbationDay() {
        return probationDay;
    }

    public void setProbationDay(LocalDateTime probationDay) {
        this.probationDay = probationDay;
    }

    public LocalDateTime getStartWorkingDay() {
        return startWorkingDay;
    }

    public void setStartWorkingDay(LocalDateTime startWorkingDay) {
        this.startWorkingDay = startWorkingDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<EmployeePermissionRequest> getEmployeePermissionRequestList() {
        return employeePermissionRequestList;
    }

    public void setEmployeePermissionRequestList(List<EmployeePermissionRequest> employeePermissionRequestList) {
        this.employeePermissionRequestList = employeePermissionRequestList;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }
}
