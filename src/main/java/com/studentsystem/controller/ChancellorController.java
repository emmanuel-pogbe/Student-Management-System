package com.studentsystem.controller;

import com.studentsystem.dto.response.StudentListResponse;
import com.studentsystem.dto.response.TeacherListResponse;
import com.studentsystem.service.interfaces.ChancellorService;
import com.studentsystem.service.interfaces.OrganizationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.SuccessResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chancellor")
public class ChancellorController {
    private final OrganizationService organizationService;
    private final ChancellorService chancellorService;

    public ChancellorController(OrganizationService organizationService, ChancellorService chancellorService) {
        this.organizationService = organizationService;
        this.chancellorService = chancellorService;
    }

    @PostMapping("/organization/create")
    public SuccessResponse applyForOrganization(@RequestBody OrganizationCreateRequest organizationCreateRequest, Authentication auth) {
        return organizationService.createOrganizationApplication(organizationCreateRequest, auth.getName());
    }

    @PostMapping("/organization/teacher/approve")
    public SuccessResponse approveTeacherApplication(@RequestBody Map<String, Object> requestMap, Authentication auth) {
        String teacherEmail = (String) requestMap.get("teacherEmail");
        return chancellorService.approveTeacherApplication(teacherEmail);
    }

    @GetMapping("/organization/teachers")
    public ResponseEntity<List<TeacherListResponse>> getTeachers(@RequestParam(required = false) Boolean isVerified, Authentication auth) {
        if (isVerified == null) {
           return ResponseEntity.ok(chancellorService.getAllTeachers(auth));
        }
        return ResponseEntity.ok(chancellorService.getAllTeachers(auth, isVerified));
    }

    @GetMapping("/organization/students")
    public ResponseEntity<List<StudentListResponse>> getStudents(@RequestParam(required = false) Boolean isVerified, Authentication auth) {
        if (isVerified == null) {
            return ResponseEntity.ok(chancellorService.getAllStudents(auth));
        }
        return ResponseEntity.ok(chancellorService.getAllStudents(auth, isVerified));
    }

    @PostMapping("/organization/student/approve")
    public ResponseEntity<SuccessResponse> approveStudentApplication(@RequestBody Map<String, Object> requestMap, Authentication auth) {
        String studentEmail = (String) requestMap.get("studentEmail");
        return ResponseEntity.ok(chancellorService.approveStudentApplication(studentEmail));
    }
}
