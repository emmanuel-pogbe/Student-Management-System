package com.studentsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String registrationNumber;
    private String name;
    private String address;
    private boolean isVerified;
}
