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
    private String department;
    private String specialty;
    private boolean isVerified;
}
