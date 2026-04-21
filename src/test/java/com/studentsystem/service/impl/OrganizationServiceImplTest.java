package com.studentsystem.service.impl;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.OrganizationResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Nested
    @DisplayName("Class to test creating organization applications")
    class CreateOrganizationApplicationMethod {
        OrganizationCreateRequest organizationCreateRequest;
        Organization organization;

        User testUser;
        @BeforeEach
        void setUp() {
            organizationCreateRequest = new OrganizationCreateRequest();
            organizationCreateRequest.setName("Test Organization");
            organizationCreateRequest.setAddress("123 Main St");
            organizationCreateRequest.setRegistrationNumber("123");

            testUser = new User(
                    "testUser@gmail.com",
                    "encoded password",
                    "Pogbe Emmanuel",
                    RoleEnum.CHANCELLOR,
                    LocalDateTime.now(),
                    false
            );

            organization = new Organization();
            organization.setId(1L);
            organization.setName("Existing test organization");
            organization.setAddress("123 Not Main St");
            organization.setRegistrationNumber("123");
            organization.setVerified(true);
        }
        @Test
        @DisplayName("what happens when chancellor not found")
        void whatHappensWhenChancellorNotFound() {
            // given
            when(userRepository.findByEmailAndUserRole("testChancellor@gmail.com", RoleEnum.CHANCELLOR))
                    .thenReturn(Optional.empty());
            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                organizationService.createOrganizationApplication(organizationCreateRequest, "testChancellor@gmail.com");

            });
            // then
            assertEquals("User not found", exception.getMessage());
            verify(userRepository,times(1)).findByEmailAndUserRole("testChancellor@gmail.com", RoleEnum.CHANCELLOR);
        }

        @Test
        @DisplayName("what happens when organization number already exists")
        void whatHappensWhenOrganizationNumberAlreadyExists() {
            // given
            when(userRepository.findByEmailAndUserRole("testUser@gmail.com", RoleEnum.CHANCELLOR))
                    .thenReturn(Optional.of(testUser));
            when(organizationRepository.findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber()))
                    .thenReturn(Optional.of(organization));

            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                organizationService.createOrganizationApplication(organizationCreateRequest,"testUser@gmail.com");
            });

            // then
            assertEquals("Organization Number already exists!", exception.getMessage());
            verify(organizationRepository,times(1)).findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
            verify(userRepository,times(1)).findByEmailAndUserRole("testUser@gmail.com", RoleEnum.CHANCELLOR);
        }

        @Test
        @DisplayName("what happens when organization create request successful")
        void whatHappensWhenOrganizationCreateRequestSuccessful() {
            // given
            when(userRepository.findByEmailAndUserRole("testUser@gmail.com", RoleEnum.CHANCELLOR))
                    .thenReturn(Optional.of(testUser));
            when(organizationRepository.findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber()))
                    .thenReturn(Optional.empty());

            // when
            SuccessResponse successResponse = organizationService.createOrganizationApplication(organizationCreateRequest,"testUser@gmail.com");

            // then
            assertEquals("Organization created successfully! Awaiting verification", successResponse.getMessage());
            verify(organizationRepository,times(1)).findByRegistrationNumber(organizationCreateRequest.getRegistrationNumber());
            verify(userRepository,times(1)).findByEmailAndUserRole("testUser@gmail.com", RoleEnum.CHANCELLOR);
            verify(organizationRepository,times(1)).save(any(Organization.class));
        }

    }
    @Nested
    @DisplayName("Find pending organizations method")
    class findPendingOrganizationsMethod {
        private Organization organization;
        @BeforeEach

        void setUp() {
            organization = new Organization();
            organization.setId(1L);
            organization.setName("Existing test organization");
            organization.setAddress("123 Not Main St");
            organization.setRegistrationNumber("123");
            organization.setVerified(true);
        }

        @Test
        @DisplayName("Find pending organizations")
        void ensureThatApprovedOrganizationsDontShowUp() {
            // Given
            when(organizationRepository.findPendingOrganizationRequests())
                    .thenReturn(List.of(organization));
            // when
            List<OrganizationResponse> organizationResponses = organizationService.findPendingOrganizations();
            // then
            verify(organizationRepository,times(1)).findPendingOrganizationRequests();
            assertEquals(1, organizationResponses.size());
            assertEquals(organization.getName(), organizationResponses.get(0).getName());
            assertEquals(organization.getAddress(), organizationResponses.get(0).getAddress());
            assertEquals(organization.getRegistrationNumber(), organizationResponses.get(0).getRegistrationNumber());
        }

    }
    
    @Nested
    @DisplayName("Approving an organization")
    class ApprovingOrganization {
        private Organization organization;
        @BeforeEach

        void setUp() {
            organization = new Organization();
            organization.setId(1L);
            organization.setName("Existing test organization");
            organization.setAddress("123 Not Main St");
            organization.setRegistrationNumber("123");
            organization.setVerified(true);
        }
        @Test
        @DisplayName("what happens when the organization does not exist")
        void whenOrganizationDoesNotExist() {
            // given
            String registrationNumber = "123";

            when(organizationRepository.findByRegistrationNumber(registrationNumber))
                    .thenReturn(Optional.empty());

            // when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                organizationService.approveOrganization(registrationNumber);
            });
            // then
            assertEquals("Organization not found", exception.getMessage());
            verify(organizationRepository,times(1)).findByRegistrationNumber(registrationNumber);
        }

        @Test
        @DisplayName("what happens when the organization is already verified")
        void whenOrganizationIsAlreadyVerified() {
            String registrationNumber = "123";
            organization.setVerified(true);
            when(organizationRepository.findByRegistrationNumber(registrationNumber))
            .thenReturn(Optional.of(organization));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                organizationService.approveOrganization(registrationNumber);
            });
            assertEquals("Organization is already verified", exception.getMessage());
            verify(organizationRepository,times(1)).findByRegistrationNumber(registrationNumber);
        }

        @Test
        @DisplayName("what happens on successful verification of organization")
        void whenOrganizationSuccessfulVerification() {
            String registrationNumber = "123";
            organization.setVerified(false);
            when(organizationRepository.findByRegistrationNumber(registrationNumber))
                    .thenReturn(Optional.of(organization));

            SuccessResponse result = organizationService.approveOrganization(registrationNumber);
            assertTrue(organization.isVerified());
            verify(organizationRepository,times(1)).findByRegistrationNumber(registrationNumber);
            verify(organizationRepository,times(1)).save(organization);
            assertEquals("Organization approved successfully!",result.getMessage());
        }
    }
}