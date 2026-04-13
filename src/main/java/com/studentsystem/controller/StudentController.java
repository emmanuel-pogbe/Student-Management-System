package com.studentsystem.controller;

import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.service.interfaces.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping("/organization/join/{organizationNumber}")
    public ResponseEntity<SuccessResponse> joinOrganization(@PathVariable String organizationNumber, Authentication authentication) {
        return ResponseEntity.ok(studentService.requestJoinOrganization(organizationNumber,authentication));
    }
}
