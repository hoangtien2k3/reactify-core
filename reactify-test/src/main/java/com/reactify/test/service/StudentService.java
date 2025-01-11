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
