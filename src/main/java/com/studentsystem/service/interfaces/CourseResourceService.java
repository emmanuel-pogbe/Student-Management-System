package com.studentsystem.service.interfaces;

import com.studentsystem.dto.request.CourseResourceCreate;
import com.studentsystem.dto.response.CourseResourceResponse;
import com.studentsystem.dto.response.SuccessResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CourseResourceService {
    SuccessResponse createCourseResource(String courseCode, CourseResourceCreate courseResourceCreate, Authentication authentication);

    List<CourseResourceResponse> getAllCourseResources(String courseCode, Authentication authentication);
}
