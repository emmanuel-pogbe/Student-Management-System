package com.studentsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherListResponse {
    private String email;
    private String fullName;
    private boolean isVerified;

    // Extra roles for future use
    // private String department;
    // private String specialty;
}
