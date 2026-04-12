package com.studentsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreate {
    private String email;
    private String password;
    private String fullName;
    private String role;

    // Teacher- and student-specific roles
    private String department;

    // Teacher-specific role
    private String specialty;

    // Student specific roles
    private String level;
}
