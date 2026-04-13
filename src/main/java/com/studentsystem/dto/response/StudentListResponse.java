package com.studentsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentListResponse {
    private String email;
    private String fullName;
    private String level;
    private String department;
    private boolean isVerified;
}
