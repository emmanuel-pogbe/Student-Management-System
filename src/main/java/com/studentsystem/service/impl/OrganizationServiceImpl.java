package com.studentsystem.service.impl;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.OrganizationResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.OrganizationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Optional<User> chancellor = userRepository.findByEmailAndUserRole(authenticatedEmail,RoleEnum.CHANCELLOR);
        if (!chancellor.isPresent()) {
            throw new RuntimeException("User not found");
        }
        Optional<Organization> doesExist = organizationRepository.findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
        if (doesExist.isPresent()) {
            throw new RuntimeException("Organization Number already exists!");
        }
        Organization organization = new Organization();
        organization.setRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
        organization.setName(organizationCreateRequest.getName());
        organization.setAddress(organizationCreateRequest.getAddress());
        organization.setOwnedBy(chancellor.get());
        organization.setVerified(false);
        organizationRepository.save(organization);

        return new SuccessResponse("Organization created successfully! Awaiting verification");
    }


    public List<OrganizationResponse> findPendingOrganizations() {
        List<Organization> organizations = organizationRepository.findPendingOrganizationRequests();
        List<OrganizationResponse> organizationResponses = new ArrayList<>();
        for (Organization organization: organizations) {
                organizationResponses.add(new OrganizationResponse(
                organization.getId(),
                organization.getRegistrationNumber(),
                organization.getName(),
                organization.getAddress(),
                organization.isVerified()
            )); 
        }
        return organizationResponses;
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
