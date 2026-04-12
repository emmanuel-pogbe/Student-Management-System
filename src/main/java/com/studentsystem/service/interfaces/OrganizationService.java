package com.studentsystem.service.interfaces;

import java.util.List;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Organization;

public interface OrganizationService {
    SuccessResponse createOrganizationApplication(OrganizationCreateRequest organizationCreateRequest, String authenticatedEmail);

    List<Organization> findPendingOrganizations();

    SuccessResponse approveOrganization(String registrationNumber);
}
