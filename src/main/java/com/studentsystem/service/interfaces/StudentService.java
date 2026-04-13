package com.studentsystem.service.interfaces;

import com.studentsystem.dto.request.CourseRegisterRequest;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.user.Student;
import org.springframework.security.core.Authentication;

public interface StudentService {
    SuccessResponse requestJoinOrganization(String organizationNumber, Authentication authentication);

    SuccessResponse registerCourse(CourseRegisterRequest courseRegisterRequest, Authentication authentication);
}
