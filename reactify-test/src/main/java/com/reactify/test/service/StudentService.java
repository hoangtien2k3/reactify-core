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
package com.reactify.test.service;

import com.reactify.CacheStore;
import com.reactify.DataUtil;
import com.reactify.KeyCloakClient;
import com.reactify.SecurityUtils;
import com.reactify.exception.BusinessException;
import com.reactify.model.AccessToken;
import com.reactify.model.request.LoginRequest;
import com.reactify.request.LocalCacheRequest;
import com.reactify.test.model.Student;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final KeyCloakClient keyCloakClient;

    public StudentService(KeyCloakClient keyCloakClient) {
        this.keyCloakClient = keyCloakClient;
    }

    public Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest) {
        return keyCloakClient.getToken(loginRequest);
    }

    public Mono<List<Student>> getAllStudents() {
        // fake list student test
        List<Student> listStudent = List.of(
                new Student(1L, "Nguyen Van A", 16, "10A1"),
                new Student(2L, "Tran Thi B", 17, "11B2"),
                new Student(3L, "Le Minh C", 15, "9C3"),
                new Student(4L, "Pha Thi D", 18, "12D4")
        );
        return Mono.just(listStudent);
    }

    private static final String reflectionPath = "com.reactify";

    public Mono<Integer> clearCacheByName(LocalCacheRequest request) {
        String type =  request.getType();
        String nameCache = request.getNameCache();

        if (DataUtil.isNullOrEmpty(type)) {
            return Mono.error(new BusinessException("CCBN00001", "local.cache.type"));
        }

        if (!List.of("SERVICE_LEVEL", "METHOD_LEVEL", "ALL_LEVEL").contains(type)) {
            return Mono.error(new BusinessException("CCBN00002", "local.cache.not.allow"));
        }

        if ("ALL_LEVEL".equals(type)) {
            return Mono.defer(() -> {
                try {
                    int count  = CacheStore.clearAllCaches();
                    return Mono.just(count);
                } catch (Exception ex) {
                    return Mono.error(new BusinessException("CCBN00003", "removeCache.fault"));
                }
            });
        }

        if (nameCache == null || nameCache.isEmpty()) {
            return Mono.error(new BusinessException("CCBN00004", "local.cache.nameCache"));
        }

        return Mono.defer(() -> {
            try {
                if ("METHOD_LEVEL".equals(type)) {
                    int count = CacheStore.clearCachesByName(nameCache);
                    return Mono.just(count);
                }

                return Mono.error(new BusinessException("CCBN00005", "removeCache.fault"));
            } catch (Exception ex) {

                return Mono.error(new BusinessException("CCBN00006", "removeCache.fault"));
            }
        });
    }

    public Mono<List<String>> getLstCache() {
        return Mono.just(CacheStore.getCaches());
    }

    public Mono<Integer> clearAllCaches() {
        return Mono.just(CacheStore.clearAllCaches());
    }

    public Mono<String> getTokenUser() {
        return SecurityUtils.getTokenUser();
    }
}
