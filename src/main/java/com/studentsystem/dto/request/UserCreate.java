package com.studentsystem.dto.request;

import java.time.LocalDateTime;

import com.studentsystem.enums.RoleEnum;

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
    private RoleEnum userRole;

    // Teacher- and student-specific roles
    private String department;

    // Teacher-specific role
    private String specialty;

    // Student specific roles
    private String level;

    // Authentication for creating an admin
    private String applicationPassword;

    private LocalDateTime created_at;

    public UserCreate(String email, String password, String fullName, RoleEnum userRole) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userRole = userRole;
    }
}
