package com.studentsystem.controller;

import com.studentsystem.dto.request.CourseRegisterRequest;
import com.studentsystem.dto.response.CourseResourceResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.service.interfaces.CourseResourceService;
import com.studentsystem.service.interfaces.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final CourseResourceService courseResourceService;

    public StudentController(StudentService studentService, CourseResourceService courseResourceService) {
        this.studentService = studentService;
        this.courseResourceService = courseResourceService;
    }
    @PostMapping("/organization/join/{organizationNumber}")
    public ResponseEntity<SuccessResponse> joinOrganization(@PathVariable String organizationNumber, Authentication authentication) {
        return ResponseEntity.ok(studentService.requestJoinOrganization(organizationNumber,authentication));
    }

    @PostMapping("/course/register")
    public ResponseEntity<SuccessResponse> registerCourse(@RequestBody CourseRegisterRequest courseRegisterRequest, Authentication authentication) {
        return ResponseEntity.ok(studentService.registerCourse(courseRegisterRequest, authentication));
    }

    @GetMapping("/course/resource/{courseCode}")
    public ResponseEntity<List<CourseResourceResponse>> getCourseResources(@PathVariable String courseCode, Authentication authentication) {
        return ResponseEntity.ok(courseResourceService.getAllCourseResources(courseCode, authentication));
    }
}
