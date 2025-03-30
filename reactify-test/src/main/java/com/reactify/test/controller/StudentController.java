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
package com.reactify.test.controller;

import com.reactify.DataUtil;
import com.reactify.LocalCache;
import com.reactify.request.LocalCacheRequest;
import com.reactify.test.client.BaseCurrencyClient;
import com.reactify.test.model.GeoPluginResponse;
import com.reactify.test.model.Student;
import com.reactify.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BaseCurrencyClient baseCurrencyClient;

    // API get list Student demo using LocalCache
    @LocalCache
    @GetMapping("/students")
    public Mono<List<Student>> getStudents() {
        var lstStudent = studentService.getAllStudents();
        if (DataUtil.isNullOrEmpty(lstStudent)) {
            return null;
        }
        return lstStudent;
    }

    // baseCurrency -> base_currency = VN
    @LocalCache
    @GetMapping("/base-currency")
    public Mono<GeoPluginResponse> getBaseCurrency(String baseCurrency) {
        return baseCurrencyClient.getBaseCurrency(baseCurrency);
    }

    @PostMapping("/clear-by-name")
    public Mono<Integer> clearCacheByName(@RequestBody LocalCacheRequest request) {
        return studentService.clearCacheByName(request);
    }

    @PostMapping("/clear-all")
    public Mono<Integer> clearAllCache() {
        return studentService.clearAllCaches();
    }

    @GetMapping("/get-list-cache")
    public Mono<List<String>> getLocalCache() {
        return studentService.getLstCache();
    }
}
