package com.studentsystem.service.interfaces;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.SuccessResponse;

public interface OrganizationService {
    SuccessResponse createOrganizationApplication(OrganizationCreateRequest organizationCreateRequest);
}
