package com.studentsystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentsystem.dto.request.OrganizationCreateRequest;

@RestController
@RequestMapping("/api/v1/chancellor")
public class Chancellor {
    @PostMapping("/organization/create")
    public String applyForOrganization(@RequestBody OrganizationCreateRequest organizationCreateRequest) {
        
        return null;
    }
}
