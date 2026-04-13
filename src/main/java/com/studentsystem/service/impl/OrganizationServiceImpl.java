package com.studentsystem.service.impl;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.models.user.Chancellor;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.OrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    public SuccessResponse createOrganizationApplication(OrganizationCreateRequest organizationCreateRequest, String authenticatedEmail) {
        Optional<User> chancellor = userRepository.findByEmail(authenticatedEmail);
        if (!chancellor.isPresent()) {
            throw new RuntimeException("User not found");
        }
        if (!"CHANCELLOR".equals(chancellor.get().getRole())) {
            throw new RuntimeException("User is not a chancellor");
        }
        Optional<Organization> doesExist = organizationRepository.findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
        if (doesExist.isPresent()) {
            throw new RuntimeException("Organization Number already exists!");
        }
        Organization organization = new Organization();
        organization.setRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
        organization.setName(organizationCreateRequest.getName());
        organization.setAddress(organizationCreateRequest.getAddress());
        organization.setChancellor((Chancellor) chancellor.get());
        organization.setVerified(false);
        organizationRepository.save(organization);

        return new SuccessResponse("Organization created successfully! Awaiting verification");
    }

    public List<Organization> findPendingOrganizations() {
        // refactor this using a DTO - this is not good
        return organizationRepository.findPendingOrganizationRequests();
    }
    public SuccessResponse approveOrganization(String registrationNumber) {
        Optional<Organization> organization = organizationRepository.findByRegistrationNumber(registrationNumber);
        if (organization.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }
        Organization result = organization.get();
        if (result.isVerified()) {
            throw new RuntimeException("Organization is already verified");
        }
        result.setVerified(true);
        organizationRepository.save(result);
        return new SuccessResponse("Organization approved successfully!");
    }
    
}
