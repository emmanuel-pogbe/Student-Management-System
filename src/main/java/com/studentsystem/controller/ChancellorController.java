package com.studentsystem.controller;

import com.studentsystem.service.interfaces.OrganizationService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentsystem.dto.request.OrganizationCreateRequest;
import com.studentsystem.dto.response.SuccessResponse;

@RestController
@RequestMapping("/api/v1/chancellor")
public class ChancellorController {
    private final OrganizationService organizationService;

    public ChancellorController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/organization/create")
    public SuccessResponse applyForOrganization(@RequestBody OrganizationCreateRequest organizationCreateRequest, Authentication auth) {
        return organizationService.createOrganizationApplication(organizationCreateRequest, auth.getName());
    }
}
