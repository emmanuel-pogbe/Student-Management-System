package com.studentsystem.service.interfaces;

import com.studentsystem.dto.response.SuccessResponse;
import org.springframework.security.core.Authentication;

public interface TeacherService {

    public SuccessResponse requestJoinOrganization(String organizationNumber, Authentication authentication);
}
