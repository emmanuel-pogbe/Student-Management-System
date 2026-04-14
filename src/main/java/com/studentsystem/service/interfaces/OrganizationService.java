package com.studentsystem.service.interfaces;

import java.util.List;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.OrganizationResponse;
import com.studentsystem.dto.response.SuccessResponse;

public interface OrganizationService {
    SuccessResponse createOrganizationApplication(OrganizationCreateRequest organizationCreateRequest, String authenticatedEmail);

    List<OrganizationResponse> findPendingOrganizations();

    SuccessResponse approveOrganization(String registrationNumber);
}
