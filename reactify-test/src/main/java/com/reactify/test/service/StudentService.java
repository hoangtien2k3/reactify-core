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

import com.reactify.LocalCache;
import com.reactify.test.model.Student;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {
    @LocalCache
    public List<Student> getAllStudents() {
        // Fack dữ liệu học sinh
        return Arrays.asList(
                new Student(1L, "Nguyen Van A", 16, "10A1"),
                new Student(2L, "Tran Thi B", 17, "11B2"),
                new Student(3L, "Le Minh C", 15, "9C3"),
                new Student(4L, "Pham Thi D", 18, "12D4")
        );
    }
}
