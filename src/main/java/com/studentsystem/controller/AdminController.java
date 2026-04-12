package com.studentsystem.controller;

import java.util.List;

import com.studentsystem.service.interfaces.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Organization;

@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {
    private final OrganizationService organizationService;
    public AdminController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    @GetMapping("/organization/requests")
    public ResponseEntity<List<Organization>> viewPendingRequests() {
        return ResponseEntity.ok(organizationService.findPendingOrganizations());
    }
    
    @PatchMapping("/organization/requests/accept/{number}")
    public ResponseEntity<SuccessResponse> acceptOrganizationRequest(@PathVariable("number") String registrationNumber) {
        return null;
    }
}
