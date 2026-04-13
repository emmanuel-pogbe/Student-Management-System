package com.studentsystem.controller;

import com.studentsystem.dto.request.CourseCreate;
import com.studentsystem.dto.request.CourseResourceCreate;
import com.studentsystem.dto.response.CourseResourceResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.service.interfaces.CourseResourceService;
import com.studentsystem.service.interfaces.CourseService;
import com.studentsystem.service.interfaces.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final CourseResourceService courseResourceService;

    public TeacherController(TeacherService teacherService, CourseService courseService, CourseResourceService courseResourceService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.courseResourceService = courseResourceService;
    }

    @PostMapping("/organization/join/{organizationNumber}")
    public ResponseEntity<SuccessResponse> joinOrganization(@PathVariable String organizationNumber, Authentication authentication) {
        return ResponseEntity.ok(teacherService.requestJoinOrganization(organizationNumber,authentication));
    }

    @PostMapping("/course")
    public ResponseEntity<SuccessResponse> createCourse(@RequestBody CourseCreate courseCreate, Authentication authentication) {
        return ResponseEntity.ok(courseService.createCourse(courseCreate,authentication));
    }

    @PostMapping("/course/{courseCode}/resource")
    public ResponseEntity<SuccessResponse> createCourseResource(@PathVariable String courseCode, @RequestBody CourseResourceCreate courseResourceCreate, Authentication authentication) {
        return ResponseEntity.ok(courseResourceService.createCourseResource(courseCode, courseResourceCreate, authentication));
    }

    @GetMapping("/course/{courseCode}/resource")
    public ResponseEntity<List<CourseResourceResponse>> getAllCourseResources(@PathVariable String courseCode, Authentication authentication) {
        return ResponseEntity.ok(courseResourceService.getAllCourseResources(courseCode, authentication));
    }
}