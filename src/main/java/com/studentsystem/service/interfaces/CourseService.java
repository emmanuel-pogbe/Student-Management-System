package com.studentsystem.service.interfaces;

import com.studentsystem.dto.request.CourseCreate;
import com.studentsystem.dto.response.SuccessResponse;
import org.springframework.security.core.Authentication;

public interface CourseService {
    SuccessResponse createCourse(CourseCreate courseCreate, Authentication authentication);
}
