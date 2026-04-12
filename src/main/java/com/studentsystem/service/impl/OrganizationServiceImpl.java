package com.studentsystem.service.impl;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Organization;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.service.interfaces.OrganizationService;

import java.util.Optional;

public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;   

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public SuccessResponse createOrganizationApplication(OrganizationCreateRequest organizationCreateRequest) {
        Optional<Organization> doesExist = organizationRepository.findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
        if (doesExist.isPresent()) {
            throw new RuntimeException("Organization Number already exists!");
        }
        Organization organization = new Organization();
        organization.setRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
        organization.setName(organizationCreateRequest.getName());
        organization.setAddress(organizationCreateRequest.getAddress());

        organizationRepository.save(organization);

        return new SuccessResponse("Organization created successfully! Awaiting verification");
    }
    
}
